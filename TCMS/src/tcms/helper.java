/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcms;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.python.util.PythonInterpreter;
import org.python.core.*;
/**
 *
 * @author el safer
 */
public class helper {
    
    //private static helper instance=new helper();
    private helper(){try {
        Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            show_error(ex.toString());
        }
}
    public static helper getInstance(){
        return new helper();
    }
    
    public static char csvDelimiter=settings.csvDelimiter;
    private Connection connection = null;
    private PreparedStatement ps = null;
    private final String DB_URL="jdbc:mysql://localhost:3306/tcms?useUnicode=yes&characterEncoding=UTF-8",user="root",pwd="pwdpwd";
    private ResultSet rs=null;
    private final String TeacherName="SELECT * FROM TEACHERS";
    private final String checkSession_once="SELECT";
    
    public Object[] sessionData(String ID){
        Object data[]=new Object[3];
        try{connection=DriverManager.getConnection(DB_URL,user,pwd);
        ps=connection.prepareStatement("SELECT NAME,PRICE,STUDENTS FROM SESSIONS WHERE ID=?");
        ps.setString(1, ID);
            System.out.println(ps.toString());
        rs=ps.executeQuery();
        while(rs.next()){
            data[0]=rs.getString(1);//name
            data[2]=rs.getInt(2);//price
            data[1]=rs.getInt(3);//students
        }
        }catch(SQLException se){
            System.out.println("Error!:"+se.toString());
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return data;
        }
    }/**
     * Check if student already reserved!
     */
    public boolean unNamedFunction_1(String sessionID,String ID){
        boolean flag=false;
        try{connection=DriverManager.getConnection(DB_URL,user,pwd);
        ps=connection.prepareStatement("SELECT * FROM m"+sessionID+" where ID=?");
            ps.setString(1, ID);
            System.out.println(ps.toString());
        rs=ps.executeQuery();
        while(rs.next()){
            flag=true;
        }
        }catch(SQLException se){
            System.out.println("Error!:"+se.toString());flag=false;
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return flag;
        }
    }
    public double[] calculateSessionReport(int x,int y,String equation)throws Exception{
        double data[]=new double[2];
        PythonInterpreter pi=new PythonInterpreter();
        equation=equation.replace("%","/float(100)");
        pi.set("x", new PyFloat(x));
        pi.set("y", new PyFloat(y));
        String script="z=float("+equation+')';
        pi.exec(script);
        data[0]=pi.get("z").asDouble();//Centre income
        data[1]=(x*y)-data[0];//teacher income
        return data;
    }
    /**
     * Example input: m314412
    */
    public void deleteTable(String tableName){
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("DROP TABLE IF EXISTS "+tableName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            show_error("Couldn't delete Table:"+ex.toString());
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            
        }
    }
   public void addTeacherIncome(double income,String teacher)throws Exception{
       try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("UPDATE TEACHERS SET EARN_M=EARN_M+?,EARN_Y=EARN_Y+? WHERE NAME=?");
            ps.setDouble(1, income);
            ps.setDouble(2, income);
            ps.setString(3, teacher);
            ps.executeUpdate();
        } catch (SQLException ex) {
            show_error(":لم يتم اضافة دخل المدرس-للمركز "+':'+ex.toString());throw new Exception();
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            
        }
   }
    /**
     *data[0]-->name
     * data[1]-->students
     *data[2]-->price
     * data[3]-->equation
     */
    public Object[] sessionData_2(String ID){
        Object data[]=new Object[5];
        try{connection=DriverManager.getConnection(DB_URL,user,pwd);
        ps=connection.prepareStatement("SELECT NAME,PRICE,STUDENTS,EQ,TEACHER FROM SESSIONS WHERE ID=?");
        ps.setString(1, ID);
            System.out.println(ps.toString());
        rs=ps.executeQuery();
        while(rs.next()){
            data[0]=rs.getString(1);//name
            data[2]=rs.getInt(2);//price
            data[1]=rs.getInt(3);//students
            data[3]=rs.getString(4);//equation
            data[4]=rs.getString(5);
        }
        }catch(SQLException se){
            System.out.println("Error!:"+se.toString());
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return data;
        }
    }
    
    public void sessionTableToCSV(String path,String table) throws Exception{
        //prepare file writer
        System.out.println("Writing!");
        OutputStreamWriter writer;
        FileOutputStream fos;
        fos = new FileOutputStream(path);
        byte[] enc = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF }; fos.write(enc);
        writer = new OutputStreamWriter(fos, "UTF-8");
        //open Database Connection
        try{
        connection=DriverManager.getConnection(DB_URL,user,pwd);
        ps=connection.prepareStatement("SELECT * FROM "+table);
        rs=ps.executeQuery();
        while(rs.next()){
            String ln="";
            ln=rs.getString(1);
            ln+=csvDelimiter+rs.getString(2);
            ln+=csvDelimiter+rs.getString(3)+"\n";
            writer.write(ln);
        }
        }
        catch(SQLException ex){ex.printStackTrace();show_error("Couldn't generate the File! :"+ex.toString());}
        finally{  try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }}
        System.out.println("HIAS Done");
        //writer.write(ln_2) ;
        writer.close();
    }
    
    public void addStudent(String sessionID,String ID){
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("SELECT (STUDENTS) FROM SESSIONS WHERE ID=? AND STATE='N' AND ((START<=NOW() AND END >= NOW())OR (TIMEDIFF(START,NOW())>0 AND TIMEDIFF(START,NOW())<'00:30:00'))");
            ps.setString(1, sessionID);
            rs=ps.executeQuery();
            if(rs.next()){
                int students=rs.getInt(1);
                
                ps=connection.prepareStatement("CREATE TABLE IF NOT EXISTS m"+sessionID+" ( `ID` TEXT NOT NULL ,`NAME` TEXT NOT NULL, `TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,) ENGINE = InnoDB;".replace("?",sessionID));
                //ps.setString(1, sessionID);
                System.out.println(ps.toString());
                ps.executeUpdate();
                try{
                String stmt="INSERT INTO m"+sessionID+" (ID,NAME) VALUES(?,(SELECT NAME FROM STUDENTS WHERE ID=?));";
                ps=connection.prepareStatement(stmt);
                //ps.setString(1, sessionID);
                ps.setString(1, ID);
                ps.setString(2, ID);
                System.out.println(ps.toString());
                ps.executeUpdate();
                show_message("Student is added!");
                
                ps=connection.prepareStatement("UPDATE SESSIONS SET `STUDENTS`=`STUDENTS` +1 WHERE ID=? ;");
                //ps.setInt(1, students+1);
                ps.setString(1, sessionID);
                ps.executeUpdate();
            }catch(SQLException sqE){show_error("Error occured while adding the student : maybe the student is not registered :"+sqE.toString());}}
            else show_error("Couldn't Find session !!");
        } catch (SQLException e) {show_error("Couldn't Add studnet!");System.out.println(e.toString());
        }finally{  try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }}
        
    }
    /**
     *
     * 
     */
    public DefaultTableModel sessionTableModel(String sessionID){
        String headers[]={"Name","Time"};
        DefaultTableModel tabelModel=new DefaultTableModel(headers,0){

    @Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
};
        
        Object arr[]=new Object[2];
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("SELECT NAME,TIME FROM "+'m'+sessionID);
            System.out.println("SessionTableModel"+ps.toString());
            rs=ps.executeQuery();
            while(rs.next()){
            arr[0]=rs.getString(1);
                System.out.println("-"+arr[0]);
                
            arr[1]=rs.getString(2);
            System.out.println("-"+arr[1]);
            tabelModel.addRow(arr);
            }
            
        } catch (Exception e) {
            tabelModel=null;
            show_error(e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return tabelModel;
        }
    }
    public DefaultTableModel allStudentsTable(String query){
        String headers[]={"ID","Name","ParentNo","EL","History"};
        DefaultTableModel tabelModel=new DefaultTableModel(headers,0){

    @Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
};
        
        Object arr[]=new Object[5];
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement(query);
            System.out.println("SessionTableModel"+ps.toString());
            rs=ps.executeQuery();
            while(rs.next()){
            arr[0]=rs.getString(1);
            arr[1]=rs.getString(2);
            arr[2]=rs.getString(3);
            arr[3]=rs.getString(4);
            arr[4]=rs.getString(5);
            tabelModel.addRow(arr);
            }
            
        } catch (SQLException e) {
            tabelModel=null;
            show_error(e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return tabelModel;
        }
    }
    public DefaultTableModel allStudentsTable(String query,String name){
        String headers[]={"ID","Name","ParentNo","EL","History"};
        DefaultTableModel tabelModel=new DefaultTableModel(headers,0){

    @Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
};
        
        Object arr[]=new Object[5];
        try {
            name='%'+name+'%';
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement(query);
            ps.setString(1, name);
            System.out.println("SessionTableModel"+ps.toString());
            rs=ps.executeQuery();
            while(rs.next()){
            arr[0]=rs.getString(1);
            arr[1]=rs.getString(2);
            arr[2]=rs.getString(3);
            arr[3]=rs.getString(4);
            arr[4]=rs.getString(5);
            tabelModel.addRow(arr);
            }
            
        } catch (SQLException e) {
            tabelModel=null;
            show_error(e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return tabelModel;
        }
    }
    public DefaultTableModel allTeachersTable_m(String query,String name){
        String headers[]={"Name","EL","Phone","EARN_M","EARN_Y"};
        DefaultTableModel tabelModel=new DefaultTableModel(headers,0){

    @Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
};
        
        Object arr[]=new Object[5];
        try {
            name='%'+name+'%';
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement(query);
            ps.setString(1, name);
            System.out.println("SessionTableModel"+ps.toString());
            rs=ps.executeQuery();
            while(rs.next()){
            arr[0]=rs.getString(1);
            arr[1]=rs.getString(2);
            arr[2]=rs.getString(3);
            arr[3]=rs.getDouble(4);
            arr[4]=rs.getDouble(5);
            
            tabelModel.addRow(arr);
            }
            
        } catch (SQLException e) {
            tabelModel=null;
            show_error(e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return tabelModel;
        }
    }
    public DefaultTableModel allTeachersTable_m(String query){
        String headers[]={"Name","EL","Phone","EARN_M","EARN_Y"};
        DefaultTableModel tabelModel=new DefaultTableModel(headers,0){

    @Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
};
        
        Object arr[]=new Object[5];
        try {
            //name='%'+name+'%';
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement(query);
            //ps.setString(1, name);
            System.out.println("SessionTableModel"+ps.toString());
            rs=ps.executeQuery();
            while(rs.next()){
            arr[0]=rs.getString(1);
            arr[1]=rs.getString(2);
            arr[2]=rs.getString(3);
            arr[3]=rs.getDouble(4);
            arr[4]=rs.getDouble(5);
            tabelModel.addRow(arr);
            }
            
        } catch (SQLException e) {
            tabelModel=null;
            show_error(e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return tabelModel;
        }
    }
    
    public String selectDir() throws Exception{
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        f.showSaveDialog(null);

       // System.out.println(f.getCurrentDirectory());
        System.out.println(f.getSelectedFile());
        return f.getSelectedFile().getAbsolutePath();
    }
    public boolean stuExists(String ID){
     return false;
    }
    public void markAttend(String sessionID) throws Exception{
        boolean fl=false;
        SQLException sqlex2=null;
        try{
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("SELECT NAME,PRICE,START FROM SESSIONS WHERE ID=? ;");
            ps.setString(1, sessionID);
            rs=ps.executeQuery();
            String signature="";
            while(rs.next()){
                signature+=rs.getString(1)+"/"+Integer.toString(rs.getInt(2))+"/"+rs.getString(3)+"|";
            }
            ps=connection.prepareStatement("SELECT ID FROM m"+sessionID);
            rs=ps.executeQuery();
            String IDs="(";
            while(rs.next()){
                IDs+="'"+rs.getString(1)+"',";
            }
            IDs+="'');";
            ps=connection.prepareStatement("UPDATE STUDENTS SET HISTORY=CONCAT(HISTORY,?) WHERE ID IN "+IDs);
            ps.setString(1, signature);
            ps.executeUpdate();
            
        }catch(SQLException sqlex){show_error(sqlex.toString());flag=true;sqlex2=sqlex;}
        finally{try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        if(fl)throw sqlex2;}
    }
    public void cancelReservation(String sessionID,String ID){
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            DatabaseMetaData dbm =connection.getMetaData();
            rs=dbm.getTables(null, null, "m"+sessionID, null);
            if(rs.next()){
                //Table EXists
                try {
                    ps=connection.prepareStatement("DELETE FROM m"+sessionID+" WHERE ID=?;");
                    ps.setString(1, ID);
                    int x=ps.executeUpdate();
                    if(x<1)show_error("Student didn't reserve!");
                    
                    else {
                        //ps=connection.prepareStatement("SELECT STUDENTS FROM SESSIONS WHERE ID= ? ;");
                        //ps.setString(1, sessionID);
                        //rs=ps.executeQuery();
                        //int students=0;
                        //while(rs.next())students=rs.getInt(1);
                        ps=connection.prepareStatement("UPDATE SESSIONS SET STUDENTS=STUDENTS-1 WHERE ID= ? ;");
                        //ps.setInt(1, students-1);
                        ps.setString(1, sessionID);
                        ps.executeUpdate();
                        show_message("Done!");}
                } catch (Exception e) {
                    System.out.println("e"+e.toString());
                    show_error("OOPS! error occured:"+e.toString());
                }
            }
            else{show_error("Session's Students' table Doesn't Exist!");}
        } catch (SQLException e) {show_error("Couldn't Cancel reservation!");System.out.println(e.toString());
        }   finally{try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }}
    }
    /*
     *returns -1 when error occure or the result of executeUpdate();
     */
    public int execUpdate(String statement){
        int x=-1;
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement(statement);
            System.out.println(ps.toString());
            rs=ps.executeQuery();
            if(rs.next())x=1;
            else x=0;
        } catch (SQLException ex) {
            Logger.getLogger(helper.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return x;
        }
        
    }
    public void teachersName(JComboBox CB){
        try {
            
            connection = DriverManager.getConnection(DB_URL, user, pwd);
            ps=connection.prepareStatement(TeacherName);
            rs=ps.executeQuery();
            while(rs.next()){
                CB.addItem(rs.getString("NAME"));
            }
        } catch (Exception e) {
            show_error("Error: "+e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }
    private final String ALPHA_NUMERIC_STRING="1234567890";
    public String generateID(int len){
        StringBuilder builder = new StringBuilder();
        while (len-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
    private final String createSession="INSERT INTO SESSIONS (ID,NAME,TEACHER,EL,PRICE,START,END,CAPACITY,CLASS,EQ) VALUES(?,?,?,?,?,ADDDATE(?,INTERVAL ? DAY),ADDDATE(?,INTERVAL ? DAY),?,?,?);";
    public void createSession(String name,String teacher,String EL,int price,String start,String end,int cap,int CLASS,String EQ,int rep_times){
        try {
            connection = DriverManager.getConnection(DB_URL, user, pwd);
            for(int i=0;i<rep_times;i++){
                String ID=generateID(6);
                ps=connection.prepareStatement(createSession);
                ps.setString(1,ID);
                ps.setString(2, name);
                ps.setString(3, teacher);
                ps.setString(4, EL);
                ps.setInt(5, price);
                ps.setString(6, start);
                ps.setInt(7,(i*7));
                ps.setString(8, end);
                ps.setInt(9,(i*7));
                ps.setInt(10, cap);
                ps.setInt(11, CLASS);
                ps.setString(12, EQ);
                System.out.println("create "+ps.toString());
                int x=ps.executeUpdate();
                System.out.println("x "+x);
                show_message("Session Created With success");
            }
        } catch (SQLException e) {
            show_error("Error: "+e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }
    public String read_DatePicker(JXDatePicker datePicker){
        try{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        datePicker.setFormats(dateFormat);
        DateFormat sys=new SimpleDateFormat("yyyy-MM-dd ");
        return sys.format(datePicker.getDate()).toString();}
        catch(Exception e){System.out.println("Exception came from here");return "";}
        
    }
    public void deleteSession(String id){
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("DELETE FROM SESSIONS WHERE ID=? ;");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            show_error("Couldn't delete Session, please refresh The table and try again");
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            
        }
        
    }
    public void deleteTeacher(String name){
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("DELETE FROM TEACHERS WHERE NAME=? ;");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            show_error("Couldn't delete Teacher, please refresh The table:"+ex.toString());
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            
        }}
    public DefaultTableModel refresh(String query,int state,String[] headers){
        DefaultTableModel tabelModel=new DefaultTableModel(headers,0){

    @Override
    public boolean isCellEditable(int row, int column) {
       return false;
    }
};
        int l=12;
        if(state==0)l=11;
        //else l=12;
        Object arr[]=new Object[l];
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement(query);
            System.out.println("refresh "+ps.toString());
            rs=ps.executeQuery();
            while(rs.next()){
            arr[0]=rs.getString(1);
            arr[1]=rs.getString(2);
            arr[2]=rs.getString(3);
            arr[3]=rs.getString(4);
            arr[4]=rs.getString(5);//
            arr[5]=rs.getString(6);
            arr[6]=rs.getInt(8);
            if (l==12)arr[11]=rs.getString(7).length();//
            arr[7]=rs.getInt(9);
            String[] mem1=rs.getString(10).split(";");
            arr[8]=mem1[0]+"/"+mem1[2];
            arr[9]=rs.getString(11);
            tabelModel.addRow(arr);
            }
            
        } catch (SQLException e) {
            tabelModel=null;
            show_error(e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return tabelModel;
        }
    }
    public int markPending(){
        int x=-1;
        try {
            connection=DriverManager.getConnection(DB_URL,user,pwd);
            ps=connection.prepareStatement("UPDATE SESSIONS SET STATE='Y' WHERE END <=NOW()");
           
            x=ps.executeUpdate();
        } catch (SQLException ex) {
            show_error("Couldn't mark Sessions as pending please make sure the Database is working!");
        }finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return x;
        }
    }
    public String make_end(String start,String Duration){
        System.out.println("Dur "+Duration);
        String result=null;
        try {
            
            connection = DriverManager.getConnection(DB_URL, user, pwd);
            ps=connection.prepareStatement("SELECT ADDTIME(?,?)");
            ps.setString(1, start);
            ps.setString(2, Duration);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println("ok here");
                result=rs.getString(1);
                
            }
            System.out.println("res "+result);
        } catch (Exception e) {
            show_error("Error: "+e.toString());
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            return result;
        }
    }
    private boolean flag=false;
    private String check_sessions="SELECT * \n" +
"FROM SESSIONS \n" +
"WHERE CLASS= ? AND (((START<=? AND ? <END)\n" +
"OR (START<? AND ?<=END)\n" +
"OR (?<=START AND START<? )\n" +
"OR (?<END AND END<=?))";
    private String check_session2=" OR ((START<=DATE_ADD(?,INTERVAL Z DAY) AND DATE_ADD(?,INTERVAL Z DAY) <END)\n" +
"OR (START<DATE_ADD(?,INTERVAL Z DAY) AND DATE_ADD(?,INTERVAL Z DAY)<=END)\n" +
"OR (DATE_ADD(?,INTERVAL Z DAY)<=START AND START<DATE_ADD(?,INTERVAL Z DAY) )\n" +
"OR (DATE_ADD(?,INTERVAL Z DAY)<END AND END<=DATE_ADD(?,INTERVAL Z DAY)))";
    private String addDays="DATE_ADD( )";
    public boolean CheckSession(String start,String end,int ClASS,int rep_times){
        try {
            System.out.println("start "+start +" end "+end);
            //prepare Statement
            String statement=check_sessions;
            if(rep_times>1){System.out.println("SIGN");
                for(int i=1;i<rep_times;i++){
                    //System.out.println("Char: "+(char)('0'+(i*7)));
                    //System.out.println("i "+i);
                    statement+=check_session2.replace("Z", Integer.toString(i*7));
                }
            }
            
            statement=statement.concat(");");
            System.out.println(statement);
            
            connection = DriverManager.getConnection(DB_URL, user, pwd);
            
            ps=connection.prepareStatement(statement);
            ps.setInt(1, ClASS);
            for(int i=0;i<rep_times;i++){
                ps.setString((i*8)+2, start);
                ps.setString((i*8)+3, start);
                ps.setString((i*8)+4, end);
                ps.setString((i*8)+5, end);
                ps.setString((i*8)+6, start);
                ps.setString((i*8)+7, end);
                ps.setString((i*8)+8, start);
                ps.setString((i*8)+9, end);
            }
            System.out.println("Check "+ ps.toString());
            rs=ps.executeQuery();
            
            while(rs.next()){
                flag=true;
                show_error("Found session by :"+rs.getString("START"));
            }
            
            
        } catch (Exception e) {
            show_error("Error: "+e.toString());
            e.printStackTrace();
            flag=true;
        }
        finally{
            try { rs.close(); } catch (Exception e) { /* ignored */ }
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
            if(!flag){return true;}
            flag=false;
            return false;
        }
        
    }
    public void show_error(String error) {
        JOptionPane.showMessageDialog(new JFrame(),error,"Error",JOptionPane.WARNING_MESSAGE);
    }

    public void show_message(String msg) {
        JOptionPane.showMessageDialog(new JFrame(),msg,"Info",JOptionPane.PLAIN_MESSAGE);
    }

    
    
}
