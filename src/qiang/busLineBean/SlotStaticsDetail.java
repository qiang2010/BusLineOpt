package qiang.busLineBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SlotStaticsDetail {

	//long slotBegin;
	//long slotEnd;
	Set<String> busIds;
	int countIC;
	public SlotStaticsDetail(){
		countIC =0;
		busIds = new HashSet<>();
		//this.slotBegin = changeStringToTimestamp(slotBegin);
		//this.slotEnd = changeStringToTimestamp(slotEnd);
		
	}
	public Set<String> getBusIds(){
		return this.busIds;
	}
	public void increaseICcount(){
		countIC++;
	}
	
//	public long getSlotBegin() {
//		return slotBegin;
//	}
//	public void setSlotBegin(long slotBegin) {
//		this.slotBegin = slotBegin;
//	}
//	public long getSlotEnd() {
//		return slotEnd;
//	}
//	public void setSlotEnd(long slotEnd) {
//		this.slotEnd = slotEnd;
//	}
	public int getCountIC() {
		return countIC;
	}
	public void setCountIC(int countIC) {
		this.countIC = countIC;
	}


	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	static Calendar cal = Calendar.getInstance();
	public static long changeStringToTimestamp (String time){
		try {
					
			Date date = format.parse(time);
			cal.setTime(date);
		return cal.getTimeInMillis()/1000;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
}
