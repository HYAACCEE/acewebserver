package com.svv.dms.app;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
public class EmailUtil {
	
	// 发送邮件
	public static void sendEmail(String emailAddress, String title, String content, String filePath) throws MessagingException{
	    Properties prop=new Properties();  
	    prop.put("mail.host","smtp.mxhichina.com" );  
	    prop.put("mail.transport.protocol", "smtp");  
	    prop.put("mail.smtp.auth", "true");
	    //使用java发送邮件5步骤  
	    
	    //1.创建sesssion  
	    Session session=Session.getInstance(prop);  
	    //开启session的调试模式，可以查看当前邮件发送状态  
	    session.setDebug(true);
	    
	    //2.通过session获取Transport对象（发送邮件的核心API）  
	    Transport ts=session.getTransport();
	    
	    //3.通过邮件用户名密码链接，阿里云默认是开启个人邮箱pop3、smtp协议的，所以无需在阿里云邮箱里设置  
	    ts.connect(ACE.EMAILADDRESS, ACE.EMAILPASSWORD);
	    
	    //4.创建邮件
	    Message msg = createSimpleMail(session,emailAddress, title, content, filePath);
	    
	    //5.发送电子邮件
	    ts.sendMessage(msg, msg.getAllRecipients());
	}
	// 发送附件
	public static String sendEmailFile(String emailAddress, String title, String content, String filePath) {
	    
	    System.out.println("mail.to:"+emailAddress);
	    System.out.println("mail.title:"+title);
	    
		Properties prop = new Properties();  
	    prop.put("mail.host","smtp.mxhichina.com" );  
	    prop.put("mail.transport.protocol", "smtp");  
	    prop.put("mail.smtp.auth", "true");
	    //使用java发送邮件5步骤  
	    
	    //1.创建sesssion  
	    Session session = Session.getInstance(prop);  
	    //开启session的调试模式，可以查看当前邮件发送状态  
	    session.setDebug(true);
	    
	    //2.通过session获取Transport对象（发送邮件的核心API）  
	    Transport ts;
		try {
			ts = session.getTransport();			

		    //3.通过邮件用户名密码链接，阿里云默认是开启个人邮箱pop3、smtp协议的，所以无需在阿里云邮箱里设置  
		    ts.connect(ACE.EMAILADDRESS, ACE.EMAILPASSWORD);

		    //4.创建邮件
		    Message msg = createSimpleMail(session,emailAddress, title, content, filePath);

		    //5.发送电子邮件
			ts.sendMessage(msg, msg.getAllRecipients());
			
			return "SUCCESS";
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "系统正忙，请稍后再试";
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "邮箱不正确";
			
		}
	}

    public static MimeMessage createSimpleMail(Session session, String toEmail, String title, String content, String filePath) throws AddressException, MessagingException{  
	    //创建邮件对象  
	    MimeMessage message = new MimeMessage(session); 
	   
	    //设置发件人 
	    message.setFrom(new InternetAddress(ACE.EMAILADDRESS));
	    //设置收件人  
	    message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
	
	    message.setSubject(title);
	    
	    if(filePath.isEmpty()){
	    	// 没有文件
	        message.setContent(content,"text/html;charset=gbk");	
	    }else{
	        MimeBodyPart picBodyPart=getPicBodyPart(content);  
	        MimeBodyPart attached1BodyPart = null;
	    	try {
	    		attached1BodyPart = getAttachedBodyPart(filePath);
	    	} catch (UnsupportedEncodingException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}//注意附件名是中文的
	          
	        MimeMultipart mmp = new MimeMultipart("mixed");//MIME消息头组合类型是mixed(html+附件)
	        mmp.addBodyPart(attached1BodyPart);
	        mmp.addBodyPart(picBodyPart); 
	        message.setContent(mmp);
	    }
	    
        message.saveChanges();
	
	    return message;  

    }
	
	// 发送附件
	public static void sendEmailTest(String[] emailAddress, String title, String content, String filePath) throws MessagingException{
	    Properties prop=new Properties();  
	    prop.put("mail.host","smtp.mxhichina.com" );  
	    prop.put("mail.transport.protocol", "smtp");  
	    prop.put("mail.smtp.auth", "true");
	    //使用java发送邮件5步骤  
	    
	    //1.创建sesssion  
	    Session session=Session.getInstance(prop);  
	    //开启session的调试模式，可以查看当前邮件发送状态  
	    session.setDebug(true);
	    
	    //2.通过session获取Transport对象（发送邮件的核心API）  
	    Transport ts=session.getTransport();
	    
	    //3.通过邮件用户名密码链接，阿里云默认是开启个人邮箱pop3、smtp协议的，所以无需在阿里云邮箱里设置  
	    ts.connect(ACE.EMAILADDRESS, ACE.EMAILPASSWORD);
	    
	    //4.创建邮件
	    createSimpleMailTest(ts,session,emailAddress, title, content, filePath);
	}

