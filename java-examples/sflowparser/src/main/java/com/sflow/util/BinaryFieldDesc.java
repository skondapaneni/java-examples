package com.sflow.util;

public class BinaryFieldDesc {

	private String     name;
	private DataTypeE  type;
	private int        len;
	private int        ordinal;
		
	public BinaryFieldDesc(String name, DataTypeE type, int n, int oridinal) {
		this.name = name;
		this.setType(type);
		this.setLen(n);
		this.setOrdinal(ordinal);
	}

	public String getName() {
		return name;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public DataTypeE getType() {
		return type;
	}

	public void setType(DataTypeE type) {
		this.type = type;
	}
	
	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int val) {
		this.ordinal = val;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}
	
	public BinaryFieldDesc next(BinaryFieldDescList fl){
		return fl.getFields().get((getOrdinal()+1) % fl.getFields().size());
    }
	
	public BinaryFieldDesc prev(BinaryFieldDescList fl) {
		return fl.getFields().get((getOrdinal()-1) % fl.getFields().size());	
	}

	public int fieldOffset(BinaryFieldDescList fl) {
		if (getOrdinal() == 0) {
			return 0;
		}
		return (this.prev(fl).getLen() * this.prev(fl).getType().nbytes()) + 
				this.prev(fl).fieldOffset(fl);
	}

	public void parse(byte[] data, int offset) throws HeaderParseException {
		
	}

	public Object getFieldValue(BinaryFieldDescList binaryFieldDescList, 
			byte[] data, int offset) 
			throws UtilityException {
		
		int fo = fieldOffset(binaryFieldDescList);

		switch (this.type) {
		case IPV4:
			return Utility.fourBytesToIpAddr(data, offset + fo);
		case IPV6:
			return new V6Address(data, offset + fo);
		case UINT16:
			return Utility.twoBytesToInteger(data, offset + fo);
		case UINT32:
			return Utility.fourBytesToLong(data, offset + fo);
		case UINT64:
			return Utility.eightBytesToBigInteger(data, offset + fo);
		case UINT8:
			return Utility.oneByteToShort(data, offset + fo);
		default:
			break;	
		}
		
		return null;
	}	
	
}
