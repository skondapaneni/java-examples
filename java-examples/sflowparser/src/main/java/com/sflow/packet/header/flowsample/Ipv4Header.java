package com.sflow.packet.header.flowsample;

import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sflow.util.Address;
import com.sflow.util.FlowFilter;
import com.sflow.util.FlowFilterE;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;

public class Ipv4Header extends MacHeader {
     private int version; /* IPv4 or IPv6 */
     private int headerLength; /*
                                    * The Header Length field is given in units of
                                    * 4-byte words. A header length of “5” is therefore
                                    * equal to 20 bytes. This also imposes a maximum
                                    * length on the IPv4 header: 0xF 4-byte words, or
                                    * 60 bytes
                                    */
     private short tos; /* IP priority */
     private int length; /*
                               * The length of the IP packet excluding lower layer
                               * encapsulations. This is the length of the entire IPv4
                               * header and payload. This is why padding does not
                               * necessarily have to be stripped from a received
                               * Ethernet frame.
                               */
     private int identification; /*
                                         * An assigned number which distinguishes this
                                         * IP packet from other received packets. This
                                         * value is used to help assemble received IP
                                         * packets that have been fragmented
                                         */
     private short frag_off;
     private short ttl;

 	
     public static int TCP = 0x06;
     public static int UDP = 0x11;
     public static int IGMP = 0x02;
     public static int ICMP = 0x01;

     private int protocol; /*
                               * IP next header (for example, TCP = 0x06, UDP = 0x11,
                               * IGMP = 0x02, ICMP 0x1)
                               */
     private Address srcIp;
     private Address destIp;

     private long srcPort; /* TCP/UDP source port number or equivalent (16) */
     private long destPort; /* TCP/UDP destination port number or equivalent(16) */

     private long tcpSeqNumber; /* TCP sequence number (32 bits) */
     private long tcpAckNumber; /* acknowledgement number (32 bits) */

     private int tcpHeaderLength; /* 4 bits, specified in 4 byte quantitites */
     private short tcpReserved; /* 6 bits */
     private int tcpFlags; /* TCP flags (6) */

     private int tcpWindowSize; /* window size (16) */
     private int tcpChecksum; /* checksum (16) */
     private int tcpUrgent; /* urgent pointer (16) */

     private int udpLen;
     private int udpChecksum;

     private short icmpType; /* message type (8 bits) */
     private short icmpCode; /* type sub-code (8 bits) */

     private boolean hasVlanTag;
     protected byte payload[];
     MacHeader m;
     
     private static final Logger log = LogManager.getLogger(Ipv4Header.class.getName());

     public Ipv4Header(MacHeader m) {
          this.m = m;
          if (m instanceof TaggedMacHeader) {
               this.setHasVlanTag(true);
          }
          this.m.gotIPV4 = true;
     }

     public MacHeader getMacHeader() {
          return m;
     }

     public int getVersion() {
          return version;
     }

     public void setVersion(int version) {
          this.version = version;
     }

     public int getHeaderLength() {
          return headerLength;
     }

     public void setHeaderLength(int headerLength) {
          this.headerLength = headerLength;
     }

     public int getLength() {
          return length;
     }

     public void setLength(int length) {
          this.length = length;
     }

     public int getIdentification() {
          return identification;
     }

     public void setIdentification(int identification) {
          this.identification = identification;
     }

     public int getProtocol() {
          return protocol;
     }

     public void setProtocol(int protocol) {
          this.protocol = protocol;
     }

     public Address getSrcIp() {
          return srcIp;
     }

     public void setSrcIp(Address srcIp) {
          this.srcIp = srcIp;
     }

     public Address getDestIp() {
          return destIp;
     }

     public void setDestIp(Address destIp) {
          this.destIp = destIp;
     }

     public long getSrcPort() {
          return srcPort;
     }

     public void setSrcPort(long srcPort) {
          this.srcPort = srcPort;
     }

     public long getDestPort() {
          return destPort;
     }

     public void setDestPort(long destPort) {
          this.destPort = destPort;
     }

     public int getTcpFlags() {
          return tcpFlags;
     }

     public void setTcpFlags(int tcpFlags) {
          this.tcpFlags = tcpFlags;
     }

     public short getTos() {
          return tos;
     }

     public void setTos(short tos) {
          this.tos = tos;
     }

     public short getTtl() {
          return ttl;
     }

     public void setTtl(short ttl) {
          this.ttl = ttl;
     }

     public short getFrag_off() {
          return frag_off;
     }

