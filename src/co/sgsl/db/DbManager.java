package co.sgsl.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import co.sgsl.core.AppProperties;

public enum DbManager {
	instance;
	
	private Connection getConnection() throws Exception{
		Connection connection = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			/*StringBuilder sb = new StringBuilder();
			
			sb.append("jdbc:sqlserver://").append(AppProperties.getInstance().getProperty("DATABASE_IP")).append(":").append(AppProperties.getInstance().getProperty("DATABASE_PORT")).append(";user=").append(AppProperties.getInstance().getProperty("DATABASE_USERNAME")).append(";password=").append(AppProperties.getInstance().getProperty("DATABASE_PASSWORD")).append(";database=").append(AppProperties.getInstance().getProperty("DATABASE_NAME"));
			
			System.out.println("connection3 "+sb.toString()+"---"+AppProperties.getInstance().getProperty("DATABASE_PASSWORD")+"---"+AppProperties.getInstance().getProperty("DATABASE_USERNAME"));
			*/
			
			
			connection = DriverManager.getConnection("jdbc:sqlserver://172.20.6.225:1433;user=sa;password=Servion@123;database=CHOLA");
		}catch(Exception e){
			System.out.println(e); 
		}
			return connection;
	}
	
	public List<HashMap<String, String>> fetchData(String sql){
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<HashMap<String, String>> retList = new ArrayList<HashMap<String,String>>();
		try {
			con = getConnection();
			if(con != null){
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				System.out.println(rs);
				while(rs.next()){
					HashMap<String, String> retHash = new LinkedHashMap<String, String>();
					System.out.println(columnCount);
					for (int i = 1; i <= columnCount; i++ ) {
					  String name = rsmd.getColumnName(i);
					  String value = rs.getString(i);
					  System.out.println(name+""+value);
 					  retHash.put(name, value);
					}
					
					retList.add(retHash);
				}
			}
			
		} catch (Exception e) {
			retList = null;
			//LoggerUtil.error("[DbManager fetchData] Error occured", e);
		}finally{
			try {
				if(rs != null)
					rs.close();
				
				if(stmt != null)
					stmt.close();
				
				if(con != null)
					con.close();
			} catch (Exception e) {
				//LoggerUtil.error("[DbManager fetchData] Error occured in close", e);
			}
		}
		
		return retList;
	}
	
	public void updateData(String... sqlArr){
		
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = getConnection();
			System.out.println("enter udate data 1 "); 
			
			if(con != null){
				con.setAutoCommit(false);
				stmt = con.createStatement();
				String sqlExt = null;
				for (String sql : sqlArr) {
					sqlExt = sql;
					//stmt.addBatch(sql);
					System.out.println("sql qury ::::::: "+sqlExt); 
					stmt.executeUpdate(sqlExt);
					
					System.out.println("enter udate data 2 "); 
				}
				
				stmt.executeBatch();
				con.commit();
			}
			
		} catch (Exception e) {
			//LoggerUtil.error("[DbManager updateData] Error occured", e);
		}finally{
			try {
				if(stmt != null)
					stmt.close();
				
				if(con != null)
					con.close();
			} catch (Exception e) {
				//LoggerUtil.error("[DbManager updateData] Error occured in close", e);
			}
		}
		
	}
	
	
public boolean updateQuery(String sqlExt){
		
		Connection con = null;
		Statement stmt = null;
		int rows = 0;
		try {
			con = getConnection();
			System.out.println("enter udate data 1 "); 
			
			if(con != null){
				con.setAutoCommit(true);
				stmt = con.createStatement();
				//String sqlExt = null;
				//for (String sql : sqlArr)
				{
					//sqlExt = sql;
					//stmt.addBatch(sql);
					System.out.println("sql qury ::::::: "+sqlExt); 
					 rows = stmt.executeUpdate(sqlExt);
					
					System.out.println("enter udate data 2 "); 
				}
				
				//stmt.executeBatch();
				con.commit();
			}
			
		} catch (Exception e) {
			//LoggerUtil.error("[DbManager updateData] Error occured", e);
		}finally{
			try {
				if(stmt != null)
					stmt.close();
				
				if(con != null)
					con.close();
			} catch (Exception e) {
				//LoggerUtil.error("[DbManager updateData] Error occured in close", e);
			}
		}
		
		if(rows > 0)
		{
			return true;
		}
		
		return false;
		
	}
	
	
	
	
	
	
public void insterData(String... sqlArr){
		
		Connection con = null;
		Statement stmt = null;
		String sqlString = null;
		try {
			con = getConnection();
			
			if(con != null){
				con.setAutoCommit(false);
				stmt = con.createStatement();
				
				for (String sql : sqlArr) {
					sqlString = sql;
				}
				System.out.println("===>"+sqlString); 
				if(sqlString !=null ){
				stmt.executeUpdate(sqlString);
				con.commit();
				}
			}
			
		} catch (Exception e) {
			//LoggerUtil.error("[DbManager updateData] Error occured", e);
		}finally{
			try {
				if(stmt != null)
					stmt.close();
				
				if(con != null)
					con.close();
			} catch (Exception e) {
				//LoggerUtil.error("[DbManager updateData] Error occured in close", e);
			}
		}
		
	}
}
