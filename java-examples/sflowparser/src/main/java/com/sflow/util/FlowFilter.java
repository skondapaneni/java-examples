package com.sflow.util;

public class FlowFilter {
    protected byte q[];
    
    public FlowFilter() {
    	q = new byte[11*8];
    }
    
    public byte[] getFilterAsByteArray() {
    	return q;
    }
    
    public byte getValueAsByte(FlowFilterE e) throws InvalidTypeException {   	
    	if (e.getType() != DataTypeE.UINT8 ||
    			e.getType().nbytes() != 1)  {
			throw new InvalidTypeException(e + " is not a uint8 type!!");
    	} else {
    		return q[e.offset()];
    	}  	
    }
    
    public byte[] getValueAsByteArray(FlowFilterE e) throws InvalidTypeException {   	
    	if (e.getType() != DataTypeE.UINT8 ||
    			e.getType().nbytes() == 1)  {
			throw new InvalidTypeException(e + " is not a byte[] type!!");
    	} else {
    		byte[] retval = new byte[e.getType().nbytes()];
    		System.arraycopy(q[e.offset()], 0, retval, 0, e.getType().nbytes());
    		return retval;
    	}  	
    }
    
    public int getValueAsUint16(FlowFilterE e) throws InvalidTypeException {   	
    	if (e.getType() != DataTypeE.UINT16 ||
    			e.getType().nbytes() != 1 ) {
			throw new InvalidTypeException(e + " is not a uint16 type!!");
    	} else {
    		byte[] retval = new byte[2];
    		System.arraycopy(q[e.offset()], 0, retval, 0, 1);
    		try {
				return Utility.twoBytesToInteger(retval);
			} catch (UtilityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new InvalidTypeException(e + " is not a uint16 type!!");
			}
    	}  	
    }
    
    public long getValueAsUint32(FlowFilterE e) throws InvalidTypeException {   	
    	if (e.getType() != DataTypeE.UINT32 ||
    			e.getType().nbytes() != 1 ) {
			throw new InvalidTypeException(e + " is not a uint32 type!!");
    	} else {
    		byte[] retval = new byte[4];
    		System.arraycopy(q[e.offset()], 0, retval, 0, 4);
    		try {
				return Utility.fourBytesToLong(retval);
			} catch (UtilityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new InvalidTypeException(e + " is not a uint32 type!!");
			}
    	}  	
    }  
    
    public void setValue(FlowFilterE e, byte[] b, int offset)  {
		System.arraycopy(b,  offset, q, e.offset(), e.getLen() * e.getType().nbytes() );
    }
    
    public void setValue(FlowFilterE e, byte[] b, int offset, long mask)  {
		System.arraycopy(b, offset, q, e.offset(), e.getLen() * e.getType().nbytes() );
		int nbytes = e.getType().nbytes()*e.getLen(); 
		for (int i = 0; i < nbytes; i++) {
	        q[e.offset()+i] = (byte) (q[e.offset()+i] & ((int) (mask >> (nbytes - i - 1)*8 ) & 0xFF));
		}
    }
}

