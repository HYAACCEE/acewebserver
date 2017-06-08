<%@page import="java.io.*"%>
<%@ page contentType="text/html; charset=UTF8"%>  
<%@page import="java.net.*"%>  
  
<html>  
<title>下载标准文档</title>  
  
<script language="javascript">  
</script>  
<body topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">  
<%  
    String path = (String)request.getAttribute("filename");//即将下载的文件的相对路径  
     
    String filedownload = path;//即将下载的文件的相对路径  
   /*  String filedisplay = filedownload.substring(filedownload.lastIndexOf("/")+1);//下载文件时显示的文件保存名称 
    String contentType = application.getMimeType(filedisplay); 
    response.setContentType(contentType);//设置为下载application/x-download  
    String filenamedisplay = URLEncoder.encode(filedisplay,"utf-8");  
    response.setCharacterEncoding("utf-8"); */
    //response.addHeader("Content-Disposition","attachment;filename=" + filenamedisplay);
    filedownload = new String(filedownload.getBytes("iso8859-1"),"utf-8");
    String realPath = application.getRealPath("/upfile/MM");
    String contentType = application.getMimeType(filedownload);
    response.setContentType(contentType);
    response.setHeader("Content-Disposition","inline;filename=" + filedownload);
    
    try{
    /* java.net.URL url = new java.net.URL(path);
	java.net.URLConnection conn = url.openConnection();
	java.io.InputStream is = conn.getInputStream();  */ 
	FileInputStream is = new FileInputStream(realPath+"/"+filedownload);
    byte[] b = new byte[is.available()];
    is.read(b);
   	//获取响应报文输出流对象 
   	out.clear(); 
	out=pageContext.pushBody(); 
    ServletOutputStream outs = response.getOutputStream();
    outs.write(b);
    }catch(Exception e){
    	throw new RuntimeException(e);
    }
%>  
</body>  
</html>