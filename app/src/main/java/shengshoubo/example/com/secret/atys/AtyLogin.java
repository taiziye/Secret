package shengshoubo.example.com.secret.atys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import shengshoubo.example.com.secret.Config;
import shengshoubo.example.com.secret.R;
import shengshoubo.example.com.secret.net.GetCode;
import shengshoubo.example.com.secret.net.Login;
import shengshoubo.example.com.secret.tools.MD5Tool;

/**
 * Created by shengshoubo on 2015/8/30.
 */
public class AtyLogin extends Activity {

    private EditText etPhone=null;
    private EditText etCode=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        etPhone= (EditText) findViewById(R.id.etPhoneNum);
        etCode= (EditText) findViewById(R.id.etCode);
        findViewById(R.id.btnGetCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPhone.getText())){
                    Toast.makeText(AtyLogin.this,R.string.phone_num_can_not_be_empty,Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog pd=ProgressDialog.show(AtyLogin.this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));
                new GetCode(etPhone.getText().toString(), new GetCode.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        Toast.makeText(AtyLogin.this,R.string.success_to_get_code,Toast.LENGTH_LONG).show();
                    }
                }, new GetCode.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();
                        Toast.makeText(AtyLogin.this,R.string.fail_to_get_code,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPhone.getText())){
                    Toast.makeText(AtyLogin.this,R.string.phone_num_can_not_be_empty,Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(etCode.getText())){
                    Toast.makeText(AtyLogin.this,R.string.code_can_not_be_empty,Toast.LENGTH_LONG).show();
                }

                final ProgressDialog pd=ProgressDialog.show(AtyLogin.this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));
                //获取手机号的MD5加密需要使用MD5工具
                new Login(MD5Tool.md5(etPhone.getText().toString()), etCode.getText().toString(), new Login.SuccessCallback() {
                    @Override
                    public void onSuccess(String token) {
                        pd.dismiss();
                        Config.cacheToken(AtyLogin.this, token);
                        Config.cachePhoneNum(AtyLogin.this, etPhone.getText().toString());

                        Intent intent=new Intent(AtyLogin.this,AtyTimeline.class);
                        intent.putExtra(Config.KEY_TOKEN,token);
                        intent.putExtra(Config.KEY_PHONE_NUM,etPhone.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }, new Login.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();
                        Toast.makeText(AtyLogin.this,R.string.fail_to_login,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
