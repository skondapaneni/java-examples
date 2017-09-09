/*
 * This file is adding to jsFlow.
 *
 */
package com.sflow.packet.header.flowsample;


import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;
import com.sflow.util.MacAddress;

public class EthernetFlowRecord {

      protected long ethLen;
      protected MacAddress source; // sourceMac
      protected MacAddress destination; // destinationMac
      protected long type;  // PacketType

      public MacAddress getDestination() {
            return destination;
      }

      public MacAddress getSource() {
            return source;
      }

      public long getEthernetLength() {
            return ethLen;
      }

      public long getType() {
            return type;
      }

      public void setDestination(MacAddress destination) {
            this.destination = destination;
      }

      public void setSource(MacAddress source) {
            this.source = source;
      }

      public void setType(long type) {
            this.type = type;
      }

      public void setEthernetLength(long length) {
            this.ethLen = length;
      }
      
      public static EthernetFlowRecord parse(byte data[]) throws HeaderParseException {
            
            EthernetFlowRecord        m = null;            
            try {
                  m = new EthernetFlowRecord();
                  
              m.setEthernetLength(Utility.fourBytesToLong(data, 0));                        
              // source
              m.setSource(new MacAddress(data, 4));
              // destination
              m.setDestination(new MacAddress(data, 10));
              m.setType(Utility.fourBytesToLong(data, 16));
      
              return m;

            } catch (Exception e) {
                  throw new HeaderParseException("Parse error: " + e.getMessage());
            }            
      }
      
      public byte[] getBytes() throws HeaderBytesException {
            try {
                  byte[] data = new byte[20];
                  // ethLen
                  System.arraycopy(Utility.longToFourBytes(ethLen), 0, data, 0, 4);
                  // source
                  System.arraycopy(source, 0, data, 4, 6);
                  // destination
                  System.arraycopy(destination, 0, data, 10, 6);
                  // type
                  System.arraycopy(Utility.longToFourBytes(type), 0, data, 16, 4);
                  
                  return data;
            } catch (Exception e) {
                  throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
            }
      }

      public String toString() {
            StringBuilder sb = new StringBuilder();
            
            sb.append("\n ---- Ether Header -----");

            sb.append(", EthLen: 0x0");
            sb.append(Long.toHexString(getEthernetLength()));
            
            sb.append(", Src: ");
            sb.append(getSource());

            sb.append(", Dst: ");
            sb.append(getDestination());

            sb.append(", EthType: 0x0");
            sb.append(Long.toHexString(getType()));
            
            return sb.toString();
      }
}
