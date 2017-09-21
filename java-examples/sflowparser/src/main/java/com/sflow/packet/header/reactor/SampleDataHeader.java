package com.sflow.packet.header.reactor;

import com.sflow.packet.reactor.SFlowHeader;
import com.sflow.util.HeaderParseException;

public class SampleDataHeader {
	public final static long FLOWSAMPLE = 1;
	public final static long COUNTERSAMPLE = 2;
	public final static long EXPANDEDFLOWSAMPLE = 3;
	public final static long EXPANDEDCOUNTERSAMPLE = 4;

	private long sampleDataFormat; // 20 bit enterprise & 12 bit format;
	// standard enterprise 0, format 1, 2, 3, 4
	private long sampleLength; // in byte

	private FlowSampleHeader flowSampleHeader;
	private ExpandedFlowSampleHeader expandedFlowSampleHeader;
	private ExpandedCounterSampleHeader expandedCounterSampleHeader;
	private CounterSampleHeader counterSampleHeader;

	public SampleDataHeader() {
	}

	public void setSampleDataFormat(long format) {
		this.sampleDataFormat = format;
	}

	public void setSampleLength(long sampleLength) {
		this.sampleLength = sampleLength;
	}

	public long getSampleDataFormat() {
		return sampleDataFormat;
	}

	public long getSampleLength() {
		return sampleLength;
	}

	public FlowSampleHeader getFlowSampleHeader() {
		return flowSampleHeader;
	}

	public void setFlowSampleHeader(FlowSampleHeader flowSampleHeader) {
		this.flowSampleHeader = flowSampleHeader;
	}

	public void setExpandedFlowSampleHeader(ExpandedFlowSampleHeader efs) {
		expandedFlowSampleHeader = efs;
	}

	public ExpandedFlowSampleHeader getExpandedFlowSampleHeader() {
		return expandedFlowSampleHeader;
	}

	public void setExpandedCounterSampleHeader(ExpandedCounterSampleHeader ecs) {
		expandedCounterSampleHeader = ecs;
	}

	public ExpandedCounterSampleHeader getExpandedCounterSampleHeader() {
		return expandedCounterSampleHeader;
	}

	public CounterSampleHeader getCounterSampleHeader() {
		return counterSampleHeader;
	}

	public void setCounterSampleHeader(CounterSampleHeader counterSampleHeader) {
		this.counterSampleHeader = counterSampleHeader;
	}

	public static SampleDataHeader parse(long format, byte[] data, int offset, long len, SFlowHeader sflowHeader)
			throws HeaderParseException {
		try {
			if ((data.length - offset) < 8)
				throw new HeaderParseException("Data array too short.");

			SampleDataHeader sdh = new SampleDataHeader();

			// format
			sdh.setSampleDataFormat(format);

			// length
			sdh.setSampleLength(len);

			if (sdh.getSampleDataFormat() == EXPANDEDFLOWSAMPLE) {
				ExpandedFlowSampleHeader efs = ExpandedFlowSampleHeader.parse(data, offset, sflowHeader);
				sdh.setExpandedFlowSampleHeader(efs);
			} else if (sdh.getSampleDataFormat() == FLOWSAMPLE) {
				FlowSampleHeader efs = FlowSampleHeader.parse(data, offset, sflowHeader);
				sdh.setFlowSampleHeader(efs);
			} else if (sdh.getSampleDataFormat() == EXPANDEDCOUNTERSAMPLE) {
				ExpandedCounterSampleHeader ecs = ExpandedCounterSampleHeader.parse(data, offset, sflowHeader);
				sdh.setExpandedCounterSampleHeader(ecs);
			} else if (sdh.getSampleDataFormat() == COUNTERSAMPLE) {
				CounterSampleHeader cs = CounterSampleHeader.parse(data, offset, sflowHeader);
				sdh.setCounterSampleHeader(cs);
			} else {
				System.err.println(
						"SampleDataHeader : Sample data format not yet supported: " + sdh.getSampleDataFormat());
			}

			return sdh;
		} catch (Exception e) {
			throw new HeaderParseException("Parse error: " + e.getMessage());
		}
	}
}
