/*
 * Adapted from jsflow 
 */
package com.sflow.packet.header.countersample;

import java.math.BigInteger;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

// App Resources counter 
public class AppResourcesCounterHeader {
    // enterprise = 0, format = 2203

     private long userTime;   /* in milliseconds */
     private long systemTime; /* in milliseconds */
     private BigInteger memUsed;
     private BigInteger memMax;
     private long fdOpen;
     private long fdMax;
     private long connOpen;
     private long connMax;
    
     private CounterData counterData;

     public long getUserTime() {
          return userTime;
     }

     public void setUserTime(long userTime) {
          this.userTime = userTime;
     }

     public long getSystemTime() {
          return systemTime;
     }

     public void setSystemTime(long systemTime) {
          this.systemTime = systemTime;
     }

     public BigInteger getMemUsed() {
          return memUsed;
     }

     public void setMemUsed(BigInteger memUsed) {
          this.memUsed = memUsed;
     }

     public BigInteger getMemMax() {
          return memMax;
     }

     public void setMemMax(BigInteger memMax) {
          this.memMax = memMax;
     }

     public long getFdOpen() {
          return fdOpen;
     }

     public void setFdOpen(long fdOpen) {
          this.fdOpen = fdOpen;
     }

     public long getFdMax() {
          return fdMax;
     }

     public void setFdMax(long fdMax) {
          this.fdMax = fdMax;
     }

     public long getConnOpen() {
          return connOpen;
     }

     public void setConnOpen(long connOpen) {
          this.connOpen = connOpen;
     }

     public long getConnMax() {
          return connMax;
     }

     public void setConnMax(long connMax) {
          this.connMax = connMax;
     }

     public CounterData getCounterData() {
          return counterData;
     }

     public void setCounterData(CounterData counterData) {
          this.counterData = counterData;
     }
     
     public static AppResourcesCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 40) throw new HeaderParseException("Data array too short.");
               AppResourcesCounterHeader arc = new AppResourcesCounterHeader();
                        
               arc.setUserTime(Utility.fourBytesToLong(data, 0));
               arc.setSystemTime(Utility.fourBytesToLong(data, 4));
               arc.setMemUsed(Utility.eightBytesToBigInteger(data, 8));
               arc.setMemMax(Utility.eightBytesToBigInteger(data,16));
               arc.setFdOpen(Utility.fourBytesToLong(data, 24));
               arc.setFdMax(Utility.fourBytesToLong(data, 28));
               arc.setConnOpen(Utility.fourBytesToLong(data, 32));
               arc.setConnMax(Utility.fourBytesToLong(data, 36));

               if (data.length > 40) {
                    // counter data
                    byte[] subData = new byte[data.length - 28];
                    System.arraycopy(data, 28, subData, 0, data.length - 28);

                    CounterData cd = CounterData.parse(subData);
                    arc.setCounterData(cd);
               }

               return arc;
          }  catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }

     public byte[] getBytes() throws HeaderBytesException {
          try {
               byte[] counterDataBytes = null;
               int counterDataLen = 0;
               if (counterData != null) {
                    counterDataBytes = counterData.getBytes();
                    counterDataLen = counterDataBytes.length;
               }
               byte[] data = new byte[40 + counterDataLen];

               System.arraycopy(Utility.longToFourBytes(userTime), 0, data, 0, 4);               
               System.arraycopy(Utility.longToFourBytes(systemTime), 0, data, 4, 4);
               System.arraycopy(Utility.BigIntegerToEightBytes(memUsed), 0, data, 8, 8);
               System.arraycopy(Utility.BigIntegerToEightBytes(memMax), 0, data, 16, 8);
               System.arraycopy(Utility.longToFourBytes(fdOpen), 0, data, 24, 4);
               System.arraycopy(Utility.longToFourBytes(fdMax), 0, data, 28, 4);
               System.arraycopy(Utility.longToFourBytes(connOpen), 0, data, 32, 4);
               System.arraycopy(Utility.longToFourBytes(connMax), 0, data, 36, 4);

               // counter data
               if (counterDataLen != 0) {
                    System.arraycopy(counterDataBytes, 0, data, 40, counterDataBytes.length);
               }

               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {

          StringBuilder sb = new StringBuilder();
          sb.append("[AppResourcesCounterHeader]:");

          sb.append("UserTime: ");
          sb.append(getUserTime());

          sb.append(", SystemTime: ");
          sb.append(getSystemTime());

          sb.append(", MemUsed: ");
          sb.append(getMemUsed());

          sb.append(", MemMax: ");
          sb.append(getMemMax());

          sb.append(", FdOpen: ");
          sb.append(getFdOpen());

          sb.append(", FdMax: ");
          sb.append(getFdMax());
          
          sb.append(", connOpen: ");
          sb.append(getConnOpen());

          sb.append(", connMax: ");
          sb.append(getConnMax());
          
          return sb.toString();

     }
}
