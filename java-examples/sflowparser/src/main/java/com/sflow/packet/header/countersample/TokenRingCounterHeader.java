/*
 *
 * Adapted from jsflow and added support for parsing token Ring Counter .
 *
 */
package com.sflow.packet.header.countersample;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

/* Token Ring Counters - see RFC 1748 */
/* opaque = counter_data; enterprise = 0; format = 3 */

public class TokenRingCounterHeader {
     // enterprise = 0, format = 3
     private long dot5StatsLineErrors;
     private long dot5StatsBurstErrors;
     private long dot5StatsACErrors;
     private long dot5StatsAbortTransErrors;
     private long dot5StatsInternalErrors;
     private long dot5StatsLostFrameErrors;
     private long dot5StatsReceiveCongestions;
     private long dot5StatsFrameCopiedErrors;
     private long dot5StatsTokenErrors;
     private long dot5StatsSoftErrors;
     private long dot5StatsHardErrors;
     private long dot5StatsSignalLoss;
     private long dot5StatsTransmitBeacons;
     private long dot5StatsRecoverys;
     private long dot5StatsLobeWires;
     private long dot5StatsRemoves;
     private long dot5StatsSingles;
     private long dot5StatsFreqErrors;

     private CounterData counterData;

     public long getDot5StatsLineErrors() {
          return dot5StatsLineErrors;
     }

     public void setDot5StatsLineErrors(long dot5StatsLineErrors) {
          this.dot5StatsLineErrors = dot5StatsLineErrors;
     }

     public long getDot5StatsBurstErrors() {
          return dot5StatsBurstErrors;
     }

     public void setDot5StatsBurstErrors(long dot5StatsBurstErrors) {
          this.dot5StatsBurstErrors = dot5StatsBurstErrors;
     }

     public long getDot5StatsACErrors() {
          return dot5StatsACErrors;
     }

     public void setDot5StatsACErrors(long dot5StatsACErrors) {
          this.dot5StatsACErrors = dot5StatsACErrors;
     }

     public long getDot5StatsAbortTransErrors() {
          return dot5StatsAbortTransErrors;
     }

     public void setDot5StatsAbortTransErrors(long dot5StatsAbortTransErrors) {
          this.dot5StatsAbortTransErrors = dot5StatsAbortTransErrors;
     }

     public long getDot5StatsInternalErrors() {
          return dot5StatsInternalErrors;
     }

     public void setDot5StatsInternalErrors(long dot5StatsInternalErrors) {
          this.dot5StatsInternalErrors = dot5StatsInternalErrors;
     }

     public long getDot5StatsLostFrameErrors() {
          return dot5StatsLostFrameErrors;
     }

     public void setDot5StatsLostFrameErrors(long dot5StatsLostFrameErrors) {
          this.dot5StatsLostFrameErrors = dot5StatsLostFrameErrors;
     }

     public long getDot5StatsReceiveCongestions() {
          return dot5StatsReceiveCongestions;
     }

     public void setDot5StatsReceiveCongestions(long dot5StatsReceiveCongestions) {
          this.dot5StatsReceiveCongestions = dot5StatsReceiveCongestions;
     }

     public long getDot5StatsFrameCopiedErrors() {
          return dot5StatsFrameCopiedErrors;
     }

     public void setDot5StatsFrameCopiedErrors(long dot5StatsFrameCopiedErrors) {
          this.dot5StatsFrameCopiedErrors = dot5StatsFrameCopiedErrors;
     }

     public long getDot5StatsTokenErrors() {
          return dot5StatsTokenErrors;
     }

     public void setDot5StatsTokenErrors(long dot5StatsTokenErrors) {
          this.dot5StatsTokenErrors = dot5StatsTokenErrors;
     }

     public long getDot5StatsSoftErrors() {
          return dot5StatsSoftErrors;
     }

     public void setDot5StatsSoftErrors(long dot5StatsSoftErrors) {
          this.dot5StatsSoftErrors = dot5StatsSoftErrors;
     }

     public long getDot5StatsHardErrors() {
          return dot5StatsHardErrors;
     }

