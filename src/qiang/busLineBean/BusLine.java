package qiang.busLineBean;

import java.util.ArrayList;

import qiang.busStation.Station;

public class BusLine {

	String lineId;
	String lineKey;
	// 分时段的详细信息
	ArrayList< SlotStaticsDetail> slots ;
	
	// 该线路上各个车站的信息，包括gps等固有信息
	ArrayList<Station> stations ;
	
	public BusLine(String lineId,int timeSegNum){
		this.lineId = lineId;
		slots = new ArrayList<>();
		for(int i=0;i<timeSegNum;i++){
			slots.add( new SlotStaticsDetail());
		}
		stations = new ArrayList<>();
	}
	
	public String getLineKey() {
		return lineKey;
	}

	public void setLineKey(String lineKey) {
		this.lineKey = lineKey;
	}

	public boolean addBusId(int timeSeg,String busid){
		if(timeSeg > slots.size())return false;
		slots.get(timeSeg).getBusIds().add(busid);
		slots.get(timeSeg).increaseICcount();
		return true;
	}
	
	
	public ArrayList<Station> getStations() {
		return stations;
	}
	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}
	public ArrayList<SlotStaticsDetail> getSlots() {
		return slots;
	}
	public void setSlots(ArrayList<SlotStaticsDetail> slots) {
		this.slots = slots;
	}
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	
}
