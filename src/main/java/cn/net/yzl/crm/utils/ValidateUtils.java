package cn.net.yzl.crm.utils;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 * @author 刘发冠
 * @date 2019-10-11 16:13:31
 */
public class ValidateUtils {


    /**
     * 验证是否含有全部必填字段
     * object 需要验证的对象 ，requiredColumns为必须要传的字段
     * @param requiredColumns 必填的参数字段名称 逗号隔开 比如"userId,name,telephone"
     */
    public static Map<String,Object> allField(final Object object, String requiredColumns) {
        JSONObject jsonObject = (JSONObject) JSONUtil.parse(object);
        Map<String,Object> notNullValidate = new HashMap<>();
        if (StringUtils.isNotBlank(requiredColumns)) {
            //验证字段非空
            String[] fields = requiredColumns.split(",");
            String missField = "";
            for (String field : fields) {
                //按照必填字段取前台传过来的参数
                if(field.equals("Direction")){
                    field = "direction";
                }
                String val =jsonObject.get(field.trim())+"";
                //System.out.println(field);
                //如果没有查到那个值，就代表着传过来的字段少了
                if (StringUtils.isBlank(val) || StringUtils.isEmpty(val) || val.trim().equals("null")) {
                    missField += field + " ";
                }
            }
            if (StringUtils.isNotBlank(missField)) {
                jsonObject.clear();
                notNullValidate.put("code",0);
                notNullValidate.put("msg", "缺少必填参数或参数值为空:" + missField.trim());
                return notNullValidate;
            }else{
                notNullValidate.put("code",1);
                return notNullValidate;
            }
        }else{
            return notNullValidate;
        }
    }

    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2="^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";// 验证手机号
        if(StringUtils.isNotBlank(str)){
            p = Pattern.compile(s2);
            m = p.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static boolean booleanRequired(final Object object, String requiredColumns) {
        JSONObject jsonObject = (JSONObject) JSONUtil.parse(object);
        Map<String,Object> notNullValidate = new HashMap<>();
        if (StringUtils.isNotBlank(requiredColumns)) {
            //验证字段非空
            String[] fields = requiredColumns.split(",");
            String missField = "";
            for (String field : fields) {
                //按照必填字段取前台传过来的参数
                String val = jsonObject.get(field.trim())+"";
                //如果没有查到那个值，就代表着传过来的字段少了
                if (StringUtils.isBlank(val)|| StringUtils.isEmpty(val) || val.trim().equals("null")) {
                    missField += field + " ";
                }
            }
            if (StringUtils.isNotBlank(missField)) {
                jsonObject.clear();
                notNullValidate.put("code",0);
                notNullValidate.put("msg", "缺少必填参数:" + missField.trim());
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    /**
     * yyyy-MM-dd
     * @param format
     * @return
     */
    public static String DateFormatyyyyMMdd(Date format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(format);
    }

    public static String DateFormatyMd(Date format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(format);
    }

    public static String DateFormatyyyyMMddHHmmss(Date format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(format);
    }

    public static String DateFormatyyyyMMddHHmmssTrade(Date format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(format);
    }

    public static String DateFormatyyyyMMddHHmm(Date format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(format).replaceAll( " ", "").trim();
    }




    public static Date StringFormatyyyy_MM_dd_HHmmss(String format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =null;
        try {
            date= formatter.parse(format+" 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date StringFormatyyyy_MM_dd(String format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        try {
            date= formatter.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date StringFormatyyyy_MM_ddHHmm(String format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date =null;
        try {
            date= formatter.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static Date StringFormatyyyy_MM_ddHHmmss(String format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =null;
        try {
            date= formatter.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static Date DateFormatyyyyMMddHHmmFordata(Date format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date =null;
        try {
             date =formatter.parse(formatter.format(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String StringFormatyyyyMMddHHmm(Date format){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm");
        return formatter.format(format).replaceAll( " ", "").trim();
    }

    public static Date computingTime(Date format,int time,String Type){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format);//设置起时间
        if("YEAR".equals(Type)){
            calendar.add(Calendar.YEAR, time);//增加年
        }else if("MONTH".equals(Type)){
            calendar.add(Calendar.MONTH, time);//增加一个月 
        }else if("DATE".equals(Type)){
            calendar.add(Calendar.DATE, time);//增加天 
        }else if("HOUR".equals(Type)){
            calendar.add(Calendar.HOUR, time);//增加小时  24小时制
        }else if("MINUTE".equals(Type)){
            calendar.add(Calendar.MINUTE, time);//增加分钟  24小时制
        }
        return calendar.getTime();
    }


    public static int YuanToPoints(double yuan) {
        int points= (int) (yuan*100);
        return points;
    }

    public static String PointsToYuan(int points) {
        //设置保留位数
        DecimalFormat df=new DecimalFormat("#.00");
        BigDecimal yuan = new BigDecimal(df.format((float)points/100));
        return String.valueOf(yuan);
    }

    public static String PointsToYuan(long points) {
        //设置保留位数
        DecimalFormat df=new DecimalFormat("#.00");
        BigDecimal yuan = new BigDecimal(df.format((points*1.0)/100));
        //return String.valueOf(yuan); // 这样会出现科学计数法
        return yuan.toPlainString();    // 这样就避免了科学计数法
    }

    public static String PointsToWanYuan(long points) {
        //设置保留位数
        DecimalFormat df=new DecimalFormat("#.0000");
        BigDecimal wanYuan = new BigDecimal(df.format((points*1.0)/1000000));
        return wanYuan.toPlainString();
    }

    /**
     * 分转元 返回BigDecimal类型
     * @param points
     * @return
     */
    public static BigDecimal PointsToYuanBigDecimal(int points) {
        //设置保留位数
        DecimalFormat df=new DecimalFormat("#.00");
        BigDecimal yuan = new BigDecimal(df.format((float)points/100));
        return yuan;
    }

    /**
     * 将一个String型数字转换为BigDecimal
     *
     * @param strNum
     * @return
     */
    public static BigDecimal getBigDecimalFromString(String strNum) {
        BigDecimal num = null;
        if (strNum != null && !"".equals(strNum) && !"".equals(strNum.trim())) {
            strNum = strNum.trim();
            if (strNum.startsWith("-") || strNum.endsWith("-")) {
                strNum = strNum.replace("-", "");
                num = new BigDecimal(strNum).multiply(new BigDecimal(-1));
            } else {
                num = new BigDecimal(strNum);
            }
            return num;
        }
        return new BigDecimal(0);
    }

    /**
     * 将参数与当前日期(yyyy-MM-dd)做比对 参数小于当前日期 true, 否则 返回 true.
     * @param date
     * @return
     */
    public static boolean beforeNow(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String beginTime = sdf.format(date);
            Date vt = sdf.parse(beginTime);
            String endTime = sdf.format(new Date());
            Date now = sdf.parse(endTime);
            return vt.before(now);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean beforeOrEqNow(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String beginTime = sdf.format(date);
            Date vt = sdf.parse(beginTime);
            String endTime = sdf.format(new Date());
            Date now = sdf.parse(endTime);
            if(vt.equals(now)||vt.after(now)){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 获取当前日期
     * <p>
     * 如果 dfm ==null 则返回默认格式 yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param dfm 格式化参数
     * @return 当前时间
     */
    public static String getNowDateTime(String dfm) {
        LocalDateTime ldt = LocalDateTime.now();
        //ldt = ldt.plusDays(-1);
        if (dfm == null) {
            return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        } else
            return ldt.format(DateTimeFormatter.ofPattern(dfm));
    }

    /**
     * 获取当前日期
     * <p>
     * 如果 dfm ==null 则返回默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param dfm 格式化参数
     * @return 当前时间
     */
    public static String getNowDateTime2(String dfm) {
        LocalDateTime ldt = LocalDateTime.now();
        //ldt = ldt.plusDays(-1);
        if (dfm == null) {
            return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else
            return ldt.format(DateTimeFormatter.ofPattern(dfm));
    }

    /**
     * 获取与当前系统日期前(date负数),后(date整数)特定日期的 ,格式:yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getSpecificDate(int date) {
        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.plusDays(date);
        return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 获取指定年月的第一天
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 日期类型转字符串 yyyy-MM-dd HH:mm:ss
     * @param date date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String Date2String(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date).trim();
    }

    /**
     * 日期类型转字符串 yyyy-MM-dd
     * @param date date
     * @param frm frm
     * @return yyyy-MM-dd
     */
    public static String Date2StringYyyyMMdd(Date date,String frm){
        if (frm == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date).trim();
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(frm);
            return formatter.format(date).trim();
        }
    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static String dateFormat(Date date,String df){
        SimpleDateFormat formatter = new SimpleDateFormat(df==null?"yyyy-MM-dd HH:mm:ss":df);
        return formatter.format(date);
    }

    public static Date dateFormatForDate(Date format,String df){
        SimpleDateFormat formatter = new SimpleDateFormat(df==null?"yyyy-MM-dd HH:mm:ss":df);
        Date date =null;
        try {
            date =formatter.parse(formatter.format(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date string2Date(String sdate,String df) {
        SimpleDateFormat formatter = new SimpleDateFormat(df==null?"yyyy-MM-dd HH:mm:ss":df);
        Date date =null;
        try {
            date =formatter.parse(sdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 判断给定 date 是否在 sd 与 ed 之间 在 返回true 不在返回 false,异常也返回false
     * <BR><BR/>
     * date 日期值,sd,ed 日期格式:MM-dd
     *
     * @param date 被判定的日期 yyyy-MM-dd
     * @param sd   开始时段 MM-dd
     * @param ed   结束时段 MM-dd
     * @return date 在 返回true 不在返回 false,异常也返回false
     */
    public static boolean betweenStAndEt(Date date, String sd, String ed) {
        String format = Date2StringYyyyMMdd(date,"yyyy")+"-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
//            LocalDateTime dt = LocalDateTime.now();
            sd = Date2StringYyyyMMdd(date,"yyyy") + "-" + sd;
            ed = Date2StringYyyyMMdd(date,"yyyy") + "-" + ed;
            String checkDate = sdf.format(date);
            Date vt = sdf.parse(checkDate);
            Date dSt = sdf.parse(sd);
            Date dEt = sdf.parse(ed);
            return ((vt.after(dSt) || vt.equals(dSt)) && (vt.before(dEt) || vt.equals(dEt)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 验证是否是URL
     * @param url
     * @return
     */
    public static boolean verifyUrl(String url){

        // URL验证规则
        String regEx ="[a-zA-z]+://[^\\s]*";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;

    }

    /**
     * create by: Liu Faliang
     * description: 获取商品描述
     * create time: 2020/6/23 15:00
     *
     * @Param: orderTypeSet
     *
     */
    public static String getProductDesc(List<Integer> list){
        if (list!=null&&list.size()>0){
            int size = list.size();
            if (size==1){
                int val = list.get( 0 );
                switch(val)
                {
                    case 0:
                        return "景点门票";
                    case 1:
                        return "酒店";
                    case 2:
                        return "特产";
                    default:
                        return "未知商品";
                }
            }else {
                return "旅游产品";
            }
        }else {
            return "未知商品";
        }
    }

    /**
     * 判断给定 date 是否在 sd 与 ed 之间 在 返回true 不在返回 false,异常也返回false
     * <BR><BR/>
     * date 日期值,sd,ed 日期格式:MM-dd
     *
     * @param date 被判定的日期 yyyy-MM-dd
     * @param sd   开始时段 yyyy-MM-dd
     * @param ed   结束时段 yyyy-MM-dd
     * @return date 在 返回true 不在返回 false,异常也返回false
     */
    public static boolean betweenStAndEt2(String date, String sd, String ed) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date vt = sdf.parse(date);
            Date dSt = sdf.parse(sd);
            Date dEt = sdf.parse(ed);
            return ((vt.after(dSt) || vt.equals(dSt)) && (vt.before(dEt) || vt.equals(dEt)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 2020-9-1
     * 前N个或者后N个天
     *
     * @param lastOrNextDay 正数当前日期后N,负数当前日期前N
     * @return 上N个或者下N个月 yyyy-MM-dd
     */
    public static String getLastOrNextDay(int lastOrNextDay) {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.plusDays(lastOrNextDay).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 2020-9-1
     * 前N个或者后N个月
     *
     * @param lastOrNextMonth 正数当前日期后N,负数当前日期前N
     * @return 上N个或者下N个月 yyyy-MM-dd
     */
    public static String getLastOrNextMonth(@NotNull int lastOrNextMonth) {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.plusMonths(lastOrNextMonth).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 2020-9-1
     * 前N个或者后N个年
     *
     * @param lastOrNextYear 正数当前日期后N,负数当前日期前N
     * @return 上N个或者下N个月 yyyy-MM-dd
     */
    public static String getLastOrNextYear(int lastOrNextYear) {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.plusYears(lastOrNextYear).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static void main1(String[] args) {
        System.out.println(getLastOrNextDay(1));
        System.out.println(getLastOrNextDay(-1));

        System.out.println(getLastOrNextMonth(1));
        System.out.println(getLastOrNextMonth(-1));

        System.out.println(getLastOrNextYear(1));
        System.out.println(getLastOrNextYear(-1));


/*
        String orderArrivalDate = "2020-08-01";
        String startDate = "";
        //String startDate = "2020-08-02";
        //String endDate = "2020-12-03";
        String endDate = "2020-12-03";

        if(StringUtils.isNotBlank(startDate)) {
            if (StringUtils.isNotBlank(endDate)) {
                if(!ValidateUtils.betweenStAndEt2(orderArrivalDate,startDate,endDate)) {
                    System.out.println("---------------日期不符");
                }
                else {
                    System.out.println("==============OK");
                }
            } else {
                System.out.println("==============结束日期为空");
            }
        } else {
            //   淡旺季 结束日期格式: 05-01~10-31 或者 01-01~04-30,11-01~12-31
            endDate = "05-01~10-31";
            endDate = "01-01~04-30,11-01~12-31";
            if (StringUtils.isNotBlank(endDate)) {
                String tempArrivalDate = orderArrivalDate.substring(0,5);
                String[]  danWangJi = endDate.split(",");
                boolean validArrivalDate = false;   // 有效的游玩日期
                for(int i=0;i<danWangJi.length;i++) {
                    String[] danWangJiShiDuan = danWangJi[i].split("~");
                    String sd = danWangJiShiDuan[0];
                    sd = tempArrivalDate.concat(sd);
                    String ed = danWangJiShiDuan[1];
                    ed = tempArrivalDate.concat(ed);
                    if(ValidateUtils.betweenStAndEt2(orderArrivalDate,sd,ed)) {
                        validArrivalDate = true;
                        break;
                    }
                }
                if(!validArrivalDate) {
                    System.out.println("-------------日期未在有效期范围内");
                } else {
                    System.out.println("==============2222222222OK");
                }
            } else {
                System.out.println("==============22222222222结束日期为空");
            }
        }*/
    }
}
