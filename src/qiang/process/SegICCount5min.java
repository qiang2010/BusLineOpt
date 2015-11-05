package qiang.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import qiang.bean.OneTrip;
import qiang.util.FileUtil;


/**
 * 以分钟为粒度，统计一天中，每个时间打卡数量
 * 
 * @author jq
 *
 */
public class SegICCount5min {

	public static void main(String[] args) {
		
		Map<Long,Integer> segCount  = new SegICCount5min().getSegCount("20150804.csv");
		for(long a:segCount.keySet()){
			System.out.println(a+"\t"+segCount.get(a));
		}
	}
	
	Map<Long,Integer> segCount = new HashMap<>();
	
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	static Calendar cal = Calendar.getInstance();
	private void initSegCount(String fileName){
		try {
			Date date = format.parse(fileName.substring(0,8));
			long begin = date.getTime()/1000;
			System.out.println(begin);
			for(int i =0;i< 24*60/5;i+=5){
				segCount.put(begin+i*60, 0);
			}
			System.out.println(segCount.size());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		
	}
	
	
	
	static String icCardDataPath = "F:\\公交线路数据\\icCardData\\";
	public Map<Long,Integer> getSegCount(String fileName){
		initSegCount(fileName);
		FileUtil fileUtil = new FileUtil(icCardDataPath+fileName);
		String line1,line2;
		fileUtil.readLine();
		OneTrip oneTrip;
		int count =0;
		while((line1 = fileUtil.readLine())!=null){
			if(line1.length() < 130 )
				line2 = fileUtil.readLine();
			else line2 = "";
			oneTrip = OneTrip.tripFactory(line1+line2);
			if(oneTrip !=null && segCount.containsKey(oneTrip.getMarkTime())){
				long mk = oneTrip.getMarkTime();
				segCount.put(mk, segCount.get(mk)+1);
			}
			count++;
			if(count %10000==0)System.out.println(count);
		}
		return segCount;
	}
}
