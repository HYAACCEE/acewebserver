package com.svv.dms.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.db.util.DateUtil;
import com.jeesoon.util.logger.Logger;

public class LWUtil {
	public static Logger logger = Logger
			.getLogger(LWUtil.class.getSimpleName());
	final static int BUFFER_SIZE = 4096;

	static String f = "yyyy-MM-dd HH:mm:ss";

	public static long toLongTime(String date) {
		return DateUtil.parseDate(date, f).getTime();
	}
	
	// 计算本周几
	public static String getWeekOfDate(String dates) throws java.text.ParseException {
		String times = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String today = format.format(now);
		
  		Calendar calendar = Calendar.getInstance();
  		Date date = format.parse(today);
  		calendar.setFirstDayOfWeek(Calendar.MONDAY);
  		calendar.setTime(date);

  		Date date1 = format.parse(dates);
  		Calendar calendar1 = Calendar.getInstance();
  		calendar1.setFirstDayOfWeek(Calendar.MONDAY);
  		calendar1.setTime(date1);
  		
  		if(date.equals(date1)){
  			 return "今天";
  		}else if(calendar.get(Calendar.WEEK_OF_YEAR) == calendar1.get(Calendar.WEEK_OF_YEAR)){
  			 System.out.println("本周");
  			 times = "本周";
  		}else if(calendar1.get(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR) == 1){
 			 System.out.println("下周");
  			 times = "下周";
  		}else{
  			return "月份";
  		}
  		
	    String[] weekOfDays = {"日", "一", "二", "三", "四", "五", "六"};
	    if(date1 != null){
	    	calendar.setTime(date1);      
	    }        
	    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
	    if (w < 0){        
	        w = 0;      
	    }
	    System.out.println("[getFirstDayOfWeek]======================================="+date+"测试是否相等"+date1);
	    return times + weekOfDays[w];    
	}
	
	public static String HTML_toDatalist(String format, String[][] list, String removeLastSpliter, String removeMoreEnd){
		//<span>上海好致网络</span><span class='grey f12'><span class='pd5'>|</span>品牌经理 ，...</span>
		StringBuilder s = new StringBuilder();
		int size = list.length;
		String format_new = format.replaceAll(removeMoreEnd, "");
		System.out.println("[HTML_toDatalist] size="+size+"   format_new="+format_new);
		for(int i=0; i<size; i++){
//			System.out.println("[HTML_toDatalist] "+Arrays.toString(list[i]));
//			if(i>0) System.out.println("[HTML_toDatalist] 111 "+s.toString());
			if(list[i]==null || list[i][0]==null) break;
			s.append(String.format(i==size-1?format:format_new, list[i]));
//			System.out.println("[HTML_toDatalist] 222 "+s.toString());
		}
//		System.out.println("[HTML_toDatalist] TTT "+s.toString());
		if(removeLastSpliter.length()>0){
			int pos = s.lastIndexOf(removeLastSpliter);
			s.replace(pos, pos+removeLastSpliter.length(), "");
		}
		return s.toString();
	}
	public static String HTML_toDatalist(String format, TreeMap<Integer, String> map, String removeLastSpliter, String removeMoreEnd){
		//<span>上海好致网络</span><span class='grey f12'><span class='pd5'>|</span>品牌经理 ，...</span>
		StringBuilder s = new StringBuilder();
		int size = map.size();
		String format_new = format.replaceAll(removeMoreEnd, "");
		Set<Integer> keys = map.keySet();
		int i = 0;
		for(Integer key: keys){
			s.append(String.format(i==size-1?format:format_new, new Object[]{key+"", map.get(key)}));
			i++;
		}
//		System.out.println("[HTML_toDatalist] TTT "+s.toString());
		if(removeLastSpliter.length()>0){
			int pos = s.lastIndexOf(removeLastSpliter);
			s.replace(pos, pos+removeLastSpliter.length(), "");
		}
		return s.toString();
	}
	public static List<String> HTML_getFormatHTMLs(String html, String[] subMarkers){
		List<String> rtn = new ArrayList<String>();
		Pattern p;
		Matcher m;
		String html_new = html;
		String subFormat;
		rtn.add(html_new);
		for(String subMarker: subMarkers){
			subFormat = "<"+subMarker+">(.+?)</"+subMarker+">";
			System.out.println("[HTML_getFormatHTMLs] " + subFormat);
			p = Pattern.compile(subFormat);
			m = p.matcher(html);
			while(m.find()){
				System.out.println("====== "+subMarker+" finded========"+m.group(1));
				html_new = html_new.replaceAll(subFormat, "%s");
				rtn.set(0, html_new);
				rtn.add(m.group(1));
			}
		}
		return rtn;
	}
	public static List<String> HTML_getFormatHTMLs(String html){
		List<String> rtn = new ArrayList<String>();
		Pattern p;
		Matcher m;
		String html_new = html;
		rtn.add(html_new);
		String subFormat = "<LIST>(.+?)</LIST>";
		p = Pattern.compile(subFormat);
		m = p.matcher(html);
		while(m.find()){
			html_new = html_new.replaceAll(subFormat, "%s");
			rtn.set(0, html_new);
			rtn.add(m.group(1));
		}
		return rtn;
	}


	public static String toChatTime(String date) {
		long itime = DateUtil.parseDate(date, f).getTime();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		long curyear_time = calendar.getTimeInMillis();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		long curday_time = calendar.getTimeInMillis();

		long curtime = new Date().getTime();
		long t = curtime - itime;

		SimpleDateFormat f;
		if (t < 60000 * 2) { // 1分钟内
			return "刚刚";
		} else if (t < 3600000 * 1) { // 1小时内
			return t / (60000) + "分钟前";
		} else if (itime > curday_time) { // 今天前
			return t / (3600000) + "小时前";
		} else if (itime > curday_time - 3600000 * 24) { // 昨天
			// f = new SimpleDateFormat("a HH:mm", Locale.CHINESE);
			return "昨天";
		} else if (t < 2592000000L) { // 1个月内 3600000*24*30
			// f = new SimpleDateFormat("MM月dd日  a HH:mm", Locale.CHINESE);
			// //new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			// return f.format(itime);
			return t / (3600000 * 24) + "天前";
		} else if (t < 31104000000L) { // 1年内 3600000*24*365
			// f = new SimpleDateFormat("MM月dd日  a HH:mm", Locale.CHINESE);
			// //new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			// return f.format(itime);
			return t / (2592000000L) + "个月前";
		} else { // 今年前
					// f = new SimpleDateFormat("yyyy年MM月dd日  a HH:mm",
					// Locale.CHINESE);
					// return f.format(itime);
			return t / (31104000000L) + "年前";
		}
	}

	public static String url(String url) {
		return url(url, "gb2312");
	}

	public static String url(String url, String ucode) {
		URLConnection cn;

		try {
			// System.out.println(url);
			cn = new URL(url).openConnection();
			cn.addRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			cn.connect();

			InputStream in = cn.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] data = new byte[BUFFER_SIZE];
			int count = -1;
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);

			data = null;

			String rtn = new String(outStream.toByteArray(), ucode);

			System.out.println("===================" + rtn);
			return rtn;
		} catch (Exception e) {
			System.out.println("ERROR to URL: " + url);
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String urlPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static boolean moveFile(String sFile, String dFile) {
		System.out.println(sFile+" to "+dFile);
		try {
			File afile = new File(sFile);
			if (afile.renameTo(new File(dFile))) {
				System.out.println(sFile+" success move to "+dFile);
				return true;
			} else {
				System.out.println("File failed to move!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
	}	
}
