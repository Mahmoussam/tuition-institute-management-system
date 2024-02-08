/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcms;
import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author el safer
 */
public class studentHelper {
    private static studentHelper instance=new studentHelper();
    private studentHelper(){try {
        Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(studentHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    public static studentHelper getInstance(){
        return instance;
    }
    private Connection connection = null;
    private PreparedStatement ps = null;
    private final String DB_URL="jdbc:mysql://localhost:3306/tcms?useUnicode=yes&characterEncoding=UTF-8",user="root",pwd="pwdpwd";
    private ResultSet rs=null;
    private final String check_id="SELECT * FROM STUDENTS WHERE ID = ? ;",add_student="INSERT INTO STUDENTS (ID,NAME,PARENTNO,EL,HISTORY) VALUES(?,?,?,?,'');";
    public void register(String id,String name,String parentNum,String EL){
        try {
            
            connection = DriverManager.getConnection(DB_URL, user, pwd);
            ps = connection.prepareStatement(check_id);
            ps.setString(1, id);
            rs=ps.executeQuery();
            if(!rs.next()){
                ps=connection.prepareStatement(add_student);
                ps.setString(1, id);
                ps.setString(2, name);
                ps.setString(3, parentNum);
                ps.setString(4, EL);
                int x=ps.executeUpdate();
                if(x==0){
                    //Couldn't add Student
                    show_error("Couldn't add Student");
                }
                else {
                    show_message("Student is added");
                    //Create CSV file for that student
                    File file=new File("students/"+id+".csv");
                    if(!file.exists())file.mkdirs();
                }
            }
            else{
                show_error("Student ID already Exists!");
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

    private void show_error(String error) {
        JOptionPane.showMessageDialog(new JFrame(),error,"Error",JOptionPane.WARNING_MESSAGE);
    }

    private void show_message(String msg) {
        JOptionPane.showMessageDialog(new JFrame(),msg,"Info",JOptionPane.PLAIN_MESSAGE);
    }
}
