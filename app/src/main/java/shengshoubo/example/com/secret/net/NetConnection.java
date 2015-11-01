package shengshoubo.example.com.secret.net;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import shengshoubo.example.com.secret.Config;

/**
 * Created by shengshoubo on 2015/8/30.
 */
public class NetConnection {

    //指明通信的具体参数（参数对），采用字符串可变参数
    public NetConnection(final String url,final HttpMethod method,final SuccessCallback successCallback, final FailCallback failCallback,final String ... kvs){


        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                StringBuffer paramStr=new StringBuffer();
                for(int i=0;i<kvs.length;i+=2){
                    //注意最后一对键值对后面没有“&”
                    paramStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
                }
                try {
                    URLConnection uc;
                    switch (method){
                        case POST:
                            uc=new URL(url).openConnection();
                            uc.setDoOutput(true);
                            OutputStream os=uc.getOutputStream();
                            OutputStreamWriter osw=new OutputStreamWriter(os,Config.CHARSET);
                            BufferedWriter bw=new BufferedWriter(osw);
                            bw.write(paramStr.toString());
                            bw.flush();
                            bw.close();
                            osw.close();
                            os.close();
                            break;
                        default:
                            uc=new URL(url+"?"+paramStr.toString()).openConnection();
                            break;
                    }

                    System.out.println("Request url:"+uc.getURL());
                    System.out.println("Request data:"+paramStr.toString());

                    InputStream is=uc.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is,Config.CHARSET);
                    BufferedReader br=new BufferedReader(isr);
                    String line;
                    StringBuffer result=new StringBuffer();
                    while((line=br.readLine())!=null){
                        result.append(line);
                    }
                    br.close();
                    isr.close();
                    is.close();

                    System.out.println("result:"+result);

                    return result.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s!=null){
                    if(successCallback!=null){
                        successCallback.onSuccess(s);
                    }
                }else{
                    if (failCallback!=null){
                        failCallback.onFail();
                    }

                }
            }
        }.execute();
    }

    //通知调用者
    public static interface SuccessCallback{
        void onSuccess(String result);
    }

    public static interface FailCallback{
        void onFail();
    }
}
