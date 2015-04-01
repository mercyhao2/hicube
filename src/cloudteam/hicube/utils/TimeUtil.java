package cloudteam.hicube.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    /**
     * 暂时格式化时间不调这个方法
     * 以秒的单位计算 个性时间显示 服务器端由于没有存储毫秒数
     *
     * @param timestamp 以秒为单位
     * @return
     */
    protected static String converTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis() / 1000L;
        long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > 24 * 60 * 60 * 30 * 365) {// 1年以上
            timeStr = timeGap / (24 * 60 * 60 * 30 * 365) + "年前";
        } else if (timeGap > 24 * 60 * 60 * 30) {// 30天以上
            timeStr = timeGap / (24 * 60 * 60 * 30) + "月前";
        } else if (timeGap > 24 * 60 * 60) {// 1-30天
            timeStr = timeGap / (24 * 60 * 60) + "天前";
        } else if (timeGap > 60 * 60) {// 1小时-24小时
            timeStr = timeGap / (60 * 60) + "小时前";
        } else if (timeGap > 60) {// 1分钟-59分钟
            timeStr = timeGap / 60 + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "1分钟前";
        }
        return timeStr;
    }

    /**
     * 格式化 当前时间
     *
     * @param form 格式字符串 可为空串
     * @return
     */
    public static String getCurrentDateTime(String form) {
        String localform = form;
        if ("".equals(localform)) {
            localform = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat time = new SimpleDateFormat(localform);
        return time.format(new Date());
    }

    /**
     * 格式化 当前时间
     *
     * @param form 格式字符串 可为空串
     * @return
     */
    public static long getCurrentDateTimeToLong(String form) {
        long time = 0;
        try {
            String localform = form;
            if ("".equals(localform)) {
                localform = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(localform);
            String timeStr = dateFormat.format(System.currentTimeMillis());
            Date date = dateFormat.parse(timeStr);
            time = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    /**
     * @param dt   构造Date
     * @param form 格式化字符串 可为空串
     * @return
     */
    public static String dateTime2String(Date dt, String form) {
        String localForm = form;
        if ("".equals(localForm)) {
            localForm = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat time = new SimpleDateFormat(localForm);
        return time.format(dt);
    }

    /**
     * 时间戳转换格式化的时间
     *
     * @param ldt  传入值要是毫秒精度
     *             时间戳
     * @param form yyyy-MM-dd HH:mm:ss 格式化字符串 可为空
     * @return
     */
    public static String getTime2String(long ldt, String form) {
        String localform = form;
        if ("".equals(localform)) {
            localform = "yyyy-MM-dd HH:mm:ss";
        }
        Date dt = new Date(ldt);
        SimpleDateFormat time = new SimpleDateFormat(localform);
        return time.format(dt);
    }

    /**
     * 以字串形式的时间进行计算，增加或减少iHouer小时，返回日期时间字串
     *
     * @param dateString
     * @param iHouer
     * @return
     */
    public static String stringDateTimePlus(String dateString, int iHouer) {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设定格式
        dateFormat.setLenient(false);
        try {
            Date timeDate = dateFormat.parse(dateString);// util类型
            // Timestamp类型,timeDate.getTime()返回一个long型
            java.sql.Timestamp dateTime = new java.sql.Timestamp(
                    timeDate.getTime() + (iHouer * 60 * 60 * 1000L));
            return dateTime.toString().substring(0, 19);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    /**
     * 字符串解析成Date对象
     *
     * @param dataString 要解析字符串
     * @param pattern    要解析的格式 如 yyyy年MM月dd
     * @return Date
     */
    public static Date ParseStringtoDate(String dataString, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);// 设定格式
        try {
            return dateFormat.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数字补零操作如:01
     *
     * @param obj
     * @return
     */
    public static String Timezerofill(Object obj) {
        String parten = "00";
        DecimalFormat decimal = new DecimalFormat(parten);
        return decimal.format(obj);
    }

    /**
     * 该方法时间戳精度到s
     * 5分钟之内，显示“刚刚”；
     * 今天之内，显示“今天 XX：XX”；
     * 昨天之内，显示“昨天 XX：XX”；
     * 昨天之前的时间段，显示具体日期“xx月XX日”；
     *
     * @param timestamp 以秒为单位
     * @return -20140304 wh G--
     */
    public static String converFromSecondTime(long timestamp) {
        long cuurentMillis = System.currentTimeMillis();
        long currentSeconds = cuurentMillis / 1000;
        long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
        Calendar targetCalendar = Calendar.getInstance();
        //当前时间
        targetCalendar.setTime(new Date(cuurentMillis));
        int currentDay = targetCalendar.get(Calendar.DAY_OF_YEAR);
        int currentYear = targetCalendar.get(Calendar.YEAR);
        //传递进来的时间
        targetCalendar.setTime(new Date(timestamp));
        int targetDay = targetCalendar.get(Calendar.DAY_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        String timeStr = null;

        if (currentYear - targetYear > 0) {
            timeStr = targetYear + "年";
        } else if (currentDay - targetDay > 1) {// 30天以上
            timeStr = getTime2String(timestamp, "M月d日");
        } else if (currentDay - targetDay == 1) {
            timeStr = "昨天";//+getTime2String(timestamp*1000,"HH:mm");
        } else {//if (timeGap > 60*5) {// 5分钟之内以后
            timeStr = "今天";//+ getTime2String(timestamp*1000,"HH:mm");
        } //else {// 5分钟以内
        //timeStr = "刚刚";
        //}---20140304--wh-
        return timeStr;
    }

    public static String int2ymd(int paramInt) {
        if (paramInt == 0) return "";
        String str = paramInt + "";
        return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
    }

    public static int ymd2Int(String paramString) {
        return Integer.parseInt(paramString.replace("-", ""));
    }

    public static String getFirstOrEndDate(int paramInt)
    {
        Calendar localCalendar = Calendar.getInstance();
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        localCalendar.set(Calendar.DATE, 1);
        localCalendar.roll(Calendar.DATE, -1);
        Date localDate = localCalendar.getTime();
        if (paramInt == 1)
            return localSimpleDateFormat.format(localDate);
        localCalendar.set(Calendar.DATE, 1);
        return localSimpleDateFormat.format(localCalendar.getTime());
    }

    public static String getTodayDate()
    {
        Calendar localCalendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd").format(localCalendar.getTime());
    }
}