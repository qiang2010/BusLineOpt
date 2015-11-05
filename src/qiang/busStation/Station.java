package qiang.busStation;

/**
 *  Station 类记录的是一个车站的详细信息，包括车站的位置
 *  名称，id等，但是不包括经过的公交车信息。
 * @author jq
 *
 */
public class Station {

	String stationId;
	int stationNum;
	public int getStationNum() {
		return stationNum;
	}
	public void setStationNum(int stationNum) {
		this.stationNum = stationNum;
	}
	String stationName;
	double alt;
	double lot;
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public double getAlt() {
		return alt;
	}
	public void setAlt(double alt) {
		this.alt = alt;
	}
	public double getLot() {
		return lot;
	}
	public void setLot(double lot) {
		this.lot = lot;
	}
	
	
}
