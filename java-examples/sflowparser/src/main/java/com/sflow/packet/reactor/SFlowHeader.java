package com.sflow.packet.reactor;

import java.util.Date;
import java.util.Vector;

import reactor.bus.Event;
import reactor.bus.EventBus;

import com.sflow.collector.SFlowCollector;
import com.sflow.packet.header.reactor.CounterRecordConsumer;
import com.sflow.packet.header.reactor.FlowRecordConsumer;
import com.sflow.packet.header.reactor.SampleDataHeader;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;
import com.sflow.util.Address;
import com.sflow.util.BinaryFieldDesc;
import com.sflow.util.BinaryFieldDescList;
import com.sflow.util.DataTypeE;

import com.sflow.util.HeaderException;

public class SFlowHeader  {

	private static BinaryFieldDesc[] bfds = new BinaryFieldDesc[] {
			new BinaryFieldDesc("version", DataTypeE.UINT32, 1, 0),	 // 2, 4, 5
			new BinaryFieldDesc("ipVersionAgent", DataTypeE.UINT32, 1, 1), // 1=v4, 2=v6
			new BinaryFieldDesc("addressAgent", DataTypeE.IPV4, 1, 2),
			new BinaryFieldDesc("subAgentId", DataTypeE.UINT32, 1, 3),
			new BinaryFieldDesc("seqNumber", DataTypeE.UINT32, 1, 4), /* Incremented with each 
																sample datagram
																generated by a sub-agent within an
																agent. */
			new BinaryFieldDesc("sysUptime", DataTypeE.UINT32, 1, 5), // in milliseconds
			new BinaryFieldDesc("numberSamples", DataTypeE.UINT32, 1, 6), // in datagrams
	};

	private static BinaryFieldDescList bfdList = new BinaryFieldDescList();

	static {
		for (int i = 0; i < bfds.length; i++) {
			bfdList.addField(bfds[i]);
		}
	}

	private byte[] data;
	private int offset;

	private Date timestamp;
	private Vector<SampleDataHeader> sampleDataHeaders;
	private SFlowCollector collector;

	public SFlowHeader(byte[] buffer, int offset, SFlowCollector collector) {
		this.timestamp = new Date();
		this.setCollector(collector);
		this.data = buffer;
		this.offset = offset;
		sampleDataHeaders = new Vector<SampleDataHeader>();
	}

	public SFlowHeader(Date timestamp, byte[] buffer, int offset, SFlowCollector collector) {
		this.timestamp = timestamp;
		this.setCollector(collector);
		this.data = buffer;
		this.offset = offset;
		sampleDataHeaders = new Vector<SampleDataHeader>();
	}

	public long getVersion() throws UtilityException, HeaderException {
		long version = Utility.fourBytesToLong(data, offset);
		
		if (!((version == 2) || (version == 4) || (version == 5))) 
			throw new HeaderException("Version " + version + " is not in the valid range (2, 4, 5)");
		return version;	
	}

	public long getIPVersionAgent() throws UtilityException, HeaderException {		
		long ipVersionAgent = Utility.fourBytesToLong(data, offset+4);
		if (!((ipVersionAgent == 1) || (ipVersionAgent == 2))) 
			throw new HeaderException("IPVersionAgent " + ipVersionAgent + " is not in the valid rang (1, 2)");
		return ipVersionAgent;
	}

	public Address getAddressAgent() throws UtilityException {
		long ipVersionAgent = Utility.fourBytesToLong(data, offset+4);

		// agent ip address
		if (ipVersionAgent == 1) { // IPv4
			Address ip4addr = Utility.fourBytesToIpAddr(data, offset+8);
			return ip4addr;
		}

		if (ipVersionAgent == 2) { // IPv6
			System.err.println("IPv6 is not supported by RawPacketHeader");
		}

		return null;
	}

	public long getSubAgentID() throws UtilityException {
		long subAgentID = Utility.fourBytesToLong(data, offset+12);
		return subAgentID;
	}

	public long getSeqNumber() throws UtilityException {
		long seqNumber = Utility.fourBytesToLong(data, offset+16);
		return seqNumber;
	}

	public long getSysUptime() throws UtilityException {
		long sysUptime = Utility.fourBytesToLong(data, offset+20);
		return sysUptime;
	}

	public long getNumberSamples() throws UtilityException {
		long numberSamples = Utility.fourBytesToLong(data, offset+24);
		return numberSamples;
	}

	public void addSampleDataHeader(SampleDataHeader sampleDataHeader) {
		sampleDataHeaders.add(sampleDataHeader);
	}

	public Vector<SampleDataHeader> getSampleDataHeaders() {
		return sampleDataHeaders;
	}

	public SFlowCollector getCollector() {
		return collector;
	}

	public void setCollector(SFlowCollector collector) {
		this.collector = collector;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public static SFlowHeader parse(Date date, byte[] data, 
			SFlowCollector collector, EventBus counterEventBus, 
			EventBus flowEventBus) throws HeaderParseException {

		try {
			if (data.length < 28) throw new HeaderParseException("Data array too short.");	
			SFlowHeader rph = new SFlowHeader(date, data, 0, collector);

			// sample data headers
			int offset = 28;

			for (int i = 0; i < rph.getNumberSamples(); i++) {

				long format = Utility.fourBytesToLong(data, offset);
				long sampleLength = Utility.fourBytesToLong(data, offset+4);

				if (format == SampleDataHeader.EXPANDEDFLOWSAMPLE ||
						format == SampleDataHeader.FLOWSAMPLE) {
					flowEventBus.notify(FlowRecordConsumer.flowEvent, 
							Event.wrap(new FlowRecordConsumer.FlowEventData(rph, format, data, 
									offset + 8, sampleLength)) );
				} else if (format == SampleDataHeader.COUNTERSAMPLE ||
						format == SampleDataHeader.EXPANDEDCOUNTERSAMPLE) {
					counterEventBus.notify(CounterRecordConsumer.counterEvent,
							Event.wrap(new CounterRecordConsumer.CounterEventData(rph, format, data, 
									offset + 8, sampleLength)) );	
				}

				offset += (sampleLength + 8);
			}

			return rph;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HeaderParseException("Parse error: " + e.getMessage());
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[SFlowHeader]: ");
		try {
			sb.append("Version: ");
			sb.append(getVersion());
			sb.append(", IPAgentVersion: ");
			sb.append(getIPVersionAgent());
			sb.append(", IPAdressOfAgent: ");
			sb.append(getAddressAgent());
			sb.append(", SubAgentID: ");
			sb.append(getSubAgentID());
			sb.append(", DatagramSequenceNumber: ");
			sb.append(getSeqNumber());
			sb.append(", SwitchUptime: ");
			sb.append(getSysUptime());
			sb.append(", Samples: ");
			sb.append(getNumberSamples());
			sb.append(", ");
			for(SampleDataHeader sdh : sampleDataHeaders){
				sb.append(sdh);
				sb.append(" ");
			}
		} catch (UtilityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

}
