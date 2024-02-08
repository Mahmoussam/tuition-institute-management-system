/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcms;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author el safer
 */
public class teacherHelper {
    private static teacherHelper instance=new teacherHelper();
    private teacherHelper(){try {
        Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(teacherHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    public static teacherHelper getInstance(){
        return instance;
    }
    private Connection connection = null;
    private PreparedStatement ps = null;
    private final String DB_URL="jdbc:mysql://localhost:3306/tcms?useUnicode=yes&characterEncoding=UTF-8",user="root",pwd="pwdpwd";
    private ResultSet rs=null;
    private final String check_name="SELECT * FROM TEACHERS WHERE NAME = ? ;",add_teacher="INSERT INTO TEACHERS (NAME,EL,PHONE) VALUES(?,?,?);";
    
    public void register(String name,String EL,String phone){
        try {
            
            connection = DriverManager.getConnection(DB_URL, user, pwd);
            ps = connection.prepareStatement(check_name);
            ps.setString(1, name);
            rs=ps.executeQuery();
            if(!rs.next()){
                ps=connection.prepareStatement(add_teacher);
                ps.setString(1, name);
                ps.setString(2, EL);
                ps.setString(3, phone);
                int x=ps.executeUpdate();
                if(x==0){
                    //Couldn't add Student
                    show_error("Couldn't add Teacher");
                }
                else {
                    show_message("Teacher is added");
                    //Create CSV file for that student
                    File file=new File("teachers/"+name+".csv");
                    if(!file.exists())file.mkdirs();
                }
            }
            else{
                show_error("Teacher Name already Exists!");
            }
            
        } catch (Exception ex) {
            show_error("Error: "+ex.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
       
    }

    public static void show_error(String error) {
        JOptionPane.showMessageDialog(new JFrame(),error,"Error",JOptionPane.WARNING_MESSAGE);
    }

    public static void show_message(String msg) {
        JOptionPane.showMessageDialog(new JFrame(),msg,"Info",JOptionPane.PLAIN_MESSAGE);
    }
}