     public void setDot5StatsHardErrors(long dot5StatsHardErrors) {
          this.dot5StatsHardErrors = dot5StatsHardErrors;
     }

     public long getDot5StatsSignalLoss() {
          return dot5StatsSignalLoss;
     }

     public void setDot5StatsSignalLoss(long dot5StatsSignalLoss) {
          this.dot5StatsSignalLoss = dot5StatsSignalLoss;
     }

     public long getDot5StatsTransmitBeacons() {
          return dot5StatsTransmitBeacons;
     }

     public void setDot5StatsTransmitBeacons(long dot5StatsTransmitBeacons) {
          this.dot5StatsTransmitBeacons = dot5StatsTransmitBeacons;
     }

     public long getDot5StatsRecoverys() {
          return dot5StatsRecoverys;
     }

     public void setDot5StatsRecoverys(long dot5StatsRecoverys) {
          this.dot5StatsRecoverys = dot5StatsRecoverys;
     }

     public long getDot5StatsLobeWires() {
          return dot5StatsLobeWires;
     }

     public void setDot5StatsLobeWires(long dot5StatsLobeWires) {
          this.dot5StatsLobeWires = dot5StatsLobeWires;
     }

     public long getDot5StatsRemoves() {
          return dot5StatsRemoves;
     }

     public void setDot5StatsRemoves(long dot5StatsRemoves) {
          this.dot5StatsRemoves = dot5StatsRemoves;
     }

     public long getDot5StatsSingles() {
          return dot5StatsSingles;
     }

     public void setDot5StatsSingles(long dot5StatsSingles) {
          this.dot5StatsSingles = dot5StatsSingles;
     }

     public long getDot5StatsFreqErrors() {
          return dot5StatsFreqErrors;
     }

     public void setDot5StatsFreqErrors(long dot5StatsFreqErrors) {
          this.dot5StatsFreqErrors = dot5StatsFreqErrors;
     }

     public CounterData getCounterData() {
          return counterData;
     }

     public void setCounterData(CounterData counterData) {
          this.counterData = counterData;
     }

