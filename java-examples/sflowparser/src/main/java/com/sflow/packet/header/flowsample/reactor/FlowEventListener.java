package com.sflow.packet.header.flowsample.reactor;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.sflow.metrics.CumulativeMovingAverage;
import com.sflow.metrics.ExponentialMovingAverage;
import com.sflow.packet.header.countersample.reactor.CounterEventListener;
import com.sflow.packet.header.flowsample.ArpHeader;
import com.sflow.packet.header.flowsample.ExtendedSwitchFlowRecord;
import com.sflow.packet.header.flowsample.FlowRecordHeader;
import com.sflow.packet.header.flowsample.Ipv4Header;
import com.sflow.packet.header.flowsample.Ipv6Header;
import com.sflow.packet.header.flowsample.LLDPHeader;
import com.sflow.packet.header.flowsample.MacHeader;
import com.sflow.packet.header.flowsample.RawPacketHeader;
import com.sflow.packet.header.flowsample.TaggedMacHeader;
import com.sflow.packet.header.reactor.ExpandedFlowSampleHeader;
import com.sflow.packet.header.reactor.FlowSampleHeader;
import com.sflow.packet.reactor.SFlowHeader;
import com.sflow.records.domain.RawPacketData;
import com.sflow.util.HeaderException;
import com.sflow.util.UtilityException;

@Component
public class FlowEventListener  {
		
    private static final Logger                             log =
            LogManager.getLogger(FlowEventListener.class.getName());

    public static final Marker LSMarker = MarkerManager.getMarker("LOGSTASH_MARKER");
    
	// Keep track of the last RawPacketData Record per interface..
	private HashMap<ByteBuffer, RawPacketDataProcessor> ifPacketDataMap = 
			new HashMap<ByteBuffer, RawPacketDataProcessor>();
	
	@Autowired
	CounterEventListener 						counterEventListener;

	private long getIfSpeed(String ipAddr, long subAgentId, long ifIndex) {
		if (counterEventListener != null) {
			return counterEventListener.getIfSpeed(ipAddr, subAgentId, ifIndex);
		}
		return -1;
	}
	
	public void newFlowRecord(ByteBuffer key, RawPacketData record) {
		log.info(LSMarker, record);
	}
	
	public void computeUtilization(RawPacketDataProcessor rpdr, RawPacketData cur,  boolean update) {
   		double utilization;	
		double frameRate;
		double bitRate;
		   		
		long deltaOctets;
		long deltaPackets;
		
		BigInteger bigIntegerMaxL = BigInteger.valueOf(Long.MAX_VALUE);
		BigInteger bigIntegerMaxI = BigInteger.valueOf(Integer.MAX_VALUE);
		long max = Integer.MAX_VALUE * 2L; // unsigned int

		double sampleWindowSecs = (cur.getSysUptime() -
				rpdr.getRpd().getSysUptime())/1000 ;
		
		deltaPackets = cur.getSamplePool() - rpdr.getRpd().getSamplePool();	
		
		// This is data roll-over computation which will most likely
		// not happen for a 64 bit value.
		if (deltaPackets < 0) {
			if (rpdr.getRpd().getSamplePool() < max) {
				deltaPackets = bigIntegerMaxI.longValue() - 
						rpdr.getRpd().getSamplePool() + 
						cur.getSamplePool();
			} else {
				deltaPackets = bigIntegerMaxL.longValue() - 
						rpdr.getRpd().getSamplePool() + 
						cur.getSamplePool();
			}
		}
		
		frameRate = (deltaPackets )/(sampleWindowSecs);					
		deltaOctets = (long) (deltaPackets * rpdr.getCma().getAverage() * 8);
		bitRate = (deltaOctets/(sampleWindowSecs));
		     
		long ifSpeed = getIfSpeed(cur.getIpAddress(), cur.getSubAgentID(), 
				cur.getInInterface());
		if (ifSpeed != -1) {
			// bits/sec
			utilization = (bitRate/ifSpeed)*100;
			if (update)
				rpdr.getEma().update(rpdr.getRpd().getSysUptime(), 
						cur.getSysUptime(), utilization);
			cur.setIfUtilization(rpdr.getEma().getAverage());
		}		
		
		cur.setIfFrameRate(frameRate);
		cur.setIfBitRate(bitRate);
	}
	
		
	class RawPacketDataProcessor {
		RawPacketData    			rpdLast;
		RawPacketData				prev;
		
