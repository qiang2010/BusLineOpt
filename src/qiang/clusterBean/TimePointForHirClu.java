package qiang.clusterBean;

public class TimePointForHirClu {
	public long timeBegin;
	
	public TimePointForHirClu(long timeBegin) {
		super();
		this.timeBegin = timeBegin;
	}
  
	public long dis(TimePointForHirClu b){
		return (this.timeBegin - b.timeBegin)*(this.timeBegin - b.timeBegin);
	}
}
