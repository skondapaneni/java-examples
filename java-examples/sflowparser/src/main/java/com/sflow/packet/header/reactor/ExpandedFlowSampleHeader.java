/*
 * This file is part of jsFlow.
 *
 * Copyright (c) 2009 DE-CIX Management GmbH <http://www.de-cix.net> - All rights
 * reserved.
 * 
 * Author: Thomas King <thomas.king@de-cix.net>
 *
 * This software is licensed under the Apache License, version 2.0. A copy of 
 * the license agreement is included in this distribution.
 */
package com.sflow.packet.header.reactor;

import java.util.Vector;

import reactor.bus.Event;

import com.sflow.packet.header.flowsample.FlowRecordHeader;
import com.sflow.packet.header.flowsample.reactor.ExpFlowSampleHeaderConsumer;
import com.sflow.packet.reactor.SFlowHeader;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

public class ExpandedFlowSampleHeader {
	// enterprise 0, format 3
	private long seqNumber;
	private long sourceIDType; // 0 = ifindex, 1 = smonVlanDataSource, 2 =
	// entPhysicalEntry
	private long sourceIDIndex;
	private long samplingRate;
	private long samplePool; // total number of packets that could have been
	// sampled
	private long drops; // packets dropped due a lack of resources
	private long inputInterfaceFormat;
	private long inputInterfaceValue;
	private long outputInterfaceFormat;
	private long outputInterfaceValue;
	private long numberFlowRecords;

	private Vector<FlowRecordHeader> flowRecords;

	private SFlowHeader sflowHeader;

	public ExpandedFlowSampleHeader() {
		flowRecords = new Vector<FlowRecordHeader>();
	}

	public long getSequenceNumber() {
		return seqNumber;
	}

	public long getSourceIDType() {
		return sourceIDType;
	}

	public long getSourceIDIndex() {
		return sourceIDIndex;
	}

	public long getSamplingRate() {
		return samplingRate;
	}

	public long getSamplePool() {
		return samplePool;
	}

	public long getDrops() {
		return drops;
	}

	public int getInputInterfaceFormat() {
		return (int) inputInterfaceFormat;
	}

	public long getInputInterfaceValue() {
		return inputInterfaceValue;
	}

	public int getOutputInterfaceFormat() {
		return (int) outputInterfaceFormat;
	}

	public long getOutputInterfaceValue() {
		return outputInterfaceValue;
	}

	public long getNumberFlowRecords() {
		return numberFlowRecords;
	}

	public void setSequenceNumber(long seqNumber) {
		this.seqNumber = seqNumber;
	}

	public void setSourceIDType(long sourceIDType) {
		this.sourceIDType = sourceIDType;
	}

	public void setSourceIDIndex(long sourceIDIndex) {
		this.sourceIDIndex = sourceIDIndex;
	}

	public void setSamplingRate(long samplingRate) {
		this.samplingRate = samplingRate;
	}

	public void setSamplePool(long samplePool) {
		this.samplePool = samplePool;
	}

	public void setDrops(long drops) {
		this.drops = drops;
	}

	public void setInputInterfaceFormat(long inputInterfaceFormat) {
		this.inputInterfaceFormat = inputInterfaceFormat;
	}

	public void setInputInterfaceValue(long inputInterfaceValue) {
		this.inputInterfaceValue = inputInterfaceValue;
	}

	public void setOutputInterfaceFormat(long outputInterfaceFormat) {
		this.outputInterfaceFormat = outputInterfaceFormat;
	}

	public void setOutputInterfaceValue(long outputInterfaceValue) {
		this.outputInterfaceValue = outputInterfaceValue;
	}

	public void setNumberFlowRecords(long numberFlowRecords) {
		this.numberFlowRecords = numberFlowRecords;
	}

	public void addFlowRecord(FlowRecordHeader flowRecord) {
		flowRecords.add(flowRecord);
	}

	public Vector<FlowRecordHeader> getFlowRecords() {
		return flowRecords;
	}

	public SFlowHeader getSflowHeader() {
		return sflowHeader;
	}

	public void setSflowHeader(SFlowHeader sflowHeader) {
		this.sflowHeader = sflowHeader;
	}

