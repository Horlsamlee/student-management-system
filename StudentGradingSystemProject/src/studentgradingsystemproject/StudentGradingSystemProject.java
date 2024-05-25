package studentgradingsystemproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class StudentGradingSystemProject {
    public static List students;
    public static List courses;
    public static List departments;
    public static List  grades;
    public static List  attendance;
     public static Connection connection; 
    
    public static SimpleDateFormat fmt;
    
    public static void main(String[] args) {
        students = new ArrayList();
        courses = new ArrayList();
        departments = new ArrayList();
        grades = new ArrayList();
        attendance = new ArrayList();
        fmt = new SimpleDateFormat("dd/MM/yyyy");    
        
                try {
            connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/studentregsys","root", "");
        } catch (SQLException ex) {
          Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //test_students();
        create_students();
        create_course();
        create_department();
        create_grade();
        create_attendance();
        new Menu().setVisible(true);
        
        System.out.printf("\n");
    }
    
      public static Boolean check_student(int std_id){
           ResultSet res;
           String strSQL;
           Boolean ret=false;
      try {
            strSQL="select count(*) from students where students.std_id = ?";
            
            PreparedStatement prepStatement=connection.prepareStatement(strSQL); 
            prepStatement.setInt(1, std_id); 
            res = prepStatement.executeQuery();
            
            //Initiall res points to null, we need to advance it to the first record
            res.next();
            
            // if the first field value is > 0 then there is a record which
            // student id is equal to std_id
            if (res.getInt(1)>0) ret=true;
            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    return ret; // returns true if found; returns false if not found
    }
    
      public static void add_student(int std_id, String std_no, String std_name, 
                       String std_surname, char std_gender, 
                       String std_nationality, GregorianCalendar std_birthday) {
        try {
            
            String strSQL = "insert into students "+
                    "(std_id, std_no, std_name, std_surname, std_gender, "+
                    "std_nationality, std_birthdate ) values "+
                    "(?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement prepStatement=            
                    connection.prepareStatement(strSQL); 
                    
                    prepStatement.setInt(1, std_id); 
                    prepStatement.setString(2, std_no); 
                    prepStatement.setString(3, std_name); 
                    prepStatement.setString(4, std_surname); 
                    prepStatement.setObject(5, std_gender, java.sql.Types.CHAR);
                    prepStatement.setString(6, std_nationality);
                    prepStatement.setObject(7, std_birthday.getTime(), java.sql.Types.DATE);
                    
                    prepStatement.executeUpdate();
                    
             } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*        Student st =new Student(std_id, std_no, std_name, std_surname,
            std_gender, std_nationality, std_birthday);
            
            students.add(st);
        */ 
    }
    
    public static void edit_student(int std_id, String std_no, String std_name, 
                       String std_surname, char std_gender, 
                       String std_nationality, GregorianCalendar std_birthday) {
        
        try {
            String strSQL="update students set std_no=?, std_name=?, "+
                          "std_surname=?, std_gender=?, std_nationality=?, "+
                          "std_birthdate=? where std_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(7, std_id); 
            prepStatement.setString(1, std_no); 
            prepStatement.setString(2, std_name); 
            prepStatement.setString(3, std_surname); 
            prepStatement.setObject(4, std_gender, java.sql.Types.CHAR);
            prepStatement.setString(5, std_nationality);
            prepStatement.setObject(6, std_birthday.getTime(), java.sql.Types.DATE);
                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*
            Student st=null;
            Boolean found=false;
            Iterator <Student> itr = students.iterator();
            while (itr.hasNext()) {
            st = itr.next();
            if(std_id==st.getStd_id()) {
            found=true;
            break;
            }
            }
            if (found) {
            st.setStd_no(std_no);
            st.setStd_name(std_name);
            st.setStd_surname(std_surname);
            st.setStd_nationality(std_nationality);
            st.setStd_gender(std_gender);
            st.setStd_birthdate(std_birthday);
            }
        */      
    }
    
    
    public static void delete_student(int std_id) {
     try {
            String strSQL="delete from students where std_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(1, std_id); 
                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    /*    
      Student st=null;
      Boolean found=false;
      Iterator <Student> itr = students.iterator();
      while (itr.hasNext()) {
          st = itr.next(); 
          if(std_id==st.getStd_id()) {
              found=true;
              break;
          }
      }
      if (found) students.remove(st);
*/
      }

    public static void draw_line(int num) {
        String ln="";
        for (int i=0; i<num; i++) ln+="=";
        System.out.printf("\n"+ln);
    }
    
    public static void list_students() {
      Student st;
      //SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
      
      Iterator <Student> itr = students.iterator();
      System.out.printf("\n%10s %10s %15s %15s %10s %12s %12s",
              "Student Id", "Student No","Std. Name", "Std. Surname", 
              "Gender","Nationality", "Birthday");
        draw_line(90);
        
      while (itr.hasNext()) {
          st = itr.next(); 
          fmt.setCalendar(st.getStd_birthdate());
          System.out.printf("\n%10d %10s %15s %15s %10s %12s %12s",
              st.getStd_id(), st.getStd_no(), st.getStd_name(), 
              st.getStd_surname(), st.getStd_gender(), st.getStd_nationality(), 
              fmt.format(st.getStd_birthdate().getTime()));
      }
      draw_line(90);
        
    }
    
    
    public static void backup_student() throws IOException
    {
     File outfile  = new File("students.dat");
     FileOutputStream outfilestream = new FileOutputStream(outfile);
     ObjectOutputStream outObjectStream = new ObjectOutputStream(outfilestream);
     
     outObjectStream.writeObject(students);
     outObjectStream.close();
     
    }
    
    public static void retrieve_student() throws IOException, ClassNotFoundException
    {
     File infile  = new File("students.dat");
     FileInputStream infilestream = new FileInputStream(infile);
     ObjectInputStream inObjectStream = new ObjectInputStream(infilestream);
     students = (ArrayList)inObjectStream.readObject();
     
     inObjectStream.close();
     
    }
    
    public static GregorianCalendar strToGregorianCalendar(String stDate){
        GregorianCalendar bdate;
        
        bdate = new GregorianCalendar(
                Integer.parseInt(stDate.substring(6,10)),
                Integer.parseInt(stDate.substring(3,5))-1,
                Integer.parseInt(stDate.substring(0,2)));
        return bdate;       
    }
    
    public static void test_students() {
        
        try {
        System.out.printf("\n Tests for Class Student\n\n");
        System.out.printf("\n Add_student()\n\n");
        
        add_student(1,"189222", "Ayse","Cengiz",'F',"Turkey",
                    strToGregorianCalendar("28/03/2002"));
        add_student(2,"193342","Philip","Udoye",'M',"Nigeria",
                    strToGregorianCalendar("16/09/2003"));
        add_student(3,"189931","Kemal","Salih",'M',"TRNC",
                    strToGregorianCalendar("17/05/2002"));
        add_student(4,"188883","Fathima","Mohammad",'F',"Syria",
                    strToGregorianCalendar("22/11/2001"));                
        add_student(5,"189447","Jasmin","Faruq",'F',"Egypt",
                    strToGregorianCalendar("19/02/2002"));                

        System.out.printf("\n List_student()\n\n");
        list_students();
        
        System.out.printf("\n Edit_student()\n\n");
        edit_student(3,"189931","Kemal","Salih",'M',"Turkey",
                    strToGregorianCalendar("17/04/2002"));
        System.out.printf("\n List_student()\n\n");
        list_students();

        backup_student();
        
        System.out.printf("\n Delete_student()\n\n");
        delete_student(3);
        System.out.printf("\n List_student()\n\n");
        list_students();
        
        retrieve_student();
        System.out.printf("\n List_student()\n\n");
        list_students();
        }
        catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error");
                }
    }
    
    public static void create_students() {
        
                        
        try {
            Statement st = StudentGradingSystemProject.connection.createStatement();
            st.executeUpdate("truncate students");
        } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.printf("\n Add_student()\n\n");
        
        add_student(1,"189222", "Ayse","Cengiz",'F',"Turkey",
                    strToGregorianCalendar("28/03/2002"));
        add_student(2,"193342","Philip","Udoye",'M',"Nigeria",
                    strToGregorianCalendar("16/09/2003"));
        add_student(3,"189931","Kemal","Salih",'M',"TRNC",
                    strToGregorianCalendar("17/05/2002"));
        add_student(4,"188883","Fathima","Mohammad",'F',"Syria",
                    strToGregorianCalendar("22/11/2001"));                
        add_student(5,"189447","Jasmin","Faruq",'F',"Egypt",
                    strToGregorianCalendar("19/02/2002"));                

        System.out.printf("\n List_student()\n\n");
        list_students();
    }
    
    
    public static Boolean check_course(int crs_id){
           ResultSet res;
           String strSQL;
           Boolean ret=false;
      try {
            strSQL="select count(*) from courses where courses.crs_id = ?";
            
            PreparedStatement prepStatement=connection.prepareStatement(strSQL); 
            prepStatement.setInt(1, crs_id); 
            res = prepStatement.executeQuery();
            
            //Initiall res points to null, we need to advance it to the first record
            res.next();
            
            // if the first field value is > 0 then there is a record which
            // student id is equal to std_id
            if (res.getInt(1)>0) ret=true;
            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    return ret; // returns true if found; returns false if not found
    }
    
       public static void create_course(){
           
        try {
             Statement cs = StudentGradingSystemProject.connection.createStatement();
               cs.executeUpdate("truncate courses");
        } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        add_course(1,1,"ITEC314","Multi Platform Programming");
        add_course(2,1,"ITEC413","Information Systems Security");
        add_course(3,2,"ECON101","Introduction to Economics");
        add_course(4,2,"BUSS103","Fundamentals of Business Administration");
       }
        public static void retrieve_course() throws IOException, ClassNotFoundException
    {
     File infile  = new File("courses.dat");
     FileInputStream infilestream = new FileInputStream(infile);
     ObjectInputStream inObjectStream = new ObjectInputStream(infilestream);
     courses = (ArrayList)inObjectStream.readObject();
     
     inObjectStream.close();
     
    }
    public static void backup_Course() throws IOException
    {
     File outfile  = new File("courses.dat");
     FileOutputStream outfilestream = new FileOutputStream(outfile);
     ObjectOutputStream outObjectStream = new ObjectOutputStream(outfilestream);
     
     outObjectStream.writeObject(courses);
     outObjectStream.close();
     
    }
    
    public static void add_course(int crs_id, int dept_id, String crs_code, String crs_name) {
            try {
            
            String strSQL = "insert into courses "+
                    "(crs_id, dept_id, crs_code, crs_name) values "+
                    "(?, ?, ?, ?)";
            
            PreparedStatement prepStatement=            
                    connection.prepareStatement(strSQL); 
                    
                    prepStatement.setInt(1, crs_id); 
                    prepStatement.setInt(2, dept_id); 
                    prepStatement.setString(3, crs_code); 
                    prepStatement.setString(4, crs_name); 

                    
                    prepStatement.executeUpdate();
                    
             } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static void edit_course(int crs_id, int dept_id, String crs_code, String crs_name) {
       try {
            String strSQL="update courses set dept_id=?, crs_code=?, "+
                          "std_surname=? where crs_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(4, crs_id); 
            prepStatement.setInt(1, dept_id); 
            prepStatement.setString(2, crs_code); 
            prepStatement.setString(3, crs_code); 
           

                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
     public static void delete_course(int crs_id) {
     try {
            String strSQL="delete from courses where crs_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(1, crs_id); 
                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
     
        public static void list_course() {
      Course cs;
      Iterator <Course> itr = courses.iterator();
      System.out.printf("\n%8s %10s %12s %13s",
              "Course Id", "dept id","course code","course name");
        draw_line(79);
        
      while (itr.hasNext()) {
          cs = itr.next(); 
          System.out.printf("\n%3d %10d %16s %18s", 
              cs.getCrs_Id(), cs.getDept_id(), 
              cs.getCrs_code(), cs.getCrs_name());
      }
      draw_line(79);
        
    } 
        
     public static Boolean check_dept(int dept_id){
           ResultSet res;
           String strSQL;
           Boolean ret=false;
      try {
            strSQL="select count(*) from departments where departments.dept_id = ?";
            
            PreparedStatement prepStatement=connection.prepareStatement(strSQL); 
            prepStatement.setInt(1, dept_id); 
            res = prepStatement.executeQuery();
            
            //Initiall res points to null, we need to advance it to the first record
            res.next();
            
            // if the first field value is > 0 then there is a record which
            // student id is equal to std_id
            if (res.getInt(1)>0) ret=true;
            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    return ret; // returns true if found; returns false if not found
    }
        
    public static void create_department(){
        
         try {
               Statement ds = StudentGradingSystemProject.connection.createStatement();
               ds.executeUpdate("truncate departments");
        } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        add_department(1, "Information Technology");
        add_department(2, "Economics");
        add_department(3, "Business");
    } 
    public static void retrieve_department() throws IOException, ClassNotFoundException
    {
     File infile  = new File("departments.dat");
     FileInputStream infilestream = new FileInputStream(infile);
     ObjectInputStream inObjectStream = new ObjectInputStream(infilestream);
     departments = (ArrayList)inObjectStream.readObject();
     
     inObjectStream.close();
     
    }
    public static void backup_department() throws IOException
    {
     File outfile  = new File("departments.dat");
     FileOutputStream outfilestream = new FileOutputStream(outfile);
     ObjectOutputStream outObjectStream = new ObjectOutputStream(outfilestream);
     
     outObjectStream.writeObject(departments);
     outObjectStream.close();
     
    }
    
      public static void add_department(int dept_id, String dept_name) {
        try {
            
            String strSQL = "insert into departments "+
                    "(dept_id, dept_name) values "+
                    "(?, ?)";
            
            PreparedStatement prepStatement=            
                    connection.prepareStatement(strSQL); 
                    
                    prepStatement.setInt(1, dept_id); 
                    prepStatement.setString(2, dept_name); 


                    
                    prepStatement.executeUpdate();
                    
             } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void edit_department(int dept_id, String dept_name) {
 try {
            String strSQL="update departments set dept_name=?, "+
                          " where dept_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(2, dept_id); 
            prepStatement.setString(1, dept_name); 
            
           

                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
      }

public static void delete_department(int dept_id) {
    try {
            String strSQL="delete from departments where dept_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(1, dept_id); 
                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    }



 public static Boolean check_grade(int grd_id){
           ResultSet res;
           String strSQL;
           Boolean ret=false;
      try {
            strSQL="select count(*) from grades where grades.grd_id = ?";
            
            PreparedStatement prepStatement=connection.prepareStatement(strSQL); 
            prepStatement.setInt(1, grd_id); 
            res = prepStatement.executeQuery();
            
            //Initiall res points to null, we need to advance it to the first record
            res.next();
            
            // if the first field value is > 0 then there is a record which
            // student id is equal to std_id
            if (res.getInt(1)>0) ret=true;
            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    return ret; // returns true if found; returns false if not found
    }
 
 
 
     public static void  create_grade(){
         
       try {
               Statement gs = StudentGradingSystemProject.connection.createStatement();
               gs.executeUpdate("truncate grades");
             } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        add_grade(1,1,1, 90f, 88f, 96f, "A");
        add_grade(2, 2,1,80f, 85f, 82f, "B+");
        add_grade(3,3,2, 75f, 80f, 77f, "B");
        add_grade(4,4,2, 69f, 75f, 66f, "C");
        add_grade(5,5,1, 88f, 80f, 82f, "A-");
     }  
    public static void retrieve_grade() throws IOException, ClassNotFoundException
    {
     File infile  = new File("grades.dat");
     FileInputStream infilestream = new FileInputStream(infile);
     ObjectInputStream inObjectStream = new ObjectInputStream(infilestream);
     grades = (ArrayList)inObjectStream.readObject();
     
     inObjectStream.close();
     
    }
    public static void backup_grades() throws IOException
    {
     File outfile  = new File("grades.dat");
     FileOutputStream outfilestream = new FileOutputStream(outfile);
     ObjectOutputStream outObjectStream = new ObjectOutputStream(outfilestream);
     
     outObjectStream.writeObject(grades);
     outObjectStream.close();
     
    }
    
    public static void add_grade(int grd_id, int std_id, int crs_id, float grd_mt,
            float grd_hw, float grd_final, String grd_lgrade) {
                       try {
            
            String strSQL = "insert into grades "+
                    "(grd_id, std_id, crs_id, grd_mt, grd_hw, grd_final, grd_lgrade) values "+
                    "(?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement prepStatement=            
                    connection.prepareStatement(strSQL); 
                    
                    prepStatement.setInt(1, grd_id); 
                    prepStatement.setInt(2, std_id); 
                    prepStatement.setInt(3, crs_id); 
                    prepStatement.setFloat(4, grd_mt); 
                    prepStatement.setFloat(5, grd_hw);
                    prepStatement.setFloat(6, grd_final);
                    prepStatement.setString(7, grd_lgrade);

                    
                    prepStatement.executeUpdate();
                    
             } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 public static void edit_grade(int grd_id, int std_id, int crs_id, 
                               float grd_mt, float grd_hw, float grd_final, 
                               String grd_lgrade) {
        try {
            String strSQL="update grades set std_id=?, crs_id=?, "+
                          "grd_mt=?, grd_hw=?, grd_final=?, "+
                          "grd_lgrade? where grd_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(7, grd_id); 
            prepStatement.setInt(1, std_id); 
            prepStatement.setInt(2, crs_id); 
            prepStatement.setFloat(3, grd_mt); 
            prepStatement.setFloat(4, grd_hw);
            prepStatement.setFloat(5, grd_final);
            prepStatement.setString(6, grd_lgrade);
                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
      }

public static void delete_grade(int grd_id) {
    try {
            String strSQL="delete from grades where grd_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(1, grd_id); 
                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
      }

    
    public static void list_grades() {
      Grades gd;
      Iterator <Grades> itr = grades.iterator();
      System.out.printf("\n%2s %10s %15s %15s %10s %12s %12s",
              "Id", "Student No","Std. Name", "Std. Surname", 
              "Gender","Nationality", "Birthday");
        draw_line(79);
        
      while (itr.hasNext()) {
          gd = itr.next(); 
          System.out.printf("\n%2d %10s %15s %15s %10s %12s %12s", 
              gd.getGrd_id(), gd.getStd_id(), 
              gd.getCrs_id(), gd.getGrd_mt(),
              gd.getGrd_hw(), gd.getGrd_final(), gd.getGrd_lgrade());
      }
      draw_line(79);
        
    }
    
    
     public static Boolean check_attendance(int att_id){
           ResultSet res;
           String strSQL;
           Boolean ret=false;
      try {
            strSQL="select count(*) from attendance where attendance.att_id = ?";
            
            PreparedStatement prepStatement=connection.prepareStatement(strSQL); 
            prepStatement.setInt(1, att_id); 
            res = prepStatement.executeQuery();
            
            //Initiall res points to null, we need to advance it to the first record
            res.next();
            
            // if the first field value is > 0 then there is a record which
            // student id is equal to std_id
            if (res.getInt(1)>0) ret=true;
            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    return ret; // returns true if found; returns false if not found
    }
    
    public static void create_attendance(){
       try {
               Statement at = StudentGradingSystemProject.connection.createStatement();
               at.executeUpdate("truncate attendance");
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
    
        add_attendance(1,1,1, "09/04,2021");
        add_attendance(2,2,1, "09/04/2021");
        add_attendance(3,3,2, "09/04,2021");
        add_attendance(4,4,2, "09/04,2021");
        add_attendance(5,5,1, "09/04,2021");
        add_attendance(6,1,1, "12/04,2021");
        add_attendance(7,2,1, "12/04,2021");
        add_attendance(8,5,1, "12/04,2021");
    }
     public static void retrieve_attendance() throws IOException, ClassNotFoundException
    {
     File infile  = new File("attendance.dat");
     FileInputStream infilestream = new FileInputStream(infile);
     ObjectInputStream inObjectStream = new ObjectInputStream(infilestream);
     attendance = (ArrayList)inObjectStream.readObject();
     
     inObjectStream.close();
     
    }
    public static void backup_attendance() throws IOException
    {
     File outfile  = new File("attendance.dat");
     FileOutputStream outfilestream = new FileOutputStream(outfile);
     ObjectOutputStream outObjectStream = new ObjectOutputStream(outfilestream);
     
     outObjectStream.writeObject(attendance);
     outObjectStream.close();
     
    }
    
    public static void add_attendance(int att_id, int std_id, int crs_id, String att_date) {
            try {
            
            String strSQL = "insert into attendance "+
                    "(att_id, std_id, crs_id, "+
                    "att_date) values "+
                    "(?, ?, ?, ?)";
            
            PreparedStatement prepStatement=            
                    connection.prepareStatement(strSQL); 
                    
                    prepStatement.setInt(1, att_id); 
                    prepStatement.setInt(2, std_id); 
                    prepStatement.setInt(3, crs_id); 
                    prepStatement.setString(4, att_date); 
                   
                    
                    prepStatement.executeUpdate();
                    
             } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void edit_attendance(int att_id,int std_id, int crs_id, String att_date) {
 try {
            String strSQL="update attendance set std_id=?, crs_id=?, "+
                          "att_id=? where att_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(4, att_id); 
            prepStatement.setInt(1, std_id); 
            prepStatement.setInt(2, crs_id); 
            prepStatement.setString(3, att_date); 
           

                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
      }

    public static void delete_attendance(int att_id) {
     try {
            String strSQL="delete from attendance where att_id=?";
            PreparedStatement prepStatement =
                    connection.prepareStatement(strSQL);

            prepStatement.setInt(1, att_id); 
                    
            prepStatement.executeUpdate();            
            } catch (SQLException ex) {
            Logger.getLogger(StudentGradingSystemProject.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
    
    
    public static void list_attendance() {
      Attendance at;
      Iterator <Attendance> itr = attendance.iterator();
      System.out.printf("\n%2s %10s %15s %15s",
              "Att Id", "Student id","crs Id", "Att Date");
        draw_line(79);
        
      while (itr.hasNext()) {
          at = itr.next(); 
          System.out.printf("\n%2d %10s %15s %15s", 
              at.getAtt_id(), at.getStd_id(), at.getCrs_id(), 
              at.getAtt_date());
      }
      draw_line(79);
        
    }
   
} // end of class StudentGradingSystemProject
