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
package com.sflow.packet.header.countersample;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

public class EthernetCounterHeader {
     // enterprise = 0, format = 2
     // RFC 2358
     
     public final static int SFL_CTR_ETHERNET_XDR_SIZE = 52;

     private long dot3StatsAlignmentErrors;
     private long dot3StatsFCSErrors;
     private long dot3StatsSingleCollisionFrames;
     private long dot3StatsMultipleCollisionFrames;
     private long dot3StatsSQETestErrors;
     private long dot3StatsDeferredTransmissions;
     private long dot3StatsLateCollisions;
     private long dot3StatsExcessiveCollisions;
     private long dot3StatsInternalMacTransmitErrors;
     private long dot3StatsCarrierSenseErrors;
     private long dot3StatsFrameTooLongs;
     private long dot3StatsInternalMacReceiveErrors;
     private long dot3StatsSymbolErrors;

     private CounterData counterData;

     public long getDot3StatsAlignmentErrors() {
          return dot3StatsAlignmentErrors;
     }

     public long getDot3StatsFCSErrors() {
          return dot3StatsFCSErrors;
     }

     public long getDot3StatsSingleCollisionFrames() {
          return dot3StatsSingleCollisionFrames;
     }

     public long getDot3StatsMultipleCollisionFrames() {
          return dot3StatsMultipleCollisionFrames;
     }

     public long getDot3StatsSQETestErrors() {
          return dot3StatsSQETestErrors;
     }

     public long getDot3StatsDeferredTransmissions() {
          return dot3StatsDeferredTransmissions;
     }

     public long getDot3StatsLateCollisions() {
          return dot3StatsLateCollisions;
     }

     public long getDot3StatsExcessiveCollisions() {
          return dot3StatsExcessiveCollisions;
     }

     public long getDot3StatsInternalMacTransmitErrors() {
          return dot3StatsInternalMacTransmitErrors;
     }

     public long getDot3StatsCarrierSenseErrors() {
          return dot3StatsCarrierSenseErrors;
     }

     public long getDot3StatsFrameTooLongs() {
          return dot3StatsFrameTooLongs;
     }

     public long getDot3StatsInternalMacReceiveErrors() {
          return dot3StatsInternalMacReceiveErrors;
     }

     public long getDot3StatsSymbolErrors() {
          return dot3StatsSymbolErrors;
     }

     public void setDot3StatsAlignmentErrors(long dot3StatsAlignmentErrors) {
          this.dot3StatsAlignmentErrors = dot3StatsAlignmentErrors;
     }

     public void setDot3StatsFCSErrors(long dot3StatsFCSErrors) {
          this.dot3StatsFCSErrors = dot3StatsFCSErrors;
     }

     public void setDot3StatsSingleCollisionFrames(long dot3StatsSingleCollisionFrames) {
          this.dot3StatsSingleCollisionFrames = dot3StatsSingleCollisionFrames;
     }

     public void setDot3StatsMultipleCollisionFrames(
               long dot3StatsMultipleCollisionFrames) {
          this.dot3StatsMultipleCollisionFrames = dot3StatsMultipleCollisionFrames;
     }

     public void setDot3StatsSQETestErrors(long dot3StatsSQETestErrors) {
          this.dot3StatsSQETestErrors = dot3StatsSQETestErrors;
     }

     public void setDot3StatsDeferredTransmissions(
               long dot3StatsDeferredTransmissions) {
          this.dot3StatsDeferredTransmissions = dot3StatsDeferredTransmissions;
     }

     public void setDot3StatsLateCollisions(long dot3StatsLateCollisions) {
          this.dot3StatsLateCollisions = dot3StatsLateCollisions;
     }

     public void setDot3StatsExcessiveCollisions(long dot3StatsExcessiveCollisions) {
          this.dot3StatsExcessiveCollisions = dot3StatsExcessiveCollisions;
     }

     public void setDot3StatsInternalMacTransmitErrors(long dot3StatsInternalMacTransmitErrors) {
          this.dot3StatsInternalMacTransmitErrors = dot3StatsInternalMacTransmitErrors;
     }

     public void setDot3StatsCarrierSenseErrors(long dot3StatsCarrierSenseErrors) {
          this.dot3StatsCarrierSenseErrors = dot3StatsCarrierSenseErrors;
     }

     public void setDot3StatsFrameTooLongs(long dot3StatsFrameTooLongs) {
          this.dot3StatsFrameTooLongs = dot3StatsFrameTooLongs;
     }

     public void setDot3StatsInternalMacReceiveErrors(long dot3StatsInternalMacReceiveErrors) {
          this.dot3StatsInternalMacReceiveErrors = dot3StatsInternalMacReceiveErrors;
     }

     public void setDot3StatsSymbolErrors(long dot3StatsSymbolErrors) {
          this.dot3StatsSymbolErrors = dot3StatsSymbolErrors;
     }

     public void setCounterData(CounterData counterData) {
          this.counterData = counterData;
     }

     public CounterData getCounterData() {
          return counterData;
     }

