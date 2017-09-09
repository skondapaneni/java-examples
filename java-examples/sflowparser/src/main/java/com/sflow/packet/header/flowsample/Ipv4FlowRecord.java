package com.sflow.packet.header.flowsample;

import java.net.UnknownHostException;

import com.sflow.util.Address;
import com.sflow.util.FlowFilter;
import com.sflow.util.FlowFilterE;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;

public class Ipv4FlowRecord {
     private long      length;    /* The length of the IP packet excluding
                                   * lower layer encapsulations.
                                   */
     private long      protocol;  /* IP protocol type
                                   * (for example, TCP = 0x06, UDP = 0x11, IGMP = 0x02, ICMP 0x1) 
                                   */
     private Address   srcIp;
     private Address   destIp;
     
     private long      srcPort;     /* TCP/UDP source port number or equivalent */
     private long      destPort;    /* TCP/UDP destination port number or equivalent */
     private long      tcpFlags;    /* TCP flags */
     private long      tos;         /* IP priority */

     
     public static int  TCP         = 0x06;
     public static int  UDP         = 0x11;
     public static int  IGMP        = 0x02;
     public static int  ICMP        = 0x01;

     protected FlowFilter flowFilter;

     public Ipv4FlowRecord() {
    	 flowFilter = new FlowFilter();
     }
     
     public long getLength() {
          return length;
     }

     public void setLength(long length) {
          this.length = length;
     }

     public long getProtocol() {
          return protocol;
     }

     public void setProtocol(long protocol) {
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

     public long getTcpFlags() {
          return tcpFlags;
     }

     public void setTcpFlags(long tcpFlags) {
          this.tcpFlags = tcpFlags;
     }

     public long getTos() {
          return tos;
     }

     public void setTos(long tos) {
          this.tos = tos;
     }

     public FlowFilter getFlowFilter() {
		return flowFilter;
	}

	public void setFlowFilter(FlowFilter flowFilter) {
		this.flowFilter = flowFilter;
	}

	public static Ipv4FlowRecord parse(byte data[]) throws HeaderParseException {
          
          Ipv4FlowRecord h = new Ipv4FlowRecord();
          
          try {
               h.setLength(Utility.fourBytesToLong(data, 0)); 
               h.setProtocol(Utility.fourBytesToLong(data, 4)); 
               h.setSrcIp(new Address(data[8], data[9], data[10], data[11]));
               h.setDestIp(new Address(data[12], data[13], data[14], data[15]));
               h.setSrcPort(Utility.fourBytesToLong(data, 16)); 
               h.setDestPort(Utility.fourBytesToLong(data, 20)); 
               h.setTcpFlags(Utility.fourBytesToLong(data, 24)); 
               h.setTos(Utility.fourBytesToLong(data, 28)); 
               
               h.flowFilter.setValue(FlowFilterE.SIP, data, 8);
               h.flowFilter.setValue(FlowFilterE.DIP, data, 12);
               h.flowFilter.setValue(FlowFilterE.PROTO, data, 4);
               
               h.flowFilter.setValue(FlowFilterE.SP, data, 16);
               h.flowFilter.setValue(FlowFilterE.DP, data, 20);
               h.flowFilter.setValue(FlowFilterE.TCP_FLAGS, data, 24);


               
               return h;
          } catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }          
     }
     
     public byte[] getBytes() throws HeaderBytesException {          
          // ToDo
          return null;
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[Ipv4 FlowRecord]: ");

          sb.append(", Length: ");
          sb.append(getLength());
          
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
          
          sb.append(", SrcPort: ");
          sb.append(getSrcPort());
          
          sb.append(", DestPort: ");
          sb.append(getDestPort());

          sb.append(", TCPFlags: ");
          sb.append(getTcpFlags());

          sb.append(", TOS: ");
          sb.append(getTos());
          
          return sb.toString();
     }
}
