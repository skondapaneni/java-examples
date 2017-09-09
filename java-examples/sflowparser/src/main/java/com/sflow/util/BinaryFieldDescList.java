package com.sflow.util;

import java.util.ArrayList;

public class BinaryFieldDescList {
	private ArrayList<BinaryFieldDesc> fields = new ArrayList<BinaryFieldDesc>(7);
		
	/* Add a field item (value, name) to an field List */
	public void addField(String name, DataTypeE type, int n) {
		fields.add(new BinaryFieldDesc(name, type, n, fields.size()));
	}
		
	public void addField(BinaryFieldDesc binaryFieldDesc) {
		fields.add(binaryFieldDesc);
	}
	
	public BinaryFieldDesc fromString(String text) {
		if (text != null) {			
			for (int i = 0; i < fields.size(); i++) {
				BinaryFieldDesc b = fields.get(i);
				if (text.equalsIgnoreCase(b.getName())) {					
					return b;
				}
			}
		}
	
		throw new IllegalArgumentException();
	}

	public BinaryFieldDesc convert(String arg0) {
		return fromString(arg0);
	}

	public ArrayList<BinaryFieldDesc> getFields() {
		return fields;
	}

	public void setFields(ArrayList<BinaryFieldDesc> vals) {
		this.fields = vals;
	}
	
	public int offset() {
		BinaryFieldDesc last = fields.get(fields.size()-1);		
		return last.fieldOffset(this) + last.getLen() * last.getType().nbytes();
	}
	
	 public void parse(byte[] data, int offset) throws HeaderParseException {
		if ((data.length - offset) < offset()) throw new HeaderParseException("Data array too short.");
		for (int i = 0; i < fields.size(); i++) {
			fields.get(i).parse(data, offset);
		}
	 }
	 
	 public Object getFieldValue(String name, byte[] data, int offset) throws UtilityException {
		 BinaryFieldDesc bfd = convert(name);
		 if (bfd != null) {
			 return bfd.getFieldValue(this, data, offset);
		 }
		 
		 return null;
	 }
}