	public static ExpandedFlowSampleHeader parse(byte[] data, int offset, SFlowHeader sflowHeader)
			throws HeaderParseException {
		try {
			if (data.length < 44)
				throw new HeaderParseException("Data array too short.");
			ExpandedFlowSampleHeader efsh = new ExpandedFlowSampleHeader();
			efsh.setSflowHeader(sflowHeader);

			// sample sequence number
			efsh.setSequenceNumber(Utility.fourBytesToLong(data, 0 + offset));

			// source id type
			efsh.setSourceIDType(Utility.fourBytesToLong(data, 4 + offset));

			// source id index
			efsh.setSourceIDIndex(Utility.fourBytesToLong(data, 8 + offset));

			// sampling rate
			efsh.setSamplingRate(Utility.fourBytesToLong(data, 12 + offset));

			// sample pool
			efsh.setSamplePool(Utility.fourBytesToLong(data, 16 + offset));

			// drops
			efsh.setDrops(Utility.fourBytesToLong(data, 20 + offset));

			// input interface format
			efsh.setInputInterfaceFormat(Utility.fourBytesToLong(data, 24 + offset));

			// input interface value
			efsh.setInputInterfaceValue(Utility.fourBytesToLong(data, 28 + offset));

			// output interface format
			efsh.setOutputInterfaceFormat(Utility.fourBytesToLong(data, 32 + offset));

			// output interface value
			efsh.setOutputInterfaceValue(Utility.fourBytesToLong(data, 36 + offset));

			// number flow records
			efsh.setNumberFlowRecords(Utility.fourBytesToLong(data, 40 + offset));

			// flow records
			int offset2 = 44 + offset;
			for (int i = 0; i < efsh.getNumberFlowRecords(); i++) {
				FlowRecordHeader fr = FlowRecordHeader.parse(data, offset2);
				efsh.addFlowRecord(fr);
				offset2 += (fr.getFlowDataLength() + 8);
			}

			// publish the event to for listeners
			sflowHeader.getCollector().getRawPacketRecordBus()
					.notify(ExpFlowSampleHeaderConsumer.expandedFlowSampleEvent, Event.wrap(efsh));

			return efsh;
		} catch (Exception e) {
			throw new HeaderParseException("Parse error: " + e.getMessage());
		}
	}

	public byte[] getBytes() throws HeaderBytesException {
		try {
			int lengthFlowRecords = 0;
			for (FlowRecordHeader fr : flowRecords) {
				lengthFlowRecords += (fr.getFlowDataLength() + 8);
			}
			byte[] data = new byte[44 + lengthFlowRecords];
			// sequence number
			System.arraycopy(Utility.longToFourBytes(seqNumber), 0, data, 0, 4);
			// source id type
			System.arraycopy(Utility.longToFourBytes(sourceIDType), 0, data, 4, 4);
			// source id index
			System.arraycopy(Utility.longToFourBytes(sourceIDIndex), 0, data, 8, 4);
			// sampling rate
			System.arraycopy(Utility.longToFourBytes(samplingRate), 0, data, 12, 4);
			// sample pool
			System.arraycopy(Utility.longToFourBytes(samplePool), 0, data, 16, 4);
			// drops
			System.arraycopy(Utility.longToFourBytes(drops), 0, data, 20, 4);
			// input interface format
			System.arraycopy(Utility.longToFourBytes(inputInterfaceFormat), 0, data, 24, 4);
			// input interface value
			System.arraycopy(Utility.longToFourBytes(inputInterfaceValue), 0, data, 28, 4);
			// output interface format
			System.arraycopy(Utility.longToFourBytes(outputInterfaceFormat), 0, data, 32, 4);
			// output interface value
			System.arraycopy(Utility.longToFourBytes(outputInterfaceValue), 0, data, 36, 4);
			// number flow records
			System.arraycopy(Utility.longToFourBytes(numberFlowRecords), 0, data, 40, 4);

			int offset = 0;
			for (FlowRecordHeader fr : flowRecords) {
				byte[] temp = fr.getBytes();
				System.arraycopy(temp, 0, data, 44 + offset, temp.length);
				offset += (fr.getFlowDataLength() + 8);
			}
			return data;
		} catch (Exception e) {
			throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ExpandedFlowSampleHeader]: ");
		sb.append("SequenceNumber: ");
		sb.append(getSequenceNumber());
		sb.append(", SourceIDtype: ");
		sb.append(getSourceIDType());
		sb.append(", SourceIDindex: ");
		sb.append(getSourceIDIndex());
		sb.append(", SamplingRate: ");
		sb.append(getSamplingRate());
		sb.append(", SamplePool: ");
		sb.append(getSamplePool());
		sb.append(", Drops: ");
		sb.append(getDrops());
		sb.append(", InputInterfaceFormat: ");
		sb.append(getInputInterfaceFormat());
		sb.append(", InputInterfaceValue: ");
		sb.append(getInputInterfaceValue());
		sb.append(", OutputInterfaceFormat: ");
		sb.append(getOutputInterfaceFormat());
		sb.append(", OutputInterfaceValue: ");
		sb.append(getOutputInterfaceValue());
		sb.append(", FlowRecords: ");
		sb.append(getNumberFlowRecords());
		for (FlowRecordHeader frh : flowRecords) {
			sb.append(frh);
			sb.append(" ");
		}

		return sb.toString();
	}
}
