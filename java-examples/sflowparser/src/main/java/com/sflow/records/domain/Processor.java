package com.sflow.records.domain;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Processor {
		
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
		
	@JsonProperty("eventTime")    
	private Date eventTime;
	
	@JsonProperty("sampleSequenceNumber")
	private long sampleSequenceNumber;   /* sample sequence number */

	@JsonProperty("sysUptime")
	private long sysUptime; // in milliseconds
	
	@JsonProperty("seqNumber")
	private long seqNumber;

	@JsonProperty("5sCpu")
	private long fiveSecCpu;
	
	@JsonProperty("1mCpu")
	private long oneMinCpu;
	
	@JsonProperty("5mCpu")
	private long fiveMinCpu;
	
	@JsonProperty("totalMemory")
	private BigInteger totalMemory;
	
	@JsonProperty("freeMemory")
	private BigInteger freeMemory;

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

	public int getBucket() {
		return bucket;
	}

	public void setBucket(int bucket) {
		this.bucket = bucket;
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

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public long getSampleSequenceNumber() {
		return sampleSequenceNumber;
	}

	public void setSampleSequenceNumber(long sampleSequenceNumber) {
		this.sampleSequenceNumber = sampleSequenceNumber;
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

	public long getFiveSecCpu() {
		return fiveSecCpu;
	}

	public void setFiveSecCpu(int fiveSecCpu) {
		this.fiveSecCpu = fiveSecCpu;
	}

	public long getOneMinCpu() {
		return oneMinCpu;
	}

	public void setOneMinCpu(int oneMinCpu) {
		this.oneMinCpu = oneMinCpu;
	}

	public long getFiveMinCpu() {
		return fiveMinCpu;
	}

	public void setFiveMinCpu(long l) {
		this.fiveMinCpu = l;
	}

	public BigInteger getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(BigInteger totalMemory) {
		this.totalMemory = totalMemory;
	}

	public BigInteger getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(BigInteger freeMemory) {
		this.freeMemory = freeMemory;
	}
	
	@Override
	public String toString() {
		return "Processor [ipVersionAgent=" + ipVersionAgent + ", ipAddress="
				+ ipAddress + ", bucket=" + bucket + ", subAgentID="
				+ subAgentID + ", sourceIDType=" + sourceIDType
				+ ", sourceIDIndex=" + sourceIDIndex + ", eventTime="
				+ eventTime + ", sampleSequenceNumber=" + sampleSequenceNumber
				+ ", sysUptime=" + sysUptime + ", seqNumber=" + seqNumber
				+ ", fiveSecCpu=" + fiveSecCpu + ", oneMinCpu=" + oneMinCpu
				+ ", fiveMinCpu=" + fiveMinCpu + ", totalMemory=" + totalMemory
				+ ", freeMemory=" + freeMemory + "]";
	}
}
