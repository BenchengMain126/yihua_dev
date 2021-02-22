package com.yihua.program.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.yihua.program.R;
import com.yihua.program.application.App;
import com.yihua.program.base.activity.BaseActivity;
import com.yihua.program.tools.I;
import com.yihua.program.tools.L;
import com.yihua.program.tools.P;
import com.yihua.program.utils.IOSToast;
import com.yihua.program.utils.RegularUtils;
import com.yihua.program.utils.StatusBarUtil;
import com.yihua.program.view.contract.SmsLoginContract;
import com.yihua.program.view.presenter.SmsLoginPresenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SmsLoginActivity extends BaseActivity<SmsLoginContract.IPresenter> implements SmsLoginContract.IView {
    @BindView(R.id.phone)
    TextInputEditText phone;
    @BindView(R.id.verify_code)
    TextInputEditText verifyCode;
    @BindView(R.id.verify_get_code)
    Button verifyGetCode;
    @BindView(R.id.psw_login)
    TextView pswLogin;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.version)
    TextView version;

    private boolean mMachPhoneNum;
    private String phoneNo;

    @Override
    protected void initPresenter() {
        mPresenter = new SmsLoginPresenter(activity, this);
    }

    @Override
    protected void initSome(Bundle savedInstanceState) {
        super.initSome(savedInstanceState);
        StatusBarUtil.initStatusBar(activity, true, true, true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_sms;
    }

    @Override
    protected void initView() {
        super.initView();
        version.setText("V" + App.getVersionName());

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                mMachPhoneNum = RegularUtils.isMobile(input);
                if (mMachPhoneNum) {
                    phoneNo = input;
                    verifyGetCode.setTextColor(Color.BLUE);
                    verifyGetCode.setClickable(true);
                } else {
                    verifyGetCode.setTextColor(Color.WHITE);
                    verifyGetCode.setClickable(false);
                }
            }
        });

        //监听输入框变化，判断登录按钮是否可用
        TextChanger textChanger = new TextChanger();
        phone.addTextChangedListener(textChanger);
        verifyCode.addTextChangedListener(textChanger);
    }

    class TextChanger implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btnLogin.setEnabled(false);
            if (!mMachPhoneNum) {
                return;
            }else if (verifyCode.getText().toString().length()<6) {
                return;
            }else{
                btnLogin.setEnabled(true);
            }
        }
    }


    @OnClick({R.id.psw_login, R.id.btnLogin, R.id.verify_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.psw_login:
                I.toLoginActivity(this);
                break;

            case R.id.btnLogin:
                if (!mMachPhoneNum) {
                    IOSToast.show(this, "手机号格式错误");
                    return;
                } else if (TextUtils.isEmpty(verifyCode.getText().toString())) {
                    IOSToast.show(this, "验证码不为空");
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("mobile", phone.getText().toString());
                params.put("verificationCode", verifyCode.getText().toString());
                params.put("source", String.valueOf(2));
                mPresenter.loginOrRegisterByMobile(params);
                break;

            case R.id.verify_get_code:
                if (mMachPhoneNum) {
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                    times = 60;
                    runnable = new MyRunnable();
                    handler.postDelayed(runnable, 1000);
                    verifyGetCode.setTextColor(Color.WHITE);

                    mPresenter.getVerificationCode(phoneNo);
                }
                break;
        }
    }

    @Override
    public void onError(String msg) {
        IOSToast.showFail(this, msg);
        verifyCode.setText("");
    }

    @Override
    public void getVerificationCodeSuccess(String msg) {
        IOSToast.show(this, msg);
        verifyCode.setText("");
    }

    @Override
    public void getVerificationCodeFailure(String msg) {
        IOSToast.show(this, msg);
        verifyCode.setText("");
    }

    @Override
    public void loginOrRegisterSuccess(String msg) {
        String phoneNo = phone.getText().toString();
        P.getInstance(this).putAccount(phoneNo);
        P.getInstance(this).putLogin(true);
        I.toBrowserActivity(this);
    }

    @Override
    public void loginOrRegisterFailure(String msg) {
        IOSToast.showFail(this, msg);
        verifyCode.setText("");
    }

    @Override
    protected void onDestroy() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    //倒计时
    private int times = 60;
    private Handler handler = new Handler();
    private MyRunnable runnable;

    //倒计时
    class MyRunnable implements Runnable {
        @Override
        public void run() {
            times--;
            verifyGetCode.setText("重新发送(" + times + ")");
            if (times == 0) {
                verifyGetCode.setText("获取验证码");
                times = 60;
                verifyGetCode.setClickable(true);
                verifyGetCode.setTextColor(Color.BLUE);
            } else {
                verifyGetCode.setClickable(false);
                handler.postDelayed(runnable, 1000);
            }
        }
    }
}
