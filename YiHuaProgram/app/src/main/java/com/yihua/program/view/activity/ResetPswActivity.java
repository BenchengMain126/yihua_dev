package com.yihua.program.view.activity;

import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.yihua.program.view.contract.ResetPswContract;
import com.yihua.program.view.presenter.ResetPwsPresenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPswActivity extends BaseActivity<ResetPswContract.IPresenter> implements ResetPswContract.IView {
    private static final String TAG = "ResetPswActivity";

    @BindView(R.id.phone)
    TextInputEditText phone;
    @BindView(R.id.verify_code)
    TextInputEditText verifyCode;
    @BindView(R.id.verify_get_code)
    Button verifyGetCode;
    @BindView(R.id.new_pws)
    TextInputEditText newPws;
    @BindView(R.id.delete_pws)
    ImageView deletePws;
    @BindView(R.id.open_pws)
    ImageView openPws;
    @BindView(R.id.pswLogin)
    TextView pswLogin;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.version)
    TextView version;

    private boolean mMachPhoneNum;
    private String phoneNo;
    private boolean isOpenSee;

    @Override
    protected void initPresenter() {
        mPresenter = new ResetPwsPresenter(activity, this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_psw;
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
                    verifyCode.requestFocus();
                }else{
                    verifyGetCode.setTextColor(Color.WHITE);
                    verifyGetCode.setClickable(false);
                    verifyCode.setText("");
                    newPws.setText("");
                }
            }
        });

        //监听输入框变化，判断登录按钮是否可用
        TextChanger textChanger = new TextChanger();
        phone.addTextChangedListener(textChanger);
        verifyCode.addTextChangedListener(textChanger);
        newPws.addTextChangedListener(textChanger);

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
            } else if (newPws.getText().toString().length()<6) {
                return;
            } else if ( verifyCode.getText().toString().length()<6) {
                return;
            }else{
                btnLogin.setEnabled(true);
            }
        }
    }

    @OnClick({R.id.delete_pws, R.id.open_pws, R.id.btnLogin, R.id.pswLogin, R.id.verify_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.delete_pws:
                newPws.setText("");
                break;

            case R.id.open_pws:
                isOpenSee = !isOpenSee;
                openPws.setSelected(isOpenSee);
                if(isOpenSee){
                    newPws.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    newPws.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                }
                break;

            case R.id.btnLogin:
                if (!mMachPhoneNum) {
                    IOSToast.show(this, "手机号格式错误");
                    return;
                }else if (TextUtils.isEmpty(verifyCode.getText().toString())) {
                    IOSToast.show(this, "验证码不为空");
                    return;
                }else if (TextUtils.isEmpty(newPws.getText().toString())) {
                    IOSToast.show(this, "请输入新密码");
                    return;
                }else if (newPws.getText().toString().length() < 6) {
                    IOSToast.show(this, "新密码长度不少六位");
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("account", phone.getText().toString());
                params.put("verificationCode", verifyCode.getText().toString());
                params.put("newPassword", newPws.getText().toString());
                mPresenter.forgetPassword(params);
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

            case R.id.pswLogin:
                I.toLoginActivity(this);
                break;
        }
    }


    @Override
    public void onError(String msg) {
        IOSToast.showFail(this, msg);
    }

    @Override
    public void getVerificationCodeSuccess(String msg) {
        IOSToast.show(this, msg);
    }

    @Override
    public void getVerificationCodeFailure(String msg) {
        IOSToast.show(this, msg);
    }

    @Override
    public void forgetPasswordSuccess(String msg) {
        IOSToast.show(this, msg);
        P.getInstance(this).putAccount(phoneNo);
    }

    @Override
    public void forgetPasswordFailure(String msg) {
        IOSToast.show(this, msg);
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
