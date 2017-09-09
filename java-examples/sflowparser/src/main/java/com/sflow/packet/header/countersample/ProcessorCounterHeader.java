/*
 * Adapted from jsflow and add support for Processor Counter
 */
package com.sflow.packet.header.countersample;

import java.math.BigInteger;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

// Processor counter 
public class ProcessorCounterHeader {
     // enterprise = 0, format = 1001

     private long fiveSecCpu;
     private long oneMinCpu;
     private long fiveMinCpu;

     private BigInteger totalMemory;
     private BigInteger freeMemory;

     private CounterData counterData;

     public long getFiveSecCpu() {
          return fiveSecCpu;
     }

     public void setFiveSecCpu(long fiveSecCpu) {
          this.fiveSecCpu = fiveSecCpu;
     }

     public long getOneMinCpu() {
          return oneMinCpu;
     }

     public void setOneMinCpu(long oneMinCpu) {
          this.oneMinCpu = oneMinCpu;
     }

     public long getFiveMinCpu() {
          return fiveMinCpu;
     }

     public void setFiveMinCpu(long fiveMinCpu) {
          this.fiveMinCpu = fiveMinCpu;
     }

     public BigInteger getTotalMemory() {
          return totalMemory;
     }

     public void setTotalMemory(BigInteger totalMemory) {
          this.totalMemory = totalMemory;
     }

     public BigInteger getFreeMemory() {
          return freeMemory;
     }

     public void setFreeMemory(BigInteger freeMemory) {
          this.freeMemory = freeMemory;
     }

     public CounterData getCounterData() {
          return counterData;
     }

     public void setCounterData(CounterData counterData) {
          this.counterData = counterData;
     }

     public static ProcessorCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 28) throw new HeaderParseException("Data array too short.");
               
               ProcessorCounterHeader eic = new ProcessorCounterHeader();

               // 5s_cpu
               byte[] fiveSecCpu = new byte[4];
               System.arraycopy(data, 0, fiveSecCpu, 0, 4);
               eic.setFiveSecCpu(Utility.fourBytesToLong(fiveSecCpu));

               // 1m_cpu
               byte[] oneMinCpu = new byte[4];
               System.arraycopy(data, 4, oneMinCpu, 0, 4);
               eic.setOneMinCpu(Utility.fourBytesToLong(oneMinCpu));

               // 5m_cpu
               byte[] fiveMinCpu = new byte[4];
               System.arraycopy(data, 8, fiveMinCpu, 0, 4);
               eic.setFiveMinCpu(Utility.fourBytesToLong(fiveMinCpu));

               // totalMemory
               byte[] totalMemory = new byte[8];
               System.arraycopy(data, 12, totalMemory, 0, 8);
               eic.setTotalMemory(Utility.eightBytesToBigInteger(totalMemory));

               // freeMemory
               byte[] freeMemory = new byte[8];
               System.arraycopy(data, 20, freeMemory, 0, 8);
               eic.setFreeMemory(Utility.eightBytesToBigInteger(freeMemory));

               // counter data
               if (data.length > 28) {
                    byte[] subData = new byte[data.length - 28];
                    System.arraycopy(data, 28, subData, 0, data.length - 28);

                    CounterData cd = CounterData.parse(subData);
                    eic.setCounterData(cd);
               }

               return eic;
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
               byte[] data = new byte[28 + counterDataLen];

               // 5s_cpu
               System.arraycopy(Utility.longToFourBytes(fiveSecCpu), 0, data, 0, 4);

               // 1m_cpu
               System.arraycopy(Utility.longToFourBytes(oneMinCpu), 0, data, 4, 4);

               // 5m_cpu
               System.arraycopy(Utility.longToFourBytes(fiveMinCpu), 0, data, 8, 4);

               // totalMemory
               System.arraycopy(Utility.BigIntegerToEightBytes(totalMemory), 0, data, 12, 8);

               // freeMemory
               System.arraycopy(Utility.BigIntegerToEightBytes(freeMemory), 0, data, 20, 8);

               // counter data
               if (counterDataLen != 0) {
                    System.arraycopy(counterDataBytes, 0, data, 28, counterDataBytes.length);
               }

               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[ProcessorCounterHeader]:");

          sb.append("5sCpu: ");
          sb.append(getFiveSecCpu());

          sb.append(", 1mCpu: " + this.getOneMinCpu());
          sb.append(", 5mCpu: " + this.getFiveMinCpu());

          sb.append(", totalMemory: ");
          sb.append(getTotalMemory());

          sb.append(", freeMemory: ");
          sb.append(getFreeMemory());
          
          return sb.toString();
     }
}
