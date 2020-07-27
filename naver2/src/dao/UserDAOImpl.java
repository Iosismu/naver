package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DB;
import dto.DataDTO;


import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO{

	@Override
	public void insert(DataDTO dto) {
	
	PreparedStatement pstmt = null;
	Connection conn = null;
	
	 try{	            
		 	conn = DB.conn();
			String sql = "INSERT INTO busan_weather (title, originallink, link, description, pubDate) VALUES (?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			// 4. 데이터 binding
			pstmt.setObject(1, dto.getTitle());
			pstmt.setObject(2, dto.getOriginallink());
			pstmt.setObject(3, dto.getLink());
			pstmt.setObject(4, dto.getDescription());
			pstmt.setObject(5, dto.getPubDate());
			
			
			
            int count = pstmt.executeUpdate();
            if( count == 0 ){
                System.out.println("데이터 입력 실패");
            }
            else{
                System.out.println("데이터 입력 성공");
            }
            
            if( conn != null && !conn.isClosed()){
                conn.close();
            }
            if( pstmt != null && !pstmt.isClosed()){
                pstmt.close();
            }   
         
	 }	        
    catch(Exception e){
         System.out.println("에러: " + e);
        }
    finally{
          try{
              if( conn != null && !conn.isClosed()){
                  conn.close();
              }
          }
          catch(Exception e){
              e.printStackTrace();
          }
    	}           
	}

}
