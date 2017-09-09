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

public class RawPacketHeader {
	// enterprise = 0, format = 1
	public static final int ETHERNET_ISO88023 = 1;
	public static final int TOKENBUS_ISO88024 = 2;
	public static final int TOKENRING_ISO88025 = 3;
	public static final int FDDI = 4;
	public static final int FRAME_RELAY = 5;
	public static final int X25 = 6;
	public static final int PPP = 7;
	public static final int SMDS = 8;
	public static final int AAL5 = 9;
	public static final int AAL5_IP = 10;  /* e.g. Cisco AAL5 mux */
	public static final int IPV4 = 11;
	public static final int IPV6 = 12;
	public static final int MPLS = 13;
	public static final int POS = 14;   /* RFC 1662, 2615 */
	public static final int IEEE80211MAC = 15;
	public static final int IEEE80211_AMPDU = 16;
	public static final int IEEE80211_AMSDU_SUBFRAME = 17;

	private long headerProtocol;        /* Format of sampled header */
	private long frameLength;           /* Original length of packet before sampling */
	private long stripped; 				/* number of bytes removed from the packet  */
	private long headerSize;
	
	private MacHeader macHeader;

	public long getHeaderProtocol() {
		return headerProtocol;
	}

	public long getFrameLength() {
		return frameLength;
	}

	public long getStripped() {
		return stripped;
	}

	public long getHeaderSize() {
		return headerSize;
	}

	public MacHeader getMacHeader() {
		return macHeader;
	}

	public void setHeaderProtocol(long headerProtocol) {
		this.headerProtocol = headerProtocol;
	}

	public void setFrameLength(long frameLength) {
		this.frameLength = frameLength;
	}

	public void setStripped(long stripped) {
		this.stripped = stripped;
	}

	public void setHeaderSize(long headerSize) {
		this.headerSize = headerSize;
	}

	public void setMacHeader(MacHeader macHeader) {
		this.macHeader = macHeader;
	}
	
	public static RawPacketHeader parse(byte[] data) throws HeaderParseException {
		try {
			if (data.length < 16) throw new HeaderParseException("Data array too short.");
			RawPacketHeader rp = new RawPacketHeader();

			// header protocol
			byte[] headerProtocol = new byte[4];
			System.arraycopy(data, 0, headerProtocol, 0, 4);
			rp.setHeaderProtocol(Utility.fourBytesToLong(headerProtocol));

			// frame length
			byte[] frameLength  = new byte[4];
			System.arraycopy(data, 4, frameLength, 0, 4);
			rp.setFrameLength(Utility.fourBytesToLong(frameLength));

			// stripped length
			byte[] stripped  = new byte[4];
			System.arraycopy(data, 8, stripped, 0, 4);
			rp.setStripped(Utility.fourBytesToLong(stripped));

			// header size
			byte[] headerSize  = new byte[4];
			System.arraycopy(data, 12, headerSize, 0, 4);
			rp.setHeaderSize(Utility.fourBytesToLong(headerSize));
			
			// 802.3 Ethernet Frame
			if (rp.getHeaderProtocol() == ETHERNET_ISO88023) {
				byte[] macHeader = new byte[data.length - 16]; 
				System.arraycopy(data, 16, macHeader, 0, data.length - 16);
				MacHeader m = MacHeader.parse(macHeader);
				rp.setMacHeader(m);
			} else if (rp.getHeaderProtocol() == IPV4) {
				/* ToDo : revisit */
				MacHeader m = new MacHeader();
				m.gotIPV4 = true;
				Ipv4Header h = Ipv4Header.parse(m);
				rp.setMacHeader(h);
			} else if (rp.getHeaderProtocol() == IPV6) {
				/* ToDo : revisit */
				MacHeader m = new MacHeader();
				m.gotIPV6 = true;
				Ipv6Header h = Ipv6Header.parse(m);
				rp.setMacHeader(h);
/*			} else if (rp.getHeaderProtocol() == IEEE80211MAC) {
				MacHeader m = new MacHeader();
				return WIFIHeader.parse(m);
*/
			} else {
				System.err.println("RawPacketHeader:Sample data format not yet supported: " + 
						rp.getHeaderProtocol());
			}
			
			return rp;
		}  catch (Exception e) {
			e.printStackTrace();
			throw new HeaderParseException("Parse error: " + e.getMessage());
		}		
	}
	
	public byte[] getBytes() throws HeaderBytesException {
		try {
			byte[] macHeaderBytes = macHeader.getBytes();
			byte[] data = new byte[16 + macHeaderBytes.length];
			// header protocol
			System.arraycopy(Utility.longToFourBytes(headerProtocol), 0, data, 0, 4);
			// interface type
			System.arraycopy(Utility.longToFourBytes(frameLength), 0, data, 4, 4);
			// stripped
			System.arraycopy(Utility.longToFourBytes(stripped), 0, data, 8, 4);
			// header size
			System.arraycopy(Utility.longToFourBytes(headerSize), 0, data, 12, 4);
			
			// mac header
			System.arraycopy(macHeaderBytes, 0, data, 16, macHeaderBytes.length);
			
			return data;
		} catch (Exception e) {
			throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[RawPacketheader]: ");
		sb.append(", HeaderProtocol: ");
		sb.append(getHeaderProtocol());
		sb.append(", FrameLength: ");
		sb.append(getFrameLength());
		sb.append(", StrippedBytes: ");
		sb.append(getStripped());
		sb.append(", HeaderSize: ");
		sb.append(getHeaderSize());
		sb.append(",\n hash: ");
		sb.append(macHeader.hash());
		sb.append(",\n");

		sb.append(macHeader);
		
		return sb.toString();
	}
}

