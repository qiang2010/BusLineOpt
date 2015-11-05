package qiang.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import qiang.util.FileUtil;


/**
 *  按照线路统计
 * @author jq
 *
 */
public class StaticsICEachLine {

	public static void main(String[] args) {
		
		StaticsICEachLine stat = new StaticsICEachLine();
		Map<String,Integer> staticsOneDay = stat.getAllTimePoint("20150803");
		ArrayList<Integer> ic = new ArrayList<>();
		for(String key:staticsOneDay.keySet()){
			System.out.println(key+"\t"+staticsOneDay.get(key));
			ic.add(staticsOneDay.get(key));
		}
		int max = Collections.max(ic);
		int n = max/10+1;
		int []coun = new int[n];
		for(int a:ic){
			coun[a/1000]++;
		}
		System.out.println("count");
		for(int k:coun){
			System.out.println(k);
		}
	}
	static String icCardDataPath = "F:\\公交线路数据\\icCardData\\";
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	static Calendar cal = Calendar.getInstance();
	Map<String,Integer> staticsOneDay = new HashMap<>();
	
	public  Map<String,Integer>  getAllTimePoint(String filePre){
		staticsOneDay.clear();
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
		while((line1 = fileUtil.readLine())!=null){
			if(line1.length() < 130 )
				line2 = fileUtil.readLine();
			else line2 = "";
			String []split = (line1+line2).split("\",\"");
			long [] times = changeToTimestamp(split[4], split[3]);
			if(times !=null){
				if(times[0] > timeFilter && times[1] < timeFilter+24*3600 && times[0] < times[1] ){
					String lineNu = split[7].substring(split[7].length()-3,split[7].length());
					if(staticsOneDay.containsKey(lineNu))
					{
						staticsOneDay.put(lineNu, staticsOneDay.get(lineNu)+1);
					}else{
						staticsOneDay.put(lineNu, 1);
					}
				}else exp++;
			}else{
				exp++;
			}
			count++;
			if(count %10000==0)System.out.println(count);;
		}
		System.out.println(staticsOneDay.size() + " "+ exp);
		return staticsOneDay;
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
}
