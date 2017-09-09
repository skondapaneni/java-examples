package com.sflow.packet.header.flowsample;

import com.sflow.util.FlowFilterE;
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.MacAddress;
import com.sflow.util.Utility;

public class TaggedMacHeader extends MacHeader {
     // vlan tag
     protected int tpid;
     protected int tci;
     
     public TaggedMacHeader() {
    	 super();
     }
     
     public int getTCI() {
          return tci;
     }
     
     public int getTpid() {
          return tpid;
     }

     public void setTpid(int tpid) {
          this.tpid = tpid;
     }
     
     public void setTCI(int tci) {
          this.tci = tci;
     }
	
     public static TaggedMacHeader parse(byte data[]) throws HeaderParseException {
		try {
			if (data.length < 18)
				throw new HeaderParseException("Data array too short.");
			
			TaggedMacHeader m = new TaggedMacHeader();
			// destination
			m.flowFilter.setValue(FlowFilterE.DMAC, data, 0);
			// source
			m.flowFilter.setValue(FlowFilterE.SMAC, data, 6);
			m.flowFilter.setValue(FlowFilterE.VLAN, data, 12);
			m.flowFilter.setValue(FlowFilterE.L2_FLAGS, data, 14);

			// destination
			m.setDestination(new MacAddress(data, 0));
			
			// source
			m.setSource(new MacAddress(data, 6));

			// vlan tag
			byte tpid[] = { (byte) 0x81, (byte) 0x00 };
			m.tpid = Utility.twoBytesToInteger(tpid);

			// vlan tag contents
			// Qos 3 bits
			// CI 1 bit
			// VID 12 bits
			byte tci[] = new byte[2];
			System.arraycopy(data, 14, tci, 0, 2);
			m.setTCI(Utility.twoBytesToInteger(tci));
			
			return m;
		} catch (Exception e) {
			throw new HeaderParseException("Parse error: " + e.getMessage());
		}
     }
     
     public byte[] getBytes() throws HeaderBytesException {
          try {
               byte[] data = new byte[18 + offcut.length];
               // destination
               System.arraycopy(destination, 0, data, 0, 6);
               // source
               System.arraycopy(source, 0, data, 6, 6);
               // vlan tag
               System.arraycopy(Utility.integerToTwoBytes(tpid), 0, data, 12, 2);
               System.arraycopy(Utility.integerToTwoBytes(tci), 0, data, 14, 2);
               // type
               System.arraycopy(Utility.integerToTwoBytes(type), 0, data, 16, 2);
               // offcut
               System.arraycopy(offcut, 0, data, 18, offcut.length);
               
               return data;
          } catch (Exception e) {
               throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
          }
     }
     
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("[TaggedMacHeader]: ");
          sb.append(super.toString());
          sb.append(", TPID(VLAN): ");
          sb.append(getTpid());
          sb.append(", TCI(VLAN): ");
          sb.append(getTCI());
          
          return sb.toString();
     }
}
