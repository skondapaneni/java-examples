package com.sflow.packet.header.countersample;

import java.math.BigInteger;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

public class OpenFlowPortCounterHeader 
{
     private BigInteger  datapathId;
     private long          portNum;
     
     public BigInteger getDatapathId() {
          return datapathId;
     }

     public void setDatapathId(BigInteger datapathId) {
          this.datapathId = datapathId;
     }

     public long getPortNum() {
          return portNum;
     }

     public void setPortNum(long portNum) {
          this.portNum = portNum;
     }

     public static OpenFlowPortCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 12) throw new HeaderParseException("Data array too short.");

               OpenFlowPortCounterHeader ofh = new OpenFlowPortCounterHeader();
               
               ofh.setDatapathId(Utility.eightBytesToBigInteger(data, 0));
               ofh.setPortNum(Utility.fourBytesToLong(data, 8));

               return ofh;
          }  catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }
     
     public byte[] getBytes() throws HeaderBytesException {
          try {
               byte[] data = new byte[12];
               // dataPathId
               System.arraycopy(Utility.BigIntegerToEightBytes(datapathId), 0, data, 0, 8);               
               // portNum
               System.arraycopy(Utility.longToFourBytes(portNum), 0, data, 8, 4);
               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
          
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[OpenFlowPortCounterHeader]:");
          
          sb.append("DataPathId: ");
          sb.append(getDatapathId());
          
          sb.append(", OpenFlow PortNum: ");
          sb.append(getPortNum());
          
          return sb.toString();
          
     }
}