		CumulativeMovingAverage 	cma = new CumulativeMovingAverage();
		ExponentialMovingAverage    ema = new ExponentialMovingAverage(5*60*1000); // 5 min avg
		private ByteBuffer 			key;	

		public RawPacketData getRpd() {
			return rpdLast;
		}
		
		public CumulativeMovingAverage getCma() {			
			return cma;
		}
		
		public void setCma(CumulativeMovingAverage cma) {
			this.cma = cma;
		}	
		
		public ExponentialMovingAverage getEma() {			
			return ema;
		}
		
		public void setEma(ExponentialMovingAverage ema) {
			this.ema = ema;
		}	
		
		public synchronized void setRpd(RawPacketData rpd) {
			RawPacketData last = null;
			last = prev;
								
			cma.update(rpd.getFrameLength());
	
			if (last == null ||
					(last.getSampleSequenceNumber() == rpd.getSampleSequenceNumber()) ||
					(last.getSysUptime() == rpd.getSysUptime())) {
				prev = rpd;
				if (rpdLast != null) {
					computeUtilization(this, rpd, false);
				}
				
				newFlowRecord(key, rpd);
				return;
			} else if (last.getSampleSequenceNumber() > rpd.getSampleSequenceNumber() ||
					last.getSysUptime() > rpd.getSysUptime()) {
				// we drop the sample.. to avoid problems in classifiers
				log.warn(" dropping Flow Record - " +
						rpd.getIpAddress().toString() + " " +
                        rpd.getSysUptime() + "  " +
                        rpd.getSampleSequenceNumber() );
				return;
			}
			
			// last sample sequence number is less than cur sequence number..

			if (rpdLast == null) {
				rpdLast = prev;
			}

			computeUtilization(this, rpd, true);	
		
			// update/set the last packet..
			rpdLast = last;	
			prev = rpd;
			newFlowRecord(key, rpd);
		}

		public void setKey(ByteBuffer key) {
			this.key = key;			
		}
		
		public ByteBuffer getKey() {
			return key;
		}
	}
	
	@PostConstruct
	public void init() {
	}
	
	
	public void addChassisRecord(LLDPHeader h, RawPacketData record) {		
		record.setSysName(h.getSysName());
		record.setNeighborMac(h.getMacAddr().toString());
		record.setLinkName(h.getLinkName());
		record.setLldpTtl(h.getTtl());
	}
	
	public void addMacHeaderRecord(MacHeader macHeader,
			RawPacketData record) {		
		record.setSrcMac(macHeader.getSource().toString());
		record.setDestMac(macHeader.getDestination().toString());
		record.setPacketType(macHeader.getType());
		record.setFrameType(macHeader.getFrameType());
		if (macHeader instanceof TaggedMacHeader) {
			TaggedMacHeader tm = (TaggedMacHeader) macHeader;
			record.setVlanId(tm.getTCI() & 0xfff);
			record.setQos(tm.getTCI() >> 13);
			record.setTagProtocolId(tm.getTpid());
		}
	}
	
	public void addFlowRecord(ArpHeader arpHeader,
			RawPacketData record) {
		
		addMacHeaderRecord(arpHeader.getMacHeader(), record);

		record.setHwType(arpHeader.getHwType());
		record.setProtocolType(arpHeader.getProtocolType());
		record.setHwAddrSize(arpHeader.getHwSize());
		record.setProtocolSize(arpHeader.getProtocolSize());
		record.setOpcode(arpHeader.getOpcode());
		record.setSenderMac(arpHeader.getSender().toString());
		record.setSenderIpAddr(arpHeader.getSenderProto().toString());
		record.setTargetMac(arpHeader.getTarget().toString());
		record.setTargetIpAddr(arpHeader.getTargetProto().toString());

	}
	
