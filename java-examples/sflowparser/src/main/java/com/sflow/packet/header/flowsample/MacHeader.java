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
 * 
 * Author: Srihari.K -  Modifed to support IPV4/ARP/IPV6 parsing..
 */
package com.sflow.packet.header.flowsample;

import com.sflow.util.FlowFilter;
import com.sflow.util.FlowFilterE;
import com.sflow.util.MurmurHash3;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.MacAddress;
import com.sflow.util.Utility;

// decodeLinkLayer
public class MacHeader {
     protected MacAddress destination; // destinationMac
     protected MacAddress source; // sourceMac
     protected int type; // PacketType
     protected String frameType;
     protected byte offcut[];
     protected byte data;
     protected FlowFilter flowFilter;
     protected int  offset;

     protected boolean gotIPV4 = false;
     protected boolean gotIPV6 = false;

     public static int ETH_TYPE_UNK = 0x0000;
     public static int ETH_TYPE_XNS_IDP = 0x0600;
     public static int ETH_TYPE_IP = 0x0800;
     public static int ETH_TYPE_X25L3 = 0x0805;
     public static int ETH_TYPE_ARP = 0x0806;
     public static int ETH_TYPE_VINES_IP = 0x0bad;
     public static int ETH_TYPE_VINES_ECHO = 0x0baf;
     public static int ETH_TYPE_TRAIN = 0x1984;
     public static int ETH_TYPE_CGMP = 0x2001;
     public static int ETH_TYPE_CENTRINO_PROMISC = 0x2452;
     public static int ETH_TYPE_3C_NBP_DGRAM = 0x3c07;
     public static int ETH_TYPE_EPL_V1 = 0x3e3f;
     public static int ETH_TYPE_DEC = 0x6000;
     public static int ETH_TYPE_DNA_DL = 0x6001;
     public static int ETH_TYPE_DNA_RC = 0x6002;
     public static int ETH_TYPE_DNA_RT = 0x6003;
     public static int ETH_TYPE_LAT = 0x6004;
     public static int ETH_TYPE_DEC_DIAG = 0x6005;
     public static int ETH_TYPE_DEC_CUST = 0x6006;
     public static int ETH_TYPE_DEC_SCA = 0x6007;
     public static int ETH_TYPE_ETHBRIDGE = 0x6558;
     public static int ETH_TYPE_RAW_FR = 0x6559;
     public static int ETH_TYPE_RARP = 0x8035;
     public static int ETH_TYPE_DEC_LB = 0x8038;
     public static int ETH_TYPE_DEC_LAST = 0x8041;
     public static int ETH_TYPE_APPLETALK = 0x809b;
     public static int ETH_TYPE_SNA = 0x80d5;
     public static int ETH_TYPE_AARP = 0x80f3;
     public static int ETH_TYPE_VLAN = 0x8100;
     public static int ETH_TYPE_IPX = 0x8137;
     public static int ETH_TYPE_SNMP = 0x814c;
     public static int ETH_TYPE_WCP = 0x80ff;
     public static int ETH_TYPE_STP = 0x8181;
     public static int ETH_TYPE_ISMP = 0x81fd;
     public static int ETH_TYPE_ISMP_TBFLOOD = 0x81ff;
     public static int ETH_TYPE_IPv6 = 0x86dd;
     public static int ETH_TYPE_WLCCP = 0x872d;
     public static int ETH_TYPE_MAC_CONTROL = 0x8808;
     public static int ETH_TYPE_SLOW_PROTOCOLS = 0x8809;
     public static int ETH_TYPE_PPP = 0x880b;
     public static int ETH_TYPE_COBRANET = 0x8819;
     public static int ETH_TYPE_MPLS = 0x8847;
     public static int ETH_TYPE_MPLS_MULTI = 0x8848;
     public static int ETH_TYPE_FOUNDRY = 0x885a;
     public static int ETH_TYPE_PPPOED = 0x8863;
     public static int ETH_TYPE_PPPOES = 0x8864;
     public static int ETH_TYPE_INTEL_ANS = 0x886d;
     public static int ETH_TYPE_MS_NLB_HEARTBEAT = 0x886f;
     public static int ETH_TYPE_CDMA2000_A10_UBS = 0x8881;
     public static int ETH_TYPE_EAPOL = 0x888e;
     public static int ETH_TYPE_PROFINET = 0x8892;
     public static int ETH_TYPE_HYPERSCSI = 0x889a;
     public static int ETH_TYPE_CSM_ENCAPS = 0x889b;
     public static int ETH_TYPE_TELKONET = 0x88a1;
     public static int ETH_TYPE_AOE = 0x88a2;
     public static int ETH_TYPE_EPL_V2 = 0x88ab;
     public static int ETH_TYPE_BRDWALK = 0x88ae;
     public static int ETH_TYPE_IEEE802_OUI_EXTENDED = 0x88b7;
     public static int ETH_TYPE_IEC61850_GOOSE = 0x88b8;
     public static int ETH_TYPE_IEC61850_GSE = 0x88b9;
     public static int ETH_TYPE_IEC61850_SV = 0x88ba;
     public static int ETH_TYPE_TIPC = 0x88ca;
     public static int ETH_TYPE_RSN_PREAUTH = 0x88c7;
     public static int ETH_TYPE_LLDP = 0x88cc;
     public static int ETH_TYPE_3GPP2 = 0x88d2;
     public static int ETH_TYPE_MRP = 0x88e3;
     public static int ETH_TYPE_LOOP = 0x9000;
     public static int ETH_TYPE_RTMAC = 0x9021;
     public static int ETH_TYPE_RTCFG = 0x9022;
     public static int ETH_TYPE_LLT = 0xcafe;
     public static int ETH_TYPE_FCFT = 0xfcfc;

