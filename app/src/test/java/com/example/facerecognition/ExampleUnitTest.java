package com.example.facerecognition;

import android.util.Log;

import com.mysql.cj.api.x.View;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;
//--------
import java.sql.PreparedStatement;
import java.util.Scanner;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void demo1() {
        System.out.print("");
        PreparedStatement ps = null;
        try {
            //注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://114.55.64.47:3306/face";
            Connection conn = DriverManager.getConnection(url, "root", "root");

            String sql = "select * from user";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString(1) + "-----" + rs.getString(2));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
    }
}
//--------------------------------------------------------




//





