/*
 * Adapted from jsflow and add support for VG Counter
 */
package com.sflow.packet.header.countersample;


import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.MacAddress;
import com.sflow.util.Utility;

/* LAG Port Statistics - see http://sflow.org/sflow_lag.txt */
/* opaque = counter_data; enterprise = 0; format = 7 */

public class LACPCounterHeader {
     // enterprise = 0, format = 7

     private MacAddress actorSystemId;
     private MacAddress partnerSystemId;
     private long attachedAggId;
     private LACPPortState portState;
     private long PDUsRx;
     private long markerPDUsRx;
     private long markerResponsePDUsRx;
     private long unknownRx;
     private long illegalRx;
     private long PDUsTx;
     private long markerPDUsTx;
     private long markerResponsePDUsTx;

     private CounterData counterData;

     public MacAddress getActorSystemId() {
          return actorSystemId;
     }

     public void setActorSystemId(MacAddress actorSystemId) {
          this.actorSystemId = actorSystemId;
     }

     public MacAddress getPartnerSystemId() {
          return partnerSystemId;
     }

     public void setPartnerSystemId(MacAddress partnerSystemId) {
          this.partnerSystemId = partnerSystemId;
     }

     public long getAttachedAggId() {
          return attachedAggId;
     }

     public void setAttachedAggId(long attachedAggId) {
          this.attachedAggId = attachedAggId;
     }

     public LACPPortState getPortState() {
          return portState;
     }

     public void setPortState(LACPPortState portState) {
          this.portState = portState;
     }

     public long getPDUsRx() {
          return PDUsRx;
     }

     public void setPDUsRx(long pDUsRx) {
          PDUsRx = pDUsRx;
     }

     public long getMarkerPDUsRx() {
          return markerPDUsRx;
     }

     public void setMarkerPDUsRx(long markerPDUsRx) {
          this.markerPDUsRx = markerPDUsRx;
     }

     public long getMarkerResponsePDUsRx() {
          return markerResponsePDUsRx;
     }

     public void setMarkerResponsePDUsRx(long markerResponsePDUsRx) {
          this.markerResponsePDUsRx = markerResponsePDUsRx;
     }

     public long getUnknownRx() {
          return unknownRx;
     }

     public void setUnknownRx(long unknownRx) {
          this.unknownRx = unknownRx;
     }

     public long getIllegalRx() {
          return illegalRx;
     }

     public void setIllegalRx(long illegalRx) {
          this.illegalRx = illegalRx;
     }

     public long getPDUsTx() {
          return PDUsTx;
     }

     public void setPDUsTx(long pDUsTx) {
          PDUsTx = pDUsTx;
     }

     public long getMarkerPDUsTx() {
          return markerPDUsTx;
     }

     public void setMarkerPDUsTx(long markerPDUsTx) {
          this.markerPDUsTx = markerPDUsTx;
     }

     public long getMarkerResponsePDUsTx() {
          return markerResponsePDUsTx;
     }

     public void setMarkerResponsePDUsTx(long markerResponsePDUsTx) {
          this.markerResponsePDUsTx = markerResponsePDUsTx;
     }

     public CounterData getCounterData() {
          return counterData;
     }

     public void setCounterData(CounterData counterData) {
          this.counterData = counterData;
     }

     public static LACPCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 56) throw new HeaderParseException("Data array too short.");
               LACPCounterHeader lacp = new LACPCounterHeader();
          
               lacp.setActorSystemId(new MacAddress(data, 0));
               lacp.setPartnerSystemId(new MacAddress(data, 6));
               lacp.setAttachedAggId(Utility.fourBytesToLong(data, 12));
               lacp.setPortState(LACPPortState.parse(data, 16));
               lacp.setPDUsRx(Utility.fourBytesToLong(data, 24));
               lacp.setMarkerPDUsRx(Utility.fourBytesToLong(data, 28));
               lacp.setMarkerResponsePDUsRx(Utility.fourBytesToLong(data, 32));
               lacp.setUnknownRx(Utility.fourBytesToLong(data, 36));
               lacp.setIllegalRx(Utility.fourBytesToLong(data, 40));
               lacp.setPDUsTx(Utility.fourBytesToLong(data, 44));
               lacp.setMarkerPDUsTx(Utility.fourBytesToLong(data, 48));
               lacp.setMarkerResponsePDUsTx(Utility.fourBytesToLong(data, 52));

               if (data.length > 56) {
                    // counter data
                    byte[] subData = new byte[data.length - 56]; 
                    System.arraycopy(data, 56, subData, 0, data.length - 56);
                    CounterData cd = CounterData.parse(subData);
                    lacp.setCounterData(cd);
               }

               return lacp;
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
               byte[] data = new byte[56 +  counterDataLen];
               
               System.arraycopy(actorSystemId.getBytes(), 0, data, 0, 6);
               System.arraycopy(partnerSystemId.getBytes(), 0, data, 6, 6);
               System.arraycopy(Utility.longToFourBytes(attachedAggId), 0, data, 12, 4);
               System.arraycopy(portState.getBytes(), 0, data, 16, 8);
               System.arraycopy(Utility.longToFourBytes(PDUsRx), 0, data, 24, 4);
               System.arraycopy(Utility.longToFourBytes(markerPDUsRx), 0, data, 28, 4);
               System.arraycopy(Utility.longToFourBytes(markerResponsePDUsRx), 0, data, 32, 4);
               System.arraycopy(Utility.longToFourBytes(unknownRx), 0, data, 36, 4);
               System.arraycopy(Utility.longToFourBytes(illegalRx), 0, data, 40, 4);
               System.arraycopy(Utility.longToFourBytes(PDUsTx), 0, data, 44, 4);
               System.arraycopy(Utility.longToFourBytes(markerPDUsTx), 0, data, 46, 4);
               System.arraycopy(Utility.longToFourBytes(markerResponsePDUsTx), 0, data, 52, 4);
               
               if (counterDataBytes != null) {
                    // counter data
                    System.arraycopy(counterDataBytes, 0, data, 56, counterDataBytes.length);
               }
               
               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[LACP CounterHeader]:");
          
          sb.append("actorSystemId: ");
          sb.append(actorSystemId.toString());
          sb.append(", partnerSystemId: ");
          sb.append(partnerSystemId.toString());
          sb.append(", attachedAggId: ");
          sb.append(attachedAggId);
          sb.append(", portState: ");
          sb.append(portState.toString());
          sb.append(", PDUsRx: ");
          sb.append(PDUsRx);
          sb.append(", markerPDUsRx: ");
          sb.append(markerPDUsRx);
          sb.append(", markerResponsePDUsRx: ");
          sb.append(markerResponsePDUsRx);
          sb.append(", unknownRx: ");
          sb.append(unknownRx);
          sb.append(", illegalRx: ");
          sb.append(illegalRx);
          sb.append(", PDUsTx: ");
          sb.append(PDUsTx);
          sb.append(", markerPDUsTx: ");
          sb.append(markerPDUsTx);
          sb.append(", markerResponsePDUsTx: ");
          sb.append(markerResponsePDUsTx);
          
          return sb.toString();
     }
}
