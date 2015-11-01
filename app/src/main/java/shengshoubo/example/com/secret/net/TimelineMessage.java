package shengshoubo.example.com.secret.net;

/**
 * Created by shengshoubo on 2015/9/1.
 */
public class TimelineMessage {
    private String msgID=null;
    private String msg=null;
    private String phone_md5=null;

    //创建Message之后便不能再被修改，因此不需要使用setters方法
    public TimelineMessage(String msgID, String msg, String phone_md5) {
        this.msgID = msgID;
        this.msg = msg;
        this.phone_md5 = phone_md5;
    }

    public String getMsgID() {
        return msgID;
    }

    public String getMsg() {
        return msg;
    }

    public String getPhone_md5() {
        return phone_md5;
    }
}
