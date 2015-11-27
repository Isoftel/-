package com.database;

import com.xml.PostXML;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class ProcessDatabase  {
    
    Logger Log = Logger.getLogger(this.getClass());
    
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    PostXML xml = new PostXML();
    String local = msg.getString("localhost");
    String data_base = msg.getString("data");
    String user = msg.getString("user");
    String pass = msg.getString("pass");
    String url = msg.getString("true_url");

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    public PrintWriter request_printwriter(PrintWriter out, String encoding,String get_string) {

        
        
        //out.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
        

        return out;
    }
    
    
    public String ProcessDatabase(String result,PrintWriter out) {
        //System.out.println("Test xml start : " + result);
        result="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
"<message id=\"routerTestbed@Testbed:3104400\">\n" +
"<sms type=\"mo\">\n" +
"<retry count=\"0\" max=\"0\"/>\n" +
"<destination messageid=\"6156634A\">\n" +
"<address>\n" +
"<number type=\"abbreviated\">4688900</number>\n" +
"</address>\n" +
"</destination>\n" +
"<source>\n" +
"<address>\n" +
"<number type=\"international\">9853435568</number>\n" +
"</address>\n" +
"</source>\n" +
"<ud type=\"text\">R</ud>\n" +
"<scts>2009-05-21T11:03:20Z</scts>\n" +
"<service-id>7112402001</service-id>\n" +
"</sms>\n" +
"<from>SMPP_CMG1</from>\n" +
"<to>HttpAdapter:: 0101102156</to>\n" +
"</message>";
        //7112402001 -3
        
        this.Log.info("Get Xml true : " + result);
  
        String encoding     = (getdata(result, "?xml version=\"1.0\" encoding=\"",  2,""));
        String message      = (getdata(result, "message id=\"",                     3,""));
        String sms          = (getdata(result, "sms type=\"",                       3,""));
        String messageid    = (getdata(result, "destination messageid=\"",          3,""));
        String destination  = (getdata(result, "number type=\"abbreviated\"",       4,"number"));
        String number       = (getdata(result, "number type=\"international\"",     4,"number"));
        String ud           = (getdata(result, "ud type=\"text\"",                  4,"ud"));
        String time         = (getdata(result, "scts",                              1,"scts"));
        String service      = (getdata(result, "service-id",                        1,""));
        String from         = (getdata(result, "from",                              1,""));
        String to           = (getdata(result, "to",                                1,""));
        
        
        //System.out.println(" 1 " + encoding + " 2 " + sms + " 3 " + service + " 4 " + destination + " 5 " + number + " 6 " + ud + " 7 " +time);
        
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            
            //String sql = "INSERT INTO register (api_req)VALUES('" + jumid_schedules + "')";
            
            
             //String sql = "INSERT INTO register (api_req)VALUES('" + jumid_schedules + "')";
            //stmt.execute(sql);
            
            
//            String sql = "UPDATE register SET status = '3' WHERE reg_id='" + id + "' ";
//            stmt.executeUpdate(sql);
            
            
            out.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
            
            
            conn.close();
        } catch (Exception e) {
            //System.out.println("Error SQL : " + e);
        }
        return result;
    }

    public String getdata(String in, String Tag, int ifroob,String back) {
        StringBuilder sb = new StringBuilder();
        String result = null;
        try {
            String document = in;
            String startTag = "";
            String endTag = "";
            if (ifroob == 1) {
                startTag = "<" + Tag + ">";
                endTag = "</" + Tag + ">";
            } else if (ifroob == 2) {
                startTag = "<" + Tag;
                endTag = "\"?>";
            } else if (ifroob == 3) {
                startTag = "<" + Tag + "\"";
                endTag = "\">";
            }else if (ifroob == 4) {
                startTag = "<" + Tag + ">";
                endTag = "</" + back + ">";
            }
            int start = document.indexOf(startTag) + startTag.length();
            int end = document.indexOf(endTag);
            result = document.substring(start, end);
        } catch (Exception ex) {
            //System.out.println("error : "+ex.getMessage());
            return result;
        }
        return result;
    }

 
}