	public void addFlowRecord(Ipv4Header v4Header,
			RawPacketData record) {
		
		addMacHeaderRecord(v4Header.getMacHeader(), record);

		record.setVersion(v4Header.getVersion());
		
		 /*
         * The Header Length field is given in units of
         * 4-byte words. A flow length of “5” is therefore            
         * equal to 20 bytes. This also imposes a maximum
         * length on the IPv4 flow: 0xF 4-byte words, or              
         * 60 bytes
         */
		record.setHeaderLength(v4Header.getHeaderLength());
		
		record.setTos(v4Header.getTos());
		
		/*
         * The length of the IP packet excluding lower layer
         * encapsulations. This is the length of the entire IPv4
         * flow and payload. This is why padding does not
         * necessarily have to be stripped from a received
         * Ethernet frame.
         */
		record.setIpv4PacketLen(v4Header.getLength());
		
		 /*
         * An assigned number which distinguishes this          
         * IP packet from other received packets. This          
         * value is used to help assemble received IP           
         * packets that have been fragmented
         */
		record.setIdentification(v4Header.getIdentification());
		record.setFragOff(v4Header.getFrag_off());
		record.setTtl(v4Header.getTtl());
		
		/*
         * IP next header (for example, TCP = 0x06, UDP = 0x11,
         * IGMP = 0x02, ICMP 0x1)
         */
		record.setL3Protocol(v4Header.getProtocol());
		record.setSrcIp(v4Header.getSrcIp().toString());
		record.setDestIp(v4Header.getDestIp().toString());
		record.setSrcPort(v4Header.getSrcPort());
		record.setDestPort(v4Header.getDestPort());
		record.setTcpSeq(v4Header.getTcpSeqNumber());
		record.setTcpAckNumber(v4Header.getTcpAckNumber());
		record.setTcpHeaderLen(v4Header.getTcpHeaderLength());
		record.setTcpReservedBits(v4Header.getTcpReserved());
		record.setTcpFlags(v4Header.getTcpFlags());
		record.setTcpWindowSize(v4Header.getTcpWindowSize());
		record.setTcpChecksum(v4Header.getTcpChecksum());
		
		record.setUdpLen(v4Header.getUdpLen());
		record.setUdpChecksum(v4Header.getUdpChecksum());
		
		record.setIcmpCode(v4Header.getIcmpCode());
		record.setIcmpType(v4Header.getIcmpType());
			
	}
	
	public void addFlowRecord(Ipv6Header v6Header,
			RawPacketData record) {
		
		addMacHeaderRecord(v6Header.getMacHeader(), record);
		
		record.setVersion(v6Header.getVersion());
		record.setTos(v6Header.getTos());
		
		record.setFlowLabel(v6Header.getFlowLabel());
		record.setPayloadLen(v6Header.getPayloadLength());
		record.setNextHeader(v6Header.getNextHeader());
		record.setHopLimit(v6Header.getHopLimit());
		
		record.setSrcIp(v6Header.getSrcIp().toString());
		record.setDestIp(v6Header.getDestIp().toString());
		record.setTcpSeq(v6Header.getTcpSeqNumber());
		record.setTcpAckNumber(v6Header.getTcpAckNumber());
		record.setTcpHeaderLen(v6Header.getTcpHeaderLength());
		record.setTcpReservedBits(v6Header.getTcpReserved());
		record.setTcpFlags(v6Header.getTcpFlags());
		record.setTcpWindowSize(v6Header.getTcpWindowSize());
		record.setTcpChecksum(v6Header.getTcpChecksum());

		record.setUdpLen(v6Header.getUdpLen());
		record.setUdpChecksum(v6Header.getUdpChecksum());

		record.setIcmpCode(v6Header.getIcmpCode());
		record.setIcmpType(v6Header.getIcmpType());

	}
	
	public void addExtendedSwitchFlowRecord(ExtendedSwitchFlowRecord switchRecord,
			RawPacketData record) {
		record.setSrcVlan(switchRecord.getSrcVlan());
		record.setSrcPriority(switchRecord.getSrcPriority());
		record.setDestVlan(switchRecord.getDestVlan());
		record.setDestPriority(switchRecord.getDestPriority());
	}
	
	public void addFlowRecord(RawPacketHeader rph,
			RawPacketData record) {
		record.setHeaderProtocol(rph.getHeaderProtocol());
		record.setFrameLength(rph.getFrameLength());
		if (rph.getFrameLength() == 0) {
			Exception e = new Exception("framelength is 0");
			e.printStackTrace();
			System.out.println("rph " + rph);
		}
		record.setStripped(rph.getStripped());
		
		MacHeader macHeader = rph.getMacHeader();
		
		if (macHeader instanceof Ipv4Header ) {
			Ipv4Header ipv4Header = (Ipv4Header) macHeader;
			addFlowRecord(ipv4Header, record);
		} else if (macHeader instanceof Ipv6Header) {
			Ipv6Header ipv6Header = (Ipv6Header) macHeader;
			addFlowRecord(ipv6Header, record);
		} else if (macHeader instanceof ArpHeader) {
			ArpHeader arpHeader = (ArpHeader) macHeader;
			addFlowRecord(arpHeader, record);
		} else if (macHeader instanceof LLDPHeader) {
			LLDPHeader h = (LLDPHeader) macHeader;
			addMacHeaderRecord(h.getMacHeader(), record);
			addChassisRecord(h, record);
		} else {
			addMacHeaderRecord(macHeader, record);
		}		
	}
	

