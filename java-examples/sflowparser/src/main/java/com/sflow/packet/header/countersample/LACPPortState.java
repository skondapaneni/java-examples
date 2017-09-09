package com.sflow.packet.header.countersample;

/*
 * Adapted from jsflow and add support for SFlow LACP PortState
 */
import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;
import com.sflow.util.UtilityException;

/* LAG Port Statistics - see http://sflow.org/sflow_lag.txt */
/* opaque = counter_data; enterprise = 0; format = 7 */

public class LACPPortState {
    // enterprise = 0, format = 1001

    private long all;
    private short actorAdmin;
    private short actorOper;
    private short partnerAdmin;
    private short partnerOper;
        
    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }

    public short getActorAdmin() {
        return actorAdmin;
    }

    public void setActorAdmin(short actorAdmin) {
        this.actorAdmin = actorAdmin;
    }

    public short getActorOper() {
        return actorOper;
    }

    public void setActorOper(short actorOper) {
        this.actorOper = actorOper;
    }

    public short getPartnerAdmin() {
        return partnerAdmin;
    }

    public void setPartnerAdmin(short partnerAdmin) {
        this.partnerAdmin = partnerAdmin;
    }

    public short getPartnerOper() {
        return partnerOper;
    }

    public void setPartnerOper(short partnerOper) {
        this.partnerOper = partnerOper;
    }

    public static LACPPortState parse(byte[] data, int offset) 
            throws HeaderParseException {
        try {
            
            LACPPortState lacp = new LACPPortState();

            lacp.setAll(Utility.fourBytesToLong(data, offset));
            lacp.setActorAdmin(Utility.oneByteToShort(data, offset+4));
            lacp.setActorOper(Utility.oneByteToShort(data, offset+5));
            lacp.setPartnerAdmin(Utility.oneByteToShort(data, offset+6));
            lacp.setPartnerOper(Utility.oneByteToShort(data, offset+7));

            return lacp;
        }  catch (Exception e) {
            throw new HeaderParseException("Parse error: " + e.getMessage());
        }
    }

    public byte[] getBytes() throws HeaderBytesException {
        try {
            byte[] data = new byte[8];

            System.arraycopy(Utility.longToFourBytes(all), 0, data, 0, 4);
            System.arraycopy(Utility.shortToOneByte(actorAdmin), 0, data, 4, 1);
            System.arraycopy(Utility.shortToOneByte(actorOper), 0, data, 5, 1);
            System.arraycopy(Utility.shortToOneByte(partnerAdmin), 0, data, 6, 1);
            System.arraycopy(Utility.shortToOneByte(partnerOper), 0, data, 7, 1);
            
            return data;
        } catch (Exception e) {
            throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[LACP PortState]:");

        sb.append("all: ");
        sb.append(getAll());

        try {
            sb.append(", actorAdmin: " + Utility.shortToOneByte(actorAdmin));
            sb.append(", actorOper: " + Utility.shortToOneByte(actorOper));
            sb.append(", actorOper: " + Utility.shortToOneByte(partnerAdmin));
            sb.append(", actorOper: " + Utility.shortToOneByte(partnerOper));
            
        } catch (UtilityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return sb.toString();
    }
}