    public static void createSimpleMailTest(Transport ts,Session session, String[] toEmail, String title, String content, String filePath) throws AddressException,MessagingException{  
	    //创建邮件对象  
	    MimeMessage message = new MimeMessage(session); 
	   
	    //设置发件人
	    message.setFrom(new InternetAddress(ACE.EMAILADDRESS));
	    
		  InternetAddress[] toAddresss = new InternetAddress[toEmail.length];  
		  for ( int len=0;len<toEmail.length ;len++){  
			  toAddresss[0] = new InternetAddress(toEmail[len]);  
		  }  
	    //设置收件人  
		  int i = 0;  
		while (i < toAddresss. length ) {    
	    message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail[i]));
	
	    message.setSubject(title);
	    
	    if(filePath.isEmpty()){
	    	// 没有文件
	        message.setContent(content,"text/html;charset=gbk");	
	    }else{
	        MimeBodyPart picBodyPart=getPicBodyPart(content);  
	        MimeBodyPart attached1BodyPart = null;
	    	try {
	    		attached1BodyPart = getAttachedBodyPart(filePath);
	    	} catch (UnsupportedEncodingException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}//注意附件名是中文的
	          
	        MimeMultipart mmp = new MimeMultipart("mixed");//MIME消息头组合类型是mixed(html+附件)
	        mmp.addBodyPart(attached1BodyPart);
	        mmp.addBodyPart(picBodyPart); 
	        message.setContent(mmp);
	    }
	    
        message.saveChanges();
	    //5.发送电子邮件
        ts.sendMessage(message, message.getAllRecipients());
        i++;
	}
//	    return message;  
//		return message;

    }

    private static MimeBodyPart getAttachedBodyPart(String filePath) throws MessagingException,  
           UnsupportedEncodingException{  
         MimeBodyPart attached=new MimeBodyPart();  
         FileDataSource fds=new FileDataSource(filePath);  
         attached.setDataHandler(new DataHandler(fds));  
         String fileName=doHandlerFileName(filePath);  
         attached.setFileName(MimeUtility.encodeWord(fileName));//处理附件文件的中文名问题  
         return attached; 
    }
    /** 
     * 处理文件名 
     * 此处是针对Window下的。 
     * @param filePath 
     * @return 
     */  
    private static String doHandlerFileName(String filePath){  
         String fileName=filePath;  
         if(null !=filePath && !"".equals(filePath)){  
          fileName=filePath.substring(filePath.lastIndexOf("//")+1);  
         }  
         return fileName;  
    }
    /** 
     * 处理html加图片的类型(related) 
     * @param content 
     * @param picName 
     * @return 
     * @throws MessagingException 
     */  
    private static MimeBodyPart getPicBodyPart(String content) throws MessagingException{  
         MimeBodyPart contentPart=new MimeBodyPart();  
           
         MimeMultipart mmp=new MimeMultipart("related");//此处MIME消息头组合类型为related  
         MimeBodyPart contented=new MimeBodyPart();  
         contented.setContent(content,"text/html;charset=gb2312");//因正文内容中有中文  
           
         mmp.addBodyPart(contented);
           
         contentPart.setContent(mmp);  
           
         return contentPart;  
    } 
   /* 
     * 邮件用户名和密码认证 
     */  
   static class SmtpAuth extends javax.mail.Authenticator {  
      private String user , password ;  
  
      public void getuserinfo(String getuser, String getpassword) {  
          user = getuser;  
          password = getpassword;  
      }  
      protected javax.mail.PasswordAuthentication getPasswordAuthentication() {  
          return new javax.mail.PasswordAuthentication( user , password );  
      }  
   }  
    
    public static void main(String[] args) throws MessagingException{
    	// 测试发送邮件, 发送激活邮件,发送验证码,发送附件 
    	StringBuilder emailContent = new StringBuilder()  
        .append("<div style=\"background:url(../doc/images/HR/youxiang.png) no-repeat;\">")  
		.append("<span style=\"color: #565656;font-size:18px;font-weight: bold\">欢迎使用ACE招聘平台!</span><br/>")
        .append("<div style=\"height:40px\"></div>")    
        .append("您的登录账户为: <br/>")
        .append("<div style=\"height:20px\"></div>")   
        .append("请点击以下链接验证您的邮箱地址<br/>")  
        .append("<a style=\"color: #FEA008;font-weight: bold;font-size: larger;font-family: cursive;\" href=")  
        .append("linda.li@ace-elite.com")  
        .append(">")   
        .append("linda.li@ace-elite.com")  
        .append("</a>")   
        .append("</span><br/>")
        .append("<span style=\"color: #A0A0A0;\">如果以上链接无法访问,请将该网址复制并粘贴至新的浏览器窗口中<span><br/>")   
        .append("</div>");
    	String[] to = {"mike.gu@ace-elite.com","walle.liu@ace-elite.com","defi.wang@ace-elite.com","zoey.zhou@ace-elite.com"};
		EmailUtil.sendEmailTest(to,"欢迎使用ACE招聘平台!", emailContent.toString(),"");
	}      

}
