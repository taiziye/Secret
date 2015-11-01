package shengshoubo.example.com.secret.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shengshoubo.example.com.secret.Config;

/**
 * Created by shengshoubo on 2015/8/31.
 */
public class Timeline {
    public Timeline(String phone_md5,String token,int page,int perpage,final SuccessCallback successCallback,final FailCallback failCallback) {
        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(result);
                    switch (jsonObject.getInt(Config.KEY_STATUS)){
                        case Config.RESULT_STATUS_SUCCESS:
                                if(successCallback!=null){
                                    List<TimelineMessage> msgs=new ArrayList<TimelineMessage>();
                                    JSONArray msgJsonArray=jsonObject.getJSONArray(Config.KEY_TIMELINE);
                                    JSONObject msgJsonObject;
                                    for(int i=0;i<msgJsonArray.length();i++){
                                        msgJsonObject=msgJsonArray.getJSONObject(i);
                                        msgs.add(new TimelineMessage(
                                                msgJsonObject.getString(Config.KEY_MSG_ID),
                                                msgJsonObject.getString(Config.KEY_MSG),
                                                msgJsonObject.getString(Config.KEY_PHONE_MD5)
                                        ));
                                    }
                                    successCallback.onSuccess(jsonObject.getInt(Config.KEY_PAGE),jsonObject.getInt(Config.KEY_PERPAGE),msgs);
                                }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if(failCallback!=null){
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if(failCallback!=null){
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(failCallback!=null){
                        failCallback.onFail(Config.RESULT_STATUS_FAIL);
                    }
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if(failCallback!=null){
                    failCallback.onFail(Config.RESULT_STATUS_FAIL);
                }
            }
        },Config.KEY_ACTION,Config.ACTION_TIMELINE,
                Config.KEY_PHONE_MD5,phone_md5,
                Config.KEY_TOKEN,token,
                Config.KEY_PAGE,page+"",
                Config.KEY_PERPAGE,perpage+"");
    }

    public static interface SuccessCallback{
        void onSuccess(int page,int perpage,List<TimelineMessage> timeline);
    }

    public static interface FailCallback{
        void onFail(int errorCode);
    }


}


