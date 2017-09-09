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

import java.math.BigInteger;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

/* Generic interface counters - see RFC 1573, 2233 */
public class GenericIfCounterHeader {
     // enterprise = 0, format = 1
     
     public static final int SFL_CTR_GENERIC_XDR_SIZE  = 88;

     private long ifIndex;
     private long ifType;
     private BigInteger ifSpeed;
     private long ifDirection; // 0 = unknown, 1 = full-duplex, 2 = half-duplex, 3 = in, 4 = out
     private long ifStatus; // bit 0 => ifAdminStatus 0=down|1=up, bit 1 => ifOperStatus 0=down|1=up
     
     private BigInteger ifInOctets;
     private long ifInUcastPkts;
     private long ifInMulticastPkts;
     private long ifInBroadcastPkts;
     private long ifInDiscards;
     private long ifInErrors;
     private long ifInUnknownProtos;
     
     private BigInteger ifOutOctets;
     private long ifOutUcastPkts;
     private long ifOutMulticastPkts;
     private long ifOutBroadcastPkts;
     private long ifOutDiscards;
     private long ifOutErrors;
     private long ifPromiscuousMode;

     private CounterData counterData;

     public long getIfIndex() {
          return ifIndex;
     }

     public long getIfType() {
          return ifType;
     }

     public BigInteger getIfSpeed(){
          return ifSpeed;
     }

     public long getIfDirection() {
          return ifDirection;
     }

     public long getIfStatus() {
          return ifStatus;
     }

     public BigInteger getIfInOctets(){
          return ifInOctets;
     }

     public long getIfInUcastPkts() {
          return ifInUcastPkts;
     }

     public long getIfInMulticastPkts() {
          return ifInMulticastPkts;
     }

     public long getIfInBroadcastPkts() {
          return ifInBroadcastPkts;
     }

     public long getIfInDiscards() {
          return ifInDiscards;
     }

     public long getIfInErrors() {
          return ifInErrors;
     }

     public long getIfInUnknownProtos() {
          return ifInUnknownProtos;
     }

     public BigInteger getIfOutOctets(){
          return ifOutOctets;
     }

     public long getIfOutUcastPkts() {
          return ifOutUcastPkts;
     }

     public long getIfOutMulticastPkts() {
          return ifOutMulticastPkts;
     }

     public long getIfOutBroadcastPkts() {
          return ifOutBroadcastPkts;
     }

     public long getIfOutDiscards() {
          return ifOutDiscards;
     }

     public long getIfOutErrors() {
          return ifOutErrors;
     }

     public long getIfPromiscuousMode() {
          return ifPromiscuousMode;
     }

     public void setIfIndex(long ifIndex) {
          this.ifIndex = ifIndex;
     }

     public void setIfType(long ifType) {
          this.ifType = ifType;
     }

     public void setIfSpeed(BigInteger ifSpeed){
          this.ifSpeed = ifSpeed;
     }

     public void setIfDirection(long ifDirection) {
          this.ifDirection = ifDirection;
     }

     public void setIfStatus(long ifStatus) {
          this.ifStatus = ifStatus;
     }

     public void setIfInOctets(BigInteger ifInOctets){
          this.ifInOctets = ifInOctets;
     }

     public void setIfInUcastPkts(long ifInUcastPkts) {
          this.ifInUcastPkts = ifInUcastPkts;
     }

     public void setIfInMulticastPkts(long ifInMulticastPkts) {
          this.ifInMulticastPkts = ifInMulticastPkts;
     }

     public void setIfInBroadcastPkts(long ifInBroadcastPkts) {
          this.ifInBroadcastPkts = ifInBroadcastPkts;
     }

     public void setIfInDiscards(long ifInDiscards) {
          this.ifInDiscards = ifInDiscards;
     }

     public void setIfInErrors(long ifInErrors) {
          this.ifInErrors = ifInErrors;
     }

     public void setIfInUnknownProtos(long ifInUnknownProtos) {
          this.ifInUnknownProtos = ifInUnknownProtos;
     }

