package qiang.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MySqlUtil {

	static String driver = null;
	static String url = null;
	static String user = null;
	static String pass = null;
	static {
		Properties pro = new Properties();
		try {
			//File f= new File();
			//if(f.exists())System.out.println("yes");
//			pro.load(new FileInputStream("DB.properties"));
			pro.load(MySqlUtil.class.getClassLoader().getResourceAsStream("DB.properties"));
			driver = pro.getProperty("driver");
			url = pro.getProperty("url");
			user = pro.getProperty("user");
			pass = pro.getProperty("password");
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	Connection con = null;
	Statement stat = null;
	public boolean connectDatabase(){
		if(con != null) return true;
		if(driver == null) return false;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			return true;
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Statement getStatement(){
		if(con == null) return null;
		try {
			stat =  con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stat;
	}
	
	public boolean closeCon(){
		if(stat != null){
			try {
				stat.close();
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}
