package cloudteam.hicube.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * 字符串操作工具包
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils 
{
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	//private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	/**
	 * 将字符串转位日期类型
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 以友好的方式显示时间
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)+"分钟前";
			else 
				ftime = hour+"小时前";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)+"分钟前";
			else 
				ftime = hour+"小时前";
		}
		else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"天前";			
		}
		else if(days > 10){			
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}
	
	/**
	 * 判断给定字符串时间是否为今日
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate){
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if(time != null){
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * 判断给定字符串是否空白串。
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input )
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if(email == null || email.trim().length()==0) 
			return false;
	    return emailer.matcher(email).matches();
	}
	/**
	 * 字符串转整数
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try{
			return Long.parseLong(obj);
		}catch(Exception e){}
		return 0;
	}
	/**
	 * 字符串转布尔值
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try{
			return Boolean.parseBoolean(b);
		}catch(Exception e){}
		return false;
	}
	
	/**
	 * 是否是英文字
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 获取英文字长度
	 * 
	 * @param s
	 * @return
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		int length = c.length;
		for (int i = 0; i < length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 去掉html标签
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
        
        Pattern p_script= Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签 
        
        Pattern p_style= Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签 
        
        Pattern p_html= Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签 
        
        htmlStr = htmlStr.replaceAll("&nbsp;", "");

       return htmlStr.trim(); //返回文本字符串 
    } 
	
	/**
	 * 去掉换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String removeLineChar(String str) {
		if (str == null)
			return "";
		return str.replaceAll("\r\n", "").replaceAll("\n", "");
	}

	/**
	 * 以中文字长度计算，截取字符串
	 * 
	 * @param origin
	 * @param len
	 * @param c
	 * @return
	 */
	public static String substring(String origin, int len, String c) {
		if (origin == null || origin.equals("") || len < 1)
			return "";
		String temp = removeLineChar(origin);
		byte[] strByte = new byte[len];
		if (len > length(origin)) {
			return temp;
		}
		try {
			System.arraycopy(temp.getBytes("GBK"), 0, strByte, 0, len);
			int count = 0;
			for (int i = 0; i < len; i++) {
				int value = (int) strByte[i];
				if (value < 0) {
					count++;
				}
			}
			if (count % 2 != 0) {
				len = (len == 1) ? ++len : --len;
			}
			return new String(strByte, 0, len, "GBK") + c;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static int StrToIntDef(String s, int defaultValue) {
		int res = defaultValue;
		try {
			res = Integer.parseInt(s, 10);
		} catch (Exception e) {
			res = defaultValue;
		}
		return res;
	}
  /**
   *将数据集合转化拼成字符串
   * @param collection 集合
   * @param delimiter 分隔符
   * @return 
   */
	public static String join(Collection<?> collection, String delimiter) {
      StringBuilder builder = new StringBuilder();
      Iterator<?> iter = collection.iterator();
      while (iter.hasNext()) {
         builder.append(iter.next());
          if (iter.hasNext()) {
              builder.append(delimiter);
          }
      }
      return builder.toString();
}
	/**
	 * 检查是否是正确的email
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		    String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*" ;
		    Pattern p   =   Pattern.compile(regex);
		    Matcher m   =   p.matcher(email);
		   return  m.matches();
	}
	/**
	 * 检查是否是数字
	 * @param phone
	 * @return
	 */
	public static boolean checkNum(String phone){
		Pattern pattern= Pattern.compile("[0-9]+");
        Matcher match=pattern.matcher(phone);
        return match.matches();
	}
	 /**
	  *检查是否是电话号码
	  */
	 public static boolean isMobileNo(String paramString)
	  {
	    return Pattern.compile("^1[3|4|5|8]\\d{9}$").matcher(paramString).matches();
	  }
	/**
	 * 检查用户输入账号格式是否正确,只能是邮箱或电话号码
	 * @param account
	 * @return
	 */
	public static boolean checkAccount(String account){
		return (isMobileNo(account)||checkEmail(account));
	}
	
	 // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static final String KEY_APP_KEY = "JPUSH_APPKEY";
    /**
     *  获取jpush AppKey
     * @param context
     * @return
     */
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {

        }
        return appKey;
    }
	
	/**
	 * 将输入流转化为String
	 * @param is
	 * @return
	 */
	public static String readDataFromStream(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder builder = new StringBuilder();
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				builder.append(line);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	/**
	 * 补零操作
	 */
	public static String zerofill(Object obj) {
		String parten = "00";
		if(obj instanceof String)
			obj= Double.parseDouble(obj.toString());
		DecimalFormat decimal = new DecimalFormat(parten);
		return decimal.format(obj);
    }


}
