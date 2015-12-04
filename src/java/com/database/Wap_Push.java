package com.database;

import com.table_data.data_user;
import com.xml.Post_XML;
import com.xml.Set_XML;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class Wap_Push implements Runnable {

    Logger Log = Logger.getLogger(this.getClass());
    ResourceBundle msg = ResourceBundle.getBundle("configs");
    Post_XML xml = new Post_XML();
    Set_XML str_xml = new Set_XML();
    XML_insert insert_r = new XML_insert();
    String local = msg.getString("localhost");
    String data_base = msg.getString("data");
    String user = msg.getString("user");
    String pass = msg.getString("pass");
    String port = msg.getString("port");

    String url = msg.getString("true_url");
    String post_xml_true = msg.getString("true_url");
    String url_mo = msg.getString("ip_mo");

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    List<data_user> user_room = new ArrayList<data_user>();

    private List<data_user> id_user_WapPush;

    String id_user = "";
    String encode = "";
    String RegXML = "";
    String GetXML = "";

    @Override
    public void run() {

        List<data_user> id_user_reg = ProcessWapPush();
        this.Log.info("Test found data[ " + id_user_reg.size() + "] Records");
        for (data_user r : id_user_reg) {
            System.out.println("test reg : " + r.getNumber_type());
            byte[] b = r.getEncoding().getBytes(Charset.forName("UTF-8"));
            encode = new sun.misc.BASE64Encoder().encode(b);

            try {
                /////////// Wap Push
                String wap = "";
                if (wap.equals("ส่งแบบธรรมดา")) {
                    RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), r.getDescriptions(), r.getAccess(), encode, "TIS-620");
                } else if (wap.equals("ส่งแบบ binary ทำการแปลง url ก่อน")) {
                    asciiToHex("");  ///
                    RegXML = str_xml.getXmlReg(r.getService_id(), r.getNumber_type(), r.getDescriptions(), r.getAccess(), encode, "binary");
                }

//                GetXML = xml.PostXml(RegXML, msg.getString("ip_mo"), encode, "mt");
//                System.out.println("Back XML : " + GetXML);
//                insert_r.insert_r(GetXML, "MT");
            } catch (Exception e) {
                this.Log.info("Error : " + e);
                System.out.println("Error Wap Push : " + e);
            }
        }

    }

    public List<data_user> ProcessWapPush() {
        user_room.clear();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM  ");
            String id_user = "";
            while (rs.next()) {

                data_user iduser = new data_user();
                //id_user = rs.getString("sms_id");

                //iduser.setService_id(service_id);
                user_room.add(iduser);
            }

            String sql = "UPDATE sms SET status = '3' WHERE sms_id ='" + id_user + "' ";
            stmt.executeUpdate(sql);

            conn.close();
        } catch (Exception e) {
            //System.out.println("Error : " + e);
            this.Log.info("Error select sql thank" + e);
        }
        return user_room;
    }

    private static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

}
