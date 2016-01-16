package model;

/**
 *
 * @author UlTMATE
 */
import java.sql.*;
import javax.swing.*;
import java.util.*;

public class Database {
    
    ResultSet rst;
    static Connection conn;
    
    public Database(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/?", "root", "root");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS lookout");
            stmt.close();
            conn.close();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users(name varchar(50), username VARCHAR(50), password VARCHAR(50), PRIMARY KEY(username))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS messages(msg_id INT AUTO_INCREMENT, mail_addresses VARCHAR(200), sent_date DATE, subject VARCHAR(200), content VARCHAR(10000), tag VARCHAR(10), username varchar(50), PRIMARY KEY (msg_id), FOREIGN KEY(username) REFERENCES users(username))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS trash(msg_id INT, tag VARCHAR(10), deleted_at TIMESTAMP, PRIMARY KEY(msg_id))");
            stmt.close();
            conn.close();
        } catch(ClassNotFoundException | SQLException exc1){
            exc1.printStackTrace();
        }
    }
    
    public ResultSet getData(String query){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rst = stmt.executeQuery(query);
        } catch(SQLException sqlExc){
            JOptionPane.showMessageDialog(null, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            sqlExc.printStackTrace();
        }
        return rst;
    }

    public static void close(){
        try{
            conn.close();
        } catch(SQLException sqlExc){
            sqlExc.printStackTrace();
        }
    }
    
    public static java.sql.Date getLastMessageDate(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            Statement stmt = conn.createStatement();
            String query = "SELECT MAX(sent_date) FROM messages WHERE tag='Inbox' or tag='Trash'";
//            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            java.sql.Date date = rs.getDate(1);
            return date;
        } catch(SQLException sqlExc){
            JOptionPane.showMessageDialog(null, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            sqlExc.printStackTrace();
        }
        return null;
    }
    
    public static int getCount(String tag){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            Statement stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM messages WHERE tag='" +tag+ "'";
//            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int count = rs.getInt(1);
            return count;
        } catch(SQLException sqlExc){
            JOptionPane.showMessageDialog(null, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            sqlExc.printStackTrace();
        }
        return 0;
    }
    
    public static void setData(String query){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            conn.close();
        } catch(SQLException sqlExc){
            JOptionPane.showMessageDialog(null, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            sqlExc.printStackTrace();
        }
    }
    
    public static boolean isValidUser(String username, String password){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username='" +username+ "' and password='" +password+ "'");
            if(rs.next()){
                stmt.close();
                conn.close();
                return true;
            }
        } catch(SQLException sqlExc){
            JOptionPane.showMessageDialog(null, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            sqlExc.printStackTrace();
        }
        return false;
    }
    
    public static void createAccount(String name, String username, String password){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO users VALUES('" +name+ "','" +username+ "', '" +password+ "')";
            stmt.executeUpdate(query);
            stmt.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Account Created. Please Login To Continue", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        } catch(SQLException sqlExc){
            JOptionPane.showMessageDialog(null, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            sqlExc.printStackTrace();
        }
    }
    
    public static void deleteMessages(ArrayList list, String tag){
        tag = tag.trim();
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            PreparedStatement pstmt=null;
            PreparedStatement pstmt2=null;
            if(tag.equals("Trash")){
                pstmt = conn.prepareStatement("DELETE FROM messages WHERE msg_id=?");
                pstmt2 = conn.prepareStatement("DELETE FROM trash WHERE msg_id=?");
            } else {
                pstmt = conn.prepareStatement("UPDATE messages SET tag='Trash' WHERE msg_id=?");
                pstmt2 = conn.prepareStatement("INSERT INTO trash values(?,?,?)");
            }
            ListIterator liter = list.listIterator();
            while(liter.hasNext()){
                int id = (int)liter.next();
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                if(tag.equals("Trash")){
                    pstmt2.setInt(1, id);
                    pstmt2.executeUpdate();
                } else {
                    pstmt2.setInt(1, id);
                    pstmt2.setString(2, tag);
                    pstmt2.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                    pstmt2.executeUpdate();
                }
            }
            conn.close();
        } catch (SQLException sqlExc){
            sqlExc.printStackTrace();
        }
    }
    
    public static void restoreMessages(ArrayList list) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lookout", "root", "root");
            ResultSet rst=null;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT tag FROM trash WHERE msg_id=?");
                    PreparedStatement pstmt2 = conn.prepareStatement("UPDATE messages SET tag=? WHERE msg_id=?");
                    PreparedStatement pstmt3 = conn.prepareStatement("DELETE FROM trash WHERE msg_id=?");) {
                ListIterator liter = list.listIterator();
                while (liter.hasNext()) {
                    int id = (int) liter.next();
                    pstmt.setInt(1, id);
                    rst = pstmt.executeQuery();
                    rst.next();
                    String tag = rst.getString(1);
                    pstmt2.setString(1, tag);
                    pstmt2.setInt(2, id);
                    pstmt2.executeUpdate();
                    pstmt3.setInt(1, id);
                    pstmt3.executeUpdate();
                    rst.close();
                }
            }
            conn.close();
        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
        }
    }
}
