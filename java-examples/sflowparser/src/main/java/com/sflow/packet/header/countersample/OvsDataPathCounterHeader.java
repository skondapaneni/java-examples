package com.sflow.packet.header.countersample;
/*
 * Adapted from jsflow and add support for OvsDataPath Counter
 */

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;


public class OvsDataPathCounterHeader {

         // Ovs DataPath Resources counter 
         // enterprise = 0, format = 2207

         private long nHit;   
         private long nMissed;
         private long nLost;
         private long nMaskHit;
         private long nFlows;
         private long nMasks;
         
         private CounterData counterData;
          

         public long getnHit() {
              return nHit;
         }

         public void setnHit(long nHit) {
              this.nHit = nHit;
         }

         public long getnMissed() {
              return nMissed;
         }

         public void setnMissed(long nMissed) {
              this.nMissed = nMissed;
         }

         public long getnLost() {
              return nLost;
         }

         public void setnLost(long nLost) {
              this.nLost = nLost;
         }

         public long getnMaskHit() {
              return nMaskHit;
         }

         public void setnMaskHit(long nMaskHit) {
              this.nMaskHit = nMaskHit;
         }

         public long getnFlows() {
              return nFlows;
         }

         public void setnFlows(long nFlows) {
              this.nFlows = nFlows;
         }

         public long getnMasks() {
              return nMasks;
         }

         public void setnMasks(long nMasks) {
              this.nMasks = nMasks;
         }

         public CounterData getCounterData() {
              return counterData;
         }

         public void setCounterData(CounterData counterData) {
              this.counterData = counterData;
         }
         

         public static OvsDataPathCounterHeader parse(byte[] data) throws HeaderParseException {
              try {
                   if (data.length < 24) throw new HeaderParseException("Data array too short.");
                   OvsDataPathCounterHeader arc = new OvsDataPathCounterHeader();


                                 
                   arc.setnHit(Utility.fourBytesToLong(data, 0));
                   arc.setnMissed(Utility.fourBytesToLong(data, 4));
                   arc.setnLost(Utility.fourBytesToLong(data, 8));
                   arc.setnMaskHit(Utility.fourBytesToLong(data,12));
                   arc.setnFlows(Utility.fourBytesToLong(data, 16));
                   arc.setnMasks(Utility.fourBytesToLong(data, 20));

                   // counter data
                   if (data.length > 24) {
                        byte[] subData = new byte[data.length - 24];
                        System.arraycopy(data, 24, subData, 0, data.length - 24);

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
                   byte[] data = new byte[24 +  counterDataLen];
                   // nHit
                   System.arraycopy(Utility.longToFourBytes(nHit), 0, data, 0, 4);
                   
                   // nMissed
                   System.arraycopy(Utility.longToFourBytes(nMissed), 0, data, 4, 4);

                   // nLost
                   System.arraycopy(Utility.longToFourBytes(nLost), 0, data, 8, 4);
                   
                   // nMaskHit
                   System.arraycopy(Utility.longToFourBytes(nMaskHit), 0, data, 12, 4);

                   // nFlows
                   System.arraycopy(Utility.longToFourBytes(nFlows), 0, data, 16, 4);
                   
                   // nMasks
                   System.arraycopy(Utility.longToFourBytes(nMasks), 0, data, 20, 4);

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
              sb.append("[OvsDataPathCounterHeader]:");

              sb.append("nHit: ");
              sb.append(getnHit());

              sb.append(", nMissed: ");
              sb.append(getnMissed());

              sb.append(", nLost: ");
              sb.append(getnLost());

              sb.append(", nMaskHit: ");
              sb.append(getnMaskHit());

              sb.append(", nFlows: ");
              sb.append(getnFlows());

              sb.append(", nMasks: ");
              sb.append(getnMasks());
                             
              return sb.toString();

         }
}
