package qiang.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import qiang.bean.OneTrip;
import qiang.bean.TimePoint;
import qiang.bean.TimeSlot;
import qiang.util.FileUtil;


/**
 * 
 * 对时间聚类，划分，
 * 首先将所有的时间都读入
 * @author jq
 *
 */
public class KMeans {

	public static void main(String[] args) {
		
		KMeans div = new KMeans();
		String filePre = "20150803";
		Set<TimePoint> allTimePoint = div.getAllTimePoint(filePre);
		System.out.println(allTimePoint.size());
		
		Set<TimeSlot> pivo = new HashSet<>();
		pivo.add(new TimeSlot(filePre+"040000"));
		//pivo.add(new TimeSlot(filePre+"040000"));
		pivo.add(new TimeSlot(filePre+"080000"));
		pivo.add(new TimeSlot(filePre+"130000"));
		pivo.add(new TimeSlot(filePre+"180000"));
		pivo.add(new TimeSlot(filePre+"213000"));
		
		Set<TimeSlot> ans = div.kMeans(pivo);
		for(TimeSlot ss:ans){
			System.out.println(ss.getTimeInitString()+"\t"+ss.getBeginTimeString()+"\t"+ss.getEndTimeString());
		}
	}
	
	public Set<TimeSlot> kMeans(Set<TimeSlot> pivo){
		if(allTimePoint.size()==0)return null;
		Map<TimeSlot,Set<TimePoint>> clusters = new HashMap<>(); 
		TimeSlot minIndex = null;
		long min;
		Set<TimePoint> tempClu;
	
		int deep =0;
		for(;;){
			System.out.println(deep++);
			// 初始化簇
			initCluster(clusters,pivo);
			for(TimePoint tt:allTimePoint){
				min = Long.MAX_VALUE;
				for(TimeSlot slot:clusters.keySet()){
					long tempDis =slot.dis(tt); 
					if( tempDis < min){
						min = tempDis;
						minIndex = slot;
					}
				}
				tempClu = clusters.get(minIndex);
				tempClu.add(tt);
				minIndex.setSlotBegin(tt.timeBegin); // 我们只使用 上车的时间
				minIndex.setSlotEnd(tt.timeBegin);
			}
			// 计算新簇的 质心。
			int sameCount = 0;
			int size;
			double p;
			Set<TimeSlot> newPio = new HashSet<>();
			for(TimeSlot slot:clusters.keySet()){
				tempClu = clusters.get(slot);
				size = tempClu.size();
				p=0;
				TimeSlot newSlot = new TimeSlot(0);
				for(TimePoint point:tempClu){
					p+=(point.timeBegin)*1.0/size;
					newSlot.setSlotBegin(point.timeBegin);
					newSlot.setSlotEnd(point.timeBegin);
				}
				newSlot.setCount(size);
				newSlot.setTimeIni((long)p);
				newPio.add(newSlot);
				if(Math.abs(newSlot.getTimeIni()-slot.getTimeIni()) < 120){
					sameCount++;
				}
			}
			if(sameCount == clusters.size()){
				return newPio;
			}
			// 否则重新计算
			clusters.clear();
			pivo = newPio;
		}
		
		
	}
	private void initCluster(Map<TimeSlot,Set<TimePoint>> clusters,Set<TimeSlot> pivo){
		clusters.clear();
		for(TimeSlot s:pivo){
			if(clusters.containsKey(s)){
				clusters.get(s).clear();
			}else{
				clusters.put(s, new HashSet<TimePoint>());
			}
		}
	}
	public void clearCluser(Map<TimeSlot,Set<TimePoint>> clusters){
		Set<TimePoint> temp;
		for(TimeSlot s:clusters.keySet()){
			temp = clusters.get(s);
			if(temp == null){
				clusters.put(s, new HashSet<TimePoint>());
			}else{
				temp.clear();
			}
		}
	}
	
	
	static String icCardDataPath = "F:\\公交线路数据\\icCardData\\";
	
	Set<TimePoint> allTimePoint = new HashSet<>();
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	static Calendar cal = Calendar.getInstance();
	
	public Set<TimePoint> getAllTimePoint(String filePre){

		FileUtil fileUtil = new FileUtil(icCardDataPath+filePre+".csv");
		String line1,line2;
		fileUtil.readLine();
		long timeFilter = 0;
		try {
			timeFilter =format.parse(filePre+"000000").getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		OneTrip oneTrip;
		int count =0;
		int exp =0;
		while((line1 = fileUtil.readLine())!=null){
			if(line1.length() < 130 )
				line2 = fileUtil.readLine();
			else line2 = "";
			oneTrip = OneTrip.tripFactory(line1+line2);
			if(oneTrip !=null){
				TimePoint newP = new TimePoint(oneTrip.getMarkTime(),oneTrip.getTradeTime());
				if(newP.timeBegin > timeFilter && newP.timeBegin < timeFilter+24*3600 )
					allTimePoint.add(newP);
				else exp++;
			}else{
				exp++;
			}
			count++;
			if(count %10000==0)System.out.println(count);;
		}
		System.out.println(allTimePoint.size() + " "+ exp);
		return allTimePoint;
	
	}
	
	
	
}
