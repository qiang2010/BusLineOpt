package qiang.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeFormatUtil {
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	static Calendar cal = Calendar.getInstance();
	public static long[]changeToTimestamp(String marktime,String tradeTime){
		long []ans = new long[2];
		try {
			ans[0] = changeStringToTimestamp(marktime);
			ans[1] = changeStringToTimestamp(tradeTime);
		} catch (Exception e) {
			return null;
		}
		return ans;
	}
	public static long changeStringToTimestamp (String time)throws Exception{
		Date date = format.parse(time);
		cal.setTime(date);
		//cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis()/1000;
	}
	public static long changeStringToTimestampExceptionCache (String time){
		
		try {
			Date date = format.parse(time);
			cal.setTime(date);
			//cal.set(Calendar.SECOND, 0);
			return cal.getTimeInMillis()/1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	public static String  changeTimeStampToString(long stamp){
		cal.setTimeInMillis(stamp*1000);
		return format.format(cal.getTime());
	}
	
}
