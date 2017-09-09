package com.sflow.packet.header.flowsample;

import java.net.UnknownHostException;

import com.sflow.util.FlowFilterE;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;
import com.sflow.util.V6Address;

public class Ipv6Header extends MacHeader {
    public static int IGMP = 0x02;
    public static int ICMP = 0x01;
    public static int ICMPv6 = 0x3A;
    
    public static int IPPROTO_HOPOPTS  = 0x0;     /* IPv6 hop-by-hop options      */
    public static int TCP              = 0x06;
    public static int UDP              = 0x11;
   
    public static int IPPROTO_ROUTING  = 0x2B;      /* IPv6 routing header - 43     */
    public static int IPPROTO_FRAGMENT = 0x2C;      /* IPv6 fragmentation header - 44   */
    public static int RSVP             = 0x2E;

    public static int ESP              = 0x32;    /* 50 */
    public static int AUTH_HEADER      = 0x33;

    public static int IPPROTO_ICMPV6   = 0x3A;    /* ICMPv6                       */
    public static int IPPROTO_NONE     = 0x3B;    /* IPv6 no next header          */
    public static int IPPROTO_DSTOPTS  = 0x3C;      /* IPv6 destination options     */
    public static int IPPROTO_MH       = 135;     /* IPv6 mobility header         */


    private int version; /* IPv6 (4) */
    private int tos;     /* IP priority (8) */

    /* Flow Label – Indicates that this packet belongs to a specific sequence of packets 
     * between a source and destination, requiring special handling by intermediate IPv6 routers. 
     * The size of this field is 20 bits. The Flow Label is used for non-default quality of service 
     * connections, such as those needed by real-time data (voice and video). For default router 
     * handling, the Flow Label is set to 0. There can be multiple flows between a source and destination,
     * as distinguished by separate non-zero Flow Labels.
     */
    private int flowLabel; /* Flow label (20 bits) */
    private int payloadLength; /* (16 bits)  */
    /* IP next header (for example, TCP = 0x06, UDP = 0x11,
     * IGMP = 0x02, ICMP 0x1)
     */
    private int nextHeader; /* 8 bits */
    private short hopLimit; /* 8 bits */
    private V6Address srcIp;
    private V6Address destIp;

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

