package qiang.bean;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import qiang.util.TimeFormatUtil;


/**
 * 用于统计发车一次的上车情况
 * 比如  630路车，从保福寺，到xxx方向，也就是 0 方向，每个车站的上下车人数。
 * @author jq
 *
 */
public class OneBusSchedule {
	String busId;
	int dir;
	int countSum;
	long firstIcMarkTime;
	long lastIcTradTime;
	Map<Integer, Integer> stationUpIcCount = new TreeMap<Integer, Integer>();
	Map<Integer, Integer> stationOffIcCount = new TreeMap<Integer, Integer>();
	public OneBusSchedule(String busId,int dir){
		this.busId = busId;
		this.dir  = dir;
		countSum = 0;
		firstIcMarkTime  = Long.MAX_VALUE;
		lastIcTradTime   = Long.MIN_VALUE;
	}
	public void setFirstIcMarkTime(long markTime){
		this.firstIcMarkTime = this.firstIcMarkTime < markTime ? this.firstIcMarkTime :markTime ;
	}
	
	public void setLastIcTradTime(long tradeTime){
		this.lastIcTradTime = this.lastIcTradTime < tradeTime ? tradeTime:this.lastIcTradTime;
	}
	
	public void increaseCount(){
		countSum++;
	}
	public void addOneTrip(OneTrip one){
		addOneUpOff(one.getMarkstation(), one.getTradestation());
	}
	
	public void addOneUpOff(int markStation,int tradeStation){
		increaseCount();
		addOne(stationOffIcCount, tradeStation);
		addOne(stationUpIcCount, markStation);
	}
	private void addOne(Map<Integer, Integer> map,int station){
		if(map.containsKey(station)){
			map.put(station, map.get(station)+1);
		}else{
			map.put(station,1);
		}
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(busId+"\t");
		sb.append(dir+"\t");
		sb.append(countSum+"\t");
		sb.append(TimeFormatUtil.changeTimeStampToString(firstIcMarkTime)+"\t");
		sb.append(TimeFormatUtil.changeTimeStampToString(lastIcTradTime )+"\t");
		for(int s:stationUpIcCount.keySet()){
			sb.append(s+","+stationUpIcCount.get(s)+",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("\t");
		for(int s:stationOffIcCount.keySet()){
			sb.append(s+","+stationOffIcCount.get(s)+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
}
