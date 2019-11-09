package com.example.facerecognition.utils;

import com.example.facerecognition.bean.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCUtils {
    //连接数据库
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://114.55.64.47:3306/face","root","root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    //释放资源
    public static void release(Connection conn, PreparedStatement pstmt, ResultSet rs){

        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if(pstmt != null){
            try {
                pstmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean getUserByUser(User user){
        Connection conn = getConnection();
        PreparedStatement pstmt  = null;
        ResultSet rs = null;
        try {
            String sql = "select * from user where name = ? and password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            rs = pstmt.executeQuery();
           if (rs.next()){
               return  true;
           }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            release(conn,pstmt,rs);
        }
        return false;
    }
    public static ArrayList<User> getAllUsers(){
        Connection conn = getConnection();
        PreparedStatement pstmt  = null;
        ResultSet rs = null;
        User user = null;
        ArrayList<User> userList = new ArrayList<>();
        try {
            String sql = "select * from user";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                userList.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            release(conn,pstmt,rs);
        }
        return userList;
    }
    public static boolean insertUser(User user){
        Connection con = getConnection();
        PreparedStatement pstmt  = null;
        String sql = "insert into user values (?,?,?)";
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, 0);
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
            pstmt.close();
        }catch (SQLException e){
            return false;
        }
        return true;
    }
}
