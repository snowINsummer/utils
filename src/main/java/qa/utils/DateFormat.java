package qa.utils;

import qa.exception.HTTPException;
import qa.exception.RunException;
import qa.httpClient.HttpClientUtil;
import qa.httpClient.ResponseInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateFormat {
	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateToString(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(GregorianCalendar.getInstance().getTime());
	}
    /**
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateToString(long currentTimeMillis){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTimeMillis);
    }
	/**
	 * 获取日期
	 * @return yyyy-MM-dd
	 */
	public static String getDateString(){
		return new SimpleDateFormat("yyyy-MM-dd").format(GregorianCalendar.getInstance().getTime());
	}
	
	public static Date getDate(){
		return GregorianCalendar.getInstance().getTime();
	}



    /**
     * 获取当前时间戳
     * @return
     */
    public static long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

	/**
	 * 获取今日以后、以前的日期
	 * @param num 今天开始计算的天数,负数为之前
	 * @return yyyy-MM-dd
	 */
	public static String getAddDay(int num){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();  
        try {
			cal.setTime(sdf.parse(getDateString()));
		} catch (ParseException e) {
            e.printStackTrace();
		}
        cal.add(Calendar.DAY_OF_YEAR, +num);  
        String nextDate_1 = sdf.format(cal.getTime());  
        return nextDate_1;
	}
	
	/**
	 * 
	 * @param format yyyy-MM-dd HH:mm:ss
	 * @return 
	 */
	public static String getDate(String format){
		return new SimpleDateFormat(format).format(GregorianCalendar.getInstance().getTime());
	}

    /**
     *
     * @param format yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDate(String format, long currentTimeMillis){
        return new SimpleDateFormat(format).format(currentTimeMillis);
    }

	public static String getData(String format , Date data){
        return new SimpleDateFormat(format).format(data);
	}
	
	
	public static Date getData(String format , String data){
		Date date = null;
		try {
			date =  new SimpleDateFormat(format).parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}

	public static int getMonth(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    public static Date getDatafromTimeMillis(String currentTimeMillis){
        Date date = null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentTimeMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

	/**
	 * 日期格式字符串转换成时间戳
	 * @param date 字符串日期
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date,String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

    /*
   * 将时间转换为时间戳
   */
    public static long dateToStamp(String s) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

	/**
	 * 根据日期获得所在周的日期
	 * @param mdate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Date> dateToWeek(Date mdate) {
		int b = mdate.getDay();
		Date fdate;
		List<Date> list = new ArrayList<>();
		Long fTime = mdate.getTime() - b * 24 * 3600000;
		for (int a = 1; a <= 7; a++) {
			fdate = new Date();
			fdate.setTime(fTime + (a * 24 * 3600000));
			list.add(a-1, fdate);
		}
		return list;
	}

    public static long getDiffTime(String start, String end){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date d1 = df.parse(start);
            Date d2 = df.parse(end);
            long diff = d2.getTime() - d1.getTime();
            long day=diff/(24*60*60*1000);
            long hour=(diff/(60*60*1000)-day*24);
            long min=((diff/(60*1000))-day*24*60-hour*60);
            long s=(diff/1000-day*24*60*60-hour*60*60-min*60);
//            System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
            return diff;
//            long days = diff / (1000 * 60 * 60 * 24);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getWorkTime(long time){
//        long day=time/(24*60*60*1000);
        long hour=(time/(60*60*1000));
        long min=((time/(60*1000))-hour*60);
        long s=(time/1000-hour*60*60-min*60);
        return "工作时间："+hour+"小时"+min+"分"+s+"秒";
    }

    public static String needToWork(int needHour, long workTime){
        long needTime = needHour * 60 * 60 * 1000;
        long time = needTime - workTime;
        long hour=(time/(60*60*1000));
        long min=((time/(60*1000))-hour*60);
        long s=(time/1000-hour*60*60-min*60);
        return "还需要工作："+hour+"小时"+min+"分"+s+"秒";

    }

    public static String getPunchCardTime(int needHour, long workTime, String dayDate, List<List> days) throws ParseException {
        long needTime = needHour * 60 * 60 * 1000;
        long time = needTime - workTime;
            for (List<String> l : days) {
                String str = l.get(3);
                if (str.contains(dayDate)) {
                    long punchCardT = dateToStamp(str);
                    long needPunchCardTime = time + punchCardT;
                    return getDateToString(needPunchCardTime);
                }
            }
        return "";
    }

    public static void getWorkAttendance(String userName, int weekNeedToWorkHour){
        try {
            System.out.println("员工：" + userName);
            System.out.println("");
            String coderStr = StringUtil.urlEncoderUTF8(userName);
            String month = StringUtil.addZeroToIntStr(getMonth(), 2);
//        String url = "http://192.168.38.205/getsomeoneinfo?name=%E5%BC%A0%E5%8A%9B&year=2017&month="+month+"&day=undefined";
            String url = "http://192.168.38.205/getsomeoneinfo?name=" + coderStr + "&year=2017&month=" + month + "&day=undefined";
            HttpClientUtil httpClientUtil = new HttpClientUtil();
            ResponseInfo responseInfo = httpClientUtil.executeGet(url);
            List<List> list = JSONFormat.getListFromJson(responseInfo.getContent());

            Long weekWorkTime = 0l;

            // 定义输出日期格式
            Date currentDate = getDate();
            List<Date> days = dateToWeek(currentDate);
            System.out.println("今天的日期: " + getData("yyyy-MM-dd", currentDate));
            for (Date date : days) {
                List<String> punchCardTime = new ArrayList<>();
                String dateStr = getData("yyyy-MM-dd", date);
                for (List<String> l : list) {
                    String time = l.get(3);
                    if (time.contains(dateStr)) {
                        punchCardTime.add(time);
                    }
                }
                System.out.println(dateStr + "打卡时间:" + punchCardTime.toString());
                if (punchCardTime.size() > 0) {
                    long dayWorkTime = getDiffTime(punchCardTime.get(0), punchCardTime.get(punchCardTime.size() - 1));
                    weekWorkTime += dayWorkTime;
                    System.out.println(getWorkTime(dayWorkTime));
                } else {
                    System.out.println("未打卡。");
                }
                System.out.println("");

            }

            System.out.println("一周需要工作时间：" + weekNeedToWorkHour + "小时");

            System.out.println("已" + getWorkTime(weekWorkTime));

            System.out.println(needToWork(weekNeedToWorkHour, weekWorkTime));

            String firdayData = getData("yyyy-MM-dd", days.get(4));
            System.out.println("本周五日期：" + firdayData);
            System.out.println("本周五最早打卡时间：" + getPunchCardTime(weekNeedToWorkHour, weekWorkTime, firdayData, list));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

	public static void main(String[] args) throws HTTPException, ParseException, RunException {
        getWorkAttendance("张力", 45);
    }

/*
	public static void main(String[] args){
		String a = getDateString();
		a = getAddDay(1);
        a = getDate("yyyy-MM-dd HH:mm:ss");
        a = getDate("yyyy-MM-dd_HH-mm-ss",System.currentTimeMillis());
		a = date2TimeStamp("2016-10-10","yyyy-MM-dd");

        try {
            Date b = new SimpleDateFormat("yyyy-MM-dd").parse("2015-12-28");
            a = getData("w",b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(a);
	}
*/

}
