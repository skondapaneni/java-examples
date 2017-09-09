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
package com.sflow.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Utility {
	public static final byte integerToOneByte(int value) throws UtilityException {
		if ((value > Math.pow(2, 15)) || (value < 0)) {
			throw new UtilityException("Integer value " + value + " is larger than 2^15");
		}
		return (byte) (value & 0xFF);
	}
	
	public static final byte shortToOneByte(short value) throws UtilityException {
		if ((value > Math.pow(2, 15)) || (value < 0)) {
			throw new UtilityException("Integer value " + value + " is larger than 2^15");
		}
		return (byte) (value & 0xFF);
	}

	public static final byte[] integerToTwoBytes(int value) throws UtilityException {
		byte[] result = new byte[2];
		if ((value > Math.pow(2, 31)) || (value < 0)) {
			throw new UtilityException("Integer value " + value + " is larger than 2^31");
		}
		result[0] = (byte)((value >>> 8) & 0xFF);
		result[1] = (byte)(value & 0xFF);
		return result; 
	}

	public static final byte[] longToFourBytes(long value) throws UtilityException {
		byte[] result = new byte[4];
		result[0] = (byte)((value >>> 24) & 0xFF);
		result[1] = (byte)((value >>> 16) & 0xFF);
		result[2] = (byte)((value >>> 8) & 0xFF);
		result[3] = (byte)(value & 0xFF);
		return result; 
	}

	public static final byte[] longToSixBytes(long value) throws UtilityException {
		byte[] result = new byte[6];
		result[0] = (byte)((value >>> 40) & 0xFF);
		result[1] = (byte)((value >>> 32) & 0xFF);
		result[2] = (byte)((value >>> 24) & 0xFF);
		result[3] = (byte)((value >>> 16) & 0xFF);
		result[4] = (byte)((value >>> 8) & 0xFF);
		result[5] = (byte)(value & 0xFF);
		return result; 
	}

	public static final byte[] BigIntegerToEightBytes(BigInteger value) throws UtilityException{
		byte[] result = new byte[8];
		byte[] tmp = value.toByteArray();
		if(tmp.length > 8){
			System.arraycopy(tmp, tmp.length-8, result, 0, 8);
		} else {
			System.arraycopy(tmp, 0, result, 8-tmp.length, tmp.length);
		}
		return result;
	}

	public static final int oneByteToInteger(byte value) throws UtilityException {
		return (int) value & 0xFF;
	}

	public static final short oneByteToShort(byte value) throws UtilityException {
		return (short) (value & 0xFF);
	}

	public static final int twoBytesToInteger(byte[] value) throws UtilityException {
		if (value.length < 2) {
			throw new UtilityException("Byte array too short!");
		}
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		return ((temp0 << 8) + temp1);
	}

	public static final long fourBytesToLong(byte[] value) throws UtilityException {
		if (value.length < 4) {
			throw new UtilityException("Byte array too short!");
		}
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		int temp3 = value[3] & 0xFF;
		return (((long) temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
		// long unsignedValue = fourBytesIJustRead & 0xffffffffl;

	}

	public static final long sixBytesToLong(byte[] value) throws UtilityException {
		if (value.length < 6) {
			throw new UtilityException("Byte array too short!");
		}
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		int temp3 = value[3] & 0xFF;
		int temp4 = value[4] & 0xFF;
		int temp5 = value[5] & 0xFF;
		return ((((long) temp0) << 40) + (((long) temp1) << 32) + (((long) temp2) << 24) + (temp3 << 16) + (temp4 << 8) + temp5);
	}

	public static final BigInteger eightBytesToBigInteger(byte[] value) throws UtilityException {
		if(value.length < 8){
			throw new UtilityException("Byte array too short!");
		}

		BigInteger bInt = new BigInteger(1, value);
		return bInt;
	}


	public static final int oneByteToInteger(byte[] value, int offset) throws UtilityException {
		return (int) value[offset] & 0xFF;
	}

	public static final short oneByteToShort(byte[] value, int offset) throws UtilityException {
		return (short) (value[offset] & 0xFF);
	}

	public static final int twoBytesToInteger(byte[] value, int offset) throws UtilityException {
		if (value.length - offset < 2) {
			throw new UtilityException("Byte array too short!");
		}
		int temp0 = value[offset] & 0xFF;
		int temp1 = value[offset+1] & 0xFF;
		return ((temp0 << 8) + temp1);
	}

	public static final long fourBytesToLong(byte[] value, int offset) throws UtilityException {
		if (value.length-offset < 4) {
			throw new UtilityException("Byte array too short!");
		}
		int temp0 = value[offset] & 0xFF;
		int temp1 = value[offset+1] & 0xFF;
		int temp2 = value[offset+2] & 0xFF;
		int temp3 = value[offset+3] & 0xFF;
		return (((long) temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
	}
	
	public static final Address fourBytesToIpAddr(byte[] value, int offset) throws UtilityException {
		if (value.length-offset < 4) {
			throw new UtilityException("Byte array too short!");
		}
		int temp0 = value[offset] & 0xFF;
		int temp1 = value[offset+1] & 0xFF;
		int temp2 = value[offset+2] & 0xFF;
		int temp3 = value[offset+3] & 0xFF;

		return (new Address(temp0, temp1, temp2, temp3));
	}

	public static final long sixBytesToLong(byte[] value, int offset) throws UtilityException {
		if (value.length-offset < 6) {
			throw new UtilityException("Byte array too short!");
		}
		int temp0 = value[offset] & 0xFF;
		int temp1 = value[offset+1] & 0xFF;
		int temp2 = value[offset+2] & 0xFF;
		int temp3 = value[offset+3] & 0xFF;
		int temp4 = value[offset+4] & 0xFF;
		int temp5 = value[offset+5] & 0xFF;
		return ((((long) temp0) << 40) + (((long) temp1) << 32) + (((long) temp2) << 24) + (temp3 << 16) + (temp4 << 8) + temp5);
	}

	public static final BigInteger eightBytesToBigInteger(byte[] value, int offset) 
				throws UtilityException {
		if(value.length-offset < 8){
			throw new UtilityException("Byte array too short!");
		}
		byte[] temp = new byte[8];
		System.arraycopy(value, offset, temp, 0, 8); 
		BigInteger bInt = new BigInteger(1, temp);
		return bInt;
	}

	public static final String dumpBytes(byte[] data) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (byte b : data) {
			i++;
			sb.append(String.valueOf(b));
			if (i < data.length) sb.append(", ");
			if ((i % 15) == 0) sb.append("\n");
		}
		return sb.toString();
	}
	
	public static final String toString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.valueOf(b));
		}
		return sb.toString();
	}

	public static final String dumpBytes(byte[] data, int len) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (byte b : data) {
			i++;
			sb.append(String.valueOf(b));
			if (i == len) break;
		}	
		return sb.toString();
	}
	
	public static String prependZeroIfNeededForMacAddress(String string) {
		if (string == null) return "00";
		if (string.length() == 0) return "00";
		if (string.length() == 1) return "0" + string;
		return string;
	}

	public static boolean isConfigured(Inet4Address inet4Address) {
		if (inet4Address == null) return false;
		try {
			if (inet4Address.equals(Inet4Address.getByName("0.0.0.0"))) return false;
		} catch (UnknownHostException e) {
			return false;
		}
		return true;
	}

	public static boolean isConfigured(Inet6Address inet6Address) {
		if (inet6Address == null) return false;
		try {
			if (inet6Address.equals(Inet6Address.getByName("0:0:0:0:0:0:0:0"))) return false;
		} catch (UnknownHostException e) {
			return false;
		}
		return true;
	}
	
	/*
	 * convert bytes to ByteBuffer
	 */
	public static ByteBuffer fromByteArray(byte[] bytes) {
	    final ByteBuffer ret = ByteBuffer.wrap(new byte[bytes.length]);	 
	    ret.put(bytes);
	    ret.flip();	 
	    return ret;
	}
	
	/*
	 * convert byte buffer to string
	 */
	public static String toString(ByteBuffer bb) {
	    final byte[] bytes = new byte[bb.remaining()]; 
	    bb.duplicate().get(bytes); 
	    return new String(bytes);
	}
	
    public static void toHex(byte[] buf) {
        System.out.print("----keyStr " + buf.length + " ");
        for (int j = 0; j < buf.length; j++) {
             System.out.printf("%x-", buf[j]);
        }
        System.out.println();
    }
    
    public static void toHex(String str) {
    	toHex(str.getBytes());
    }
    
    public static String toHexString(byte[] bytes) {
    	//return Hex.encodeHexString( bytes);
    	final char[] hexArray = "0123456789ABCDEF".toCharArray();
    	char[] hexChars = new char[bytes.length * 2];
    	for ( int j = 0; j < bytes.length; j++ ) {
    		int v = bytes[j] & 0xFF;
    		hexChars[j * 2] = hexArray[v >>> 4];
    		hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    	}
    	return new String(hexChars);
    }
    
    public static String toHexString(ByteBuffer key) {
    	byte[] bytes = null;
    	try {
    		bytes = new String(key.array()).getBytes("ISO-8859-1");
    	} catch (UnsupportedEncodingException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return toHexString(bytes);
    }
    
	public static void printRow(String format, Object values[], String line) {
		
		if (line != null)
			System.out.format(line);	
		System.out.format(format, values);
		if (line != null)
			System.out.format(line);
	}

	public static String getHLine(int len) {
		String line = "+";
		for (int i = 0; i < len; i++) {
			line += "-";
		}
		
		line += "--";
		return line;
	}
	
	public static String getHLine(String col) {
		String line = "+";
		for (int i = 0; i < col.length(); i++) {
			line += "-";
		}
		
		line += "--";
		return line;
	}
	
	public static String getHLine(String col, int minVal) {
		return getHLine(Math.max(col.length(), minVal));
	}
	
	public static String getHFormat(int len) {
		String format = "| %-" + len + "s ";
		return format;
	}
	
	public static String getHFormat(String col) {
		String format = "| %-" + col.length() + "s ";
		return format;
	}
	
	public static String getHFormat(String col, int minVal) {
		String format = "| %-" + Math.max(col.length(), minVal) + "s ";
		return format;
	}
	
	public static String getHLineForKeys(int nkeys) {
		String line = "";
		 
	    if (nkeys==1) {
	    	line += "+-------------------";
	    } else if (nkeys==2)  {
	    	line += "+------------------------";
	    } else if (nkeys==3) {
	    	line += "+----------------------------------";
	    } else {
	    	line = "+--------------------------------------------------";
	    }
	    
	    return line;
	}
	
	public static String getHFormatForKeys(int nkeys) {
		
		String leftAlignFormat = "";
		 
	    if (nkeys==1) {
	    	leftAlignFormat += "| %-17s ";
	    } else if (nkeys==2)  {
	    	leftAlignFormat += "| %-22s ";	    	
	    } else if (nkeys==3) {
	    	leftAlignFormat += "| %-32s ";
	    } else {
	    	leftAlignFormat = "| %-48s ";
	    }
	    
	    return leftAlignFormat;
	}
	
}