     public static TokenRingCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 72) throw new HeaderParseException("Data array too short.");
               TokenRingCounterHeader eic = new TokenRingCounterHeader();

               eic.setDot5StatsLineErrors(Utility.fourBytesToLong(data, 0));
               eic.setDot5StatsBurstErrors(Utility.fourBytesToLong(data, 4));
               eic.setDot5StatsACErrors(Utility.fourBytesToLong(data, 8));
               eic.setDot5StatsAbortTransErrors(Utility.fourBytesToLong(data, 12));
               eic.setDot5StatsInternalErrors(Utility.fourBytesToLong(data, 16));
               eic.setDot5StatsLostFrameErrors(Utility.fourBytesToLong(data, 20));
               eic.setDot5StatsReceiveCongestions(Utility.fourBytesToLong(data, 24));
               eic.setDot5StatsFrameCopiedErrors(Utility.fourBytesToLong(data, 28));
               eic.setDot5StatsTokenErrors(Utility.fourBytesToLong(data, 32));
               eic.setDot5StatsSoftErrors(Utility.fourBytesToLong(data, 36));
               eic.setDot5StatsHardErrors(Utility.fourBytesToLong(data, 40));
               eic.setDot5StatsSignalLoss(Utility.fourBytesToLong(data, 44));
               eic.setDot5StatsTransmitBeacons(Utility.fourBytesToLong(data, 48));
               eic.setDot5StatsRecoverys(Utility.fourBytesToLong(data, 52));
               eic.setDot5StatsLobeWires(Utility.fourBytesToLong(data, 56));
               eic.setDot5StatsRemoves(Utility.fourBytesToLong(data, 60));
               eic.setDot5StatsSingles(Utility.fourBytesToLong(data, 64));
               eic.setDot5StatsFreqErrors(Utility.fourBytesToLong(data, 68));

               if (data.length > 72) {
                    // counter data
                    byte[] subData = new byte[data.length - 72]; 
                    System.arraycopy(data, 72, subData, 0, data.length - 72);

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
               int counterDataLength = 0;
               
               if (counterData != null) {
                    counterDataBytes = counterData.getBytes();
                    counterDataLength = counterDataBytes.length;
               }
               
               byte[] data = new byte[72 + counterDataLength];

               // dot5StatsLineErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsLineErrors), 0, data, 0, 4);

               // dot5StatsBurstErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsBurstErrors), 0, data, 4, 4);

               // dot5StatsACErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsACErrors), 0, data, 8, 4);

               // dot5StatsAbortTransErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsAbortTransErrors), 0, data, 12, 4);

               // dot5StatsInternalErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsInternalErrors), 0, data, 16, 4);

               // dot5StatsLostFrameErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsLostFrameErrors), 0, data, 20, 4);

               // dot5StatsReceiveCongestions
               System.arraycopy(Utility.longToFourBytes(dot5StatsReceiveCongestions), 0, data, 24, 4);

               // dot5StatsFrameCopiedErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsFrameCopiedErrors), 0, data, 28, 4);

               // dot5StatsTokenErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsTokenErrors), 0, data, 32, 4);

               // dot5StatsSoftErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsSoftErrors), 0, data, 36, 4);

               // dot5StatsHardErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsHardErrors), 0, data, 40, 4);

               // dot5StatsSignalLoss
               System.arraycopy(Utility.longToFourBytes(dot5StatsSignalLoss), 0, data, 44, 4);

               // dot5StatsTransmitBeacons
               System.arraycopy(Utility.longToFourBytes(dot5StatsTransmitBeacons), 0, data, 48, 4);

               // dot5StatsRecoverys
               System.arraycopy(Utility.longToFourBytes(dot5StatsRecoverys), 0, data, 52, 4);

               // dot5StatsLobeWires
               System.arraycopy(Utility.longToFourBytes(dot5StatsLobeWires), 0, data, 56, 4);

               // dot5StatsRemoves
               System.arraycopy(Utility.longToFourBytes(dot5StatsRemoves), 0, data, 60, 4);

               // dot5StatsSingles
               System.arraycopy(Utility.longToFourBytes(dot5StatsSingles), 0, data, 64, 4);

               // dot5StatsFreqErrors
               System.arraycopy(Utility.longToFourBytes(dot5StatsFreqErrors), 0, data, 68, 4);
               
               if (counterDataLength != 0) {
                    // counter data
                    System.arraycopy(counterDataBytes, 0, data, 72, counterDataBytes.length);
               }

               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();

          sb.append("[TokenRingCounterHeader]:");
          sb.append("LineErrors: " +  this.getDot5StatsLineErrors());
          sb.append(", BurstErrors: " + this.getDot5StatsBurstErrors());
          sb.append(", ACErrors: " + this.getDot5StatsACErrors());
          sb.append(", AbortTransErrors: " + this.getDot5StatsAbortTransErrors());
          sb.append(", InternalErrors: " + this.getDot5StatsInternalErrors());
          sb.append(", LostFrameErrors: " + this.getDot5StatsLostFrameErrors());
          sb.append(", ReceiveCongestions: " + this.getDot5StatsReceiveCongestions());
          sb.append(", FrameCopiedErrors: " + this.getDot5StatsFrameCopiedErrors());
          sb.append(", TokenErrors: " + this.getDot5StatsTokenErrors());
          sb.append(", SoftErrors: " + this.getDot5StatsSoftErrors());
          sb.append(", HardErrors: " + this.getDot5StatsHardErrors());
          sb.append(", SignalLoss: " + this.getDot5StatsSignalLoss());
          sb.append(", TransmitBeacons: " + this.getDot5StatsTransmitBeacons());
          sb.append(", Recoverys: " + this.getDot5StatsRecoverys());
          sb.append(", LobeWires: " + this.getDot5StatsLobeWires());
          sb.append(", Removes: " + this.getDot5StatsRemoves());
          sb.append(", Singles: " + this.getDot5StatsSingles());
          sb.append(", FreqErrors: " + this.getDot5StatsFreqErrors());

          return sb.toString();
     }
}
