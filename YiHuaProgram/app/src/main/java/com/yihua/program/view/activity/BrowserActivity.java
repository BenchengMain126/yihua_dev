package com.yihua.program.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.yihua.program.R;
import com.yihua.program.application.App;
import com.yihua.program.base.activity.BaseActivity;
import com.yihua.program.dbmodel.SaveLocal;
import com.yihua.program.tools.I;
import com.yihua.program.tools.L;
import com.yihua.program.tools.P;
import com.yihua.program.utils.GifSizeFilter;
import com.yihua.program.utils.IOSToast;
import com.yihua.program.utils.StatusBarUtil;
import com.yihua.program.view.contract.BrowserContract;
import com.yihua.program.view.presenter.BrowserPresenter;
import com.yihua.program.widget.UIController;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import me.leolin.shortcutbadger.ShortcutBadger;


public class BrowserActivity extends BaseActivity<BrowserContract.IPresenter> implements BrowserContract.IView {

    @BindView(R.id.root)
    LinearLayout root;

    BridgeWebView mBridgeWebView;
    private CallBackFunction mCallBackFunction;
    private String mUrl;
    private AgentWeb mAgentWeb;
    private int mColor;
    public static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected void initPresenter() {
        mPresenter = new BrowserPresenter(activity, this);
    }

    @Override
    protected void initSome(Bundle savedInstanceState) {
        super.initSome(savedInstanceState);
        StatusBarUtil.initStatusBar(activity, true, true, true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
        mPresenter.getAppVersionUsing();
    }

    @Override
    protected void onDestroy() {
        AgentWebConfig.clearDiskCache(this);
        mAgentWeb.clearWebCache();
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initData() {
        super.initData();
        mUrl = P.getInstance(BrowserActivity.this).getBaseUrl();
        mBridgeWebView = new BridgeWebView(this);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(root, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setIndicatorColorWithHeight(mColor, 2)
                .setWebViewClient(new BridgeWebViewClient(mBridgeWebView))
                .setWebView(mBridgeWebView)
                .setAgentWebUIController(new UIController(this))
                .setMainFrameErrorView(R.layout.activity_browser_fail, -1)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        showProgressDialog();
                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        dismissProgressDialog();
                    }
                })
                .createAgentWeb()
                .ready()
                .go(mUrl);

        //缓存在本地
        mBridgeWebView.registerHandler("saveCache", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                SaveLocal h5Params = JSON.parseObject(data, SaveLocal.class);
                LitePal.deleteAll(SaveLocal.class, "key = ?", h5Params.getKey());
                SaveLocal saveLocal = new SaveLocal();
                saveLocal.setKey(h5Params.getKey());
                saveLocal.setValue(h5Params.getValue());
                boolean save = saveLocal.save();
                function.onCallBack(String.valueOf(save));
            }
        });
        //获取本地缓存
        mBridgeWebView.registerHandler("getCache", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                SaveLocal h5Params = JSON.parseObject(data, SaveLocal.class);
                List<SaveLocal> saveLocals = LitePal.where("key = ?", h5Params.getKey()).find(SaveLocal.class);
                if (saveLocals.size() > 0) {
                    function.onCallBack(saveLocals.get(0).getValue());
                } else {
                    function.onCallBack("-1");
                }
            }
        });
        //获取本地用户信息
        mBridgeWebView.registerHandler("getUserInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                List<SaveLocal> saveLocals = LitePal.where("key = ?", "loginInfos").find(SaveLocal.class);
                if (saveLocals.size() > 0) {
                    function.onCallBack(saveLocals.get(0).getValue());
                } else {
                    function.onCallBack("");
                }
            }
        });
        //删除本地缓存
        mBridgeWebView.registerHandler("removeCache", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                SaveLocal h5Params = JSON.parseObject(data, SaveLocal.class);
                int lines = -1;
                if ("all".equals(h5Params.getKey())) {
                    lines = LitePal.deleteAll(SaveLocal.class);
                } else {
                    lines = LitePal.deleteAll(SaveLocal.class, "key = ?", h5Params.getKey());
                }
                function.onCallBack(String.valueOf(lines));
            }
        });
        //重启当前应用
        mBridgeWebView.registerHandler("restart", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                App.clearAllStorageAndRelaunch();
            }
        });
        //返回登录页面
        mBridgeWebView.registerHandler("logOut", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                App.clearAllStorage();
                //启动登录页面
                I.toLoginActivity(BrowserActivity.this);
            }
        });
        //图标角标
        mBridgeWebView.registerHandler("saveMarker", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                int count = Integer.parseInt(data);
                //清除未读信息角标
                if (count > 99) {
                    ShortcutBadger.applyCount(BrowserActivity.this, 99);
                } else {
                    ShortcutBadger.applyCount(BrowserActivity.this, count);
                }
            }
        });
        // H5调原生扫一扫
        mBridgeWebView.registerHandler("qrCodeScanning", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                ScannerActivity.show(BrowserActivity.this, new ScannerListener() {
                    @Override
                    public void getResult(String str) {
                        function.onCallBack(str);
                    }
                });
            }
        });
        // H5调原生相机
        mBridgeWebView.registerHandler("openCamera", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                mCallBackFunction = function;
                takePhoto();
            }
        });
        // H5调原生相册
        mBridgeWebView.registerHandler("openPhoto", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                mCallBackFunction = function;
                Matisse.from(BrowserActivity.this)
                        .choose(MimeType.ofImage(), false)
                        .countable(true)
                        .capture(true)
                        .captureStrategy(new CaptureStrategy(true, "com.yihua.program.provider", "test"))
                        .maxSelectable(9)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(BrowserActivity.this.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .setOnSelectedListener((uriList, pathList) -> {
                            Log.e("onSelected", "onSelected: pathList=" + pathList);
                        })
                        .showSingleMediaType(true)
                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .autoHideToolbarOnSingleTap(true)
                        .setOnCheckedListener(isChecked -> {
                            Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                        })
                        .forResult(REQUEST_CODE_CHOOSE);

            }
        });
    }


    /*
     * ---------------  拍照 start ------------------------
     */

    public final int REQUEST_TAKE_PHOTO = 1001;
    private String currentPhotoPath;

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri contentUri = FileProvider.getUriForFile(this, "com.yihua.program.provider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(currentPhotoPath)));
                }
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(timeStamp, ".jpeg", storageDir);
            currentPhotoPath = image.getAbsolutePath();
            return image;
        } catch (IOException ex) {
        }
        return null;
    }

    /**
     * ---------------  end ------------------------
     */






    /**
     * 扫二维码－－－－－－－－－－START－－－－－－－－
     */

    public interface ScannerListener {
        void getResult(String str);
    }

    public ScannerListener mScannerListener;

    public ScannerListener getScannerListener() {
        return mScannerListener;
    }

    public void setScannerListener(ScannerListener scannerListener) {
        mScannerListener = scannerListener;
    }

    /**
     * －－－－－－－－－－END－－－－－－－－
     */



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO://拍照
                    L.e(currentPhotoPath);
                    mCallBackFunction.onCallBack(currentPhotoPath);
                    break;
                case REQUEST_CODE_CHOOSE://相册
                    mCallBackFunction.onCallBack(String.valueOf(Matisse.obtainPathResult(data)));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onError(String msg) {
        IOSToast.showFail(this, msg);
    }

}
