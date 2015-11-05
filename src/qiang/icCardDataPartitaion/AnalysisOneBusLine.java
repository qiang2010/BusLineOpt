package qiang.icCardDataPartitaion;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import qiang.bean.OneBusSchedule;
import qiang.bean.OneTrip;
import qiang.util.FileUtil;



/**
 * 这里对一条线路做处理：
 * 1. 需要按照busid分组，然后分组后，组内按照时间排序。
 * 2. 目标是为了识别出给定busid，该车走了多少趟。
 * 	主要是为了车次的统计。
 * @author jq
 *
 */
public class AnalysisOneBusLine {

	public static void main(String[] args) {
		int d = 20150805;
		//String day = "20150804";
		for(;d<20150810;d++){
			System.out.println(d);
			AnalysisOneBusLine  ab = new AnalysisOneBusLine();
			ab.analysisOneDay(""+d);
		}
		
	}
	String day = null;
	String busLine;
	public void analysisOneDay(String day){
		this.day = day;
		File dir = new File(path+day);
		String []allFiles = null ;
		if(dir.exists() && dir.isDirectory()){
			allFiles = dir.list();
		}
		if(allFiles == null )return;
		File ansF= new File(ansPath+this.day+"busSchedult.txt");
		if(ansF.exists())ansF.delete();
		for(int i = 0 ; i< allFiles.length;i++){
			//System.out.println(allFiles[0]);
			analysisOneBusLine(day, allFiles[i]);
		}
	}
	
	public void analysisOneBusLine(String day,String lineIdFileName){
		this.busLine = lineIdFileName.substring(0,lineIdFileName.length()-4);
		getTripMap(path+day+"\\"+lineIdFileName);
		sortTripMap();
	}
	
	String path = "F:\\公交线路数据\\处理结果\\";
	String ansPath = "F:\\公交线路数据\\处理结果\\分车次统计\\";
	
	
	public void sortTripMap(){
		ArrayList<OneTrip> tempBusTrips;
		for(String key:tripMap.keySet()){
			tempBusTrips = tripMap.get(key);
			tempBusTrips.sort(new Comparator<OneTrip>() {
				@Override
				public int compare(OneTrip t1, OneTrip t2) {
					return (int)(t1.getMarkTime() - t2.getMarkTime());
					//return  (int)(t1.getTradeTime()-t2.getTradeTime());
				}
			});
			
			//这里根据排好序的上车station 和时间，统计车次 
//			for(OneTrip one:tempBusTrips){
//				System.out.println(one.toString());
//			}
			// 统计当前busId的每次schedule的情况
			ArrayList<OneBusSchedule>  allScheduleCount = countOneBusScheduleTimes(tempBusTrips);
			writeBusScheduleToFile(allScheduleCount);
			//break;
		}
	}
	
	public void writeBusScheduleToFile(ArrayList<OneBusSchedule>  allScheduleCount){
		FileUtil fileUtil = new FileUtil(ansPath+this.day+"busSchedult.txt", true);
		for(OneBusSchedule one :allScheduleCount){
			fileUtil.writeLine(this.busLine+"\t"+one.toString());
		}
	}
	
	/**
	 * //这里根据排好序的上车station 和时间，统计车次 
	 * 当上车站的编号小于一直都是大于下车站的编号，那么当前就是一个方向的
	 * 当上车编号小于下车编号了，说明变换为反方向了。
	 * 通过这种乒乓来判断当前车走了几趟。
	 * 
	 * 统计两个方向分别走了多少趟，
	 * 0  是 上车station 大于 下车 station
	 * 1 是   下车大于上车
	 * @param tempBusTrips
	 * @return
	 */
	public ArrayList<OneBusSchedule> countOneBusScheduleTimes(ArrayList<OneTrip> tempBusTrips){
		ArrayList<OneBusSchedule> allScheduleCount = new ArrayList<>();
		//int []ans = new int[2];
		int s = tempBusTrips.size();
		OneTrip lastTrip = tempBusTrips.get(0);
//		ans[tripDir(lastTrip)]++;
		OneTrip curTrip;
		OneBusSchedule curS = new OneBusSchedule(lastTrip.getBusId(), tripDir(lastTrip));
		allScheduleCount.add(curS);
		curS.addOneTrip(lastTrip);
		curS.setFirstIcMarkTime(lastTrip.getMarkTime());
		curS.setLastIcTradTime(lastTrip.getTradeTime());
		for(int i = 1;i<s;i++){
			curTrip = tempBusTrips.get(i);
			if(sameDir(curTrip, lastTrip)){
				curS.addOneTrip(curTrip);
				curS.setLastIcTradTime(curTrip.getTradeTime()); // 每次都要更新下车时间
				lastTrip = curTrip;
				continue;
			}
			curS = new OneBusSchedule(curTrip.getBusId(), tripDir(curTrip));
			allScheduleCount.add(curS);
			curS.addOneTrip(curTrip);
			curS.setFirstIcMarkTime(curTrip.getMarkTime());
			curS.setLastIcTradTime(curTrip.getTradeTime());
			lastTrip = curTrip;
//			ans[tripDir(curTrip)]++;
		}
		return allScheduleCount;
	}
	
	/**
	 *  由小站到大站编号，就是0方向。
	 * @param one
	 * @return
	 */
	int tripDir(OneTrip one){
		if(one.getMarkstation() > one.getTradestation()){
			return 1;
		}
		return 0;
	}
	boolean sameDir(OneTrip one,OneTrip two){
		int a = tripDir(one)+tripDir(two);
		if(a == 2 || a== 0)return true;
		return false;
	}
	
	Map<String,ArrayList<OneTrip>> tripMap = null;
	public void getTripMap(String fileName){
		 tripMap = new HashMap<>();
		FileUtil fileUtil = new FileUtil(fileName);
		String tempLine;
		OneTrip tempOneTrip;
		String busId;
		ArrayList<OneTrip> tempBusTrips;
		while((tempLine = fileUtil.readLine())!=null){
			tempOneTrip = OneTrip.tripFactory(tempLine);
			if(tempOneTrip == null)continue;
			busId = tempOneTrip.getBusId();
			if(tripMap.containsKey(busId)){
				tempBusTrips = tripMap.get(busId);
			}else{
				tempBusTrips = new ArrayList<>();
				tripMap.put(busId, tempBusTrips);
			}
			tempBusTrips.add(tempOneTrip);
		}
		System.out.println(tripMap.size());
	}
}
