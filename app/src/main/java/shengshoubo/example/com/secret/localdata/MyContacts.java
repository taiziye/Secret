package shengshoubo.example.com.secret.localdata;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shengshoubo.example.com.secret.Config;
import shengshoubo.example.com.secret.tools.MD5Tool;

/**
 * Created by shengshoubo on 2015/8/30.
 */
public class MyContacts {

    public static String getContactsJSONString(Context context) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String phoneNum = null;
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject;
        while (cursor.moveToNext()) {
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (phoneNum.charAt(0) == '+' && phoneNum.charAt(1) == '8' && phoneNum.charAt(2) == '6') {
                phoneNum=phoneNum.substring(3);
            }
            jsonObject=new JSONObject();
            try {
                jsonObject.put(Config.KEY_PHONE_MD5, MD5Tool.md5(phoneNum));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }
}
