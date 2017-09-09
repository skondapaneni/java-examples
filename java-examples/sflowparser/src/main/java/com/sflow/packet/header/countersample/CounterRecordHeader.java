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
 * Modified : srihari.k
 *   Add support for tokenRing, VG, VLAN, PROCESSOR Counter.
 */
package com.sflow.packet.header.countersample;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

/*
  |--------------------------------------------------------------------|
  | int data format counter data (20 bit enterprise & 12 bit format)   |
  |        (standard enterprise 0, formats 1,2,3,4,5,1001)             |
  |--------------------------------------------------------------------|
  |                 int counter data length                            |
  |--------------------------------------------------------------------|
  +                                                                    +
  +                     counter data                                   +
  +                                                                    +
  |--------------------------------------------------------------------|
*/

public class CounterRecordHeader {

     public static final int GENERICCOUNTER_SFLOWv5    =  1;
     public static final int ETHERNETCOUNTER_SFLOWv5   =  2;
     public static final int TOKENRINGCOUNTER_SFLOWv5  =  3;
     public static final int VGCOUNTER_SFLOWv5         =  4;
     public static final int VLANCOUNTER_SFLOWv5       =  5;
     public static final int LACPCOUNTER_SFLOWv5       =  7;
     public static final int PROCESSORCOUNTER_SFLOWv5  =  1001;
     public static final int OPENFLOWPORT_SFLOWv5      =  1004;
     public static final int PORTNAME_SFLOWv5          =  1005;
     public static final int APPRESOURCES_SFLOWv5      =  2203;
     public static final int OVSDP_SFLOWv5             =  2207;

     private long counterDataFormat; // 20 bit enterprise & 12 bit format; 
                                        // standard enterprise 0, format 1, 2, 3, 4, 5, 1001 
     private long counterDataLength; // in byte
     
     private GenericIfCounterHeader     genericCounter;
     private EthernetCounterHeader      ethernetCounter;
     private TokenRingCounterHeader     tokenRingCounter;
     private VgCounterHeader            vgCounter;
     private VlanCounterHeader          vlanCounter;
     private ProcessorCounterHeader     processorCounter;
     private OpenFlowPortCounterHeader  openFlowPortCounter;
     private PortNameCounterHeader      portNameCounter;
     private OvsDataPathCounterHeader   ovsDataPathCounter;
          
     public CounterRecordHeader() {
     }

     public long getCounterDataFormat() {
          return counterDataFormat;
     }

     public long getCounterDataLength() {
          return counterDataLength;
     }

     public void setCounterDataFormat(long counterDataFormat) {
          this.counterDataFormat = counterDataFormat;
     }

     public void setCounterDataLength(long counterDataLength) {
          this.counterDataLength = counterDataLength;
     }
     
     public void setEthernetCounterHeader(EthernetCounterHeader ethernetCounter) {
          this.ethernetCounter = ethernetCounter;
     }
     
     public EthernetCounterHeader getEthernetCounterHeader() {
          return ethernetCounter;
     }
     
     public void setGenericCounterHeader(GenericIfCounterHeader genericCounter) {
          this.genericCounter = genericCounter;
     }
     
     public GenericIfCounterHeader getGenericCounterHeader() {
          return genericCounter;
     }

     public void setTokenRingCounterHeader(TokenRingCounterHeader tokenRingCounter) {
          this.tokenRingCounter = tokenRingCounter;
     }
     
     public TokenRingCounterHeader getTokenRingCounterHeader() {
          return tokenRingCounter;
     }

     public void setVGCounterHeader(VgCounterHeader vgCounter) {
          this.vgCounter = vgCounter;
     }
     
     public VgCounterHeader getVgCounterHeader() {
          return vgCounter;
     }

     public void setVlanCounterHeader(VlanCounterHeader vlanCounter) {
          this.vlanCounter = vlanCounter;
     }
     
     public VlanCounterHeader getVlanCounterHeader() {
          return vlanCounter;
     }

     public ProcessorCounterHeader getProcessorCounterHeader() {
          return processorCounter;
     }

     public void setProcessorCounterHeader(ProcessorCounterHeader processorCounter) {
          this.processorCounter = processorCounter;
     }

     public OpenFlowPortCounterHeader getOpenFlowPortCounterHeader() {
          return openFlowPortCounter;
     }

