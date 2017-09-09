package com.sflow.packet.header.flowsample;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

/* Extended Data types */
/* Extended switch data */
public class ExtendedSwitchFlowRecord {
    private long     srcVlan; /* The 802.1Q VLAN id of incomming frame */
    private long     srcPriority;   /* The 802.1p priority */
    private long     destVlan; /* The 802.1Q VLAN id of outgoing frame */
    private long     destPriority;  /* The 802.1p priority */

    public long getSrcVlan() {
        return srcVlan;
    }

    public void setSrcVlan(long vlan) {
        this.srcVlan = vlan;
    }

    public long getSrcPriority() {
        return srcPriority;
    }

    public void setSrcPriority(long priority) {
        this.srcPriority = priority;
    }

    public long getDestVlan() {
        return destVlan;
    }

    public void setDestVlan(long vlan) {
        this.destVlan = vlan;
    }

    public long getDestPriority() {
        return destPriority;
    }

    public void setDestPriority(long priority) {
        this.destPriority = priority;
    }


    public static ExtendedSwitchFlowRecord parse(byte data[]) throws HeaderParseException {
        
        ExtendedSwitchFlowRecord h = new ExtendedSwitchFlowRecord();
        
        try {
            h.setSrcVlan(Utility.fourBytesToLong(data, 0)); 
            h.setSrcPriority(Utility.fourBytesToLong(data, 4)); 
            h.setDestVlan(Utility.fourBytesToLong(data, 8)); 
            h.setDestPriority(Utility.fourBytesToLong(data, 12)); 
            
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
        sb.append("[Extended Switch FlowRecord]: ");

        sb.append(", srcVlan: ");
        sb.append(getSrcVlan());
        
        sb.append(", SrcPriority: ");
        sb.append(getSrcPriority());
        
        sb.append(", destVlan: ");
        sb.append(getDestVlan());

        sb.append(", DestPriority: ");
        sb.append(getDestPriority());
        
        return sb.toString();
    }
}
