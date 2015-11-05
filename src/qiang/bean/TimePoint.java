package qiang.bean;

public class TimePoint {

	public long timeBegin;
	public long timeEnd;
	
	public TimePoint(long timeBegin, long timeEnd) {
		super();
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
	}
  
	public long dis(TimePoint b){
		return (this.timeBegin - b.timeBegin)*(this.timeBegin - b.timeBegin);
	}
}
