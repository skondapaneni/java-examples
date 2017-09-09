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
package com.sflow.packet.header.flowsample;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

public class FlowRecordHeader {
	private long flowDataFormat; // 20 bit enterprise & 12 bit format; standard enterprise 0, format 1, 2, 3, 4, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012 

	public static final int HEADERDATA_SFLOWv5             = 1;
	public static final int ETHERNETFRAMEDATA_SFLOWv5      = 2;
	public static final int IPv4DATA_SFLOWv5               = 3;
	public static final int IPv6DATA_SFLOWv5               = 4;
	public static final int SWITCHDATA_SFLOWv5             = 1001;
	public static final int ROUTERDATA_SFLOWv5             = 1002;
	public static final int GATEWAYDATA_SFLOWv5            = 1003;
	public static final int USERDATA_SFLOWv5               = 1004;
	public static final int URLDATA_SFLOWv5                = 1005;
	public static final int MPLSDATA_SFLOWv5               = 1006;
	public static final int NATDATA_SFLOWv5                = 1007;
	public static final int MPLSTUNNEL_SFLOWv5             = 1008;
	public static final int MPLSVC_SFLOWv5                 = 1009;
	public static final int MPLSFEC_SFLOWv5                = 1010;
	public static final int MPLSLVPFEC_SFLOWv5             = 1011;
	public static final int VLANTUNNEL_SFLOWv5             = 1012;
	public static final int EX_80211_PAYLOAD_SFLOWv5       = 1013;
	public static final int EX_80211_RX_SFLOWv5 		= 1014;
	public static final int EX_80211_TX_SFLOWv5 		= 1015;
	public static final int EX_AGGREGATION			= 1016;
	public static final int EX_NAT_PORT			= 1020;
	public static final int EX_L2_TUNNEL_OUT   		= 1021;
	public static final int EX_L2_TUNNEL_IN    		= 1022;
	public static final int EX_IPV4_TUNNEL_OUT 		= 1023;
	public static final int EX_IPV4_TUNNEL_IN  		= 1024;
	public static final int EX_IPV6_TUNNEL_OUT 		= 1025;
	public static final int EX_IPV6_TUNNEL_IN  		= 1026;
	public static final int EX_DECAP_OUT       		= 1027;
	public static final int EX_DECAP_IN        		= 1028;
	public static final int EX_VNI_OUT         		= 1029;
	public static final int EX_VNI_IN          		= 1030;
	public static final int EX_SOCKET4       		= 2100;
	public static final int EX_SOCKET6       		= 2101;
	public static final int EX_PROXYSOCKET4  		= 2102;
	public static final int EX_PROXYSOCKET6  		= 2103;
	public static final int MEMCACHE         		= 2200;
	public static final int HTTP             		= 2201;
	public static final int APP              		= 2202; /* transaction sample */
	public static final int APP_CTXT         		= 2203; /* enclosing server context */
	public static final int APP_ACTOR_INIT   		= 2204; /* initiator */
	public static final int APP_ACTOR_TGT    		= 2205; /* target */
	public static final int HTTP2            		= 2206;


	private long flowDataLength; // in byte
	
	private RawPacketHeader 	     rawPacket;
	private EthernetFlowRecord 	     ethData;
	private Ipv4FlowRecord 		     ipv4Data;
	private Ipv6FlowRecord 		     ipv6Data;
	private ExtendedSwitchFlowRecord     switchData;
	private ExtendedRouterFlowRecord     routerData;

	
	public FlowRecordHeader() {
	}

	public long getFlowDataFormat() {
		return flowDataFormat;
	}

	public long getFlowDataLength() {
		return flowDataLength;
	}

	public void setFlowDataFormat(long flowDataFormat) {
		this.flowDataFormat = flowDataFormat;
	}

	public void setFlowDataLength(long flowDataLength) {
		this.flowDataLength = flowDataLength;
	}
	
	public void setRawPacketHeader(RawPacketHeader rawPacket) {
		this.rawPacket = rawPacket;
	}
	
	public RawPacketHeader getRawPacketHeader() {
		return rawPacket;
	}
	
	public void setEthernetFlowRecord(EthernetFlowRecord data) {
		this.ethData = data;
	}
	
	public EthernetFlowRecord getEthernetFlowRecord() {
		return ethData;
	}

	public void setIpv4FlowRecord(Ipv4FlowRecord data) {
		this.ipv4Data = data;
	}
	
	public Ipv4FlowRecord getIpv4FlowRecord() {
		return ipv4Data;
	}

	public void setIpv6FlowRecord(Ipv6FlowRecord data) {
		this.ipv6Data = data;
	}
	
	public Ipv6FlowRecord getIpv6FlowRecord() {
		return ipv6Data;
	}

	public void setExtendedSwitchFlowRecord(ExtendedSwitchFlowRecord data) {
		this.switchData = data;
	}
	
	public ExtendedSwitchFlowRecord getExtendedSwitchFlowRecord() {
		return switchData;
	}

