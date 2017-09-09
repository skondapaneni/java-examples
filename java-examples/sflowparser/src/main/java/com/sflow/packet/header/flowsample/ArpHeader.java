package com.sflow.packet.header.flowsample;

import com.sflow.util.Address;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.MacAddress;
import com.sflow.util.Utility;

public class ArpHeader extends MacHeader {
     private MacHeader     m;

     private int           hwType;        /* Ethernet (0x0001) */
     private int           protocolType;        /* IP (0x0800) */

     private int           hwSize;  /* length of the hw adress in bytes */
     private int           protocolSize;  /* length of the protocol adress in bytes */

     private int           opcode;        /* ARP_REQ(1), ARP_REPLY(2), RARP_REQ(3), RARP_REPLY(4) */
     private MacAddress    sender; /* senderMac  */
     private Address       senderProto;
     private MacAddress    target; /* targetMac  */
     private Address       targetProto;
     
     
     public enum Opcode {
          ARP_REQ(1), 
          ARP_REPLY(2), 
          RARP_REQ(3), 
          RARP_REPLY(4),
          UNKNOWN(5);
          
          private int index;
          
          Opcode (int index) {
               this.index = index;
          }
          
         public static Opcode fromOrdinal(int n) {
               switch(n) {
               case 1 :
                    return ARP_REQ;
               case 2 :
                    return ARP_REPLY;
               case 3 :
                    return RARP_REQ;
               case 4 :
                    return RARP_REPLY;
               }
               return UNKNOWN;
         }

          public String toString() {
               switch(index) {
               case 1 :
                    return "1 (ARP_REQ)";
               case 2 :
                    return "2 (ARP_REPLY)";
               case 3 :
                    return "3 (RARP_REQ)";
               case 4 :
                    return "4 (RARP_REPLY)";
               }
               return "";
          }
     }

     
     public ArpHeader(MacHeader m) {          
          this.m = m;     
     }
     
     public int getHwType() {
          return hwType;
     }

     public void setHwType(int hwType) {
          this.hwType = hwType;
     }

     public int getProtocolType() {
          return protocolType;
     }

     public void setProtocolType(int protocolType) {
          this.protocolType = protocolType;
     }

     public int getHwSize() {
          return hwSize;
     }

     public void setHwSize(int hwSize) {
          this.hwSize = hwSize;
     }

     public int getProtocolSize() {
          return protocolSize;
     }

     public void setProtocolSize(int protocolSize) {
          this.protocolSize = protocolSize;
     }

     public int getOpcode() {
          return opcode;
     }

     public void setOpcode(int opcode) {
          this.opcode = opcode;
     }

     public MacAddress getSender() {
          return sender;
     }

     public void setSender(MacAddress sender) {
          this.sender = sender;
     }

     public Address getSenderProto() {
          return senderProto;
     }

     public void setSenderProto(Address senderProto) {
          this.senderProto = senderProto;
     }

     public MacAddress getTarget() {
          return target;
     }

     public void setTarget(MacAddress target) {
          this.target = target;
     }

     public Address getTargetProto() {
          return targetProto;
     }

     public void setTargetProto(Address targetProto) {
          this.targetProto = targetProto;
     }

     public MacHeader getMacHeader() {
          return m;
     }

     public void setMacHeader(MacHeader m) {
          this.m = m;
     }

     public static ArpHeader parse(MacHeader m) throws HeaderParseException {
          
          ArpHeader h = new ArpHeader(m);
                    
          try {
               if (m.offcut.length < 28) {
                    throw new HeaderParseException("Data array too short for IP packet.");
               }
               
               // hwType
               byte hwType[] = new byte[2];
               System.arraycopy(m.offcut, 0, hwType, 0, 2);          
               h.setHwType(Utility.twoBytesToInteger(hwType));
               
               // protocolType
               byte protocolType[] = new byte[2];
               System.arraycopy(m.offcut, 2, protocolType, 0, 2);          
               h.setProtocolType(Utility.twoBytesToInteger(protocolType));

               // hwSize
               byte hwSize[] = new byte[1];
               System.arraycopy(m.offcut, 4, hwSize, 0, 1);          
               h.setHwSize(Utility.oneByteToShort(hwSize[0]));

               byte protoSize[] = new byte[1];
               System.arraycopy(m.offcut, 5, protoSize, 0, 1);          
               h.setProtocolSize(Utility.oneByteToShort(protoSize[0]));
               
               // opcode
               byte opcode[] = new byte[2];
               System.arraycopy(m.offcut, 6, opcode, 0, 2);          
               h.setOpcode(Utility.twoBytesToInteger(opcode));
               
               // sender
               if (h.hwType == 1 && h.protocolType == 0x0800) { // ethernet && IP
                    
                   byte sender[] = new byte[6];
                   System.arraycopy(m.offcut, 8, sender, 0, 6);
                   h.setSender(new MacAddress(sender));
               
                   byte senderProto[] = new byte[4];
                   System.arraycopy(m.offcut, 14, senderProto, 0, 4);
                   h.setSenderProto(new Address(senderProto));
               
                   byte target[] = new byte[6];

                   System.arraycopy(m.offcut, 18, target, 0, 6);
                   h.setTarget(new MacAddress(target));
                                
                   byte targetProto[] = new byte[4];
                   System.arraycopy(m.offcut, 24, targetProto, 0, 4);
                   h.setTargetProto(new Address(targetProto));               
               }
               
               return h;
          } catch (Exception e) {
               throw new HeaderParseException("ArpHeader::Parse error: " + e.getMessage());
          }          
     }
     
     public byte[] getBytes() throws HeaderBytesException {          
          return m.getBytes();
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append(m.toString());          
          sb.append("\n---- ARP/RARP Frame -------");
          sb.append("\nHardware type: ");
          sb.append((getHwType() == 1) ? "Ethernet (1)" : getHwType());
          sb.append("\nProtocol type: ");
          sb.append( (getProtocolType() == 0x0800) ? "0x0800 (IP)" : 
               ("0x0" + Integer.toHexString(getProtocolType()) ));
          sb.append("\nHardware size: " + getHwSize() + " bytes");
          sb.append("\nProtocol size: " + getProtocolSize() + " bytes");
          sb.append("\nOpcode " + Opcode.fromOrdinal(getOpcode()).toString());
          sb.append("\nSender MAC address: " + getSender());
          sb.append("\nSender IP address: " + getSenderProto());
          sb.append("\nTarget MAC address: " + getTarget());
          sb.append("\nTarget IP address: " + getTargetProto());     
          
          sb.append("\n\n");

          return sb.toString();
     }
}