     public MacHeader() {
		flowFilter = new FlowFilter();
     }
     
     public MacAddress getDestination() {
          return destination;
     }

     public MacAddress getSource() {
          return source;
     }

     public int getType() {
          return type;
     }

     public String getFrameType() {
          return frameType;
     }

     public void setDestination(MacAddress destination) {
          this.destination = destination;
     }

     public void setSource(MacAddress source) {
          this.source = source;
     }

     public void setType(int type) {
          this.type = type;
     }

     public void setOffCut(byte offcut[]) {
          this.offcut = offcut;
     }
     
     public int hash() {   	 
    	   return  MurmurHash3.murmurhash3_x86_32(flowFilter.getFilterAsByteArray(), 
    			   0, flowFilter.getFilterAsByteArray().length, 0);
     }

     public static MacHeader parse(byte data[]) throws HeaderParseException {

          MacHeader m = null;
          int type;
          int type_offset = 12;
          int layer3_offset = 14;

          try {
               if (data.length < layer3_offset)
                    throw new HeaderParseException("Data array too short.");
               if (data.length > 256)
                    throw new HeaderParseException("Data array too big.");

               // ETH_TYPE_VLAN
               if ((data[type_offset] == (byte) (0x81 & 0xFF))
                         && (data[type_offset + 1] == (byte) (0x00 & 0xFF))) {
                    if (data.length < (type_offset + 4)) {
                         throw new HeaderParseException(
                                   "Data array too short for VLAN packet.");
                    }
                    m = TaggedMacHeader.parse(data); // 4 bytes vlan tag
                    type_offset += 4;
                    layer3_offset += 4;
               } else {
                    m = new MacHeader();
                                        
                    // destination
                    m.flowFilter.setValue(FlowFilterE.DMAC, data, 0);
                    
                    // source
                    m.flowFilter.setValue(FlowFilterE.SMAC, data, 6);

                    // destination
                    m.setDestination(new MacAddress(data, 0));
                    
                    // source
                    m.setSource(new MacAddress(data, 6));
               }

               type = Utility.twoBytesToInteger(data, type_offset);
               // type
               m.setType(type);
               
               m.flowFilter.setValue(FlowFilterE.ETYPE, data, type_offset);

               if (type <= 1500) {

                    int sap_offset = type_offset + 2;

                    // 802.2 LLC
                    /*
                     * The upper layer decoding information is provided by 3 fields,
                     * which are placed inside the payload section of the Ethernet
                     * Frame. The fields eat into the payload field, and as a result
                     * there are only 1497 bytes available for payload in an LLC
                     * encoded frame.
                     * 
                     * Destination Service Access Point (DSAP) 1byte Source Service
                     * Access Point (SSAP) 1byte Control Field 1 or 2 bytes
                     * 
                     * LLC Fields
                     * 
                     * The SAP value for IPV4 is 0x06. IPV6 does not have a SAP
                     * VALUE
                     */

                    // should we verify if the control field (2b LSB) is == 3 ?
                    layer3_offset += 3; // Move past the DSAP+SSAP + control field..
                    m.frameType = "Ethernet 802.2 LLC";
                    if (data.length < layer3_offset) {
                         throw new HeaderParseException(
                                   "Data array too short for 802.LLC packet.");
                    }

                    if ((data[sap_offset] == (byte) (0x06 & 0xFF))
                              && (data[sap_offset + 1] == (byte) (0x06 & 0xFF))) {
                         // offcut
                         byte offcut[] = new byte[data.length - layer3_offset];
                         System.arraycopy(data, layer3_offset, offcut, 0,
                                   data.length - layer3_offset);
                         m.setOffCut(offcut);
                         return Ipv4Header.parse(m);

                    } else if ((data[sap_offset] == (byte) (0x98 & 0xFF))
                              && (data[sap_offset + 1] == (byte) (0x98 & 0xFF))) {
                         // offcut
                         byte offcut[] = new byte[data.length - layer3_offset];
                         System.arraycopy(data, layer3_offset, offcut, 0,
                                   data.length - layer3_offset);
                         m.setOffCut(offcut);
                         return ArpHeader.parse(m);

                    } else if ((data[sap_offset] == (byte) (0xAA & 0xFF))
                              && (data[sap_offset + 1] == (byte) (0xAA & 0xFF))) {
                         /*
                          * Although IPv4 has assigned SAP value of 6 and there is
                          * even a SAP value of 98 hex for ARP, IP traffic is almost
                          * never encapsulated in IEEE 802.2 LLC frames without SNAP.
                          * Instead, the Internet standard RFC 1042 is usually used
                          * for encapsulating IP version 4 traffic in IEEE 802.2
                          * frames with LLC/SNAP headers in FDDI and IEEE 802
                          * networks. In Ethernet/IEEE 802.3 networks using Ethernet
                          * II framing with EtherType 800 hex for IP and 806 hex for
                          * ARP is more common.[6]
                          */
                         int ether_type_offset = layer3_offset + 3; // + OUI (3
                         // bytes)
                         layer3_offset = ether_type_offset + 2; // skip past the
                         // ether_type
                         m.frameType = "Ethernet 802.2 LLC SNAP";

                         // SNAP Encoding.. (Sub Network attachment protocol)
                         if (data.length < ether_type_offset + 2) {
                              throw new HeaderParseException(
                                        "Data array too short for 802.LLC SNAP packet.");
                         }

                         // offcut
                         byte offcut[] = new byte[data.length - layer3_offset];
                         System.arraycopy(data, layer3_offset, offcut, 0,
                                   data.length - layer3_offset);
                         m.setOffCut(offcut);

                         // IPV4
                         if ((data[ether_type_offset] == (byte) (0x08 & 0xFF))
                                   && (data[ether_type_offset + 1] == (byte) (0x00 & 0xFF))) {
                              return Ipv4Header.parse(m);
                         } else if ((data[ether_type_offset] == (byte) (0x08 & 0xFF))
                                   && (data[ether_type_offset + 1] == (byte) (0x06 & 0xFF))) {
                              return ArpHeader.parse(m);
                         } else if ((data[ether_type_offset] == (byte) (0x86 & 0xFF))
                                   && (data[ether_type_offset + 1] == (byte) (0xDD & 0xFF))) {
                              return Ipv6Header.parse(m);
                         } else if ((data[type_offset] == (byte) (0x88 & 0xFF))
                                   && (data[type_offset + 1] == (byte) (0xCC & 0xFF))) {
                              //System.out.println("parse lldp packet");
                              LLDPHeader h = LLDPHeader.parse(m);
                              //System.out.println("LLDP --- " + h.toString());
                              return h;
                         }
                    } else {
                         // offcut
                         byte offcut[] = new byte[data.length - layer3_offset];
                         System.arraycopy(data, layer3_offset, offcut, 0,
                                   data.length - layer3_offset);
                         m.setOffCut(offcut);
                    }
               } else if (type > 1536) {
                    // Ethernet 11 (DIX)

                    m.frameType = "Ethernet 11 (DIX)";

                    if (data.length < (type_offset + 2)) {
                         throw new HeaderParseException(
                                   "Data array too short for Ethernet 11 (DIX) packet.");
                    }

                    byte offcut[] = new byte[data.length - layer3_offset];
                    System.arraycopy(data, layer3_offset, offcut, 0, data.length
                              - layer3_offset);
                    m.setOffCut(offcut);

                    // IPV4
                    if ((data[type_offset] == (byte) (0x08 & 0xFF))
                              && (data[type_offset + 1] == (byte) (0x00 & 0xFF))) {
                         return Ipv4Header.parse(m);
                    } else if ((data[type_offset] == (byte) (0x08 & 0xFF))
                              && (data[type_offset + 1] == (byte) (0x06 & 0xFF))) {
                         return ArpHeader.parse(m);
                    } else if ((data[type_offset] == (byte) (0x86 & 0xFF))
                              && (data[type_offset + 1] == (byte) (0xDD & 0xFF))) {
                         return Ipv6Header.parse(m);
                    } else if ((data[type_offset] == (byte) (0x88 & 0xFF))
                              && (data[type_offset + 1] == (byte) (0xCC & 0xFF))) {
                         //System.out.println("parse lldp packet");
                         LLDPHeader h = LLDPHeader.parse(m);
                         //System.out.println("LLDP --- " + h.toString());
                         return h;
                    }

               } else if (type > 1500) {
                    System.out.println("Data array size is undefined!!");
                    byte offcut[] = new byte[data.length - type_offset];
                    System.arraycopy(data, type_offset, offcut, 0, data.length
                              - type_offset);
                    m.setOffCut(offcut);
               }

               return m;
          } catch (Exception e) {
               e.printStackTrace();
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }

     public byte[] getBytes() throws HeaderBytesException {
          try {
               byte[] data = new byte[14 + offcut.length];
               // destination
               System.arraycopy(destination, 0, data, 0, 6);
               // source
               System.arraycopy(source, 0, data, 6, 6);
               // type
               System.arraycopy(Utility.integerToTwoBytes(type), 0, data, 12, 2);
               // offcut
               System.arraycopy(offcut, 0, data, 14, offcut.length);

               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: "
                         + e.getMessage());
          }
     }

     public FlowFilter getFlowFilter() {
		return flowFilter;
	}

	public void setFlowFilter(FlowFilter flowFilter) {
		this.flowFilter = flowFilter;
	}

	public String toString() {
          StringBuilder sb = new StringBuilder();

          sb.append("\n ---- Ether Header -----");
          sb.append("\n" + frameType);

          sb.append(", Src: ");
          sb.append(getSource());

          sb.append(", Dst: ");
          sb.append(getDestination());

          sb.append(", PacketType: 0x0");
          sb.append(Integer.toHexString(getType()));

          if (offcut != null) {
               sb.append(", payload length: ");
               sb.append(offcut.length);
          }

          return sb.toString();
     }
}
