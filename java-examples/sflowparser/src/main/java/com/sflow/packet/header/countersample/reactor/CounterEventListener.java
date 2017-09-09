package com.sflow.packet.header.countersample.reactor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sflow.packet.header.countersample.CounterRecordHeader;
import com.sflow.packet.header.countersample.GenericIfCounterHeader;
import com.sflow.packet.header.reactor.CounterSampleHeader;
import com.sflow.packet.header.reactor.ExpandedCounterSampleHeader;
import com.sflow.packet.reactor.SFlowHeader;
import com.sflow.records.domain.IfCounterData;
import com.sflow.records.domain.Processor;
import com.sflow.util.Address;
import com.sflow.util.HeaderException;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;

@Component
public class CounterEventListener 
{
		
	@SuppressWarnings("unused")
	private static final Logger log = LogManager.getLogger(CounterEventListener.class.getName());	
	
	// Keep track of the last IfCounterData Record per interface..
	private HashMap<ByteBuffer, IfCounterDataRecord>  ifCounterDataMap = new HashMap<ByteBuffer, IfCounterDataRecord>();
		
	public static ByteBuffer getKey(String ipAddr, long subAgentId, long ifIndex) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);	
		Address ip;
		
		try {
			ip = new Address(ipAddr);
			dos.write(ip.getBytes());
			dos.writeLong(subAgentId);
			dos.writeLong(ifIndex);

		} catch (UtilityException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	
		byte[] bytes = null;
		try {
			bytes = new String(baos.toByteArray(), "ISO-8859-1").getBytes();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return Utility.fromByteArray(bytes);
	}	
	
	@PostConstruct
	public void init() {

	}
	
	public void computeUtilization(IfCounterDataRecord rpdr, IfCounterData cur, boolean debug) {

   		double utilizationIn;
		double utilizationOut;
		
		double frameRateIn;
		double frameRateOut;
		   		
		long deltaInOctets;
		long deltaOutOctets;
		
		long deltaInPackets;
		long deltaOutPackets;
		
		long deltaInMcastPackets;
		long deltaOutMcastPackets;
		
		long deltaInBroadcastPackets;
		long deltaOutBroadcastPackets;

		
		IfCounterData prev = rpdr.getIfd();
		
		BigInteger bigIntegerMaxL = BigInteger.valueOf(Long.MAX_VALUE);
		BigInteger bigIntegerMaxI = BigInteger.valueOf(Integer.MAX_VALUE);
		long max = Integer.MAX_VALUE * 2L; // unsigned int

		double sampleWindowSecs = (cur.getSysUptime() -
				prev.getSysUptime())/1000 ;
		
		deltaInPackets = cur.getIfInUcastPkts() 
					 - prev.getIfInUcastPkts();
								
		deltaInMcastPackets = cur.getIfInMulticastPkts()  -
				prev.getIfInMulticastPkts();
		
		deltaInBroadcastPackets = cur.getIfInBroadcastPkts() -
				prev.getIfInBroadcastPkts();
			
		deltaOutPackets = cur.getIfOutUcastPkts() +
					- prev.getIfOutUcastPkts();
							
		deltaOutMcastPackets = cur.getIfOutMulticastPkts()  -
				prev.getIfOutMulticastPkts();
		
		deltaOutBroadcastPackets = cur.getIfOutBroadcastPkts() -
				prev.getIfOutBroadcastPkts();
		
		if (deltaInPackets < 0) {
			if (prev.getIfInUcastPkts() < max) {
				deltaInPackets = bigIntegerMaxI.longValue() - 
						prev.getIfInUcastPkts() + 
						cur.getIfInUcastPkts();
			} else {
				deltaInPackets = bigIntegerMaxL.longValue() - 
						prev.getIfInUcastPkts() + 
						cur.getIfInUcastPkts();
			}
		}
		
		if (deltaInMcastPackets < 0) {
			if (prev.getIfInMulticastPkts() < max) {
				deltaInMcastPackets = bigIntegerMaxI.longValue() - 
						prev.getIfInMulticastPkts() + 
						cur.getIfInMulticastPkts();
			} else {
				deltaInMcastPackets = bigIntegerMaxL.longValue() - 
						prev.getIfInMulticastPkts() + 
						cur.getIfInMulticastPkts();
			}
		}
		
		if (deltaInBroadcastPackets < 0) {
			if (prev.getIfInBroadcastPkts() < max) {
				deltaInBroadcastPackets = bigIntegerMaxI.longValue() - 
						prev.getIfInBroadcastPkts() + 
						cur.getIfInBroadcastPkts();
			} else {
				deltaInBroadcastPackets = bigIntegerMaxL.longValue() - 
						prev.getIfInBroadcastPkts() + 
						cur.getIfInBroadcastPkts();
			}
		}
		
		if (deltaOutPackets < 0) {
			if (prev.getIfOutUcastPkts() < max) {
				deltaOutPackets = bigIntegerMaxI.longValue() - 
						prev.getIfOutUcastPkts() + 
						cur.getIfOutUcastPkts();
			} else {
				deltaOutPackets = bigIntegerMaxL.longValue() - 
						prev.getIfOutUcastPkts() + 
						cur.getIfOutUcastPkts();
			}
		}
		
		if (deltaOutMcastPackets < 0) {
			if (prev.getIfOutMulticastPkts() < max) {
				deltaOutMcastPackets = bigIntegerMaxI.longValue() - 
						prev.getIfOutMulticastPkts() + 
						cur.getIfOutMulticastPkts();
			} else {
				deltaOutMcastPackets = bigIntegerMaxL.longValue() - 
						prev.getIfOutMulticastPkts() + 
						cur.getIfOutMulticastPkts();
			}
		}
		
		if (deltaOutBroadcastPackets < 0) {
			if (prev.getIfOutBroadcastPkts() < max) {
				deltaOutBroadcastPackets = bigIntegerMaxI.longValue() - 
						prev.getIfOutBroadcastPkts() + 
						cur.getIfOutBroadcastPkts();
			} else {
				deltaOutBroadcastPackets = bigIntegerMaxL.longValue() - 
						prev.getIfOutBroadcastPkts() + 
						cur.getIfOutBroadcastPkts();
			}
		}
		
		frameRateIn = (deltaInPackets  + deltaInMcastPackets + 
				deltaInBroadcastPackets)/(sampleWindowSecs);
		
		frameRateOut = (deltaOutPackets + deltaOutMcastPackets + 
				deltaOutBroadcastPackets)/(sampleWindowSecs);
				
		cur.setIfInFrameRate(frameRateIn);
		cur.setIfOutFrameRate(frameRateOut);
					
		deltaInOctets = cur.getIfInOctets().longValue() -
    	    		prev.getIfInOctets().longValue();
			
		// This is counter rollover computation which will most likely
		// not happen for a 64 bit value.
		if (deltaInOctets < 0) {
			if (prev.getIfInOctets().longValue() < max) {
				deltaInOctets = bigIntegerMaxI.longValue() - 
						prev.getIfInOctets().longValue() + 
						cur.getIfInOctets().longValue();
			} else {
				deltaInOctets = bigIntegerMaxL.longValue() - 
						prev.getIfInOctets().longValue() + 
						cur.getIfInOctets().longValue();
			}
		}
			
   		deltaOutOctets = cur.getIfOutOctets().longValue() - 
    	    		prev.getIfOutOctets().longValue();
   			
   		// This is counter rollover computation which will most likely
		// not happen for a 64 bit value.
		if (deltaOutOctets < 0) {
			if (prev.getIfOutOctets().longValue() < max) {
				deltaOutOctets = bigIntegerMaxI.longValue() - 
						prev.getIfOutOctets().longValue() + 
						cur.getIfOutOctets().longValue();
			} else {
				deltaOutOctets = bigIntegerMaxL.longValue() - 
						prev.getIfOutOctets().longValue() + 
						cur.getIfOutOctets().longValue();
			}
		}     
     		
		utilizationIn = ((deltaInOctets) * 8 * 100 )/(sampleWindowSecs * cur.getIfSpeed().longValue());
		utilizationOut = ((deltaOutOctets) * 8 * 100 )/(sampleWindowSecs * cur.getIfSpeed().longValue());

		cur.setIfInUtilization(utilizationIn);
   		cur.setIfOutUtilization(utilizationOut);		
	}
	
	// Order the udp packets based on
	// sequence number inside a sampleSequenceNumber
	class IfCounterDataRecord {
		IfCounterData    			ifdLast;
		IfCounterData				prev;
		
		public IfCounterData getIfd() {
			return ifdLast;
		}
		
			
		public synchronized void setIfd(IfCounterData ifd) {
			IfCounterData last = null;
			last = prev;
					
			if (last == null ||
					(last.getSampleSequenceNumber() == ifd.getSampleSequenceNumber()) ||
					(last.getSysUptime() == ifd.getSysUptime())) {
				prev = ifd;
				if (ifdLast != null) {
					computeUtilization(this, ifd, false);
				}
//				counterRecordHandler.handleNewCounterRecord(ifd);
				return;
			} else if (last.getSampleSequenceNumber() > ifd.getSampleSequenceNumber() ||
					last.getSysUptime() > ifd.getSysUptime()) {
				// we drop the sample.. to avoid problems in classifiers
				return;
			}
				
			// update/set the last packet..
			ifdLast = last;
			prev = ifd;
			computeUtilization(this, ifd, false);	
//			counterRecordHandler.handleNewCounterRecord(ifd);
		}
		
	}
	
	/** 
	 * updates some of the derived features like frame rate
	 * and utilization.
	 * 
	 * @param recordLast
	 */
	public void updateRecord(IfCounterData recordLast) {
		IfCounterDataRecord rpdr  = 
				ifCounterDataMap.get(
						getKey(recordLast.getIpAddress(), recordLast.getSubAgentID(),
								recordLast.getIfIndex()));
		if (rpdr != null) {
			rpdr.setIfd(recordLast);
		} else {
			rpdr = new IfCounterDataRecord();
			rpdr.setIfd(recordLast);
			ifCounterDataMap.put(
					getKey(recordLast.getIpAddress(), 
							recordLast.getSubAgentID(), 
							recordLast.getIfIndex()), rpdr);
		}	
	}
	
	public void addGenericIfCounterRecord(GenericIfCounterHeader ifCounter,
			IfCounterData record) {	
		record.setIfIndex(ifCounter.getIfIndex());
		record.setIfType(ifCounter.getIfType());
		
		record.setIfSpeed(ifCounter.getIfSpeed());
		record.setIfDirection(ifCounter.getIfDirection());
		record.setIfStatus(ifCounter.getIfStatus());

		record.setIfInOctets(ifCounter.getIfInOctets());
		record.setIfInUcastPkts(ifCounter.getIfInUcastPkts());
		record.setIfInBroadcastPkts(ifCounter.getIfInBroadcastPkts());
		record.setIfInMulticastPkts(ifCounter.getIfInMulticastPkts());
		record.setIfInDiscards(ifCounter.getIfInDiscards());
		record.setIfInErrors(ifCounter.getIfInErrors());
		record.setIfInUnknownProtos(ifCounter.getIfInUnknownProtos());

		record.setIfOutOctets(ifCounter.getIfOutOctets());
		record.setIfOutUcastPkts(ifCounter.getIfOutUcastPkts());
		record.setIfOutMulticastPkts(ifCounter.getIfOutMulticastPkts());
		record.setIfOutBroadcastPkts(ifCounter.getIfOutBroadcastPkts());
		record.setIfOutDiscards(ifCounter.getIfOutDiscards());
		record.setIfOutErrors(ifCounter.getIfOutErrors());
		record.setIfPromiscuousMode(ifCounter.getIfPromiscuousMode());														
	}
	
	public void addSflowHeaderToRecord(IfCounterData record, 
			SFlowHeader sflowHeader) throws UtilityException, HeaderException {

		if (record != null) {
			record.setIpVersionAgent((int)sflowHeader.getIPVersionAgent());				
			record.setIpAddress(sflowHeader.getAddressAgent().toString());
			record.setSubAgentID(sflowHeader.getSubAgentID());
			record.setSampleSequenceNumber(sflowHeader.getSeqNumber());
			record.setSysUptime(sflowHeader.getSysUptime());
			record.setEventTime(sflowHeader.getTimestamp());
			record.setBucket(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		}
	}
	
	public void addSflowHeaderToRecord(Processor record, 
			SFlowHeader sflowHeader) throws UtilityException, HeaderException {

		if (record != null) {
			record.setIpVersionAgent((int)sflowHeader.getIPVersionAgent());				
			record.setIpAddress(sflowHeader.getAddressAgent().toString());
			record.setSubAgentID(sflowHeader.getSubAgentID());
			record.setSampleSequenceNumber(sflowHeader.getSeqNumber());
			record.setSysUptime(sflowHeader.getSysUptime());
			record.setEventTime(sflowHeader.getTimestamp());
			record.setBucket(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		}
	}
	
	public void addCounterRecords(CounterSampleHeader counterHeader) {
		SFlowHeader sflowHeader = counterHeader.getSflowHeader();
		for (CounterRecordHeader crh : counterHeader.getCounterRecords()) {
			if (crh.getCounterDataFormat() != 1) {
				continue;
			}
			
			IfCounterData record = new IfCounterData();
			if (record != null) {	
				try {
					addSflowHeaderToRecord(record, sflowHeader);					
					record.setSourceIDType(counterHeader.getSourceIDType());
					record.setSourceIDIndex(counterHeader.getSourceIDIndex());	
					record.setSeqNumber(counterHeader.getSequenceNumber());
					addGenericIfCounterRecord(crh.getGenericCounterHeader(), record);
					
					updateRecord(record);
				} catch (UtilityException ue)  {
					
				} catch (HeaderException he) {
					
				}
			}
		}
	}
				
	public void addCounterRecords(ExpandedCounterSampleHeader 
			expandedCounterHeader) {
		SFlowHeader sflowHeader = expandedCounterHeader.getSflowHeader();
		for (CounterRecordHeader crh : expandedCounterHeader.getCounterRecords()) {
			if (crh.getCounterDataFormat() == CounterRecordHeader.GENERICCOUNTER_SFLOWv5) {
				IfCounterData record = new IfCounterData();
				if (record != null) {
					try {
						addSflowHeaderToRecord(record, sflowHeader);					
						record.setSourceIDType(expandedCounterHeader.getSourceIDType());
						record.setSourceIDIndex(expandedCounterHeader.getSourceIDIndex());	
						record.setSeqNumber(expandedCounterHeader.getSequenceNumber());
						addGenericIfCounterRecord(crh.getGenericCounterHeader(), record);
						updateRecord(record);
					} catch (UtilityException ue)  {
					
					} catch (HeaderException he) {
					
					}
				}
			} else if (crh.getCounterDataFormat() == CounterRecordHeader.PROCESSORCOUNTER_SFLOWv5) {
				Processor record = new Processor();
				if (record != null) { 
					try {
						addSflowHeaderToRecord(record, sflowHeader);	
						record.setSourceIDType(expandedCounterHeader.getSourceIDType());
						record.setSourceIDIndex(expandedCounterHeader.getSourceIDIndex());	
						record.setSeqNumber(expandedCounterHeader.getSequenceNumber());
						record.setFiveMinCpu(crh.getProcessorCounterHeader().getFiveMinCpu());
						record.setFreeMemory(crh.getProcessorCounterHeader().getFreeMemory());
						record.setTotalMemory(crh.getProcessorCounterHeader().getTotalMemory());
					} catch (UtilityException ue)  {
						
					} catch (HeaderException he) {
					
					}
					//batchP.saveRecord(record);
					//taskExecutor.execute(new CounterRecordProcessor(record));
				}
			}
		}
	}
	

	public long getIfSpeed(String ipAddr, long subAgentId, long ifIndex) {

		IfCounterDataRecord rpdr  = 
				ifCounterDataMap.get(
						getKey(ipAddr, subAgentId, ifIndex));
		
		if (rpdr != null ) {
			if (rpdr.getIfd() != null) {
				return rpdr.getIfd().getIfSpeed().longValue();
			}
			IfCounterData cd = rpdr.prev;
			if (cd != null) {
				return cd.getIfSpeed().longValue();
			}
		}
		
		return -1;
	}
	
}
