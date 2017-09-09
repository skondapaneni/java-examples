package com.sflow.packet.header.flowsample;

import java.net.UnknownHostException;

import com.sflow.util.Address;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;

/* Extended Data types */
/* Extended router data */
public class ExtendedRouterFlowRecord {
     private Address nextHop; /* IP address of next hop router */
     private long      srcMask;   /* Source address prefix mask bits */ 
     private long      destMask; /*  Destination address prefix mask bits */

     public Address getNextHop() {
          return nextHop;
     }

     public void setNextHop(Address nextHop) {
          this.nextHop = nextHop;
     }

     public long getSrcMask() {
          return srcMask;
     }

     public void setSrcMask(long mask) {
          this.srcMask = mask;
     }

     public long getDestMask() {
          return destMask;
     }

     public void setDestMask(long mask) {
          this.destMask = mask;
     }

     public static ExtendedRouterFlowRecord parse(byte data[]) throws HeaderParseException {
          
          ExtendedRouterFlowRecord h = new ExtendedRouterFlowRecord();
          
          try {
               h.setNextHop(new Address(data[0], data[1], data[2], data[3]));
               h.setSrcMask(Utility.fourBytesToLong(data, 4)); 
               h.setDestMask(Utility.fourBytesToLong(data, 8)); 
               
               return h;
          } catch (Exception e) {
               throw new HeaderParseException("Parse error: " + e.getMessage());
          }          
     }
     
     public byte[] getBytes() throws HeaderBytesException {          
          // ToDo
          return null;
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[Extended Router FlowRecord]: ");

          sb.append(", nextHop: ");
          try {
               sb.append(getNextHop().getInetAddress());
          } catch (UnknownHostException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          } catch (UtilityException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
          }
          
          sb.append(", SrcMask: ");
          sb.append(getSrcMask());
          
          sb.append(", destMask: ");
          sb.append(getDestMask());

          return sb.toString();
     }
}
