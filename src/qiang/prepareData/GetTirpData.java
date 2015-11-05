package qiang.prepareData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import qiang.bean.OneTrip;
import qiang.util.FileUtil;
import qiang.util.MySqlUtil;

public class GetTirpData {

	public static void main(String[] args) {
		LinkedList<OneTrip> ans = getCurrentTirpFromFile("20150803.csv");
		System.out.println(ans.size());
	}
	
	
	static String icCardDataPath = "F:\\公交线路数据\\icCardData\\";
	public static LinkedList<OneTrip> getCurrentTirpFromFile(String fileName){
		
		LinkedList<OneTrip> temp =  null;;
		if(allData.containsKey(fileName)){
			temp = allData.get(fileName);
			if(temp !=null) return temp;
		}
		temp = new LinkedList<OneTrip>();
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
			if(oneTrip!=null)
				temp.addLast(oneTrip);
			count++;
			if(count %10000==0)System.out.println(count);
		}
		allData.put(fileName, temp);
		return temp;
	}
	
	static Map<String,LinkedList<OneTrip>> allData = new HashMap<>(); 
	static String selectSql = "select * from ";
	
	public static LinkedList<OneTrip> getCurrentTirpFromDatabase(String tableName){
		LinkedList<OneTrip> temp =  null;;
		if(allData.containsKey(tableName)){
			temp = allData.get(tableName);
			if(temp !=null) return temp;
		}
		temp = new LinkedList<OneTrip>();
		MySqlUtil sqlUtil = new MySqlUtil();
		if(!sqlUtil.connectDatabase()){
			return null;
		}
		Statement stat = sqlUtil.getStatement();
		try {
			ResultSet rs = stat.executeQuery(selectSql+tableName);
			OneTrip oneTrip;
			while(rs.next()){
//				oneTrip = new OneTrip
//						(Integer.parseInt(rs.getString("tradestation")),Integer.parseInt(rs.getString("markstation")) , 
//								rs.getString("lineId"), rs.getString("busId"),
//								rs.getString("markLineId"), rs.getString("markBusId"),
//								Integer.parseInt(rs.getString("ppd")), rs.getString("tradeType"), rs.getString("cardType"),
//								rs.getString("tradeTime") ,rs.getString("markTime") );
				//temp.addLast(oneTrip);
			}
			allData.put(tableName, temp);
			System.out.println(temp.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	
	
}
