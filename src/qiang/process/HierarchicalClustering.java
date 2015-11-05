package qiang.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import qiang.clusterBean.Cluster;
import qiang.clusterBean.TimePointForHirClu;
import qiang.clusterBean.TimeSlotForHirClu;
import qiang.util.FileUtil;

/**
 *  由底向上的层次聚类。
 * @author jq
 *
 */
public class HierarchicalClustering {
	ArrayList<Cluster> clusters  = new ArrayList<Cluster>();
	
	public static void main(String[] args) {
		HierarchicalClustering  hir = new HierarchicalClustering();
		hir.cluster();
	}
	
	public void cluster(){
		String filePre = "20150803";
		initClusters(filePre);
	
		hierarchicalCluster(6);
		TimeSlotForHirClu s;
		for(Cluster cc:clusters){
			s = cc.getSlot();
			System.out.println(s.getTimeInitString()+"\t"+s.getBeginTimeString()+"\t"+s.getEndTimeString()+"\t"+cc.getPoints().size());
		}
	}
	
	
	public void hierarchicalCluster(int thres){
		long tempmin = Long.MAX_VALUE;
		Cluster c1= null,minC1 = null;
		Cluster c2= null,minC2 = null;
		int cc =0;
		int size = clusters.size();
		while(size > thres){
			System.out.println(cc+++ "\t"+clusters.size()+"簇的个数");
			tempmin = Long.MAX_VALUE;
			for(int i=0;i<size;i++){
				c1 = clusters.get(i);
				for(int j = i+1;j<size;j++){
					c2 = clusters.get(j);
					long temp = c1.clusterDis1(c2);
					if(temp < tempmin){
						tempmin = temp;
						minC1 = c1;minC2 = c2;
					}
				}
			}
			// 合并两个
			Cluster newClu = minC1.mergerCluster(minC2);
			clusters.remove(minC2);
			clusters.remove(minC1);
			clusters.add(newClu);	
			size = clusters.size();
		}
		
	}
	
	static String icCardDataPath = "F:\\公交线路数据\\icCardData\\";
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	static Calendar cal = Calendar.getInstance();
	
	public void  initClusters(String filePre){
		clusters.clear();
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
			long [] times = changeToTimestamp(split[3], split[4]);
			if(times !=null){
				TimePointForHirClu newP = new TimePointForHirClu(times[0]);
				if(times[0] > timeFilter && times[1] < timeFilter+24*3600 ){
					Cluster cl = new Cluster(newP.timeBegin);
					TimeSlotForHirClu clu = cl.getSlot();
					clu.setSlotBegin(newP.timeBegin);
					clu.setSlotEnd(newP.timeBegin);
					Set<TimePointForHirClu> set = cl.getPoints();
					set.add(newP);
					clusters.add(cl);
				}else exp++;
			}else{
				exp++;
			}
			count++;
			if(count %10000==0)System.out.println(count);;
		}
		System.out.println(clusters.size() + " "+ exp);
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