     public void setOpenFlowPortCounterHeader(OpenFlowPortCounterHeader openFlowPortCounter) {
          this.openFlowPortCounter = openFlowPortCounter;
     }

     public PortNameCounterHeader getPortNameCounterHeader() {
          return portNameCounter;
     }

     public void setPortNameCounterHeader(PortNameCounterHeader portNameCounter) {
          this.portNameCounter = portNameCounter;
     }

     public OvsDataPathCounterHeader getOvsDataPathCounter() {
          return ovsDataPathCounter;
     }

     public void setOvsDataPathCounter(OvsDataPathCounterHeader ovsDataPathCounter) {
          this.ovsDataPathCounter = ovsDataPathCounter;
     }

     public static CounterRecordHeader parse(byte[] data, int offset) throws HeaderParseException {
          try {
               if (data.length - offset < 8) throw new HeaderParseException("Data array too short.");
               CounterRecordHeader crd = new CounterRecordHeader();

               // format
               crd.setCounterDataFormat(Utility.fourBytesToLong(data, 0+offset));

               // length
               crd.setCounterDataLength(Utility.fourBytesToLong(data, 4+offset));
               
               byte[] subData = new byte[(int) crd.getCounterDataLength()]; 
               System.arraycopy(data, 8+offset, subData, 0, (int) crd.getCounterDataLength());

               if (crd.getCounterDataFormat() == GENERICCOUNTER_SFLOWv5) {
                    GenericIfCounterHeader gic = GenericIfCounterHeader.parse(subData);
                    crd.setGenericCounterHeader(gic);
               } else if (crd.getCounterDataFormat() == ETHERNETCOUNTER_SFLOWv5) {
                    EthernetCounterHeader eic = EthernetCounterHeader.parse(subData);
                    crd.setEthernetCounterHeader(eic);
               } else if (crd.getCounterDataFormat() == TOKENRINGCOUNTER_SFLOWv5) {
                    TokenRingCounterHeader trc = TokenRingCounterHeader.parse(subData);
                    crd.setTokenRingCounterHeader(trc);
               } else if (crd.getCounterDataFormat() == VGCOUNTER_SFLOWv5) {
                    VgCounterHeader vgc = VgCounterHeader.parse(subData);
                    crd.setVGCounterHeader(vgc);
               } else if (crd.getCounterDataFormat() == VLANCOUNTER_SFLOWv5) {
                    VlanCounterHeader vlanc = VlanCounterHeader.parse(subData);
                    crd.setVlanCounterHeader(vlanc);
               } else if (crd.getCounterDataFormat() == PROCESSORCOUNTER_SFLOWv5) {
                    ProcessorCounterHeader prc = ProcessorCounterHeader.parse(subData);
                    crd.setProcessorCounterHeader(prc);
               } else if (crd.getCounterDataFormat() == OPENFLOWPORT_SFLOWv5) {
                    OpenFlowPortCounterHeader prc = OpenFlowPortCounterHeader.parse(subData);
                    crd.setOpenFlowPortCounterHeader(prc);
               } else if (crd.getCounterDataFormat() == PORTNAME_SFLOWv5) {
                    PortNameCounterHeader prc = PortNameCounterHeader.parse(subData);
                    crd.setPortNameCounterHeader(prc);
               } else if (crd.getCounterDataFormat() == OVSDP_SFLOWv5) {
                    OvsDataPathCounterHeader ovs = OvsDataPathCounterHeader.parse(subData);
                    crd.setOvsDataPathCounter(ovs);
               } else {
                    System.err.println("Counter data format not yet supported: " + crd.getCounterDataFormat());
               }
               
               return crd;
          } catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }
     
