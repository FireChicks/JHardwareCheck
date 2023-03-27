/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hardWare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 *
 * @author LeeJanyun
 */
public class ComEstimateDAO {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

	public ComEstimateDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/pab"; // 추후 IP로 수정
			String dbID = "dldi1021";
			String dbPassword = "1234";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        
        public int getNext() {
		String SQL = "SELECT comEstimateID  FROM comEstimate ORDER BY comEstimateID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
             
        public int inputComEstimate(String userID, Map<String, String> hardwareMap) {
		String SQL = "INSERT INTO comEstimate (ComEstimateID, userID, CPUInfo, MainBoard, GPU, Ram, Storage) VALUES(?,?,?,?,?,?,?)";
                if(checkDuplication(userID, hardwareMap) == -1) { //견적 중복 체크
                    return -2;
                }
                
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
                        pstmt.setString(2, userID);
                        pstmt.setString(3,  hardwareMap.get("CPU"));
                        pstmt.setString(4,  hardwareMap.get("Mainboard"));
                        pstmt.setString(5,  hardwareMap.get("GPU"));
                        pstmt.setString(6, hardwareMap.get("RAM"));
                        pstmt.setString(7, hardwareMap.get("Storage"));
			return pstmt.executeUpdate();  
		} catch (Exception e) {
			e.printStackTrace();
		}
                
                return -1; //DB 문제 발생시
	}
        
         public int checkDuplication(String userID, Map<String, String> hardwareMap) {
		String SQL = "SELECT comEstimateID FROM comEstimate WHERE userID = ? AND CPUInfo = ?, Mainboard = ? AND GPU = ? AND Ram = ? AND, Storage = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
                        pstmt.setString(1, userID);
                        pstmt.setString(2,  hardwareMap.get("CPU"));
                        pstmt.setString(3,  hardwareMap.get("Mainboard"));
                        pstmt.setString(4,  hardwareMap.get("GPU"));
                        pstmt.setString(5, hardwareMap.get("RAM"));
                        pstmt.setString(6, hardwareMap.get("Storage"));
			rs = pstmt.executeQuery(); 
                        
                        if(rs.next()) {
				return -1;
			}
			return 1;
                        
		} catch (Exception e) {
			e.printStackTrace();
		}
                
                return -1; //DB 문제 발생시
	}
}