    public Ipv6Header(MacHeader m) {
        this.m = m;
        if (m instanceof TaggedMacHeader) {
            this.setHasVlanTag(true);
        }
        this.m.gotIPV6 = true;

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

    public int getTos() {
        return tos;
    }

    public void setTos(int i) {
        this.tos = i;
    }

    public int getFlowLabel() {
        return flowLabel;
    }

    public void setFlowLabel(int flowLabel) {
        this.flowLabel = flowLabel;
    }

    public int getNextHeader() {
        return nextHeader;
    }

    public void setNextHeader(int nextHeader) {
        this.nextHeader = nextHeader;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(int payloadLength) {
        this.payloadLength = payloadLength;
    }

    public short getHopLimit() {
        return hopLimit;
    }

    public void setHopLimit(short hopLimit) {
        this.hopLimit = hopLimit;
    }

    public V6Address getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(V6Address srcIp) {
        this.srcIp = srcIp;
    }

    public V6Address getDestIp() {
        return destIp;
    }

    public void setDestIp(V6Address destIp) {
        this.destIp = destIp;
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

    public int getTcpFlags() {
        return tcpFlags;
    }

    public void setTcpFlags(int tcpFlags) {
        this.tcpFlags = tcpFlags;
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

    public static Ipv6Header decodeIPLayer4(Ipv6Header h) throws UtilityException {
                
        if (h.nextHeader == TCP || h.nextHeader == UDP) {
            h.setSrcPort((Utility.twoBytesToInteger(h.payload, 0)));
            h.setDestPort((Utility.twoBytesToInteger(h.payload, 2)));
            if (h.nextHeader == TCP) {
                h.tcpSeqNumber = Utility.fourBytesToLong(h.payload, 4);
                h.tcpAckNumber = Utility.fourBytesToLong(h.payload, 8);
                h.tcpHeaderLength = (Utility.oneByteToShort(h.payload, 12) >> 4) & 0xF;
                h.setTcpFlags((Utility.oneByteToShort(h.payload, 13) & 0x3F));
                h.tcpWindowSize = (Utility.twoBytesToInteger(h.payload, 14));
                h.tcpChecksum = (Utility.twoBytesToInteger(h.payload, 16));
                h.tcpUrgent = (Utility.twoBytesToInteger(h.payload, 18));
            } else {
                h.udpLen = Utility.twoBytesToInteger(h.payload, 4);
                h.udpChecksum = Utility.twoBytesToInteger(h.payload, 6);
            }
        } else if (h.nextHeader == ICMP) {
            h.icmpType = Utility.oneByteToShort(h.payload, 0);
            h.icmpCode = Utility.oneByteToShort(h.payload, 1);
        } else if (h.nextHeader == ICMPv6) {
            
        }

        return h;
    }

    public static Ipv6Header parse(MacHeader m) throws HeaderParseException {

        Ipv6Header h = new Ipv6Header(m);

        try {

            // version (4 bits)
            h.setVersion((Utility.oneByteToShort(m.offcut, 0) >> 4) & 0xF);

             // Traffic class (8 bits)
            h.setTos( 
                        (Utility.oneByteToShort(m.offcut, 0) & 0xF) + 
                        (Utility.oneByteToShort(m.offcut, 1) >> 4) & 0xF);

            // Flow label (20 bits)
            h.setFlowLabel(
                (Utility.oneByteToShort(m.offcut, 1) & 0xF) +
                (Utility.twoBytesToInteger(m.offcut, 2)));
            
            byte[] buf = new byte[1];
            buf[0] = (byte) ( (m.offcut[0] & 0xF) + ((m.offcut[1] >> 4) & 0xF));     
            h.getFlowFilter().setValue(FlowFilterE.TRAFFIC_CLASS, buf, 0);
        	
        	buf = new byte[4];
        	buf[0] = (byte) (m.offcut[1] & 0xF);
            buf[1] = m.offcut[2];
            buf[2] = m.offcut[3];
            buf[3] = 0x0;
            h.getFlowFilter().setValue(FlowFilterE.FLOW_LABEL, buf, 0);


            h.setPayloadLength(Utility.twoBytesToInteger(m.offcut, 4));
            h.setNextHeader(Utility.oneByteToShort(m.offcut, 6));
            h.setHopLimit(Utility.oneByteToShort(m.offcut, 7));

            h.setSrcIp(new V6Address(m.offcut, 8));
            h.setDestIp(new V6Address(m.offcut, 24)); 
            
            h.getFlowFilter().setValue(FlowFilterE.SIP6, m.offcut, 8);
            h.getFlowFilter().setValue(FlowFilterE.DIP6, m.offcut, 24);
            
            int payloadLen = m.offcut.length - 40;

            // skip past the extension headers.
            // size of this field is 8 bits.
            // When indicating an upper layer protocol above the Internet layer, 
            // the same values used in the IPv4 Protocol field are used here.
            while (h.nextHeader == IPPROTO_HOPOPTS ||
                    h.nextHeader == IPPROTO_ROUTING ||
                    h.nextHeader == IPPROTO_FRAGMENT ||
                    h.nextHeader == AUTH_HEADER ||
                    h.nextHeader == IPPROTO_DSTOPTS) {
                if (payloadLen < 8) {
                    throw new HeaderParseException("Parse error: Extension header length is too small!!");
                }
                h.setNextHeader(Utility.oneByteToShort(m.offcut, 40));
                h.getFlowFilter().setValue(FlowFilterE.NEXT_HEADER, m.offcut, 40);

                /* second byte gives option len in 8-byte chunks, not counting first 8 */
                int optionLen = (Utility.oneByteToShort(m.offcut, 41) + 1) * 8;
                payloadLen = payloadLen - optionLen - 2;
            }
            
            if (payloadLen < 0) {
                return h;
            }
            // payload
            byte payload[] = new byte[payloadLen];
            System.arraycopy(m.offcut, m.offcut.length-payloadLen, 
                    payload, 0, payloadLen);
            h.setPayload(payload);

            return decodeIPLayer4(h);

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

        sb.append("\n------ IPV6 Header -------------");
        sb.append("\n");
        sb.append("Version: ");
        sb.append(getVersion());

        sb.append(", Priority: ");
        sb.append(getTos());
        
        sb.append(",  FlowLabel: ");
        sb.append(getFlowLabel());

        sb.append(",  Payload Length: ");
        sb.append(getPayloadLength());

        sb.append(",  HopLimit: ");
        sb.append(getHopLimit());

        sb.append(", Protocol: ");
        sb.append(getNextHeader());

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

        if (getNextHeader() == TCP) {

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

        } else if (getNextHeader() == ICMP) {
            sb.append("\n------ UDP  Header -------------");

            sb.append("\nSrcPort: ");
            sb.append(getSrcPort());

            sb.append(", DestPort: ");
            sb.append(getDestPort());
            sb.append(", Udp Length: ");
            sb.append(getUdpLen());

            sb.append(", UDPCheckSum: ");
            sb.append(getUdpChecksum());

        } else if (getNextHeader() == ICMP) {

            sb.append("\n------ UDP  Header -------------");

            sb.append("\nIcmpType: ");
            sb.append(getIcmpType());

            sb.append(", IcmpCode: ");
            sb.append(getIcmpCode());
        }

        return sb.toString();
    }
}
