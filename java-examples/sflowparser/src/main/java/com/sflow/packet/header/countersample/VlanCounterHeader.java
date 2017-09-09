/*
 * Adapted from jsflow and add support for Vlan Counter
 */
package com.sflow.packet.header.countersample;

import java.math.BigInteger;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

// VLAN counter 
public class VlanCounterHeader {
     // enterprise = 0, format = 5

     private long vlanId;
     private BigInteger octets;
     private long ucastPkts;
     private long multicastPkts;
     private long broadcastPkts;
     private long discards;

     private CounterData counterData;
     
     public long getVlanId() {
          return vlanId;
     }

     public void setVlanId(long vlanId) {
          this.vlanId = vlanId;
     }

     public BigInteger getOctets() {
          return octets;
     }

     public void setOctets(BigInteger octets) {
          this.octets = octets;
     }

     public long getUcastPkts() {
          return ucastPkts;
     }

     public void setUcastPkts(long ucastPkts) {
          this.ucastPkts = ucastPkts;
     }

     public long getMulticastPkts() {
          return multicastPkts;
     }

     public void setMulticastPkts(long multicastPkts) {
          this.multicastPkts = multicastPkts;
     }

     public long getBroadcastPkts() {
          return broadcastPkts;
     }

     public void setBroadcastPkts(long broadcastPkts) {
          this.broadcastPkts = broadcastPkts;
     }

     public long getDiscards() {
          return discards;
     }

     public void setDiscards(long discards) {
          this.discards = discards;
     }

     public CounterData getCounterData() {
          return counterData;
     }

     public void setCounterData(CounterData counterData) {
          this.counterData = counterData;
     }


     public static VlanCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 28) throw new HeaderParseException("Data array too short.");
               VlanCounterHeader eic = new VlanCounterHeader();

               eic.setVlanId(Utility.fourBytesToLong(data, 0));
               eic.setOctets(Utility.eightBytesToBigInteger(data, 4));
               eic.setUcastPkts(Utility.fourBytesToLong(data, 12));
               eic.setMulticastPkts(Utility.fourBytesToLong(data, 16));
               eic.setBroadcastPkts(Utility.fourBytesToLong(data, 20));
               eic.setDiscards(Utility.fourBytesToLong(data, 24));

               if (data.length > 28) {
                    // counter data
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

               // vlanId
               System.arraycopy(Utility.longToFourBytes(vlanId), 0, data, 0, 4);

               // octets
               System.arraycopy(Utility.BigIntegerToEightBytes(octets), 0, data, 4, 8);

               // ucastPkts
               System.arraycopy(Utility.longToFourBytes(ucastPkts), 0, data, 12, 4);

               // multicastPkts
               System.arraycopy(Utility.longToFourBytes(multicastPkts), 0, data, 16, 4);

               // broadcastPkts
               System.arraycopy(Utility.longToFourBytes(broadcastPkts), 0, data, 20, 4);

               // discards
               System.arraycopy(Utility.longToFourBytes(discards), 0, data, 24, 4);

               if (counterDataLen != 0) {
                    // counter data
                    System.arraycopy(counterDataBytes, 0, data, 28, counterDataBytes.length);
               }

               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {

          StringBuilder sb = new StringBuilder();
          sb.append("[VlanCounterHeader]:");

          sb.append("VlanId: ");
          sb.append(getVlanId());

          sb.append(", Octets: ");
          sb.append(getOctets());

          sb.append(", ucastPkts: ");
          sb.append(getUcastPkts());

          sb.append(", multicastPkts: ");
          sb.append(getMulticastPkts());

          sb.append(", broadcastPkts: ");
          sb.append(getBroadcastPkts());

          sb.append(", discards: ");
          sb.append(getDiscards());
          
          return sb.toString();

     }
}
