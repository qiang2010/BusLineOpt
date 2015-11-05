package qiang.clusterBean;

import java.util.HashSet;
import java.util.Set;


public class Cluster {
	TimeSlotForHirClu slot;
	Set<TimePointForHirClu> points;
	public Cluster(long init) {
		super();
		this.slot = new TimeSlotForHirClu(init);
		this.points = new HashSet<TimePointForHirClu>();
	}
	
	public Cluster mergerCluster(Cluster a){
		this.points.addAll(a.getPoints());
		a.points.clear();
		this.slot.setSlotBegin(a.getSlot().getSlotBegin());
		this.slot.setSlotEnd(a.getSlot().getSlotEnd());
		return this;
	}
	
	public long clusterDis1(Cluster a){
		Set<TimePointForHirClu> points2 = a.getPoints();
		long min = Long.MAX_VALUE;
		for(TimePointForHirClu p1:this.points){
			for(TimePointForHirClu p2:points2){
				long dis = p1.dis(p2);
				if(min > dis){
					min = dis;
				}
			}
		}
		return min;
	
	}


	/**
	 * 这里是两个簇质心之间的距离
	 * @param clu1
	 * @param points1
	 * @param clu2
	 * @param points2
	 * @return
	 */
	long clusterDis2(Cluster a){
		return this.slot.dis(a.getSlot());
	}
	
	public TimeSlotForHirClu getSlot() {
		return slot;
	}

	public void setSlot(TimeSlotForHirClu slot) {
		this.slot = slot;
	}

	public Set<TimePointForHirClu> getPoints() {
		return points;
	}
}