     public static EthernetCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 52) throw new HeaderParseException("Data array too short.");
               EthernetCounterHeader eic = new EthernetCounterHeader();
               // dot3StatsAlignmentErrors
               eic.setDot3StatsAlignmentErrors(Utility.fourBytesToLong(data, 0));
               // dot3StatsAlignmentErrors
               eic.setDot3StatsFCSErrors(Utility.fourBytesToLong(data, 4));
               // dot3StatsSingleCollisionFrames
               eic.setDot3StatsSingleCollisionFrames(Utility.fourBytesToLong(data, 8));
               // dot3StatsMultipleCollisionFrames
               eic.setDot3StatsMultipleCollisionFrames(Utility.fourBytesToLong(data, 12));
               // dot3StatsSQETestErrors
               eic.setDot3StatsSQETestErrors(Utility.fourBytesToLong(data,16));
               // dot3StatsDeferredTransmissions
               eic.setDot3StatsDeferredTransmissions(Utility.fourBytesToLong(data, 20));
               // dot3StatsLateCollisions
               eic.setDot3StatsLateCollisions(Utility.fourBytesToLong(data, 24));
               // dot3StatsExcessiveCollisions
               eic.setDot3StatsExcessiveCollisions(Utility.fourBytesToLong(data, 28));
               // dot3StatsInternalMacTransmitErrors
               eic.setDot3StatsInternalMacTransmitErrors(Utility.fourBytesToLong(data, 32));
               // dot3StatsCarrierSenseErrors
               eic.setDot3StatsCarrierSenseErrors(Utility.fourBytesToLong(data, 36));
               // dot3StatsFrameTooLongs
               eic.setDot3StatsFrameTooLongs(Utility.fourBytesToLong(data, 40));
               // dot3StatsInternalMacReceiveErrors
               eic.setDot3StatsInternalMacReceiveErrors(Utility.fourBytesToLong(data, 44));
               // dot3StatsSymbolErrors
               eic.setDot3StatsSymbolErrors(Utility.fourBytesToLong(data, 48));

               // counter data
               if (data.length > 52) {
                    byte[] subData = new byte[data.length - 52]; 
                    System.arraycopy(data, 52, subData, 0, data.length - 52);
                    CounterData cd = CounterData.parse(subData);
                    eic.setCounterData(cd);
               }

               return eic;
          }  catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }
     }

     public byte[] getBytes() throws HeaderBytesException {
          try {
               byte[] counterDataBytes = null;
               int counterDataLen = 0;
               if (counterData != null) {
                    counterDataBytes = counterData.getBytes();
                    counterDataLen = counterDataBytes.length;
               }
               byte[] data = new byte[52 + counterDataLen];
               // dot3StatsAlignmentErrors
               System.arraycopy(Utility.longToFourBytes(dot3StatsAlignmentErrors), 0, data, 0, 4);
               // dot3StatsFCSErrors
               System.arraycopy(Utility.longToFourBytes(dot3StatsFCSErrors), 0, data, 4, 4);
               // dot3StatsSingleCollisionFrames
               System.arraycopy(Utility.longToFourBytes(dot3StatsSingleCollisionFrames), 0, data, 8, 4);
               // dot3StatsMultipleCollisionFrames
               System.arraycopy(Utility.longToFourBytes(dot3StatsMultipleCollisionFrames), 0, data, 12, 4);
               // dot3StatsSQETestErrors
               System.arraycopy(Utility.longToFourBytes(dot3StatsSQETestErrors), 0, data, 16, 4);
               // dot3StatsDeferredTransmissions
               System.arraycopy(Utility.longToFourBytes(dot3StatsDeferredTransmissions), 0, data, 20, 4);
               // dot3StatsLateCollisions
               System.arraycopy(Utility.longToFourBytes(dot3StatsLateCollisions), 0, data, 24, 4);
               // dot3StatsExcessiveCollisions
               System.arraycopy(Utility.longToFourBytes(dot3StatsExcessiveCollisions), 0, data, 28, 4);
               // dot3StatsInternalMacTransmitErrors
               System.arraycopy(Utility.longToFourBytes(dot3StatsInternalMacTransmitErrors), 0, data, 32, 4);
               // dot3StatsCarrierSenseErrors
               System.arraycopy(Utility.longToFourBytes(dot3StatsCarrierSenseErrors), 0, data, 36, 4);
               // dot3StatsFrameTooLongs
               System.arraycopy(Utility.longToFourBytes(dot3StatsFrameTooLongs), 0, data, 40, 4);
               // dot3StatsInternalMacReceiveErrors
               System.arraycopy(Utility.longToFourBytes(dot3StatsInternalMacReceiveErrors), 0, data, 44, 4);
               // dot3StatsSymbolErrors
               System.arraycopy(Utility.longToFourBytes(dot3StatsSymbolErrors), 0, data, 48, 4);
               
               // counter data
               if (counterDataLen != 0) {
                    System.arraycopy(counterDataBytes, 0, data, 52, counterDataBytes.length);
               }

               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[EthernetCounterHeader]:");
          sb.append("AlignmentErrors: ");
          sb.append(getDot3StatsAlignmentErrors());
          sb.append(", FCSErrors: " + this.getDot3StatsFCSErrors());
          sb.append(", SingleCollisionFrames: ");
          sb.append(getDot3StatsSingleCollisionFrames());
          sb.append(", MultipleCollisionFrames: ");
          sb.append(getDot3StatsMultipleCollisionFrames());
          sb.append(", SQETestErrors: ");
          sb.append(getDot3StatsSQETestErrors());
          sb.append(", DeferredTransmissions: ");
          sb.append(getDot3StatsDeferredTransmissions());
          sb.append(", LateCollisions: ");
          sb.append(getDot3StatsLateCollisions());
          sb.append(", ExcessiveCollisions: ");
          sb.append(getDot3StatsExcessiveCollisions());
          sb.append(", InternalMACTransmitErrors: ");
          sb.append(getDot3StatsInternalMacTransmitErrors());
          sb.append(", CarrierSenseErrors: ");
          sb.append(getDot3StatsCarrierSenseErrors());
          sb.append(", FrameTooLongs: ");
          sb.append(getDot3StatsFrameTooLongs());
          sb.append(", InternalMACReceiveErrors: ");
          sb.append(getDot3StatsInternalMacReceiveErrors());
          sb.append(", SymbolErrors: ");
          sb.append(getDot3StatsSymbolErrors());
          return sb.toString();
     }
}