	/** 
	 * updates some of the derived features like frame rate
	 * and utilization.
	 * 
	 * @param recordLast
	 */
	public void updateRecord(RawPacketData recordLast) {
		ByteBuffer key= CounterEventListener.getKey(
				recordLast.getIpAddress(),
				recordLast.getSubAgentID(),
				recordLast.getInInterface());
		
		RawPacketDataProcessor rpdr  = ifPacketDataMap.get(key);
		if (rpdr != null) {
			rpdr.setRpd(recordLast);
		} else {
			rpdr = new RawPacketDataProcessor();
			rpdr.setRpd(recordLast);
			ifPacketDataMap.put(key, rpdr);
			rpdr.setKey(key);
		}	
	}
		
	public void addFlowSample(FlowSampleHeader flowHeader) {
		SFlowHeader sflowHeader = flowHeader.getSflowHeader();		
		RawPacketData recordLast = null;
		
		for (FlowRecordHeader frh : flowHeader.getFlowRecords()) {
		
			if (frh.getFlowDataFormat() != FlowRecordHeader.HEADERDATA_SFLOWv5  && 
					frh.getFlowDataFormat() != FlowRecordHeader.SWITCHDATA_SFLOWv5) {				
				log.info("Error: flowDataFormat is not supported. " + frh.getFlowDataFormat());
				continue;
			}

			RawPacketData record = null;

			if (recordLast != null) {
				try {
					if (recordLast.getSampleSequenceNumber() == sflowHeader.getSeqNumber() && 
							recordLast.getSeqNumber() == flowHeader.getSequenceNumber()) {
						record = recordLast;
					} else {
						updateRecord(recordLast);
						recordLast = null;
					}
				} catch (UtilityException ue)  {

				}
			}
			
			if (record == null) {
				record = new RawPacketData();
			}

			try {
				record.setIpVersionAgent((int)sflowHeader.getIPVersionAgent());				
				record.setIpAddress(sflowHeader.getAddressAgent().toString());
				record.setSubAgentID(sflowHeader.getSubAgentID());
				record.setSampleSequenceNumber(sflowHeader.getSeqNumber());
				record.setSysUptime(sflowHeader.getSysUptime());
				record.setEventTime(sflowHeader.getTimestamp());
				record.setBucket(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

				record.setSourceIDType(flowHeader.getSourceIDType());
				record.setSourceIDIndex(flowHeader.getSourceIDIndex());		
				record.setSeqNumber(flowHeader.getSequenceNumber());
				record.setSamplingRate(flowHeader.getSamplingRate());
				record.setSamplePool(flowHeader.getSamplePool());
				record.setDrops(flowHeader.getDrops());
				record.setInInterfaceFormat(flowHeader.getInputInterfaceFormat());
				record.setOutInterfaceFormat(flowHeader.getOutputInterfaceFormat());
				record.setFlowDataFormat(frh.getFlowDataFormat());

				record.setInInterface(flowHeader.getInputInterfaceValue());
				record.setOutInterface(flowHeader.getOutputInterfaceValue());

				updateInterfaceName(flowHeader.getInputInterfaceValue(), 
						flowHeader.getOutputInterfaceValue());

				switch ((int) frh.getFlowDataFormat()) {
				case FlowRecordHeader.HEADERDATA_SFLOWv5 :
					addFlowRecord(frh.getRawPacketHeader(), record);				
					recordLast = record;
					break;
				case FlowRecordHeader.SWITCHDATA_SFLOWv5 : // switch data
					addExtendedSwitchFlowRecord(frh.getExtendedSwitchFlowRecord(), record);
					recordLast = record;
					break;
				default :
					System.out.println("record 3" + record);
					break;
				}	
			} catch (UtilityException ue)  {

			} catch (HeaderException he) {

			}
			
		}
		
		if (recordLast != null) {
			updateRecord(recordLast);
			recordLast = null;
		}
	}
			
	private void updateInterfaceName(long inputInterfaceValue, long outputInterfaceValue) {
	//		PortInfo pi = topo.getTopoMap().get(Long.toString(
	//		flowHeader.getInputInterfaceValue()));
	//
	//		PortInfo piOut = topo.getTopoMap().get(Long.toString(
	//		flowHeader.getOutputInterfaceValue()));
	//
	//		if (pi != null) {
	//			record.setSourceIDName(pi.getDevice());
	//			record.setInInterfaceName(pi.getPort());	
	//		}
	//
	//		if (piOut != null) {
	//			record.setOutInterfaceName(piOut.getPort());
	//		}	
	}

	public void addExpandedFlowSample(ExpandedFlowSampleHeader 
			expandedflowSampleHeader) {
		
		SFlowHeader sflowHeader = expandedflowSampleHeader.getSflowHeader();
		RawPacketData recordLast = null;

		for (FlowRecordHeader frh : expandedflowSampleHeader.getFlowRecords()) {
			if (frh.getFlowDataFormat() != FlowRecordHeader.HEADERDATA_SFLOWv5  && 
					frh.getFlowDataFormat() != FlowRecordHeader.SWITCHDATA_SFLOWv5) {
				System.out.println("unspported expanded flow sample" + frh);
				log.info("Error: flowDataFormat is not supported. " + frh.getFlowDataFormat());
				continue;
			}
			
			RawPacketData record = null;

			if (recordLast != null) {
				try {
					if (recordLast.getSampleSequenceNumber() == sflowHeader.getSeqNumber() && 
							recordLast.getSeqNumber() == expandedflowSampleHeader.getSequenceNumber()) {
						record = recordLast;
					} else {
						updateRecord(recordLast);
						recordLast = null;
					}
				} catch (UtilityException ue)  {

				}
			}
			
			
			if (record == null) {
				record = new RawPacketData();
			}
			
			try {

				record.setIpVersionAgent((int)sflowHeader.getIPVersionAgent());				
				record.setIpAddress(sflowHeader.getAddressAgent().toString());
				record.setSubAgentID(sflowHeader.getSubAgentID());
				record.setSampleSequenceNumber(sflowHeader.getSeqNumber());
				record.setSysUptime(sflowHeader.getSysUptime());
				record.setEventTime(sflowHeader.getTimestamp());
				record.setBucket(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

				record.setSourceIDType(expandedflowSampleHeader.getSourceIDType());
				record.setSourceIDIndex(expandedflowSampleHeader.getSourceIDIndex());	
				record.setSeqNumber(expandedflowSampleHeader.getSequenceNumber());
				record.setSamplingRate(expandedflowSampleHeader.getSamplingRate());
				record.setSamplePool(expandedflowSampleHeader.getSamplePool());
				record.setDrops(expandedflowSampleHeader.getDrops());

				record.setInInterfaceFormat(expandedflowSampleHeader.getInputInterfaceFormat());
				record.setOutInterfaceFormat(expandedflowSampleHeader.getOutputInterfaceFormat());

				record.setInInterface(expandedflowSampleHeader.getInputInterfaceValue());
				record.setOutInterface(expandedflowSampleHeader.getOutputInterfaceValue());
				record.setFlowDataFormat(frh.getFlowDataFormat());

				updateInterfaceName(expandedflowSampleHeader.getInputInterfaceValue(), 
						expandedflowSampleHeader.getOutputInterfaceValue());

				switch ((int) frh.getFlowDataFormat()) 	{
				case FlowRecordHeader.HEADERDATA_SFLOWv5 :
					addFlowRecord(frh.getRawPacketHeader(), record);
					recordLast = record;
					break;
				case FlowRecordHeader.SWITCHDATA_SFLOWv5 : // switch data
					addExtendedSwitchFlowRecord(frh.getExtendedSwitchFlowRecord(), record);
					recordLast = record;
					break;

				default :
					System.out.println("record 33" + record);
					break;
				}	
			} catch (UtilityException ue)  {

			} catch (HeaderException he) {

			}
		}
		
		if (recordLast != null) {
			updateRecord(recordLast);
			recordLast = null;
		}
	}	
}
