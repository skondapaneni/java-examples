package com.sflow.packet.header.flowsample;

/*
LLDPHeader.java

Copyright (C) 2010  Rice University

This software is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This software is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this software; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

====================================================================

Each frame contains one Link Layer Discovery Protocol Data Unit.
Each LLDPDU is a sequence of type-length-value (TLV) structures.

LLDPDU format

+------+-----+----+----+-----+-----+----+-------------------+
| Chassis ID | Port ID | TTL | Optional | End of LLDPDU TLV |
|    TLV     |  TLV    | TLV |   TLVs   |  type=0 length=0  |
+------+-----+----+----+-----+-----+----+-------------------+

TLV Format

+----------+------------+------------------+
| TLV type | TLV length |    TLV Value     |
| (7 bits) |  (9 bits)  | (0 to 510 bytes) |
+----------+------------+------------------+

TLV Types:

0   - end of LLDPDU
1   - Chassis ID
2   - Port ID
3   - TTL
4   - Port description (optional)
5   - System name
6   - System description
7   - System capabilities
8   - Management address
127 - Organization specific TLVs                                                                                                               

====================================================================
*/

import java.nio.charset.Charset;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sflow.util.Address;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.MacAddress;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;


public class LLDPHeader extends MacHeader {

    private static final Logger log = LogManager.getLogger(LLDPHeader.class.getName());

     public static class TLV {
          public int type;
          public int length;
          public byte[] value;
          
          public int chassisIdType;
          public String chassisId;
          public int portIdType;
          public String portId;
          public Address mgmtIp;
          public int ttl;

          public int parseTLV(byte[] buf, int pos) throws UtilityException {
               
               int bufLen = buf.length;
               
               //. Read the first 8 bits from the buffer
               short tmp = Utility.oneByteToShort(buf, pos);
               pos+=1;

               //. Get the first 7 bits
               type = (tmp & 0xFE) >> 1;
               

               //. Get the last 1 bit
               length = (tmp & 0x01) << 8;
               
               // Read the second 8 bits from the buffer
               tmp = Utility.oneByteToShort(buf, pos);
               pos+=1;
               length += tmp;
               
               if (pos+length > bufLen) {
                    System.out.println(Thread.currentThread()  + " pos + length exceeds bufLen!!" + bufLen);
                    throw new UtilityException("Error : pos + length exceeds bufLen!!");
               }
               
               value = new byte[length];
               System.arraycopy(buf, pos, value, 0, length);
                      pos += length;
          
               if (type == TLV_TYPE_CHASSIS_ID) {
                    chassisIdType = Utility.oneByteToShort(value, 0);
                    byte[] chassisIdArr = new byte[length-1];
                    System.arraycopy(value, 1, chassisIdArr, 0, length-1);
                    if (chassisIdType == SUB_CHASSIS_MAC_ADDRESS) {
                         chassisId = new MacAddress(chassisIdArr).toString();
                    } else {
                         chassisId =  new String(chassisIdArr, Charset.forName("UTF-8"));
                    }
               } else if (type == TLV_TYPE_PORT_ID) {
                    portIdType = Utility.oneByteToShort(value, 0);
                    byte[] portIdArr = new byte[length-1];
                    System.arraycopy(value, 1, portIdArr, 0, length-1);
                    if (portIdType == SUB_MAC_ADDRESS) {
                         portId = new MacAddress(portIdArr).toString();
                    } else {
                         portId = new String(portIdArr, Charset.forName("UTF-8"));
                    }
               } else if (type == TLV_TYPE_MGMT_ADDR) {
                    int addrType = Utility.oneByteToShort(value, 0);
                    if (addrType == 1) {
                         byte[] mgmtIpArr = new byte[length-1];
                         System.arraycopy(value, 1, mgmtIpArr, 0, length-1);
                         mgmtIp = new Address(mgmtIpArr);
                    } else {
                         System.out.println("unsupported addr type " + addrType + " in lldp packet");
                    }
               } else if (type == TLV_TYPE_TTL) {
                    ttl = Utility.twoBytesToInteger(value, 0);
               } 
               
               return pos;
          }

//          public int convertToBytes(byte[] buf, int pos) {
//               int typeLength = length;
//               typeLength += (type << 9);
//               pos += Utilities.setNetworkBytesUint16(buf, pos, typeLength);
//
//               for (int i = 0; i < length; i++)
//                    buf[pos++] = value[i];
//
//               return pos;
//          }
          