     public void setIfOutOctets(BigInteger ifOutOctets){
          this.ifOutOctets = ifOutOctets;
     }

     public void setIfOutUcastPkts(long ifOutUcastPkts) {
          this.ifOutUcastPkts = ifOutUcastPkts;
     }

     public void setIfOutMulticastPkts(long ifOutMulticastPkts) {
          this.ifOutMulticastPkts = ifOutMulticastPkts;
     }

     public void setIfOutBroadcastPkts(long ifOutBroadcastPkts) {
          this.ifOutBroadcastPkts = ifOutBroadcastPkts;
     }

     public void setIfOutDiscards(long ifOutDiscards) {
          this.ifOutDiscards = ifOutDiscards;
     }

     public void setIfOutErrors(long ifOutErrors) {
          this.ifOutErrors = ifOutErrors;
     }

     public void setIfPromiscuousMode(long ifPromiscuousMode) {
          this.ifPromiscuousMode = ifPromiscuousMode;
     }

     public void setCounterData(CounterData counterData) {
          this.counterData = counterData;
     }

     public CounterData getCounterData() {
          return counterData;
     }

     public static GenericIfCounterHeader parse(byte[] data) throws HeaderParseException {
          try {
               if (data.length < 88) throw new HeaderParseException("Data array too short.");
               GenericIfCounterHeader gic = new GenericIfCounterHeader();
               // interface index
               gic.setIfIndex(Utility.fourBytesToLong(data, 0));
               // interface type
               gic.setIfType(Utility.fourBytesToLong(data, 4));
               // interface speed
               gic.setIfSpeed(Utility.eightBytesToBigInteger(data, 8));
               // interface direction
               gic.setIfDirection(Utility.fourBytesToLong(data, 16));
               // interface status
               gic.setIfStatus(Utility.fourBytesToLong(data, 20));
               // interface input octets -
               gic.setIfInOctets(Utility.eightBytesToBigInteger(data, 24));
               // interface input unicast packets
               gic.setIfInUcastPkts(Utility.fourBytesToLong(data, 32));
               // interface input multicast packets
               gic.setIfInMulticastPkts(Utility.fourBytesToLong(data, 36));
               // interface input broadcast packets
               gic.setIfInBroadcastPkts(Utility.fourBytesToLong(data, 40));
               // interface input discards
               gic.setIfInDiscards(Utility.fourBytesToLong(data, 44));
               // interface input errors
               gic.setIfInErrors(Utility.fourBytesToLong(data, 48));
               // interface input unknown protocols
               gic.setIfInUnknownProtos(Utility.fourBytesToLong(data, 52));
               // interface output octets
               gic.setIfOutOctets(Utility.eightBytesToBigInteger(data, 56));
               // interface output unicast packets
               gic.setIfOutUcastPkts(Utility.fourBytesToLong(data, 64));
               // interface output multicast packets
               gic.setIfOutMulticastPkts(Utility.fourBytesToLong(data, 68));
               // interface output broadcast packets
               gic.setIfOutBroadcastPkts(Utility.fourBytesToLong(data, 72));
               // interface output discards
               gic.setIfOutDiscards(Utility.fourBytesToLong(data, 76));
               // interface output errors
               gic.setIfOutErrors(Utility.fourBytesToLong(data, 80));
               // interface promiscuous mode
               gic.setIfPromiscuousMode(Utility.fourBytesToLong(data, 84));

               if (data.length > 88) {
                    // counter data
                    byte[] subData = new byte[data.length - 88]; 
                    System.arraycopy(data, 88, subData, 0, data.length - 88);
                    CounterData cd = CounterData.parse(subData);
                    gic.setCounterData(cd);
               }

               return gic;
          } catch (Exception e) {
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
               byte[] data = new byte[88 + counterDataLen];
               
               // interface index
               System.arraycopy(Utility.longToFourBytes(ifIndex), 0, data, 0, 4);
               // interface type
               System.arraycopy(Utility.longToFourBytes(ifType), 0, data, 4, 4);
               // interface speed
               System.arraycopy(Utility.BigIntegerToEightBytes(ifSpeed), 0, data, 8, 8);
               // interface direction
               System.arraycopy(Utility.longToFourBytes(ifDirection), 0, data, 16, 4);
               // interface status
               System.arraycopy(Utility.longToFourBytes(ifStatus), 0, data, 20, 4);
               // interface input octets
               System.arraycopy(Utility.BigIntegerToEightBytes(ifInOctets), 0, data, 24, 8);
               // interface input unicast packets
               System.arraycopy(Utility.longToFourBytes(ifInUcastPkts), 0, data, 32, 4);
               // interface input multicast packets
               System.arraycopy(Utility.longToFourBytes(ifInMulticastPkts), 0, data, 36, 4);
               // interface input broadcast packets
               System.arraycopy(Utility.longToFourBytes(ifInBroadcastPkts), 0, data, 40, 4);
               // interface input discards
               System.arraycopy(Utility.longToFourBytes(ifInDiscards), 0, data, 44, 4);
               // interface input errors
               System.arraycopy(Utility.longToFourBytes(ifInErrors), 0, data, 48, 4);
               // interface input unknown protocols
               System.arraycopy(Utility.longToFourBytes(ifInUnknownProtos), 0, data, 52, 4);
               // interface output octets
               System.arraycopy(Utility.BigIntegerToEightBytes(ifOutOctets), 0, data, 56, 8);
               // interface output unicast packets
               System.arraycopy(Utility.longToFourBytes(ifOutUcastPkts), 0, data, 64, 4);
               // interface output multicast packets
               System.arraycopy(Utility.longToFourBytes(ifOutMulticastPkts), 0, data, 68, 4);
               // interface output broadcast packets
               System.arraycopy(Utility.longToFourBytes(ifOutBroadcastPkts), 0, data, 72, 4);
               // interface output discards
               System.arraycopy(Utility.longToFourBytes(ifOutDiscards), 0, data, 76, 4);
               // interface output errors
               System.arraycopy(Utility.longToFourBytes(ifOutErrors), 0, data, 80, 4);
               // interface promiscuous mode
               System.arraycopy(Utility.longToFourBytes(ifPromiscuousMode), 0, data, 84, 4);

               // counter data
               if (counterDataLen != 0) {
                    System.arraycopy(counterDataBytes, 0, data, 88, counterDataBytes.length);
               }

               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }

     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[GenericInterfaceCounterHeader]: ");
          sb.append("Index: ");
          sb.append(getIfIndex());
          sb.append(", Type: ");
          sb.append(getIfType());
          sb.append(", Speed: ");
          sb.append(getIfSpeed());
          sb.append(", Direction: ");
          sb.append(getIfDirection());
          sb.append(", Status(Admin/Oper): ");
          sb.append(getIfStatus());
          sb.append(", Octets(IN): ");
          sb.append(getIfInOctets());
          sb.append(", Unicast(IN): ");
          sb.append(getIfInUcastPkts());
          sb.append(", Multicast(IN): ");
          sb.append(getIfInMulticastPkts());
          sb.append(", Broadcast(IN): ");
          sb.append(getIfInBroadcastPkts());
          sb.append(", Discards(IN): ");
          sb.append(getIfInDiscards());
          sb.append(", Errors(IN): ");
          sb.append(getIfInErrors());
          sb.append(", UnknownProtocol(IN): ");
          sb.append(getIfInUnknownProtos());
          sb.append(", Octets(OUT): ");
          sb.append(getIfOutOctets());
          sb.append(", Unicast(OUT): ");
          sb.append(getIfOutUcastPkts());
          sb.append(", Multicast(OUT): ");
          sb.append(getIfOutMulticastPkts());
          sb.append(", Broadcast(OUT): ");
          sb.append(getIfOutBroadcastPkts());
          sb.append(", Discards(OUT): ");
          sb.append(getIfOutDiscards());
          sb.append(", Errors(OUT): ");
          sb.append(getIfOutErrors());
          sb.append(", PromiscuousMode: ");
          sb.append(getIfPromiscuousMode());
          
          return sb.toString();
     }
}
