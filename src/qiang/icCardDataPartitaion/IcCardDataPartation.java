package qiang.icCardDataPartitaion;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import qiang.util.FileUtil;
import qiang.util.TimeFormatUtil;



/**
 *  将大数据量的文件按照线路划分成小的文件
 * 
 * @author jq
 * 
 *
 */
public class IcCardDataPartation {

	public static void main(String[] args) {
		

		//String day = "20150805";
		int d = 20150805;
		//String day = "20150804";
		for(;d<20150810;d++){
			IcCardDataPartation ip = new IcCardDataPartation();
			ip.filePartation(""+d);
		}
	}
	String path = "F:\\公交线路数据\\处理结果\\";
 
	static String icCardDataPath = "F:\\公交线路数据\\icCardData\\";
	/*
	 * key 是lineNo
	 * value 是一行打卡记录
	 */
	//Map<String,LinkedList<String>> allData = new HashMap<>();
	Map<String,FileUtil> allFiles = new HashMap<>();
	
	public void  filePartation(String filePre){
	
		
		FileUtil fileUtil = new FileUtil(icCardDataPath+filePre+".csv");
		String line1,line2;
		fileUtil.readLine();
		long timeFilter = 0;
		try {
			timeFilter =TimeFormatUtil.changeStringToTimestamp(filePre+"000000");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int exp = 0;
		int count =0;
		String lineNu;
		FileUtil tempFileUtil;
		
		File f = new File(path+filePre);
		if(!f.exists()){
			f.mkdirs();
		}
		
		while((line1 = fileUtil.readLine())!=null){
			if(line1.length() < 130 )
				line2 = fileUtil.readLine();
			else line2 = "";
			String []split = (line1+line2).split("\",\"");
			long [] times = TimeFormatUtil.changeToTimestamp(split[4], split[3]);
			if(times !=null){
				if(times[0] > timeFilter && times[1] < timeFilter+24*3600 && times[0] < times[1]){
					 lineNu = split[7];
					 if(allFiles.containsKey(lineNu)){
						 tempFileUtil = allFiles.get(lineNu);
					 }else{
						 tempFileUtil = new FileUtil(path+filePre+"\\"+lineNu+".txt",true);
					 }
					 tempFileUtil.writeLine(line1+line2);
//					 if(allData.containsKey(lineNu)){
//						 tempList = allData.get(lineNu);
//					 }else{
//						 tempList = new LinkedList<>();
//						 allData.put(lineNu, tempList);
//					 }
//					 tempList.addLast(line1+line2);
				}else exp++;
			}else{
				exp++;
			}
			count++;
			if(count %10000==0)System.out.println(count);;
		}
		System.out.println(exp);
	}
 
}