          public String toString() {
               // . Currently print out only that it is a LLDPDU
               StringBuilder sb = new StringBuilder();
               switch(type) {
               case TLV_TYPE_CHASSIS_ID:
                    sb.append("\n------ CHASSIS ID -------------");
                    switch (chassisIdType) {
                    case SUB_CHASSIS_COMPONENT:
                         sb.append("\nchassis_Component: " + chassisId);
                         break;
                    case SUB_CHASSIS_INTERFACE_ALIAS:
                         sb.append("\nchassis_IntfAlias: " + chassisId);
                         break;
                    case SUB_CHASSIS_PORT_COMPONENT:
                         sb.append("\nchassis_PhysAlias: " + chassisId);
                         break;
                    case SUB_CHASSIS_MAC_ADDRESS:
                         sb.append("\nchassis_MacAddr: " + chassisId);
                         break;
                    case SUB_CHASSIS_NETWORK_ADDRESS:
                         sb.append("\nchassis_NetworkAddr: " + chassisId);
                         break;
                    case SUB_CHASSIS_INTERFACE_NAME:
                         sb.append("\nchassis_IntfName: " + chassisId);
                         break;
                    case SUB_CHASSIS_LOCALLY_ASSIGNED:
                         sb.append("\nchassis_local: " + chassisId);
                         break;
                    default:
                         sb.append("\nchassis_unknown: " + chassisId);

                    }
                    break;

               case TLV_TYPE_PORT_ID:
                    switch (portIdType) {
                    case SUB_INTERFACE_ALIAS:
                         sb.append("\nIntfAlias: " + portId);
                         break;
                    case SUB_PORT_COMPONENT:
                         sb.append("\nPhysAlias: " + portId);
                         break;
                    case SUB_MAC_ADDRESS:
                         sb.append("\nMacAddr: " + portId);
                         break;
                    case SUB_NETWORK_ADDRESS:
                         sb.append("\nNetworkAddr: " + portId);
                         break;
                    case SUB_INTERFACE_NAME:
                         sb.append("\nIntfName: " + portId);
                         break;
                    case SUB_LOCALLY_ASSIGNED:
                         sb.append("\nport local: " + portId);
                         break;
                    default:
                         sb.append("\nportId_unknown: " + portId);
                    }
                    break;

               case TLV_TYPE_TTL:
                    sb.append("\nTTL: " + ttl);
                    break;
               case TLV_TYPE_PORT_DESC:
                    sb.append("\nPortDesc: "
                              + new String(value, Charset.forName("UTF-8")) + "\n");
                    break;
               case TLV_TYPE_SYSTEM_NAME:
                    sb.append("\nSystemName: "
                              + new String(value, Charset.forName("UTF-8")) + "\n");
                    break;
               case TLV_TYPE_SYSTEM_DESC:
                    sb.append("\nSystemDesc: "
                              + new String(value, Charset.forName("UTF-8")) + "\n");
                    break;

               case TLV_TYPE_SYSTEM_CAPA:
                    sb.append("\nSystemCapabilities: "
                              + new String(value, Charset.forName("UTF-8")) + "\n");
                    break;

               case TLV_TYPE_MGMT_ADDR:
                    sb.append("\nMgmtIp: " + mgmtIp + "\n");
                    break;

               case TLV_TYPE_ORG_SPECIFIC:

                    Formatter formatter = new Formatter(sb, Locale.US);

                    // Explicit argument indices may be used to re-order output.
                    formatter.format("\nOrgSpecific: %02x-%02x-%02x", value[0],
                              value[1], value[2]);
                    try {
                         int subType = Utility.oneByteToShort(value, 3);
                         sb.append("\n\tsubType: " + Integer.toHexString(subType));
                         byte[] orgSpecific = new byte[length - 4];
                         System.arraycopy(value, 4, orgSpecific, 0, length - 4);
                         sb.append("\n\t"
                                   + new String(orgSpecific, Charset.forName("UTF-8"))
                                   + "\n");
                    } catch (UtilityException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    }

                    break;

               case TLV_TYPE_END:
               default:
                    break;
               }
               sb.append("\n");
               return sb.toString();
          }
     }
     
     /*
      * 5 - System name 
      * 6 - System description 
      * 7 - System capabilities 
      * 8 - Management address 127 - Organization specific TLVs
      */

