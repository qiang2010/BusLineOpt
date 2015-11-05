package qiang.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * kmeans聚类后的簇
 * 
 * timeBegin 是聚类中所有节点的最小值
 * timeEnd  是最大值。
 * @author jq
 *
 */
public class TimeSlot {

	long slotBegin;
	long slotEnd;
	long timeIni;
	int count;

	public TimeSlot(String init){
		this.slotBegin = Long.MAX_VALUE;
		this.slotEnd = Long.MIN_VALUE;
		this.timeIni =changeStringToTimestamp(init);
		count=0;
	}
	public TimeSlot(long init){
		this.slotBegin = Long.MAX_VALUE;
		this.slotEnd = Long.MIN_VALUE;
		this.timeIni =(init);
		count=0;
	}
	
	
	public long getSlotBegin() {
		return slotBegin;
	}
	public void setSlotBegin(long slotBegin) {
		if(slotBegin < this.slotBegin)
			this.slotBegin = slotBegin;
	}
	public long getSlotEnd() {
		return slotEnd;
	}
	public void setSlotEnd(long slotEnd) {
		if(this.slotEnd <  slotEnd)
			this.slotEnd = slotEnd;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getTimeIni() {
		return timeIni;
	}
	public String getTimeInitString(){
		cal.setTimeInMillis(this.timeIni*1000);
		return format.format(cal.getTime());
	}
	
	public void setTimeIni(long timeIni) {
		this.timeIni = timeIni;
	}
	public long dis(TimePoint point){
		return (point.timeBegin-this.timeIni)*(point.timeBegin-this.timeIni);
	}
	
	public long dis(TimeSlot slot){
		return (slot.timeIni-this.timeIni)*(slot.timeIni-this.timeIni);
	}
	
	public String getBeginTimeString(){
		cal.setTimeInMillis(this.slotBegin*1000);
		return format.format(cal.getTime());
	}
	public String getEndTimeString(){
		cal.setTimeInMillis(this.slotEnd*1000);
		return format.format(cal.getTime());
	}
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	static Calendar cal = Calendar.getInstance();
	public static long changeStringToTimestamp (String time){
		try {
					
			Date date = format.parse(time);
			cal.setTime(date);
			cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis()/1000;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
}