     public byte[] getBytes() throws HeaderBytesException {
          try {
               int length = 0;

               byte[] genericCounterBytes = null;
               byte[] ethernetCounterBytes = null;
               byte[] tokenRingCounterBytes = null;
               byte[] vgCounterBytes = null;
               byte[] vlanCounterBytes = null;
               byte[] processorCounterBytes = null;
               byte[] openFlowPortCounterBytes = null;
               byte[] portNameCounterBytes = null;
               byte[] ovsDataPathCounterBytes = null;

               if (genericCounter != null) {
                    genericCounterBytes = genericCounter.getBytes();
                    length += genericCounterBytes.length;
               }

               if (ethernetCounter != null) {
                    ethernetCounterBytes = ethernetCounter.getBytes();
                    length += ethernetCounterBytes.length;
               }

               if (tokenRingCounter != null) {
                    tokenRingCounterBytes = tokenRingCounter.getBytes();
                    length += tokenRingCounterBytes.length;
               }

               if (vgCounter != null) {
                    vgCounterBytes = vgCounter.getBytes();
                    length += vgCounterBytes.length;
               }

               if (vlanCounter != null) {
                    vlanCounterBytes = vlanCounter.getBytes();
                    length += vlanCounterBytes.length;
               }

               if (processorCounter != null) {
                    processorCounterBytes = processorCounter.getBytes();
                    length += processorCounterBytes.length;
               }
               
               if (openFlowPortCounter != null) {
                    openFlowPortCounterBytes = openFlowPortCounter.getBytes();
                    length += openFlowPortCounterBytes.length;
               }
               
               if (portNameCounter != null) {
                    portNameCounterBytes = portNameCounter.getBytes();
                    length += portNameCounterBytes.length;
               }
               
               if (ovsDataPathCounter != null) {
                    ovsDataPathCounterBytes = ovsDataPathCounter.getBytes();
                    length += ovsDataPathCounterBytes.length;
               }

               byte[] data = new byte[8 + length];

               // format
               System.arraycopy(Utility.longToFourBytes(counterDataFormat), 0, data, 0, 4);

               // length
               System.arraycopy(Utility.longToFourBytes(counterDataLength), 0, data, 4, 4);
               
               // generic interface counter
               if (genericCounter != null)  {
                   System.arraycopy(genericCounterBytes, 0, data, 8, genericCounterBytes.length);
               }

               // ethernet interface counter
               if (ethernetCounter != null) {
                   System.arraycopy(ethernetCounterBytes, 0, data, 8, ethernetCounterBytes.length);
               }

               // token ring counter
               if (tokenRingCounter != null) {
                   System.arraycopy(tokenRingCounterBytes, 0, data, 8, tokenRingCounterBytes.length);
               }

               // vg counter
               if (vgCounter != null) {
                   System.arraycopy(vgCounterBytes, 0, data, 8, vgCounterBytes.length);
               }

               // vlan counter
               if (vlanCounter != null) {
                    System.arraycopy(vlanCounterBytes, 0, data, 8, vlanCounterBytes.length);
               }

               // processor counter
               if (processorCounter != null) {
                   System.arraycopy(processorCounterBytes, 0, data, 8, processorCounterBytes.length);
               }
               
               if (openFlowPortCounter != null) {
                   System.arraycopy(openFlowPortCounterBytes, 0, data, 8, openFlowPortCounterBytes.length);
               }
               
               if (portNameCounter != null) {
                   System.arraycopy(portNameCounterBytes, 0, data, 8, portNameCounterBytes.length);
               }
               
               if (ovsDataPathCounter != null) {
                   System.arraycopy(portNameCounterBytes, 0, data, 8, ovsDataPathCounterBytes.length);
               }
               
               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {

          StringBuilder sb = new StringBuilder();
          sb.append("[CounterRecordHeader]: ");
          sb.append("Format: ");
          sb.append(getCounterDataFormat());
          sb.append(", Length:");
          sb.append(getCounterDataLength());
          sb.append("\n");
          
          if (this.getCounterDataFormat() == CounterRecordHeader.GENERICCOUNTER_SFLOWv5){
               sb.append(genericCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.ETHERNETCOUNTER_SFLOWv5){
               sb.append(ethernetCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.TOKENRINGCOUNTER_SFLOWv5){
               sb.append(tokenRingCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.VGCOUNTER_SFLOWv5){
               sb.append(vgCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.VLANCOUNTER_SFLOWv5){
               sb.append(vlanCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.PROCESSORCOUNTER_SFLOWv5){
               sb.append(processorCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.OPENFLOWPORT_SFLOWv5){
               sb.append(processorCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.PORTNAME_SFLOWv5){
               sb.append(processorCounter);
          } else if(this.getCounterDataFormat() == CounterRecordHeader.OVSDP_SFLOWv5){
               sb.append(ovsDataPathCounter);
          } else{
               sb.append("unsupported CounterRecordData format");
          }

          return sb.toString();
     }
}
