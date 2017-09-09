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

import com.sflow.packet.header.countersample.CounterRecordHeader;
import com.sflow.packet.header.countersample.reactor.ReactorExpCounterSampleHeaderConsumer;
import com.sflow.packet.reactor.SFlowHeader;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

public class ExpandedCounterSampleHeader {
     // enterprise 0, format 4
     private long seqNumber;
     private long sourceIDType; // 0 = ifindex, 1 = smonVlanDataSource, 2 = entPhysicalEntry
     private long sourceIDIndex;
     private long numberCounterRecords;
     
     private Vector<CounterRecordHeader> counterRecords;
     
     private SFlowHeader                sflowHeader;

     public ExpandedCounterSampleHeader() {
          counterRecords = new Vector<CounterRecordHeader>();
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
     
     public long getNumberCounterRecords() {
          return numberCounterRecords;
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
     
     public SFlowHeader getSflowHeader() {
          return sflowHeader;
     }

     public void setSflowHeader(SFlowHeader sflowHeader) {
          this.sflowHeader = sflowHeader;
     }
     
     public void setNumberCounterRecords(long numberCounterRecords) {
          this.numberCounterRecords = numberCounterRecords;
     }
     
     public void addCounterRecord(CounterRecordHeader counterRecord) {
          counterRecords.add(counterRecord);
     }
     
     public Vector<CounterRecordHeader> getCounterRecords() {
          return counterRecords;
     }

     public static ExpandedCounterSampleHeader parse(byte[] data, int offset, SFlowHeader sflowHeader) throws HeaderParseException {
          try {
               if (data.length - offset < 16) throw new HeaderParseException("Data array too short.");
               ExpandedCounterSampleHeader ecsh = new ExpandedCounterSampleHeader();
               ecsh.setSflowHeader(sflowHeader);

               // sample sequence number              
               ecsh.setSequenceNumber(Utility.fourBytesToLong(data, 0+offset));

               // source id type
               ecsh.setSourceIDType(Utility.fourBytesToLong(data, 4+offset));

               // source id index
               ecsh.setSourceIDIndex(Utility.fourBytesToLong(data, 8+offset));
               
               // number counter records
               ecsh.setNumberCounterRecords(Utility.fourBytesToLong(data, 12+offset));
               
               // counter records
               int offset2 = 16+offset;
               for (int i = 0; i < ecsh.getNumberCounterRecords(); i++) {
                    CounterRecordHeader cr = CounterRecordHeader.parse(data, offset2);
                    ecsh.addCounterRecord(cr);
                    offset2 += (cr.getCounterDataLength() + 8);
               }
               
               // publish the event to for listeners
               sflowHeader.getCollector().getCounterRecordBus().notify(
                         ReactorExpCounterSampleHeaderConsumer.expandedCounterSampleHeaderEvent,
                    Event.wrap(ecsh) );

               return ecsh;
          } catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }
     
     public byte[] getBytes() throws HeaderBytesException {
          try {
               int lengthCounterRecords = 0;
               for (CounterRecordHeader cr : counterRecords) {
                    lengthCounterRecords += (cr.getCounterDataLength() + 8);
               }
               byte[] data = new byte[16 + lengthCounterRecords];
               // sequence number
               System.arraycopy(Utility.longToFourBytes(seqNumber), 0, data, 0, 4);
               // source id type
               System.arraycopy(Utility.longToFourBytes(sourceIDType), 0, data, 4, 4);
               // source id index
               System.arraycopy(Utility.longToFourBytes(sourceIDIndex), 0, data, 8, 4);
               // number counter records
               System.arraycopy(Utility.longToFourBytes(numberCounterRecords), 0, data, 12, 4);
               
               int offset = 0;
               for (CounterRecordHeader cr : counterRecords) {
                    byte[] temp = cr.getBytes();
                    System.arraycopy(temp, 0, data, 16 + offset, temp.length);
                    
                    offset += (cr.getCounterDataLength() + 8);
               }
               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[ExpandedCounterSampleHeader]: ");
          sb.append(" Sequence number: ");
          sb.append(getSequenceNumber());
          sb.append(", Source ID type: ");
          sb.append(getSourceIDType());
          sb.append(", Source ID index: ");
          sb.append(getSourceIDIndex());
          sb.append(", Counter records: ");
          sb.append(getNumberCounterRecords());
          for (CounterRecordHeader crh : this.getCounterRecords()) {
               sb.append(crh);
               sb.append(" ");
          }
          
          return sb.toString();
     }
}