     /** Constants */
     public static final int TLV_TYPE_CHASSIS_ID = 1;
     public static final int TLV_TYPE_PORT_ID = 2;
     public static final int TLV_TYPE_TTL = 3;
     public static final int TLV_TYPE_PORT_DESC = 4;
     public static final int TLV_TYPE_SYSTEM_NAME = 5;
     public static final int TLV_TYPE_SYSTEM_DESC = 6;
     public static final int TLV_TYPE_SYSTEM_CAPA = 7;
     public static final int TLV_TYPE_MGMT_ADDR = 8;
     public static final int TLV_TYPE_ORG_SPECIFIC = 127;
     public static final int TLV_TYPE_END = 0;
     public static final int TLV_LENGTH_END = 0;
     
     public static final int SUB_CHASSIS_COMPONENT = 1; //  # EntPhysicalAlias (IETF RFC 4133)
     public static final int SUB_CHASSIS_INTERFACE_ALIAS = 2;   //  # IfAlias (IETF RFC 2863)
     public static final int SUB_CHASSIS_PORT_COMPONENT = 3;    //  # EntPhysicalAlias (IETF RFC 4133)
     public static final int SUB_CHASSIS_MAC_ADDRESS = 4;       //  # MAC address (IEEE std 802)
     public static final int SUB_CHASSIS_NETWORK_ADDRESS = 5;   //  # networkAddress
     public static final int SUB_CHASSIS_INTERFACE_NAME = 6;    //  # IfName (IETF RFC 2863)
     public static final int SUB_CHASSIS_LOCALLY_ASSIGNED = 7;  //  # local

     public static final int SUB_INTERFACE_ALIAS = 1; //   # ifAlias (IETF RFC 2863)
     public static final int SUB_PORT_COMPONENT = 2; //    # entPhysicalAlias (IETF RFC 4133)
     public static final int SUB_MAC_ADDRESS = 3;      //    # MAC address (IEEE Std 802)
     public static final int SUB_NETWORK_ADDRESS = 4; //   # networkAddress
     public static final int SUB_INTERFACE_NAME = 5; //    # ifName (IETF RFC 2863)
     public static final int SUB_AGENT_CIRCUIT_ID = 6; //  # agent circuit ID(IETF RFC 3046)
     public static final int SUB_LOCALLY_ASSIGNED = 7; //  # local
     
//     CAP_REPEATER = (1 << 1)             # IETF RFC 2108
//     CAP_MAC_BRIDGE = (1 << 2)           # IEEE Std 802.1D
//     CAP_WLAN_ACCESS_POINT = (1 << 3)    # IEEE Std 802.11 MIB
//     CAP_ROUTER = (1 << 4)               # IETF RFC 1812
//     CAP_TELEPHONE = (1 << 5)            # IETF RFC 4293
//     CAP_DOCSIS = (1 << 6)               # IETF RFC 4639 and IETF RFC 4546
//     CAP_STATION_ONLY = (1 << 7)         # IETF RFC 4293
//     CAP_CVLAN = (1 << 8)                # IEEE Std 802.1Q
//     CAP_SVLAN = (1 << 9)                # IEEE Std 802.1Q
//     CAP_TPMR = (1 << 10)                # IEEE Std 802.1Q
//              
     /** Members */
     /** Chassis ID */
     public TLV chassisId;

     /** Port ID */
     public TLV portId;

     /** TTL */
     public TLV ttl;

     /** optional */
     public LinkedList<TLV> optional;

     protected byte payload[];
     MacHeader m;
     
     /**
      * Constructor of LLDPHeader
      */
     public LLDPHeader(MacHeader m) {
          chassisId = new TLV();
          portId = new TLV();
          ttl = new TLV();
          optional = new LinkedList<TLV>();
          this.m = m;
     }

     public MacHeader getMacHeader() {
          return m;
     }
          
     public String getSysName() {
          Iterator<TLV> iter = optional.iterator();
          while (iter.hasNext()) {
               TLV next = iter.next();
               if (next.type ==
                         TLV_TYPE_SYSTEM_NAME) {
                    return new String(next.value, Charset.forName("UTF-8"));
               }
          }
          return new String("");
     }
     
     public Address getMgmtIp() {
          Iterator<TLV> iter = optional.iterator();
          while (iter.hasNext()) {
               TLV next = iter.next();
               if (next.type ==
                         TLV_TYPE_MGMT_ADDR) {
                    try {
                         return new Address(next.value);
                    } catch (UtilityException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    }
                    //return new String(next.value, Charset.forName("UTF-8"));
               }
          }
          return null;
     }