     public void setFrag_off(short frag_off) {
          this.frag_off = frag_off;
     }

     public boolean hasVlanTag() {
          return hasVlanTag;
     }

     public void setHasVlanTag(boolean hasVlanTag) {
          this.hasVlanTag = hasVlanTag;
     }

     public byte[] getPayload() {
          return payload;
     }

     public void setPayload(byte[] payload) {
          this.payload = payload;
     }

     public long getTcpSeqNumber() {
          return tcpSeqNumber;
     }

     public void setTcpSeqNumber(long tcpSeqNumber) {
          this.tcpSeqNumber = tcpSeqNumber;
     }

     public long getTcpAckNumber() {
          return tcpAckNumber;
     }

     public void setTcpAckNumber(long tcpAckNumber) {
          this.tcpAckNumber = tcpAckNumber;
     }

     public int getTcpHeaderLength() {
          return tcpHeaderLength;
     }

     public void setTcpHeaderLength(int tcpHeaderLength) {
          this.tcpHeaderLength = tcpHeaderLength;
     }

     public short getTcpReserved() {
          return tcpReserved;
     }

     public void setTcpReserved(short tcpReserved) {
          this.tcpReserved = tcpReserved;
     }

     public int getTcpWindowSize() {
          return tcpWindowSize;
     }

     public void setTcpWindowSize(int tcpWindowSize) {
          this.tcpWindowSize = tcpWindowSize;
     }

     public int getTcpChecksum() {
          return tcpChecksum;
     }

     public void setTcpChecksum(int tcpChecksum) {
          this.tcpChecksum = tcpChecksum;
     }

     public int getTcpUrgent() {
          return tcpUrgent;
     }

     public void setTcpUrgent(int tcpUrgent) {
          this.tcpUrgent = tcpUrgent;
     }

     public int getUdpLen() {
          return udpLen;
     }

     public void setUdpLen(int udpLen) {
          this.udpLen = udpLen;
     }

     public int getUdpChecksum() {
          return udpChecksum;
     }

     public void setUdpChecksum(int udpChecksum) {
          this.udpChecksum = udpChecksum;
     }

     public short getIcmpType() {
          return icmpType;
     }

     public void setIcmpType(short icmpType) {
          this.icmpType = icmpType;
     }

     public short getIcmpCode() {
          return icmpCode;
     }

     public void setIcmpCode(short icmpCode) {
          this.icmpCode = icmpCode;
     }

     public static Ipv4Header decodeIPLayer3(Ipv4Header h) throws UtilityException {

          if (h.protocol == TCP || h.protocol == UDP) {
               h.srcPort = (Utility.twoBytesToInteger(h.payload, 0));
               h.destPort = (Utility.twoBytesToInteger(h.payload, 2));
               h.getFlowFilter().setValue(FlowFilterE.SP, h.payload, 0);
               h.getFlowFilter().setValue(FlowFilterE.DP, h.payload, 2);

               if (h.protocol == TCP) {
                    h.tcpSeqNumber = Utility.fourBytesToLong(h.payload, 4);
                    h.tcpAckNumber = Utility.fourBytesToLong(h.payload, 8);
                    h.tcpHeaderLength = (Utility.oneByteToShort(h.payload, 12) >> 4) & 0xF;
                    h.tcpFlags = (Utility.oneByteToShort(h.payload, 13) & 0x3F);
                    h.tcpWindowSize = (Utility.twoBytesToInteger(h.payload, 14));
                    h.tcpChecksum = (Utility.twoBytesToInteger(h.payload, 16));
                    h.tcpUrgent = (Utility.twoBytesToInteger(h.payload, 18));
                    h.getFlowFilter().setValue(FlowFilterE.TCP_FLAGS, h.payload, 13, 0x3F);
               } else {
                    h.udpLen = Utility.twoBytesToInteger(h.payload, 4);
                    h.udpChecksum = Utility.twoBytesToInteger(h.payload, 6);
               }
          } else if (h.protocol == ICMP) {
               h.icmpType = Utility.oneByteToShort(h.payload, 0);
               h.icmpCode = Utility.oneByteToShort(h.payload, 1);
               h.getFlowFilter().setValue(FlowFilterE.ICMP_TYPE, h.payload, 0);
               h.getFlowFilter().setValue(FlowFilterE.ICMP_CODE, h.payload, 1);
          }

          return h;
     }

     public FlowFilter getFlowFilter() {
		return m.flowFilter;
	 }
     
