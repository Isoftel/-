/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml;

import com.table_data.Responsed;

/**
 *
 * @author Administrator
 */
public class insert_xml_data {

    public String insert_r(String xml) {
        /*
         <?xml version="1.0" encoding="ISO-8859-1"?>
         <message id="1242878588600">
         <rsr type="ack">
         <service-id>0101102156</service-id>
         <destination messageid="1242878588600">
         <address>
         <number type="international">668xxxxxxxx</number>
         </address>
         </destination>
         <source>
         <address>
         <number type="">True Move</number>
         </address>
         </source>
         <rsr_detail status="success">
         <code>000</code>
         <description>success</description>
         </rsr_detail>
         </rsr>
         </message>
         */
        String service = getdata(xml, "service-id", 1, "");
        String messageid = getdata(xml, "<destination messageid=\"", 3, "");
        
        return xml;
    }

    public Responsed getXML(String str) {
        Responsed rsp = new Responsed();
//        getdata(str, "encoding", 1, "");
        //rsp.setEncoding(getdata(str, "encoding"));
//        rsp.setSize(getdata(str, "size"));
//        rsp.setStatus(getdata(str, "status"));
//        rsp.setDescription(getdata(str, "description"));
//        rsp.setRdate(getdata(str, "dates"));
        return rsp;
    }

    public String getdata(String in, String Tag, int ifroob, String back) {
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
                endTag = "\">";
            } else if (ifroob == 3) {
                startTag = "<" + Tag + "";
                endTag = "\"?>";
            } else if (ifroob == 4) {
                startTag = "<" + Tag + ">";
                endTag = "<" + back + ">";
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
