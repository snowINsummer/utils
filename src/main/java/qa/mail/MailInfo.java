package qa.mail;

import javax.mail.internet.InternetAddress;
import java.util.List;

public class MailInfo {
    //邮箱服务器 如smtp.163.com
    private String host ;
    //端口
    private String port;
    //用户邮箱 如**@163
    private String formName ;
    // 别名
    private String formNameDes;
    //用户授权码 不是用户名密码 可以自行查看相关邮件服务器怎么查看
    private String formPassword ;
    //消息回复邮箱
    private String replayAddress ;
    //发送地址
    private String toAddress ;
    //发送主题
    private String subject ;
    //发送内容
    private String content ;
    // 收件人集合
    private List<String> allAdress;
    // 多个收件人
    private InternetAddress[] recipient;

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getFormName() {
        return formName;
    }
    public void setFormName(String formName) {
        this.formName = formName;
    }
    public String getFormPassword() {
        return formPassword;
    }
    public void setFormPassword(String formPassword) {
        this.formPassword = formPassword;
    }
    public String getReplayAddress() {
        return replayAddress;
    }
    public void setReplayAddress(String replayAddress) {
        this.replayAddress = replayAddress;
    }
    public String getToAddress() {
        return toAddress;
    }
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getFormNameDes() {
        return formNameDes;
    }

    public void setFormNameDes(String formNameDes) {
        this.formNameDes = formNameDes;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getAllAdress() {
        return allAdress;
    }

    public void setAllAdress(List<String> allAdress) {
        this.allAdress = allAdress;
    }

    public InternetAddress[] getRecipient() {
        return recipient;
    }

    public void setRecipient(InternetAddress[] recipient) {
        this.recipient = recipient;
    }
}