     public static Ipv4Header parse(MacHeader m) throws HeaderParseException {

          Ipv4Header h = new Ipv4Header(m);

          try {
               if (m.offcut.length < 20) {
                    throw new HeaderParseException(
                              "Data array too short for IP packet.");
               }

               h.setVersion((Utility.oneByteToShort(m.offcut, 0)  >> 4) & 0xF); 
               h.setHeaderLength((Utility.oneByteToShort(m.offcut, 0) & 0xF) * 4); 
               h.setTos(Utility.oneByteToShort(m.offcut, 1));
               h.setLength(Utility.twoBytesToInteger(m.offcut, 2));
               h.setIdentification(Utility.twoBytesToInteger(m.offcut, 4));
            	
               // skip next 2 bytes (unused 1bit, DF, 1bit, MF 1bit, fragment
               // offset 13 bits)
               h.ttl = Utility.oneByteToShort(m.offcut, 8);
               h.setProtocol(Utility.oneByteToShort(m.offcut, 9));

               // skip header checksum (2 bytes)
               h.setSrcIp(new Address(m.offcut[12], m.offcut[13], m.offcut[14], m.offcut[15]));
               h.setDestIp(new Address(m.offcut[16], m.offcut[17], m.offcut[18], m.offcut[19]));

               m.flowFilter.setValue(FlowFilterE.SIP, m.offcut, 12);
               m.flowFilter.setValue(FlowFilterE.DIP, m.offcut, 16);
               m.flowFilter.setValue(FlowFilterE.PROTO, m.offcut, 9);
        	
               // payload
               byte payload[] = new byte[m.offcut.length - 20];
               System.arraycopy(m.offcut, 20, payload, 0, m.offcut.length - 20);
               h.setPayload(payload);

               return decodeIPLayer3(h);

          } catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }

     public byte[] getBytes() throws HeaderBytesException {
          return m.getBytes();
     }

     public String toString() {
          StringBuilder sb = new StringBuilder();
          if (hasVlanTag()) {
               sb.append("[TaggedMacHeader]: ");
          } else {
               sb.append("[MacHeader]: ");
          }
          sb.append(m.toString());

          sb.append("\n------ IP Header -------------");
          sb.append("\n");
          sb.append("Version: ");
          sb.append(getVersion());

          sb.append(",  Header Length: ");
          sb.append(getHeaderLength());

          sb.append(", Type of service: ");
          sb.append(getTos());

          sb.append(",  Total Length: ");
          sb.append(getLength());

          sb.append(",  Identification: ");
          sb.append(getIdentification());

          sb.append(", Time to live: ");
          sb.append(getTtl());

          sb.append(", Protocol: ");
          sb.append(getProtocol());

          sb.append(", SrcIp: ");
          try {
               sb.append(getSrcIp().getInetAddress());
          } catch (UnknownHostException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }

          sb.append(", DestIp: ");
          try {
               sb.append(getDestIp().getInetAddress());
          } catch (UnknownHostException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }

          if (protocol == TCP) {

               sb.append("\n------ TCP Header -------------");

               sb.append("\nSrcPort: ");
               sb.append(getSrcPort());

               sb.append(", DestPort: ");
               sb.append(getDestPort());

               sb.append(", TCPSeqNum: ");
               sb.append(getTcpSeqNumber());

               sb.append(", TCPAckNum: ");
               sb.append(getTcpAckNumber());

               sb.append(", TCPHeaderLength: ");
               sb.append(getTcpHeaderLength());

               sb.append(", TCPFlags: ");
               sb.append(getTcpFlags());

               sb.append(", TCPWindowSize: ");
               sb.append(getTcpWindowSize());

               sb.append(", TCPCheckSum: ");
               sb.append(getTcpChecksum());

               sb.append(", TCPUrgent: ");
               sb.append(getTcpUrgent());

          } else if (protocol == ICMP) {
               sb.append("\n------ UDP  Header -------------");

               sb.append("\nSrcPort: ");
               sb.append(getSrcPort());

               sb.append(", DestPort: ");
               sb.append(getDestPort());
               sb.append(", Udp Length: ");
               sb.append(getUdpLen());

               sb.append(", UDPCheckSum: ");
               sb.append(getUdpChecksum());

          } else if (protocol == ICMP) {

               sb.append("\n------ UDP  Header -------------");

               sb.append("\nIcmpType: ");
               sb.append(getIcmpType());

               sb.append(", IcmpCode: ");
               sb.append(getIcmpCode());
          }

          return sb.toString();
     }
}
