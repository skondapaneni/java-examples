package com.sflow.packet.header.reactor;

import java.util.Vector;

import reactor.bus.Event;

import com.sflow.packet.header.countersample.CounterRecordHeader;
import com.sflow.packet.header.countersample.reactor.ReactorCounterSampleHeaderConsumer;
import com.sflow.packet.reactor.SFlowHeader;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

public class CounterSampleHeader  {
     
     // enterprise 0, format 2
     /*
        The most significant byte of the source_id is used to indicate the type
        of sFlowDataSource:
             0 = ifIndex
             1 = smonVlanDataSource
             2 = entPhysicalEntry
        The lower three bytes contain the relevant index value. */
          
     private long seqNumber;   /* Incremented with each counter sample
                                                  generated by this source_id
                                                  Note: If the agent resets any of the
                                                  counters then it must also
                                                  reset the sequence_number.
                                                  In the case of ifIndex-based
                                                  source_id's the sequence
                                                  number must be reset each time
                                                  ifCounterDiscontinuityTime
                                                  changes. */
     private long sourceIDType;    /* sFlowDataSource */
     private long sourceIDIndex;   /* sFlowDataSource */
     private long numberCounterRecords;
     private Vector<CounterRecordHeader> counterRecords;
     
     private SFlowHeader                sflowHeader;
          
     public CounterSampleHeader() {
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
     
     public void setNumberCounterRecords(long numberCounterRecords) {
          this.numberCounterRecords = numberCounterRecords;
     }
     
     public void addCounterRecord(CounterRecordHeader counterRecord) {
          if (counterRecords == null) {
               counterRecords = new Vector<CounterRecordHeader>(1);
          }
          counterRecords.add(counterRecord);
     }
     
     public Vector<CounterRecordHeader> getCounterRecords() {
          return counterRecords;
     }

     public SFlowHeader getSflowHeader() {
          return sflowHeader;
     }

     public void setSflowHeader(SFlowHeader sflowHeader) {
          this.sflowHeader = sflowHeader;
     }

     public static CounterSampleHeader parse(byte[] data, int offset, SFlowHeader sflowHeader) throws HeaderParseException {
          try {
               if (data.length - offset < 12) throw new HeaderParseException("Data array too short.");
          
               CounterSampleHeader ecsh = new CounterSampleHeader();
               ecsh.setSflowHeader(sflowHeader);

               // sample sequence number              
               ecsh.setSequenceNumber(Utility.fourBytesToLong(data, 0+offset));

               // source id type
               ecsh.setSourceIDType(Utility.fourBytesToLong(data, 4+offset) >> 24 & 0xFF);

               // source id index
               byte[] sourceIDIndex = new byte[4];
               System.arraycopy(data, 4, sourceIDIndex, 0, 4);
               ecsh.setSourceIDIndex(Utility.fourBytesToLong(data, 4+offset) & 0xFFFFFF);
               
               // number counter records
               ecsh.setNumberCounterRecords(Utility.fourBytesToLong(data, 8 + offset));
                             
              // counter records
               int offset2 = 12+offset;
               for (int i = 0; i < ecsh.getNumberCounterRecords(); i++) {
                    CounterRecordHeader cr = CounterRecordHeader.parse(data, offset2);
                    ecsh.addCounterRecord(cr);
                    offset2 += (cr.getCounterDataLength() + 8);
               }          
               
               // publish the event to for listeners
               sflowHeader.getCollector().getCounterRecordBus().notify(
                         ReactorCounterSampleHeaderConsumer.counterSampleHeaderEvent,
                    Event.wrap(ecsh) );

               
               return ecsh;
          } catch (Exception e) {
               e.printStackTrace();
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
          sb.append("[CounterSampleHeader]: ");
          sb.append(" Sequence number: ");
          sb.append(getSequenceNumber());
          sb.append(", Source ID type: ");
          sb.append(getSourceIDType());
          sb.append(", Source ID index: ");
          sb.append(getSourceIDIndex());
          sb.append(", Counter records: ");
          sb.append(getNumberCounterRecords());
          sb.append(" ");
          for (CounterRecordHeader crh : this.getCounterRecords()) {
               sb.append(crh);
               sb.append(" ");
          }
          
          return sb.toString();
     }
}