     public MacAddress getMacAddr() {
          if (chassisId.type  == TLV_TYPE_CHASSIS_ID) {
               int chassisIdType = 0;
               try {
                    chassisIdType = Utility.oneByteToShort(chassisId.value, 0);
               } catch (UtilityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
               }
               byte[] chassisIdArr = new byte[chassisId.length-1];
               System.arraycopy(chassisId.value, 1, chassisIdArr, 0, chassisId.length-1);
               if (chassisIdType == SUB_CHASSIS_MAC_ADDRESS) {
                    try {
                         return new MacAddress(chassisIdArr);
                    } catch (UtilityException e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                    }
               } 
          }
          return null;
     }
     
     public String getLinkName() {
          Iterator<TLV> iter = optional.iterator();
          while (iter.hasNext()) {
               TLV next = iter.next();
               if (next.type ==
                         TLV_TYPE_ORG_SPECIFIC) {
                    byte[] orgSpecific = new byte[next.length-4];
                    System.arraycopy(next.value, 4, orgSpecific, 0, next.length-4);     
                    return new String(orgSpecific, Charset.forName("UTF-8"));
               }
          }
          return new String("");
     }
     
     public int getTtl() {
          return ttl.ttl;
     }

     public TLV getChassisId() {
          return chassisId;
     }

     public void setChassisId(TLV chassisId) {
          this.chassisId = chassisId;
     }

     public TLV getPortId() {
          return portId;
     }

     public void setPortId(TLV portId) {
          this.portId = portId;
     }

     public TLV getTtlTlv() {
          return ttl;
     }

     public void setTtlTlv(TLV ttl) {
          this.ttl = ttl;
     }

     public LinkedList<TLV> getOptional() {
          return optional;
     }

     public void setOptional(LinkedList<TLV> optional) {
          this.optional = optional;
     }

     public byte[] getPayload() {
          return payload;
     }

     public void setPayload(byte[] payload) {
          this.payload = payload;
     }

     public void setMacHeader(MacHeader m) {
          this.m = m;
     }

     /**
      * Parse the LLDP header of this packet
      * 
      * @param buf
      *            Buffer which stores the whole packet data
      * @param pos
      *            Position of where the header starts in the buffer
      * @return The position in the buffer after parsing the header
      */
     public static LLDPHeader parse(MacHeader m) throws HeaderParseException {

          LLDPHeader h = new LLDPHeader(m);
          int pos = 0;

          try {
               //System.out.println("m.offcut.length " + m.offcut.length);
               if (m.offcut.length < 9) {
                    throw new HeaderParseException(
                              "Data array too short for IP packet.");
               }

               //System.out.println("parse chassis TLV");
               pos = h.chassisId.parseTLV(m.offcut, 0);
               pos = h.portId.parseTLV(m.offcut, pos);
               pos = h.ttl.parseTLV(m.offcut, pos);


               @SuppressWarnings("unused")
               int type_length = 0;
               while (0 != (type_length = Utility.twoBytesToInteger(m.offcut, pos))) {
                    TLV opt = new TLV();
                    pos = opt.parseTLV(m.offcut, pos);
                    h.optional.addLast(opt);
               }
               
               // . Move the pos ahead by 2, because we read a 0-0 type_length
               pos += 2;
               
               // payload
               if (m.offcut.length -pos > 0) {
                    byte payload[] = new byte[m.offcut.length - pos];
                    System.arraycopy(m.offcut, pos, payload, 0, m.offcut.length - pos);
                    h.setPayload(payload);
               }

               log.info(h);

               return h;

          } catch (Exception e) {
               e.printStackTrace();
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }
     
     public byte[] getBytes() throws HeaderBytesException {
          return m.getBytes();
     }


     public String toString() {
          // . Currently print out only that it is a LLDPDU
          StringBuilder sb = new StringBuilder();
          sb.append("[LLDP Header==]: ");
          sb.append(m.toString());

          sb.append("\n------ LLDP CHASSIS TLV info -------------\n");
          sb.append(chassisId.toString() + "\n");
          sb.append(portId.toString() + "\n");
          sb.append(ttl.toString() + "\n");
          /** optional */
          Iterator<TLV> iter = optional.iterator();
          while (iter.hasNext()) {
               sb.append(iter.next().toString() + "\n");
          }          
          return sb.toString();
     }
}
