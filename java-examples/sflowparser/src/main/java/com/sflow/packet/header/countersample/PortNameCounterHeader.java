package com.sflow.packet.header.countersample;

import com.sflow.util.HeaderParseException;
import com.sflow.util.SFlowString;

public class PortNameCounterHeader extends SFlowString {
     
     public static PortNameCounterHeader parse(byte[] data) throws HeaderParseException {
          PortNameCounterHeader pc = new PortNameCounterHeader();
          return SFlowString.parse(data, pc);
     }
               
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[PortNameCounterHeader]:");     
          sb.append(toString("PortName"));
          return sb.toString();          
     }
}
