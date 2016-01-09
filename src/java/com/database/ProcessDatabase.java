package com.database;

import com.xml.Out_XML;
import com.xml.Post_XML;
import com.xml.Set_XML;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class ProcessDatabase {

    Logger Log = Logger.getLogger(this.getClass());

    ResourceBundle msg = ResourceBundle.getBundle("configs");
    Post_XML xml = new Post_XML();
    Out_XML out_xml = new Out_XML();
    String local = msg.getString("localhost");
    String data_base = msg.getString("data");
    String data_base2 = msg.getString("data2");
    String user = msg.getString("user");
    String pass = msg.getString("pass");
    String url = msg.getString("true_url");

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    Date cdate = null;
    Date NewDate = new Date();

    public String ProcessDatabase(String result, PrintWriter out) {
        String sql = null;
        Set_XML str_xml = new Set_XML();
        //this.Log.info("Get Xml true : " + result);
        String encoding = (getdata(result, "?xml version=\"1.0\" encoding=\"", 2, ""));
        String message = (getdata(result, "message id=\"", 3, ""));
        String sms = (getdata(result, "sms type=\"", 3, ""));
        String messageid = (getdata(result, "destination messageid=\"", 3, ""));
        String destination = (getdata(result, "number type=\"abbreviated\"", 4, "number"));
        String number = (getdata(result, "number type=\"international\"", 4, "number"));
        String ud = (getdata(result, "ud type=\"text\"", 4, "ud"));
        String time = (getdata(result, "scts", 1, "scts"));
        String service = (getdata(result, "service-id", 1, "service-id"));
        String from = (getdata(result, "from", 1, ""));
        String to = (getdata(result, "to", 1, ""));
        //System.out.println("service " + service + " time " + time);

        time = time.replace("T", " ");
        time = time.replace("Z", "");
        if (!destination.equals("4557878")) {
            if (ud.equals("R") || ud.equals("r")) {
                ud = "REG";
            } else if (ud.equals("C") || ud.equals("c")) {
                ud = "UNREG";
            }
        }
        //cdate = dateFormat.parse(time);
        String New_date = "";
        try {
            DateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = Format.parse(time);
            New_date = Format.format(convertedDate);
            this.Log.info("New_date " + New_date);
        } catch (Exception e) {
        }

        int check_number = 0;
        int id_number = 0;
        int id_service = 0;
        //int id_product = 0;
        String str_msisdn = "";
        String str_service = "";
        String str_product = "";
        String product_id = "";

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            //////////////////services หา ID บริการ
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            sql = "select * from services where service_id = '" + service + "' AND access_number = '" + destination + "' ";
            rs = stmt.executeQuery(sql);
            Log.info(sql);
            while (rs.next()) {
                id_service = rs.getInt("id");
                str_service = rs.getString("service_id");
                str_product = rs.getString("access_number");
            }

            stmt = conn.createStatement();
            sql = "INSERT INTO delivery_request(TransactionID,product_id,MSISDN,Content,cdate,service_id) "
                    + "VALUES ('" + message + "','" + destination + "','" + number + "','" + ud + "','" + New_date + "','" + service + "')";
            stmt.execute(sql);

            //////////// mobile ดูว่ามีเบอร์แล้วหรือยังมี ดึง ID ไม่มีให้ INSERT
            stmt = conn.createStatement();
            sql = "exec sp_InsertMemberSubscription '" + number + "','3'";
            Log.info(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Log.info("found data msisdn " + rs.getInt("mobile_id") + " msisdn " + rs.getString("msisdn"));
                id_number = rs.getInt("mobile_id");
                str_msisdn = rs.getString("msisdn");

            }

            //this.Log.info("XML service : " + service + " destination " + destination + " SQL str_service " + str_service + " str_product " + str_product);
        } catch (Exception e) {
            //System.out.println("Error delivery_request : " + e);
            this.Log.info("Error report : " + e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        this.Log.info("id_service " + id_service + " str_service " + str_service + " str_product " + str_product);
        if (ud.equals("REG")) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                //////////////////register  non=ยังมีการทำรายการในบริการนั้น | UNREG เคยสมัคร ต้อง UPDATE | REG ส่งข้อความกลับไปแล้วสมัครแล้ว
                String text = "Success receive request";
                sql = "INSERT INTO register(api_req, reg_channel, mobile_id, service_id, reg_date, status,status_code,txid) "
                        + "VALUES('" + ud + "','SMS','" + id_number + "','" + id_service + "','" + New_date + "','0','0','" + message + "')";
                stmt.execute(sql);
                out_xml.OutXmlr(encoding, message, service, destination, number, text, messageid, out);
            } catch (Exception e) {
                this.Log.info("Error REG : " + e);
            } finally {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        } else if (ud.equals("UNREG")) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();
                String text = "Success receive request";
                sql = "INSERT INTO register(api_req, reg_channel, mobile_id, service_id, reg_date, status,status_code,txid) "
                        + "VALUES('" + ud + "','SMS','" + id_number + "','" + id_service + "','" + New_date + "','0','0','" + message + "')";
                stmt.execute(sql);
                out_xml.OutXmlr(encoding, message, service, destination, number, text, messageid, out);
            } catch (Exception e) {
                this.Log.info("Error UNREG : " + e);
            } finally {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        } else if (destination.equals("4557878")) {
            ///// ส่งข้อความ เก็บ content  draco   เชื่อม data_base2
            try {
                int id_serial = 0, point = 0, status = 0;
                String status_serial = "", dcs = "";
//                dcs = getdata(result, "dcs", 1, "dcs");
//                if (dcs.equals(null)) {
//                    ud = (getdata(result, "ud type=\"text\"", 4, "ud"));
//                    this.Log.info("ud type text : " + ud);
//                }
//                } else {
//                    ud = (getdata(result, "ud encoding=\"unicode\" type=\"text\"", 4, "ud"));
//                    this.Log.info("ud type unicode : " + ud);
//                    ud = hex_to_int(ud);
//                    ud = inthex_to_string(ud);
//                }
                this.Log.info("encode : " + ud);
                String date_format = dateFormat.format(NewDate);
                Date cdate_sms = dateFormat.parse(date_format);
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base2 + ";user=" + user + ";password=" + pass + ";";
                conn = DriverManager.getConnection(connectionUrl);
                stmt = conn.createStatement();

                //statuscode เริ่ม 0 คือไม่ โช้หน้าเวป 1 โชหน้าเวป
                sql = "select * from Draco_serial where serial = '" + ud + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    id_serial = rs.getInt("Id");
                    status_serial = rs.getString("status");
                    point = rs.getInt("point");
                }

                if (id_serial != 0) {
                    ///// serial ถูก
                    if (status_serial.equals("N")) {
                        ///// ยังไม่ถูกใช้
                        sql = "INSERT INTO Draco_point (msisdn,id_serial,point,datetime,service_id,transec_id,oper) "
                                + "VALUES ('" + str_msisdn + "','" + id_serial + "','" + New_date + "','" + destination + "','" + message + "','true')";
                        stmt.execute(sql);
                        status = 10;
                    } else {
                        ///// ถูกใช้แล้ว
                        status = 20;
                    }
                } else {
                    ///// serial ไม่มีอยู่ ผิด
                    status = 30;
                }
                sql = "INSERT INTO sms (msisdn,service_id,Product_ID,Timestamp,cdate,content,content_type,status,statuscode) "
                        + "VALUES ('" + str_msisdn + "','" + service + "','" + destination + "','" + New_date + "','" + date_format + "','" + ud + "','T','0','" + status + "')";
                stmt.execute(sql);
            } catch (Exception e) {
                this.Log.info("Error DRACO : " + e);
                System.out.println("Error 4557878 : " + e);
            } finally {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }

        return result;
    }

    public void PreparePostData() {

    }

    public String ProcessSMS(String result, PrintWriter out) {
        String sql = "";
        //String encoding = (getdata(result, "?xml version=\"1.0\" encoding=\"", 2, ""));
        String message_id = (getdata(result, "message id=\"", 3, ""));
        String service = (getdata(result, "service-id", 1, "service-id"));
        String number = (getdata(result, "number type=\"international\"", 4, "number"));
        String destination = (getdata(result, "number type=\"abbreviated\"", 4, "number"));
        String message = (getdata(result, "description", 1, "description"));
        String code = (getdata(result, "code", 1, "code"));
        String date_format = dateFormat.format(NewDate);
//        if (message.equals("Message acknowledged by SMSC")) {
//        } else if (message.equals("Successfully sent to phone")) {
//        }
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + local + ";databaseName=" + data_base + ";user=" + user + ";password=" + pass + ";";
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            String S_message = "";
            if (message.equals("The service is not associated to given subscriber") || message.equals("Message rejected by SMSC")) {
                S_message = "UNREG_IMMEDIATE";
            } else if (message.equals("Message acknowledged by SMSC")) {
                //REG_SUCCESS
                S_message = "RECURRING";
            }
            sql = "INSERT INTO delivery_report(TransactionID,ServiceID,MSISDN,Content,MMS_status,StatusCode,Date,OperId,FRDN,SSSActionReport) "
                    + "VALUES ('" + message_id + "','" + service + "','" + number + "','" + message + "','" + code + "','" + code + "','" + date_format + "','3','true','" + S_message + "')";
            this.Log.info("Log delivery_report : " + sql);
            stmt.execute(sql);
//            sql = "INSERT INTO delivery_request(TransactionID,product_id,MSISDN,Content,StatusCode,cdate,service_id) "
//                    + "VALUES ('" + message + "','" + message_id + "','" + number + "','" + destination + "','" + code + "','" + cdate_sms + "','" + service + "')";
//            stmt.execute(sql);
//            sql = "UPDATE subscribe SET description = 'UNREG',udate = '" + cdate + "' WHERE id='" + id_subscribe + "' ";
//            stmt.executeUpdate(sql);
            return result;
        } catch (Exception e) {
            this.Log.info("Error SMS : " + e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public String getdata(String in, String Tag, int ifroob, String back) {
        StringBuilder sb = new StringBuilder();
        String result = null;
        try {
            String document = in;
            String startTag = "";
            String endTag = "";

            int start = 0;
            int end = 0;

            if (ifroob == 1) {
                //ตัดแบบ หน้างหลังเหมือนกัน
                startTag = "<" + Tag + ">";
                endTag = "</" + Tag + ">";
                start = document.indexOf(startTag) + startTag.length();
                end = document.indexOf(endTag, start);
            } else if (ifroob == 2) {
                //ตัดแบบ เอาระหว่างกลาง ใส่หน้า <" ใส่หลัง ?> 
                startTag = "<" + Tag;
                endTag = "\"?>";
                start = document.indexOf(startTag) + startTag.length();
                end = document.indexOf(endTag, start);
            } else if (ifroob == 3) {
                //ตัดแบบ เอาระหว่างกลาง ใส่หน้า <" ใส่หลัง "> 
                startTag = "<" + Tag;
                endTag = "\">";
                start = document.indexOf(startTag) + startTag.length();
                end = document.indexOf(endTag, start);
                //end = document.indexOf(startTag) + startTag.length() + endTag.length();
            } else if (ifroob == 4) {
                //ตัดแบบ หน้าอีกแบบ ใส่อีกแบบ 
                startTag = "<" + Tag + ">";
                endTag = "</" + back + ">";
                start = document.indexOf(startTag) + startTag.length();
                end = document.indexOf(endTag, start);
            }
            //System.out.println("St : " + start + " End : " + end);
            result = document.substring(start, end);
        } catch (Exception ex) {
            return result;
        }
        return result;
    }

    public String dumpStrings(String text) {
        String str_unicode = "";
        for (int i = 0; i < text.length(); i++) {
            str_unicode = str_unicode + "&#" + (int) text.charAt(i) + ";";
        }
        return str_unicode;
    }

    public String EncodeToString(String text) {
        text = text.replace("&", "");
        text = text.replace(";", "");
        String[] arr = text.split("#");
        String str_unicode = "";
        try {
            for (int i = 1; i < arr.length; i++) {
                int hexVal = Integer.parseInt(arr[i]);
                str_unicode += (char) hexVal;
            }

        } catch (Exception e) {
//            System.out.println("Err en " + e);
        }
        return str_unicode;
    }

    ///// รับ hex มาแปลง เป็น int ในรูปแบบ char
    public String hex_to_int(String text) {
        text = text.replace("&#", "");
        text = text.replace(";", "");
        String[] arr = text.split("x");
        String str_unicode = "";
        int value = 0;
        try {
            for (int i = 1; i < arr.length; i++) {
                value = Integer.parseInt(arr[i], 16);
                str_unicode = str_unicode + "#" + String.valueOf(value);
            }

        } catch (Exception e) {
            //System.out.println("Err en hex " + e);
        }

        return str_unicode;
    }

    ///// รับ char ในรูปแบบ int มาแปลง เป็น string
    public String inthex_to_string(String text) {
        String[] arr = text.split("#");
        String str_unicode = "";
        try {
            for (int i = 1; i < arr.length; i++) {

                int hexVal = Integer.parseInt(arr[i]);
                str_unicode += (char) hexVal;
            }

        } catch (Exception e) {
//            System.out.println("Err en " + e);
        }
        return str_unicode;
    }
}
