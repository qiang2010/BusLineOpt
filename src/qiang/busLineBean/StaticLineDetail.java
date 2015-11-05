package qiang.busLineBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qiang.busStation.Station;
import qiang.util.FileUtil;



/**
 * 用于统计每条线路，在各个时段的详细信息。
 * 
 * @author jq
 *
 */
public class StaticLineDetail {

	public static void main(String[] args) {
		
		StaticLineDetail sd = new StaticLineDetail();
		sd.staticsLineDetail("20150804");
		sd.loadStationDetailCorLine();
		Map<String,BusLine> ans =  sd.getAllLines();
		BusLine tempL;
		ArrayList< SlotStaticsDetail> slots ;
		SlotStaticsDetail sld;
		List<Station> stations;
		int allIinesNum = ans.size();// 线路总数
		int icCardSum = 0;	// 所有ic卡打卡人数
		int busSum =0;	// 车辆总数
		int stationSum =0; // 站点总数
		Map<String,BusLine> badLine = new  HashMap<String,BusLine>();
		
		int countBadSum =0; // 统计两个指标小于给定值的数量
		double perStationIcThres = 200;
		double perStationPerBusThres = 8;
		for(String s:ans.keySet()){
			tempL = ans.get(s);
			
			slots = tempL.getSlots();
			stations = tempL.getStations();
			sld = slots.get(0);
			if(stations.size() ==0 ||  sld.getBusIds().size()==0) continue;
			System.out.print(s+"\t");
			for(int i=0;i<slots.size();i++){
				sld = slots.get(i);
				System.out.print(i); // 时段的标号
				int icCount = sld.getCountIC(); // 当前时段乘车人数
				icCardSum+=icCount;
				int busIds = sld.getBusIds().size();// 车辆数目
				busSum+= busIds;
				double aveBusIC = icCount*1.0/busIds;  // 平均每辆车的运送人数
				int stationNum = stations.size();     // 站点的数量
				stationSum+=stationNum;
				
				
				
				double perBusPerStationIc = // 平均每辆车每个站点的上车人数
						sld.getCountIC()*1.0/sld.getBusIds().size()/stations.size();
				double perStationIc = sld.getCountIC()*1.0/stations.size();// 每个站点的上车人数
				
				if(perStationPerBusThres >perBusPerStationIc && perStationIcThres >  perStationIc){
					countBadSum++;
				}
				
				System.out.print(icCount+"\t"+busIds+"\t"+aveBusIC+"\t"+stationNum+
						"\t"+perBusPerStationIc+"\t"+perStationIc);
			}
			System.out.println();
		}
		System.out.println("线路数\t总人数\t车辆总数\t");
		double perBusIcSum = icCardSum*1.0/busSum; // 平均每辆车的打卡人数
		// 平均每条线路的运送人数
		// 
		System.out.println(allIinesNum+"\t"+icCardSum+"\t"+busSum);
		System.out.println(countBadSum);
	}
	
	Map<String,BusLine> allLines = new HashMap<>();
	static String icCardDataPath = "F:\\公交线路数据\\icCardData\\";
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	static Calendar cal = Calendar.getInstance();
	static String []timeSeg = {"000000"};//,"0600","0900","1700","1800"};
	
	
	
	public  Map<String,BusLine>  staticsLineDetail(String filePre){
		try {
			setTimeSegs(filePre);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileUtil fileUtil = new FileUtil(icCardDataPath+filePre+".csv");
		String line1,line2;
		fileUtil.readLine();
		long timeFilter = 0;
		try {
			timeFilter =format.parse(filePre+"000000").getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int count =0;
		int exp =0;
		BusLine tempLine;
		while((line1 = fileUtil.readLine())!=null){
			if(line1.length() < 130 )
				line2 = fileUtil.readLine();
			else line2 = "";
			String []split = (line1+line2).split("\",\"");
			long [] times = changeToTimestamp(split[4], split[3]);
			if(times !=null){
				if(times[0] > timeFilter && times[1] < timeFilter+24*3600 && times[0] < times[1] ){
					String lineNu = split[7].substring(split[7].length()-3,split[7].length());
					if(allLines.containsKey(lineNu))
					{
						tempLine = allLines.get(lineNu);
					}else{
						tempLine = new BusLine(lineNu, timeSeg.length);
						allLines.put(lineNu, tempLine);
					}
					tempLine.addBusId(turnTimestampToTimeSeg(times[0]), split[8]);
				}else exp++;
			}else{
				exp++;
			}
			count++;
			if(count %10000==0)System.out.println(count);;
		}
		System.out.println(allLines.size() + " "+ exp);
		return allLines;
	}
	
	// 统计各个线路上的所有站点的固有信息Station
	String stationFile = "车站信息.txt";
	public void loadStationDetailCorLine(){
		
		FileUtil file = new FileUtil(icCardDataPath+stationFile);
		String tempLine,lineNum;
		String []splits;
		BusLine tempBusLine;
		ArrayList<Station> stations ;
		Station sta;
		int base;
		while((tempLine = file.readLine())!= null){
			splits = tempLine.trim().split("\\s+");
			base  = 0;
			if(splits.length < 13){
				base = 4;

			}
			if(splits.length <11){
				System.out.println(tempLine);
			}
			lineNum = lineNumFormat(splits[5-base].trim());
			if(allLines.containsKey(lineNum)){
				tempBusLine = allLines.get(lineNum);
			}else{
				tempBusLine = new BusLine(lineNum, timeSeg.length);
				allLines.put(lineNum, tempBusLine);
			}
			tempBusLine.setLineKey(splits[4-base]);
			sta = new Station();
			sta.setStationId(splits[8-base]);
			sta.setStationName(splits[9-base]);
			sta.setStationNum(Integer.parseInt(splits[10-base]));
			sta.setAlt(Double.parseDouble(splits[13-base]));
			sta.setLot(Double.parseDouble(splits[14-base]));
			stations = tempBusLine.getStations();
			stations.add(sta);
		}
	}
	
	// 站位信息中的线路信息和IC打卡不一样，需要补上0
	private String lineNumFormat(String in){
		if(in == null || in.length()==0) return "000";
		int s = in.length();
		if(s >= 3) return in;
		int dif = 3-s;
		StringBuilder sb = new StringBuilder();
		for(int i =0;i<dif;i++){
			sb.append("0");
		}
		sb.append(in);
		return sb.toString();
	}
	
	
	
	
	
	public int turnTimestampToTimeSeg(String timestamp){
		long time = -1;
		try {
			time = changeStringToTimestamp(timestamp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return turnTimestampToTimeSeg(time);
	}
	
	
	
	
	
	
	public int turnTimestampToTimeSeg(long timestamp){
		int i;
		for( i=0;i<timeSegs.length;i++){
			if(timestamp < timeSegs[i])break;
		}
		if(i==0){
			return -1;
		}
//		if(i==timeSegs.length){
//			return timeSegs.length-1;
//		}
		return i-1;
		
	}
	long []timeSegs;
	public void setTimeSegs(String filePre) throws Exception{
		int s = timeSeg.length;
		this.timeSegs = new long[s];
		int i=0;
		for(String seg:timeSeg){
			long st = changeStringToTimestamp(filePre+seg);
			this.timeSegs[i] = st;
		}
		
	}
	
	
	long[]changeToTimestamp(String marktime,String tradeTime){
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







	public Map<String, BusLine> getAllLines() {
		return allLines;
	}







	public void setAllLines(Map<String, BusLine> allLines) {
		this.allLines = allLines;
	}
	
}
