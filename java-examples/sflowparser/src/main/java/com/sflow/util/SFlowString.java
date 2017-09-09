package com.sflow.util;


import java.nio.charset.Charset;

public class SFlowString {
     
     private String       str;
     private long          len;
     

     public long getLen() {
          return len;
     }

     public void setLen(long len) {
          this.len = len;
     }

     public String getStr() {
          return str;
     }

     public void setStr(String str) {
          this.str = str;
     }

     public static <T extends SFlowString> T parse(byte[] data, T t) throws HeaderParseException {
          
          try {
               if (data.length < 4) throw new HeaderParseException("Data array too short.");
               
               t.setLen(Utility.fourBytesToLong(data, 4));
               t.setStr( Utility.dumpBytes(data, (int)t.getLen()) );

               return t;
          }  catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }
     
     public byte[] getBytes() throws HeaderBytesException {
          try {
               byte[] data = new byte[(int) (4 + len)];
               // length of string
               System.arraycopy(Utility.longToFourBytes(len), 0, data, 0, 4);
               // the string itself
               System.arraycopy(str.getBytes(Charset.forName("UTF-8")), 
                         0, data, 4, (int) len);
               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
          
     public String toString(String namePrefix) {
          StringBuilder sb = new StringBuilder();
          
          sb.append("Len: ");
          sb.append(len);
          
          sb.append(namePrefix  + ": ");
          sb.append(str);
          
          return sb.toString();          
     }

}
