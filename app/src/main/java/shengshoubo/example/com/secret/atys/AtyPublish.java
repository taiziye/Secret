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
import shengshoubo.example.com.secret.net.Publish;

/**
 * Created by shengshoubo on 2015/8/30.
 */
public class AtyPublish extends Activity {

    private EditText etMsgContent;
    private String phone_md5,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_publish);
        phone_md5=getIntent().getStringExtra(Config.KEY_PHONE_MD5);
        token=getIntent().getStringExtra(Config.KEY_TOKEN);

        etMsgContent= (EditText) findViewById(R.id.etMsgContent);
        findViewById(R.id.btnPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etMsgContent.getText())){
                    Toast.makeText(AtyPublish.this,R.string.message_content_can_not_be_empty,Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd=ProgressDialog.show(AtyPublish.this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));
                new Publish(phone_md5, token, etMsgContent.getText().toString(), new Publish.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        setResult(Config.ACTIVITY_RESULT_NEED_REFRESH);
                        Toast.makeText(AtyPublish.this,R.string.success_to_publish,Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, new Publish.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        pd.dismiss();
                        if(errorCode==Config.RESULT_STATUS_INVALID_TOKEN){
                            startActivity(new Intent(AtyPublish.this,AtyLogin.class));
                            finish();
                        }else {
                            Toast.makeText(AtyPublish.this,R.string.fail_to_publish,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
