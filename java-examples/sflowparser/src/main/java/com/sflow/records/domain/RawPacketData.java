package com.sflow.records.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RawPacketData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2130526069689545275L;

	@JsonProperty("ipVersionAgent")
	private int ipVersionAgent; // 1=v4, 2=v6

	@JsonProperty("ipAddress")
	private String ipAddress;	

	@JsonProperty("bucket")
	private int bucket; 

	@JsonProperty("subAgentId")
	private long subAgentID;

	@JsonProperty("sourceIDType")
	private long sourceIDType;    /* sFlowDataSource */

	@JsonProperty("sourceIDIndex")
	private long sourceIDIndex;   /* sFlowDataSource */

	@JsonProperty("sourceIDName")
	private String sourceIDName;   /* sFlowDataSource */

	@JsonProperty("flowDataFormat")  /* assuming enterprise=0 */
	private long flowDataFormat;

	@JsonProperty("timestamp")    
	private Date eventTime;

	@JsonProperty("sampleSequenceNumber")
	private long sampleSequenceNumber;   /* sample sequence number */

	@JsonProperty("seqNumber")
	private long seqNumber;

	@JsonProperty("sysUptime")
	private long sysUptime; // in milliseconds

	@JsonProperty("samplingRate")
	private long samplingRate;

	@JsonProperty("samplePool")
	private long samplePool;

	@JsonProperty("drops")
	private long drops;

	@JsonProperty("inInterface")
	private long inInterface;

	@JsonProperty("inInterfaceName")
	private String inInterfaceName;

	@JsonProperty("outInterface")
	private long outInterface;

	@JsonProperty("outInterfaceName")
	private String outInterfaceName;

	@JsonProperty("inInterfaceFormat")
	private int inInterfaceFormat;

	@JsonProperty("outInterfaceFormat")
	private int outInterfaceFormat;

	@JsonProperty("headerProtocol")
	private long headerProtocol;

	@JsonProperty("frameLength")
	private long frameLength;

	@JsonProperty("stripped")
	private long stripped;

	@JsonProperty("srcMac")
	private String srcMac;

	@JsonProperty("destMac")
	private String destMac;

	@JsonProperty("packetType")
	private int packetType;

	@JsonProperty("frameType")
	private String frameType;

	@JsonProperty("tagProtocolId")
	private int tagProtocolId;

	@JsonProperty("qos")
	private int qos;

	@JsonProperty("vlanId")
	private int vlanId;

	@JsonProperty("version")
	private int version;

	@JsonProperty("neighborMac")
	private String 	neighborMac;

	@JsonProperty("sysName")
	private String sysName;

	@JsonProperty("linkName")
	private String linkName;

	@JsonProperty("lldpTtl")
	private int lldpTtl;

	@JsonProperty("headerLength")
	private int headerLength;

	@JsonProperty("tos")
	private int tos;

	@JsonProperty("ipv4PacketLen")
	private int ipv4PacketLen;

	@JsonProperty("identification")
	private int identification;

	@JsonProperty("fragOff")
	private int fragOff;

	@JsonProperty("ttl")
	private int ttl;

	@JsonProperty("l3Protocol")
	private int l3Protocol;

	@JsonProperty("srcIp")
	private String srcIp;

	@JsonProperty("destIp")
	private String destIp;

	@JsonProperty("srcPort")
	private long srcPort;

	@JsonProperty("destPort")
	private long destPort;

	@JsonProperty("tcpSeq")
	private long tcpSeq;

	@JsonProperty("tcpAckNumber")
	private long tcpAckNumber;

	@JsonProperty("tcpHeaderLen")
	private int tcpHeaderLen;

	@JsonProperty("tcpReservedBits")
	private int tcpReservedBits;

	@JsonProperty("tcpFlags")
	private int tcpFlags;

	@JsonProperty("tcpWindowSize")
	private int tcpWindowSize;

	@JsonProperty("tcpChecksum")
	private int tcpChecksum;

	@JsonProperty("tcpUrgent")
	private int tcpUrgent;

	@JsonProperty("udpLen")
	private int udpLen;

	@JsonProperty("udpChecksum")
	private int udpChecksum;

	@JsonProperty("icmpType")
	private int icmpType;

	@JsonProperty("icmpCode")
	private int icmpCode;

	@JsonProperty("flowLabel")
	private int flowLabel;

	@JsonProperty("payloadLen")
	private int payloadLen;

	@JsonProperty("nextHeader")
	private int nextHeader;

	@JsonProperty("hopLimit")
	private int hopLimit;

	@JsonProperty("arpHwType")
	private int hwType;        /* Ethernet (0x0001) */

	@JsonProperty("arpProtocolType")
	private int protocolType;        /* IP (0x0800) */

	@JsonProperty("arpHwAddrSize")
	private int hwAddrSize;  /* length of the hw adress in bytes */

	@JsonProperty("arpProtocolSize")
	private int	protocolSize;  /* length of the protocol adress in bytes */

	@JsonProperty("arpOpcode")
	private int	opcode;        /* ARP_REQ(1), ARP_REPLY(2), RARP_REQ(3), RARP_REPLY(4) */

	@JsonProperty("arpSenderMac")
	private String	senderMac; /* senderMac  */

	@JsonProperty("arpSenderIpAddr")
	private String	senderIpAddr;

	@JsonProperty("arpTargetMac")
	private String	targetMac; /* targetMac  */

	@JsonProperty("arpTargetIpAddr")	
	private String	targetIpAddr;

	@JsonProperty("switchSrcVlan")
	private long 	srcVlan; /* The 802.1Q VLAN id of incomming frame */

	@JsonProperty("switchSrcPriority")
	private long 	srcPriority;   /* The 802.1p priority */

	@JsonProperty("switchDestVlan")
	private long 	destVlan; /* The 802.1Q VLAN id of outgoing frame */

	@JsonProperty("switchDestPriority")
	private long 	destPriority;  /* The 802.1p priority */
	
	@JsonProperty("ifUtilization")
	private double ifUtilization;
		
	@JsonProperty("ifFrameRate")
	private double ifFrameRate;
	
	@JsonProperty("ifBitRate")
	private double ifBitRate;
	
	public int getIpVersionAgent() {
		return ipVersionAgent;
	}

	public void setIpVersionAgent(int ipVersionAgent) {
		this.ipVersionAgent = ipVersionAgent;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getSubAgentID() {
		return subAgentID;
	}

	public void setSubAgentID(long subAgentID) {
		this.subAgentID = subAgentID;
	}

	public long getSourceIDType() {
		return sourceIDType;
	}

	public void setSourceIDType(long sourceIDType) {
		this.sourceIDType = sourceIDType;
	}

	public long getSourceIDIndex() {
		return sourceIDIndex;
	}

	public void setSourceIDIndex(long sourceIDIndex) {
		this.sourceIDIndex = sourceIDIndex;
	}

	public int getBucket() {
		return bucket;
	}

	public void setBucket(int bucket) {
		this.bucket = bucket;
	}

	public long getSampleSequenceNumber() {
		return sampleSequenceNumber;
	}

	public void setSampleSequenceNumber(long sampleSequenceNumber) {
		this.sampleSequenceNumber = sampleSequenceNumber;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
		if(eventTime != null)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(eventTime);
		}
	}

	public long getSysUptime() {
		return sysUptime;
	}

	public void setSysUptime(long sysUptime) {
		this.sysUptime = sysUptime;
	}

	public long getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(long seqNumber) {
		this.seqNumber = seqNumber;
	}

	public long getSamplingRate() {
		return samplingRate;
	}

	public void setSamplingRate(long samplingRate) {
		this.samplingRate = samplingRate;
	}

	public long getSamplePool() {
		return samplePool;
	}

	public void setSamplePool(long samplePool) {
		this.samplePool = samplePool;
	}

	public long getDrops() {
		return drops;
	}

	public void setDrops(long drops) {
		this.drops = drops;
	}

	public long getInInterface() {
		return inInterface;
	}

	public void setInInterface(long inInterface) {
		this.inInterface = inInterface;
	}

	public long getOutInterface() {
		return outInterface;
	}

	public void setOutInterface(long outInterface) {
		this.outInterface = outInterface;
	}

	public int getInInterfaceFormat() {
		return inInterfaceFormat;
	}

	public void setInInterfaceFormat(int l) {
		this.inInterfaceFormat = l;
	}

	public int getOutInterfaceFormat() {
		return outInterfaceFormat;
	}

	public void setOutInterfaceFormat(int outInterfaceFormat) {
		this.outInterfaceFormat = outInterfaceFormat;
	}

	public long getHeaderProtocol() {
		return headerProtocol;
	}

	public void setHeaderProtocol(long headerProtocol) {
		this.headerProtocol = headerProtocol;
	}

	public long getFrameLength() {
		return frameLength;
	}

	public void setFrameLength(long frameLength) {
		this.frameLength = frameLength;
		if (frameLength == 0) {
			Exception e = new Exception("Frame Length is 0");
			e.printStackTrace();
		}
	}

	public long getStripped() {
		return stripped;
	}

	public void setStripped(long stripped) {
		this.stripped = stripped;
	}

	public String getSrcMac() {
		return srcMac;
	}

	public void setSrcMac(String srcMac) {
		this.srcMac = srcMac;
	}

	public String getDestMac() {
		return destMac;
	}

	public void setDestMac(String destMac) {
		this.destMac = destMac;
	}

	public int getPacketType() {
		return packetType;
	}

	public void setPacketType(int packetType) {
		this.packetType = packetType;
		if (packetType == 0) {
			System.out.println(new Exception().fillInStackTrace().toString());
		}
	}

	public String getFrameType() {
		return frameType;
	}

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}

	public int getTagProtocolId() {
		return tagProtocolId;
	}

	public void setTagProtocolId(int tagProtocolId) {
		this.tagProtocolId = tagProtocolId;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public int getVlanId() {
		return vlanId;
	}

	public void setVlanId(int vlanId) {
		this.vlanId = vlanId;
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

	public int getTos() {
		return tos;
	}

	public void setTos(int tos) {
		this.tos = tos;
	}

	public String getNeighborMac() {
		return neighborMac;
	}

	public void setNeighborMac(String neighborMac) {
		this.neighborMac = neighborMac;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public int getLldpTtl() {
		return lldpTtl;
	}

	public void setLldpTtl(int lldpTtl) {
		this.lldpTtl = lldpTtl;
	}

	public int getIpv4PacketLen() {
		return ipv4PacketLen;
	}

	public void setIpv4PacketLen(int ipv4PacketLen) {
		this.ipv4PacketLen = ipv4PacketLen;
	}

	public int getIdentification() {
		return identification;
	}

	public void setIdentification(int identification) {
		this.identification = identification;
	}

	public int getFragOff() {
		return fragOff;
	}

	public void setFragOff(int fragOff) {
		this.fragOff = fragOff;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getL3Protocol() {
		return l3Protocol;
	}

	public void setL3Protocol(int l3Protocol) {
		this.l3Protocol = l3Protocol;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public String getDestIp() {
		return destIp;
	}

	public void setDestIp(String destIp) {
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

	public long getTcpSeq() {
		return tcpSeq;
	}

	public void setTcpSeq(long tcpSeq) {
		this.tcpSeq = tcpSeq;
	}

	public long getTcpAckNumber() {
		return tcpAckNumber;
	}

	public void setTcpAckNumber(long tcpAckNumber) {
		this.tcpAckNumber = tcpAckNumber;
	}

	public int getTcpHeaderLen() {
		return tcpHeaderLen;
	}

	public void setTcpHeaderLen(int tcpHeaderLen) {
		this.tcpHeaderLen = tcpHeaderLen;
	}

	public int getTcpReservedBits() {
		return tcpReservedBits;
	}

	public void setTcpReservedBits(int tcpReservedBits) {
		this.tcpReservedBits = tcpReservedBits;
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

	public int getIcmpType() {
		return icmpType;
	}

	public void setIcmpType(int icmpType) {
		this.icmpType = icmpType;
	}

	public int getIcmpCode() {
		return icmpCode;
	}

	public void setIcmpCode(int icmpCode) {
		this.icmpCode = icmpCode;
	}

	public int getFlowLabel() {
		return flowLabel;
	}

	public void setFlowLabel(int flowLabel) {
		this.flowLabel = flowLabel;
	}

	public int getPayloadLen() {
		return payloadLen;
	}

	public void setPayloadLen(int payloadLen) {
		this.payloadLen = payloadLen;
	}

	public int getNextHeader() {
		return nextHeader;
	}

	public void setNextHeader(int nextHeader) {
		this.nextHeader = nextHeader;
	}

	public int getHopLimit() {
		return hopLimit;
	}

	public void setHopLimit(int hopLimit) {
		this.hopLimit = hopLimit;
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

	public int getHwAddrSize() {
		return hwAddrSize;
	}

	public void setHwAddrSize(int hwAddrSize) {
		this.hwAddrSize = hwAddrSize;
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

	public String getSenderMac() {
		return senderMac;
	}

	public void setSenderMac(String senderMac) {
		this.senderMac = senderMac;
	}

	public String getSenderIpAddr() {
		return senderIpAddr;
	}

	public void setSenderIpAddr(String senderIpAddr) {
		this.senderIpAddr = senderIpAddr;
	}

	public String getTargetMac() {
		return targetMac;
	}

	public void setTargetMac(String target) {
		this.targetMac = target;
	}

	public String getTargetIpAddr() {
		return targetIpAddr;
	}

	public void setTargetIpAddr(String targetIpAddr) {
		this.targetIpAddr = targetIpAddr;
	}

	public long getSrcVlan() {
		return srcVlan;
	}

	public void setSrcVlan(long srcVlan) {
		this.srcVlan = srcVlan;
	}

	public long getSrcPriority() {
		return srcPriority;
	}

	public void setSrcPriority(long srcPriority) {
		this.srcPriority = srcPriority;
	}

	public long getDestVlan() {
		return destVlan;
	}

	public void setDestVlan(long destVlan) {
		this.destVlan = destVlan;
	}

	public long getDestPriority() {
		return destPriority;
	}

	public void setDestPriority(long destPriority) {
		this.destPriority = destPriority;
	}

	public long getFlowDataFormat() {
		return flowDataFormat;
	}

	public void setFlowDataFormat(long flowDataFormat) {
		this.flowDataFormat = flowDataFormat;
	}

	public String getSourceIDName() {
		return sourceIDName;
	}

	public void setSourceIDName(String sourceIDName) {
		this.sourceIDName = sourceIDName;
	}

	public String getInInterfaceName() {
		return inInterfaceName;
	}

	public void setInInterfaceName(String inInterfaceName) {
		this.inInterfaceName = inInterfaceName;
	}

	public String getOutInterfaceName() {
		return outInterfaceName;
	}

	public void setOutInterfaceName(String outInterfaceName) {
		this.outInterfaceName = outInterfaceName;
	}

	public double getIfUtilization() {
		return ifUtilization;
	}

	public double getIfFrameRate() {
		return ifFrameRate;
	}

	public void setIfUtilization(double ifUtilization) {
		this.ifUtilization = ifUtilization;
	}

	public void setIfFrameRate(double ifFrameRate) {
		this.ifFrameRate = ifFrameRate;
	}

	public double getIfBitRate() {
		return ifBitRate;
	}

	public void setIfBitRate(double ifBitRate) {
		this.ifBitRate = ifBitRate;
	}

	@Override
	public String toString() {
		return "RawPacketData [ipVersionAgent=" + ipVersionAgent
				+ ", ipAddress=" + ipAddress + ", bucket=" + bucket
				+ ", subAgentID=" + subAgentID + ", sourceIDType="
				+ sourceIDType + ", sourceIDIndex=" + sourceIDIndex
				+ ", sourceIDName=" + sourceIDName + ", flowDataFormat="
				+ flowDataFormat + ", eventTime=" + eventTime
				+ ", sampleSequenceNumber=" + sampleSequenceNumber
				+ ", seqNumber=" + seqNumber + ", sysUptime=" + sysUptime
				+ ", samplingRate=" + samplingRate + ", samplePool="
				+ samplePool + ", drops=" + drops + ", inInterface="
				+ inInterface + ", inInterfaceName=" + inInterfaceName
				+ ", outInterface=" + outInterface + ", outInterfaceName="
				+ outInterfaceName + ", inInterfaceFormat=" + inInterfaceFormat
				+ ", outInterfaceFormat=" + outInterfaceFormat
				+ ", headerProtocol=" + headerProtocol + ", frameLength="
				+ frameLength + ", stripped=" + stripped + ", srcMac=" + srcMac
				+ ", destMac=" + destMac + ", packetType=" + packetType
				+ ", frameType=" + frameType + ", tagProtocolId="
				+ tagProtocolId + ", qos=" + qos + ", vlanId=" + vlanId
				+ ", version=" + version + ", neighborMac=" + neighborMac
				+ ", sysName=" + sysName + ", linkName=" + linkName
				+ ", lldpTtl=" + lldpTtl + ", headerLength=" + headerLength
				+ ", tos=" + tos + ", ipv4PacketLen=" + ipv4PacketLen
				+ ", identification=" + identification + ", fragOff=" + fragOff
				+ ", ttl=" + ttl + ", l3Protocol=" + l3Protocol + ", srcIp="
				+ srcIp + ", destIp=" + destIp + ", srcPort=" + srcPort
				+ ", destPort=" + destPort + ", tcpSeq=" + tcpSeq
				+ ", tcpAckNumber=" + tcpAckNumber + ", tcpHeaderLen="
				+ tcpHeaderLen + ", tcpReservedBits=" + tcpReservedBits
				+ ", tcpFlags=" + tcpFlags + ", tcpWindowSize=" + tcpWindowSize
				+ ", tcpChecksum=" + tcpChecksum + ", tcpUrgent=" + tcpUrgent
				+ ", udpLen=" + udpLen + ", udpChecksum=" + udpChecksum
				+ ", icmpType=" + icmpType + ", icmpCode=" + icmpCode
				+ ", flowLabel=" + flowLabel + ", payloadLen=" + payloadLen
				+ ", nextHeader=" + nextHeader + ", hopLimit=" + hopLimit
				+ ", hwType=" + hwType + ", protocolType=" + protocolType
				+ ", hwAddrSize=" + hwAddrSize + ", protocolSize="
				+ protocolSize + ", opcode=" + opcode + ", senderMac="
				+ senderMac + ", senderIpAddr=" + senderIpAddr + ", targetMac="
				+ targetMac + ", targetIpAddr=" + targetIpAddr + ", srcVlan="
				+ srcVlan + ", srcPriority=" + srcPriority + ", destVlan="
				+ destVlan + ", destPriority=" + destPriority
				+ ", ifUtilization=" + ifUtilization + ", ifFrameRate="
				+ ifFrameRate + ", ifBitRate=" + ifBitRate + "]";
	}



}
