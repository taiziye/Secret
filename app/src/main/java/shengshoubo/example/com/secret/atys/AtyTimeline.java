package shengshoubo.example.com.secret.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import shengshoubo.example.com.secret.Config;
import shengshoubo.example.com.secret.R;
import shengshoubo.example.com.secret.localdata.MyContacts;
import shengshoubo.example.com.secret.net.TimelineMessage;
import shengshoubo.example.com.secret.net.Timeline;
import shengshoubo.example.com.secret.net.UploadContacts;
import shengshoubo.example.com.secret.tools.MD5Tool;

/**
 * Created by shengshoubo on 2015/8/30.
 */
public class AtyTimeline extends ActionBarActivity {

    private String phone_Num,token,phone_md5;
    private AtyTimelineMessageListAdapter adapter=null;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_timeline);

        listView= (ListView) findViewById(R.id.list);
        adapter=new AtyTimelineMessageListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TimelineMessage msg= (TimelineMessage) adapter.getItem(position);
                Intent intent=new Intent(AtyTimeline.this,AtyMessage.class);
                intent.putExtra(Config.KEY_MSG,msg.getMsg());
                intent.putExtra(Config.KEY_MSG_ID,msg.getMsgID());
                intent.putExtra(Config.KEY_PHONE_MD5,msg.getPhone_md5());
                intent.putExtra(Config.KEY_TOKEN,token);
                startActivity(intent);
            }
        });

        phone_Num=getIntent().getStringExtra(Config.KEY_PHONE_NUM);
        token=getIntent().getStringExtra(Config.KEY_TOKEN);
        phone_md5= MD5Tool.md5(phone_Num);

        final ProgressDialog pd=ProgressDialog.show(this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));
        new UploadContacts(phone_md5, token, MyContacts.getContactsJSONString(this), new UploadContacts.SuccessCallback() {
            @Override
            public void onSuccess() {
                loadMessage();
                pd.dismiss();
            }
        }, new UploadContacts.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();
//                Toast.makeText(getApplicationContext(),errorCode+"",Toast.LENGTH_LONG).show();
                if(errorCode==Config.RESULT_STATUS_INVALID_TOKEN){
                    startActivity(new Intent(AtyTimeline.this,AtyLogin.class));
                    finish();
                }else{
                    loadMessage();
                }
            }
        });
    }

    private void loadMessage(){
//        Toast.makeText(this,"loadMessage",Toast.LENGTH_LONG).show();
        final ProgressDialog pd=ProgressDialog.show(this,getResources().getString(R.string.connecting),getResources().getString(R.string.connecting_to_server));

        new Timeline(phone_md5, token, 1, 20, new Timeline.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<TimelineMessage> timeline) {
                pd.dismiss();
                adapter.clear();
                adapter.addAll(timeline);
            }
        }, new Timeline.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();
                if(errorCode==Config.RESULT_STATUS_INVALID_TOKEN){
                    startActivity(new Intent(AtyTimeline.this,AtyLogin.class));
                    finish();
                }else {
                    Toast.makeText(AtyTimeline.this,R.string.fail_to_load_timeline_data,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aty_timeline,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuShowAtyPublish:
                Intent intent=new Intent(AtyTimeline.this,AtyPublish.class);
                intent.putExtra(Config.KEY_PHONE_MD5,phone_md5);
                intent.putExtra(Config.KEY_TOKEN,token);
                startActivityForResult(intent,0);
                break;
            default:

                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Config.ACTIVITY_RESULT_NEED_REFRESH:
                loadMessage();
                break;
            default:

                break;
        }
    }
}
