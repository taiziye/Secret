package shengshoubo.example.com.secret.net;

/**
 * Created by shengshoubo on 2015/9/1.
 */
public class Comments {
    private String content,phone_md5;

    public Comments(String content, String phone_md5) {
        this.content = content;
        this.phone_md5 = phone_md5;
    }

    public String getContent() {
        return content;
    }

    public String getPhone_md5() {
        return phone_md5;
    }
}
