package com.yihua.program.view.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yihua.program.R;
import com.yihua.program.base.activity.BaseActivity;
import com.yihua.program.view.contract.ScannerContract;
import com.yihua.program.view.presenter.BrowserPresenter;
import com.yihua.program.view.presenter.ScannerPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * @描述 扫一扫界面
 */

public class ScannerActivity extends BaseActivity<ScannerContract.IPresenter> implements ScannerContract.IView, ZXingScannerView.ResultHandler {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.flash_light)
    ImageView mFlashLight;


    private static final float BEEP_VOLUME = 0.10f;
    private static final String FLASH_STATE = "FLASH_STATE";

    private MediaPlayer mMmediaPlayer;
    private boolean mCanPlayBeep = true;
    private ZXingScannerView mScannerView;
    private MyScanFinderView mMyScanFinderView;
    private boolean mPrepareded = false;
    private boolean mDoPost = false;
    private boolean vibrate = true;
    private static final long VIBRATE_DURATION = 200L;
    private static BrowserActivity.ScannerListener listener;
    private boolean mFlash;

    public static void show(Activity context, BrowserActivity.ScannerListener scannerListener) {
        Intent intent = new Intent(context, ScannerActivity.class);
        context.startActivity(intent);
        listener = scannerListener;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ScannerPresenter(activity, this);
    }

    @Override
    protected void initSome(Bundle savedInstanceState) {
        super.initSome(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @OnClick({R.id.flash_light})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.flash_light:
                mFlash = !mFlash;
                mScannerView.setFlash(mFlash);
                if (mFlash) {
                    mFlashLight.setImageDrawable(getResources().getDrawable(R.mipmap.flash_light_on));
                } else {
                    mFlashLight.setImageDrawable(getResources().getDrawable(R.mipmap.flash_light_off));
                }
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mDoPost = false;
        initScanner();
    }

    private void initScanner() {
        initBeepSound();
        mMyScanFinderView = new MyScanFinderView(this);
        mMyScanFinderView.setMaskColor(getResources().getColor(R.color.viewfinder_mask));
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return mMyScanFinderView;
            }

            @Override
            public void resumeCameraPreview(ResultHandler resultHandler) {
                super.resumeCameraPreview(resultHandler);
                if (mMyScanFinderView != null) {
                    mMyScanFinderView.startAnimator();
                }
            }

            @Override
            public void startCamera() {
                super.startCamera();
                if (mMyScanFinderView != null) {
                    mMyScanFinderView.startAnimator();
                }
            }

            @Override
            public void stopCamera() {
                super.stopCamera();
                if (mMyScanFinderView != null) {
                    mMyScanFinderView.stopAnimator();
                }
            }

            @Override
            public void stopCameraPreview() {
                super.stopCameraPreview();
                if (mMyScanFinderView != null) {
                    mMyScanFinderView.stopAnimator();
                }
            }
        };
        mScannerView.setAspectTolerance(0.2f);
        //  mScannerView.setShouldScaleToFill(true);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);
        mScannerView.setAutoFocus(true);
        flContent.addView(mScannerView);
    }

    private void playBeepSoundAndVibrate() {
        if (mCanPlayBeep && mMmediaPlayer != null && mPrepareded) {
            try {
                mMmediaPlayer.start();
            } catch (IllegalStateException e) {
            }
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private void initBeepSound() {
        AudioManager audioService = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            mCanPlayBeep = false;
        }
        if (!mCanPlayBeep || mMmediaPlayer != null) {
            return;
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mPrepareded = false;
        mMmediaPlayer = new MediaPlayer();
        mMmediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMmediaPlayer.setOnCompletionListener(mediaPlayer -> mediaPlayer.seekTo(0));
        mMmediaPlayer.setOnPreparedListener(mp -> mPrepareded = true);
        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
        try {
            mMmediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mMmediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mMmediaPlayer.prepareAsync();
        } catch (IOException e) {
            mMmediaPlayer = null;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        mScannerView.destroyDrawingCache();
        super.onDestroy();
    }


    public static String resultStr;

    @Override
    public void handleResult(Result result) {
        playBeepSoundAndVibrate();
        if (listener != null) {
            listener.getResult(result.getText());
        }
        finish();
    }

    private class MyScanFinderView extends FrameLayout implements IViewFinder {
        private Context mContext;
        private View mRootView;
        private ImageView mIvScanLine;
        private ImageView mIvScanBlock;
        private float mStartY;
        private float mEndY;
        private Handler mHandler;
        private ValueAnimator mObjectAnimator;

        public MyScanFinderView(Context context) {
            super(context);
            initView(context);
        }

        public MyScanFinderView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initView(context);
        }

        public MyScanFinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView(context);
        }

        private void initView(Context context) {
            this.mContext = context;
            mRootView = LayoutInflater.from(mContext).inflate(R.layout.view_scan_finder, null);
            mIvScanBlock = (ImageView) mRootView.findViewById(R.id.iv_scan_block);
            mIvScanLine = (ImageView) mRootView.findViewById(R.id.iv_scan_line);

            mHandler = new Handler();
            addView(mRootView);
        }

        @Override
        public void setLaserColor(int i) {

        }

        @Override
        public void setMaskColor(int maskColor) {

        }

        @Override
        public void setBorderColor(int i) {

        }

        @Override
        public void setBorderStrokeWidth(int i) {

        }

        @Override
        public void setBorderLineLength(int i) {

        }

        @Override
        public void setLaserEnabled(boolean b) {
        }

        @Override
        public void setBorderCornerRounded(boolean b) {
        }

        @Override
        public void setBorderAlpha(float v) {
        }

        @Override
        public void setBorderCornerRadius(int i) {
        }

        @Override
        public void setViewFinderOffset(int i) {
        }

        @Override
        public void setSquareViewFinder(boolean b) {
        }

        @Override
        public void setupViewFinder() {
            this.invalidate();
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Rect getFramingRect() {
            Rect mFramingRect = new Rect();
            mStartY = mIvScanBlock.getY();
            mEndY = mStartY + mIvScanBlock.getHeight() - 100;
            mHandler.post(this::startAnimator);
            mFramingRect.set(mIvScanBlock.getLeft(), mIvScanBlock.getTop(), mIvScanBlock.getRight(), mIvScanBlock.getBottom());
            return mFramingRect;
        }

        public void startAnimator() {
            if (mStartY == mEndY) return;
            if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
                return;
            }
            mObjectAnimator = ValueAnimator.ofFloat(mStartY, mEndY);
            mObjectAnimator.setDuration(3000);
            mObjectAnimator.setInterpolator(new LinearInterpolator());
            mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mObjectAnimator.addUpdateListener((animation) -> mIvScanLine.setY((Float) animation.getAnimatedValue()));
            mObjectAnimator.start();
        }

        public void stopAnimator() {
            mHandler.removeCallbacksAndMessages(null);
            if (mObjectAnimator != null)
                mObjectAnimator.cancel();
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (listener != null) {
                listener.getResult("");
            }
            finish();
            return true;
        }
        return false;
    }


}