	public void setExtendedRouterFlowRecord(ExtendedRouterFlowRecord data) {
		this.routerData = data;
	}
	
	public ExtendedRouterFlowRecord getExtendedRouterFlowRecord() {
		return routerData;
	}
	
	public static FlowRecordHeader parse(byte[] data, int offset) throws HeaderParseException {
		try {
			if (data.length < 8) throw new HeaderParseException("Data array too short.");
			FlowRecordHeader frd = new FlowRecordHeader();
			
			frd.setFlowDataFormat(Utility.fourBytesToLong(data, 0+offset));
			frd.setFlowDataLength(Utility.fourBytesToLong(data, 4+offset));
			
			// raw packet header
			byte[] subData = new byte[(int) frd.getFlowDataLength()]; 
			System.arraycopy(data, 8+offset, subData, 0, (int) frd.getFlowDataLength());

			switch ((int)frd.getFlowDataFormat()) { 
			case HEADERDATA_SFLOWv5 :
				RawPacketHeader rp = RawPacketHeader.parse(subData);
				frd.setRawPacketHeader(rp);
//				if (rp.getMacHeader().type ==  0x88cc) {
//					System.out.println("lldp packet \n" + rp);
//				}
				break;
			case ETHERNETFRAMEDATA_SFLOWv5:
				EthernetFlowRecord eth = EthernetFlowRecord.parse(subData);
				frd.setEthernetFlowRecord(eth);
				break;
			case IPv4DATA_SFLOWv5:
				Ipv4FlowRecord ipv4 = Ipv4FlowRecord.parse(subData);
				frd.setIpv4FlowRecord(ipv4);
				break;
			case IPv6DATA_SFLOWv5:
				Ipv6FlowRecord ipv6 = Ipv6FlowRecord.parse(subData);
				frd.setIpv6FlowRecord(ipv6);
				break;
			case SWITCHDATA_SFLOWv5:
				ExtendedSwitchFlowRecord switchData = ExtendedSwitchFlowRecord.parse(subData);
				frd.setExtendedSwitchFlowRecord(switchData);
				break;
			case ROUTERDATA_SFLOWv5:
				ExtendedRouterFlowRecord routerData = ExtendedRouterFlowRecord.parse(subData);
				frd.setExtendedRouterFlowRecord(routerData);
				break;
			case GATEWAYDATA_SFLOWv5:
			case USERDATA_SFLOWv5:
			case URLDATA_SFLOWv5:
			case MPLSDATA_SFLOWv5:
			case NATDATA_SFLOWv5:
			case MPLSTUNNEL_SFLOWv5:
			case MPLSVC_SFLOWv5:
			case MPLSFEC_SFLOWv5:
			case MPLSLVPFEC_SFLOWv5:
			case VLANTUNNEL_SFLOWv5:
			default :
				System.out.printf("FlowRecord format %d not supported\n", frd.getFlowDataFormat());
			}

			return frd;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HeaderParseException("Parse error: " + e.getMessage());
		}
	}
	
	public byte[] getBytes() throws HeaderBytesException {
		try {
			byte[] rawPacketBytes = rawPacket.getBytes();
			byte[] data = new byte[8 + rawPacketBytes.length];
			// format
			System.arraycopy(Utility.longToFourBytes(flowDataFormat), 0, data, 0, 4);
			// length
			System.arraycopy(Utility.longToFourBytes(flowDataLength), 0, data, 4, 4);
			
			// raw packet header
			System.arraycopy(rawPacketBytes, 0, data, 8, rawPacketBytes.length);
			return data;
		} catch (Exception e) {
			throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n[FlowRecordHeader]:");
		sb.append("Format: ");
		sb.append(getFlowDataFormat());
		sb.append(", Length: ");
		sb.append(getFlowDataLength());
		sb.append(", ");
		
		switch ((int)getFlowDataFormat()) { 
		case HEADERDATA_SFLOWv5 :
			sb.append(getRawPacketHeader());
			break;
		case ETHERNETFRAMEDATA_SFLOWv5:
			sb.append(this.getEthernetFlowRecord());
			break;
		case IPv4DATA_SFLOWv5:
			sb.append(this.getIpv4FlowRecord());
			break;
		case IPv6DATA_SFLOWv5:
			sb.append(this.getIpv6FlowRecord());
			break;
		case SWITCHDATA_SFLOWv5:
			sb.append(this.getExtendedSwitchFlowRecord());
			break;
		case ROUTERDATA_SFLOWv5:
			sb.append(this.getExtendedRouterFlowRecord());
			break;
		case GATEWAYDATA_SFLOWv5:
		case USERDATA_SFLOWv5:
		case URLDATA_SFLOWv5:
		case MPLSDATA_SFLOWv5:
		case NATDATA_SFLOWv5:
		case MPLSTUNNEL_SFLOWv5:
		case MPLSVC_SFLOWv5:
		case MPLSFEC_SFLOWv5:
		case MPLSLVPFEC_SFLOWv5:
		case VLANTUNNEL_SFLOWv5:
		default :
			break;
		}

		return sb.toString();
	}
}
