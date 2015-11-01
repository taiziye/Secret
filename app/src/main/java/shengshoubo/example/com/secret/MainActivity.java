package shengshoubo.example.com.secret;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import shengshoubo.example.com.secret.atys.AtyLogin;
import shengshoubo.example.com.secret.atys.AtyTimeline;
import shengshoubo.example.com.secret.localdata.MyContacts;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MyContacts.getContactsJSONString(this));
        String token=Config.getCatchedToken(this);
        String phone_Num=Config.getCatchedPhoneNum(this);
        if(token!=null&&phone_Num!=null){
            Intent intent=new Intent(MainActivity.this, AtyTimeline.class);
            //Timeline需要加载用户的数据，即用户发表的消息
            intent.putExtra(Config.KEY_TOKEN,token);
            intent.putExtra(Config.KEY_PHONE_NUM,phone_Num);
            startActivity(intent);
        }else {
            startActivity(new Intent(MainActivity.this, AtyLogin.class));
        }
//        startActivity(new Intent(MainActivity.this, AtyLogin.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
