package shengshoubo.example.com.secret.atys;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import shengshoubo.example.com.secret.Config;
import shengshoubo.example.com.secret.R;
import shengshoubo.example.com.secret.net.Comments;
import shengshoubo.example.com.secret.net.GetComment;
import shengshoubo.example.com.secret.net.PubComment;
import shengshoubo.example.com.secret.tools.MD5Tool;

/**
 * Created by shengshoubo on 2015/8/30.
 */
public class AtyMessage extends ActionBarActivity {

    private String phone_md5,msg,msgId,token;
    private TextView tvMessage;
    private AtyMessageCommentListAdapter adapter;
    private EditText etComment;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_message);

        listView= (ListView) findViewById(R.id.list);
        adapter=new AtyMessageCommentListAdapter(this);
        listView.setAdapter(adapter);

        tvMessage= (TextView) findViewById(R.id.tvMessage);
        etComment= (EditText) findViewById(R.id.etAddComment);

        Intent data=getIntent();
        phone_md5=data.getStringExtra(Config.KEY_PHONE_MD5);
        msg=data.getStringExtra(Config.KEY_MSG);
        msgId=data.getStringExtra(Config.KEY_MSG_ID);
        token=data.getStringExtra(Config.KEY_TOKEN);

        tvMessage.setText(msg);

        final ProgressDialog pd=ProgressDialog.show(this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));
        new GetComment(phone_md5,token,msgId,1,20, new GetComment.SuccessCallback() {
            @Override
            public void onSuccess(String megId, int page, int perpage, List<Comments> comments) {

                pd.dismiss();
                adapter.clear();
                adapter.addAll(comments);
            }
        }, new GetComment.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();
                if(errorCode==Config.RESULT_STATUS_INVALID_TOKEN){
                    startActivity(new Intent(AtyMessage.this,AtyLogin.class));
                    finish();
                }else {
                    Toast.makeText(AtyMessage.this,R.string.fail_to_get_comment,Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.btnSendComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content=etComment.getText().toString();
                final String local_phone_md5=MD5Tool.md5(Config.getCatchedPhoneNum(AtyMessage.this));

                if(TextUtils.isEmpty(content)){
                    Toast.makeText(AtyMessage.this,R.string.comment_content_can_not_be_empty,Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd=ProgressDialog.show(AtyMessage.this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));
                new PubComment(local_phone_md5, token, content, msgId, new PubComment.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        etComment.setText("");
                        Comments comments=new Comments(content,local_phone_md5);
                        adapter.getComments().add(comments);
                        adapter.notifyDataSetChanged();
                    }
                }, new PubComment.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        pd.dismiss();
                        if(errorCode==Config.RESULT_STATUS_INVALID_TOKEN){
                            startActivity(new Intent(AtyMessage.this,AtyLogin.class));
                            finish();
                        }else {
                            Toast.makeText(AtyMessage.this,R.string.fail_to_publish_comment,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
