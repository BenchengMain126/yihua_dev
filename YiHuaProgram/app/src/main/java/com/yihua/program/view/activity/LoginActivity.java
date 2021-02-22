package com.yihua.program.view.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.yihua.program.R;
import com.yihua.program.application.App;
import com.yihua.program.base.activity.BaseActivity;
import com.yihua.program.tools.I;
import com.yihua.program.tools.L;
import com.yihua.program.tools.P;
import com.yihua.program.tools.T;
import com.yihua.program.utils.IOSToast;
import com.yihua.program.utils.RegularUtils;
import com.yihua.program.view.contract.LoginContract;
import com.yihua.program.view.presenter.LoginPresenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginContract.IPresenter> implements LoginContract.IView {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.phone)
    TextInputEditText phone;
    @BindView(R.id.psw)
    TextInputEditText psw;
    @BindView(R.id.verify_code)
    TextInputEditText verifyCode;
    @BindView(R.id.verify_img_code)
    ImageView verifyImgCode;
    @BindView(R.id.refresh_code)
    ImageView refreshCode;
    @BindView(R.id.sms_login)
    TextView smsLogin;
    @BindView(R.id.forgot_psw)
    TextView forgotPsw;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.verify_ly)
    LinearLayout verifyLy;
    @BindView(R.id.baseUrl)
    TextView baseUrl;

    private boolean mMachPhoneNum;
    private String phoneNo;
    private boolean mShowVerifyCode;


    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(activity, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        mPresenter.CheckPermission();
        version.setText("V" + App.getVersionName());
        if (!TextUtils.isEmpty(P.getInstance(this).getAccount())) {
            phone.setText(P.getInstance(this).getAccount());
            phoneNo = P.getInstance(this).getAccount();
            mMachPhoneNum = true;
        }

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
                }
            }
        });

        //监听输入框变化，判断登录按钮是否可用
        TextChanger textChanger = new TextChanger();
        phone.addTextChangedListener(textChanger);
        psw.addTextChangedListener(textChanger);
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
            } else if (psw.getText().toString().length() < 6) {
                return;
            } else if (mShowVerifyCode && verifyCode.getText().toString().length() < 4) {
                return;
            } else {
                btnLogin.setEnabled(true);
            }
        }
    }

    @OnClick({R.id.refresh_code, R.id.sms_login, R.id.forgot_psw, R.id.btnLogin, R.id.baseUrl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sms_login:
                I.toSmsLoginActivity(this);
                break;

            case R.id.forgot_psw:
                I.toResetPswActivity(this);
                break;

            case R.id.baseUrl:
                new TDialog.Builder(getFragmentManager())
                        .setLayoutRes(R.layout.dialog_confrim_cancel)
                        .setScreenWidthAspect(this, 0.85f)
                        .setDimAmount(0.4f)     //设置弹窗背景透明度(0-1f)
                        .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                        .setCancelableOutside(false)
                        .setDialogAnimationRes(R.style.animate_dialog_scale)
                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    return true;
                                }
                                return false;
                            }
                        })
                        .setOnBindViewListener(new OnBindViewListener() {   //通过BindViewHolder拿到控件对象,进行修改
                            @Override
                            public void bindView(BindViewHolder bindViewHolder) {
                                bindViewHolder.setText(R.id.tv_title, "配置接口域名");
                                bindViewHolder.setText(R.id.tv_cancel, "取消");
                                bindViewHolder.setText(R.id.tv_confirm, "保存");
                                bindViewHolder.setText(R.id.tv_upgrade_content, P.getInstance(LoginActivity.this).getBaseUrl());
                            }
                        })
                        .addOnClickListener(R.id.tv_cancel, R.id.tv_confirm)   //添加进行点击控件的id
                        .setOnViewClickListener(new OnViewClickListener() {
                            @Override
                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                                TextInputEditText editInput = viewHolder.getView(R.id.tv_upgrade_content);
                                String baseUrl = editInput.getText().toString();
                                switch (view.getId()) {
                                    case R.id.tv_cancel:
                                        tDialog.dismiss();
                                        break;
                                    case R.id.tv_confirm:
                                        if (TextUtils.isEmpty(baseUrl)) {
                                            T.s("请完善域名地址");
                                        } else {
                                            tDialog.dismiss();
                                            P.getInstance(LoginActivity.this).putBaseUrl(baseUrl);
                                            T.s("配置成功,重启应用");
                                            App.reLaunchApp();
                                        }
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
                break;

            case R.id.refresh_code:
                Animation anim = new RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(true); // 设置保持动画最后的状态
                anim.setDuration(800); // 设置动画时间
                anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
                refreshCode.startAnimation(anim);
                mPresenter.createImageCode(phone.getText().toString());
                break;

            case R.id.btnLogin:
                if (!mMachPhoneNum) {
                    IOSToast.show(this, "手机号格式错误");
                    return;
                } else if (TextUtils.isEmpty(psw.getText().toString())) {
                    IOSToast.show(this, "密码不为空");
                    return;
                } else if (mShowVerifyCode && TextUtils.isEmpty(verifyCode.getText().toString())) {
                    IOSToast.show(this, "验证码不为空");
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put("account", phone.getText().toString());
                params.put("password", psw.getText().toString());
                params.put("source", String.valueOf(2));
                if (mShowVerifyCode) {
                    params.put("code", verifyCode.getText().toString());
                }
                mPresenter.login(params);
                break;

        }
    }

    @Override
    public void onError(String msg) {
        IOSToast.showFail(this, msg);
    }

    @Override
    public void createImageCodeSuccess(Bitmap bitmap) {
        verifyImgCode.setImageBitmap(bitmap);
    }

    @Override
    public void loginSuccess(String msg) {
        P.getInstance(this).putAccount(phoneNo);
        P.getInstance(this).putLogin(true);
        I.toBrowserActivity(this);
    }

    @Override
    public void loginShowVerifyFailure(String msg) {
        IOSToast.showFail(this, msg);
        mShowVerifyCode = true;
        verifyCode.setText("");
        verifyLy.setVisibility(View.VISIBLE);
        mPresenter.createImageCode(phone.getText().toString());
    }

    @Override
    public void loginFailure(String msg) {
        IOSToast.showFail(this, msg);
    }
}
