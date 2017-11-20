package qa.mail;

import org.apache.commons.lang3.StringUtils;
import qa.utils.DateFormat;
import qa.utils.FileUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.*;

public class MailUtil {

    private final static String host = "smtp.qq.com"; //服务器
    private final static String port = "587"; //端口
    private final static String formName = "1941852809@qq.com";//你的邮箱
    private final static String password = "qppmfyucdwlrcgfh"; //授权码
    private final static String replayAddress = "1941852809@qq.com"; //你的邮箱


    public static void sendHtmlMail(MailInfo info)throws Exception{
        info.setHost(host);
        info.setPort(port);
        info.setFormName(formName);
        info.setFormPassword(password);   //网易邮箱的授权码~不一定是密码
        info.setReplayAddress(replayAddress);
        List<String> allAdress = info.getAllAdress();
        if (null != allAdress){
            InternetAddress[] internetAddresses = new InternetAddress[allAdress.size()];
            for (int i=0;i<allAdress.size();i++){
                internetAddresses[i] = new InternetAddress(allAdress.get(i));
            }
            info.setRecipient(internetAddresses);
        }

        Message message = getMessage(info);
        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        // 设置HTML内容
        html.setContent(info.getContent(), "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        // 将MiniMultipart对象设置为邮件内容
        message.setContent(mainPart);
        Transport.send(message);
    }

    public static void sendTextMail(MailInfo info) throws Exception {

        info.setHost(host);
        info.setPort(port);
        info.setFormName(formName);
        info.setFormPassword(password);   //网易邮箱的授权码~不一定是密码
        info.setReplayAddress(replayAddress);
        Message message = getMessage(info);
        //消息发送的内容
        message.setText(info.getContent());

        Transport.send(message);
    }

    private static Message getMessage(MailInfo info) throws Exception{
        final Properties p = System.getProperties() ;
        p.setProperty("mail.smtp.host", info.getHost());
        p.setProperty("mail.smtp.port", info.getPort());
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.user", info.getFormName());
        p.setProperty("mail.smtp.pass", info.getFormPassword());

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getInstance(p, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("mail.smtp.user"),p.getProperty("mail.smtp.pass"));
            }
        });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        //消息发送的主题
        message.setSubject(info.getSubject());
        //接受消息的人
        message.setReplyTo(InternetAddress.parse(info.getReplayAddress()));
        //消息的发送者
        message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"),info.getFormNameDes()));
        // 创建邮件的接收者地址，并设置到邮件消息中
        if (null == info.getRecipient()){
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(info.getToAddress()));
        }else {
            message.setRecipients(Message.RecipientType.TO,info.getRecipient());
        }
        // 消息发送的时间
        message.setSentDate(new Date());


        return message ;
    }

    /**
     * 获取指定长度的随机数，第一位不包含0
     * @param length
     * @param max
     * @param min
     * @return
     */
    public static String getRandomNumber(int length, int max, int min){
        String str = String.valueOf(getRandom(9,1));
        for (int i=0;i<length-1;i++){
            str += String.valueOf(getRandom(max,min));
        }
        return str;
    }

    /**
     * 生成在[min,max]之间的随机整数
     * @param max
     * @param min
     * @return
     */
    private static int getRandom(int max, int min){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    /**
     * 一个一个发送，指定收件人
     * @throws IOException
     */
    private static void sendSingleMail() throws IOException {
        String logFileName = DateFormat.getDateString() + ".log";
        List<String> allAdress = new ArrayList<>();
        for(int k=0;k<100;k++){
            String number = getRandomNumber(9,9,0);
            number += "@qq.com";
            allAdress.add(number);
        }
        String[] emailAdress = new String[allAdress.size()];
        allAdress.toArray(emailAdress);
        String str = StringUtils.join(allAdress,";");
        /*
        String[] emailAdress = {"523830279","2486090263"
                            ,"150260905","157656975","30984694","1179719448","876377165","2023357981"
                            ,"1363682640","2355244140","54475460","392440649","755365540","851110539"
                            ,"1160827885","1094944814","240335392","1364269191","185836282","297394513"
                            ,"306519420","446898990","348482330","549887330","562104509","529159539"
                            ,"1394222197","175486780","2511799979"
                            ,"13917765194@163.com","joan_SHD@126.com","michelle@panoramart.org"
                            ,"191473855@qq.com","97055657@qq.com","cscec_idd@163.com","anlee168@163.com"
                            ,"Millie.ge@StudioHBA.com","tanjiayuan@jt111.com","can_design@126.com"
                            ,"alex@mogadeco.com","Teemo@modid.com.cn","QUEKINTERIO@163.com","gigi@enidclub.com"
                            ,"Peter@voxflor.com","fanyi_design@126.com","hong239911@163.com"};
*/
        for(int i=0;i<emailAdress.length;i++){
            String mail = emailAdress[i]; //发送对象的邮箱
//            if (!mail.contains("@")){
//                mail += "@qq.com";
//            }
            String title = "软装布艺 Casasgs原创设计师品牌";
            String content = FileUtil.getFileText("src/main/resources/Casasgs.html");  // gb2312
            String nameDes = "Casasgs原创设计师品牌";
            MailInfo info = new MailInfo();
            info.setToAddress(mail);
            info.setSubject(title);
            info.setContent(content);
            info.setFormNameDes(nameDes);
            try {
                MailUtil.sendHtmlMail(info);
                FileUtil.pushText("log/"+logFileName,DateFormat.getDateToString() + " -----> " + mail+" 发送成功。\n");
            } catch (Exception e) {
                FileUtil.pushText("log/"+logFileName,DateFormat.getDateToString() + " -----> " + mail+" 发送失败！！！" + e.getMessage() + "\n");
                System.out.println(mail+" 发送失败！！！" + e.getMessage());
                e.printStackTrace();
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 一次发送多个，必须都是有效地址
     * @throws IOException
     */
    private static void sendMultiMail() throws IOException {
        String logFileName = DateFormat.getDateString() + ".log";
        List<String> allAdress = new ArrayList<>();
//        for(int k=0;k<2;k++){
//            String number = getRandomNumber(9,9,0);
//            number += "@qq.com";
//            allAdress.add(number);
//        }
        allAdress.add("278051884@qq.com");
        allAdress.add("45641783@qq.com");
        MailInfo info = new MailInfo();
        info.setAllAdress(allAdress);
        String title = "软装布艺 Casasgs原创设计师品牌";
        String content = FileUtil.getFileText("src/main/resources/Casasgs.html");  // gb2312
        String nameDes = "Casasgs原创设计师品牌";
        info.setSubject(title);
        info.setContent(content);
        info.setFormNameDes(nameDes);

        String str = StringUtils.join(allAdress,";");
        try {
            //MailSendUtil.sendTextMail(info);
            MailUtil.sendHtmlMail(info);
            FileUtil.pushText("log/"+logFileName,DateFormat.getDateToString() + " -----> " + str+" 发送成功。\n");
        } catch (Exception e) {
            FileUtil.pushText("log/"+logFileName,DateFormat.getDateToString() + " -----> " + str+" 发送失败！！！" + e.getMessage() + "\n");
            System.out.println(str+" 发送失败！！！" + e.getMessage());
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
/*
    public static void main(String[] args) throws IOException {
        sendSingleMail();
    }
    */
}
