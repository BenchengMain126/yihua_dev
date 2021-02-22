/*
package com.yihua.program.view.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnDownloadListenerAdapter;
import com.azhon.appupdate.manager.DownloadManager;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.yihua.program.R;
import com.yihua.program.api.ApiHelper;
import com.yihua.program.application.App;
import com.yihua.program.base.presenter.BasePresenter;
import com.yihua.program.bean.UpgradeResponse;
import com.yihua.program.tools.L;
import com.yihua.program.tools.T;
import com.yihua.program.view.contract.BrowserContract;

import java.util.HashMap;
import java.util.Map;


public class BrowserPresenter extends BasePresenter<BrowserContract.IView> implements BrowserContract.IPresenter {

    public BrowserPresenter(Activity activity, BrowserContract.IView iv) {
        super(activity, iv);
    }

    @Override
    public void getAppVersionUsing() {
        Map<String, String> params = new HashMap<>();
        ApiHelper.getYiHuaApi()
                .upgrade(params)
                .compose(Transformer.<UpgradeResponse>switchSchedulers())
                .subscribe(new CommonObserver<UpgradeResponse>() {
                    @Override
                    protected void onError(String errorMsg) {
                        L.e(errorMsg);
                    }

                    @Override
                    protected void onSuccess(UpgradeResponse data) {
                        if (data.getCode() == 1 && (App.getVersionCode() < Integer.parseInt(data.getData().getVersionCode()))) {
                            progressDialog();
                            UpdateConfiguration configuration = new UpdateConfiguration()
                                    //输出错误日志
                                    .setEnableLog(true)
                                    .setJumpInstallPage(true)
                                    //设置强制更新
                                    .setForcedUpgrade(true)
                                    //设置下载过程的监听
                                    .setOnDownloadListener(listenerAdapter);
                            DownloadManager.getInstance(mActivity)
                                    .setApkName("appUpdate.apk")
                                    .setApkUrl(data.getData().getDownloadUrl())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setConfiguration(configuration)
                                    .download();
                        }
                    }
                });
    }


    private OnDownloadListenerAdapter listenerAdapter = new OnDownloadListenerAdapter() {
        @Override
        public void downloading(int max, int progress) {
            int curr = (int) (progress / (double) max * 100.0);
            progressBar.setMax(100);
            progressBar.setProgress(curr);
            if (progress == max) {
                tDialog.dismiss();
            }
        }
    };

    private TDialog tDialog;
    private ProgressBar progressBar;

    public void progressDialog() {
        tDialog = new TDialog.Builder(mActivity.getFragmentManager())
                .setLayoutRes(R.layout.dialog_loading_progress)
                .setScreenWidthAspect(mActivity, 0.8f)
                .setDimAmount(0.5f)     //设置弹窗背景透明度(0-1f)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(false)
                .setDialogAnimationRes(R.style.animate_dialog_scale)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            T.s("升级中，请勿退出");
                            return true;
                        }
                        return false;
                    }
                })
                .setOnBindViewListener(new OnBindViewListener() {
                    @Override
                    public void bindView(BindViewHolder viewHolder) {
                        viewHolder.setText(R.id.tv_title, "应用升级中，请勿退出");
                        progressBar = viewHolder.getView(R.id.progress_bar);
                    }
                })
                .create()
                .show();
    }


}
*/

package com.yihua.program.view.presenter;

import android.app.Activity;
import android.graphics.Color;

import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.yihua.program.R;
import com.yihua.program.api.ApiHelper;
import com.yihua.program.application.App;
import com.yihua.program.base.presenter.BasePresenter;
import com.yihua.program.bean.UpgradeResponse;
import com.yihua.program.tools.L;
import com.yihua.program.utils.IOSToast;
import com.yihua.program.view.activity.BrowserActivity;
import com.yihua.program.view.contract.BrowserContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BrowserPresenter extends BasePresenter<BrowserContract.IView> implements BrowserContract.IPresenter {

    public BrowserPresenter(Activity activity, BrowserContract.IView iv) {
        super(activity, iv);
    }

    @Override
    public void getAppVersionUsing() {

        XXPermissions.with(mActivity)
                .permission(Permission.CAMERA)
                .permission(Permission.Group.STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            checkUpdate();
                        } else {
                            IOSToast.show(mActivity, "部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            IOSToast.show(mActivity, "部分权限被永久拒绝授权，请手动授予");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(mActivity, denied);
                        } else {
                            IOSToast.show(mActivity, "应用权限未正常授予");
                        }
                    }
                });
    }

    private void checkUpdate() {
        Map<String, String> params = new HashMap<>();
        ApiHelper.getYiHuaApi()
                .upgrade(params)
                .compose(Transformer.<UpgradeResponse>switchSchedulers())
                .subscribe(new CommonObserver<UpgradeResponse>() {
                    @Override
                    protected void onError(String errorMsg) {
                        L.e(errorMsg);
                    }

                    @Override
                    protected void onSuccess(UpgradeResponse data) {
                        if (data.getCode() == 1 && (App.getVersionCode() < Integer.parseInt(data.getData().getVersionCode()))) {
                            startUpdate3(data.getData());
                        }
                    }
                });
    }


    private void startUpdate3(UpgradeResponse.DataBean data) {
        /*
         * 整个库允许配置的内容
         * 非必选
         */
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                .setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                .setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置对话框强制更新时进度条和文字的颜色
                .setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(false)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置是否上报数据
                .setUsePlatform(false)
                //设置强制更新
                .setForcedUpgrade(true);

        DownloadManager.getInstance(mActivity)
                .setApkName("app.apk")
                .setApkUrl(data.getDownloadUrl())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setApkVersionCode(Integer.parseInt(data.getVersionCode()))
                .setApkVersionName(data.getVersionNo())
                .setApkSize("20.4")
                .setApkDescription(data.getUpdateContent().toString())
                .download();
    }

}
