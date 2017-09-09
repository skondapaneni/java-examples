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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Address {
	
	public final static int ANY = 256;

	private int firstOctet 				= ANY;
	private int secondOctet 			= ANY;
	private int thirdOctet 				= ANY;
	private int fourthOctet				= ANY;		
	private int 						maskBits;
	
	private void setMaskBits() {
		maskBits = 32;
		if (fourthOctet == ANY) {
			maskBits = 24;
			if (thirdOctet == ANY) {
				maskBits = 16;
				if (secondOctet == ANY) {
					maskBits = 8;
				}
			}
		}
	}
	
	public Address() {
		
	}
	
	public Address(int firstOctet, int secondOctet, int thirdOctet, int fourthOctet) throws UtilityException {
		if ((firstOctet < 0) || (firstOctet > 256) || (secondOctet < 0) || (secondOctet > 256) 
				|| (thirdOctet < 0) || (thirdOctet > 256) || (fourthOctet < 0) || (fourthOctet > 256)) {
			throw new UtilityException("Address is malformed.");
		}
		this.firstOctet = firstOctet;
		this.secondOctet = secondOctet;
		this.thirdOctet = thirdOctet;
		this.fourthOctet = fourthOctet;
		setMaskBits();
	}
	
	
	public void init (byte[] address) throws UtilityException  {
		
		firstOctet = Utility.oneByteToInteger(address[0]);
		secondOctet = Utility.oneByteToInteger(address[1]);
		
		if (address.length > 2) 
			thirdOctet = Utility.oneByteToInteger(address[2]);
		
		if (address.length > 3) 
			fourthOctet = Utility.oneByteToInteger(address[3]);
	
	}
	
	public Address(String address) throws UtilityException {
		StringTokenizer st = new StringTokenizer(address, ".");
		if (st.countTokens() != 4) {
			throw new UtilityException("4 octets in address string are required.");
		}
		int i = 0;
		int temp;
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (tok.matches("\\d+")) {
				temp = Integer.parseInt(tok);
				if ((temp < 0) || (temp > 255)) {
					throw new UtilityException("Address is in incorrect format.");
				}
				switch (i) {
				case 0: firstOctet = temp; ++i; break;
				case 1: secondOctet = temp; ++i; break;
				case 2: thirdOctet = temp; ++i; break;
				case 3: fourthOctet = temp; ++i; break;
				}
			} else if (tok.equals("*")) {
				switch (i) {
				case 0: firstOctet = ANY ; ++i; break;
				case 1: secondOctet = ANY; ++i; break;
				case 2: thirdOctet = ANY ; ++i; break;
				case 3: fourthOctet =  ANY; ++i; break;
				}
			} else {
				throw new UtilityException("Address is in incorrect format.");
			}
		}
		setMaskBits();
	}
	
	public Address(byte[] address) throws UtilityException {
		if (address.length < 2) {
			throw new UtilityException("atleast 2 bytes are required.");
		}
		firstOctet = Utility.oneByteToInteger(address[0]);
		secondOctet = Utility.oneByteToInteger(address[1]);
		
		if (address.length > 2) 
			thirdOctet = Utility.oneByteToInteger(address[2]);
		
		if (address.length > 3) 
			fourthOctet = Utility.oneByteToInteger(address[3]);
		
		setMaskBits();
	}
	
	public String toString() {
		String retVal = "";
		int octval = -1;
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 0: 
				octval = firstOctet;		
				break;
			case 1: 
				octval = secondOctet;
				break;
			case 2:
				octval = thirdOctet;
				break;
			case 3: 
				octval = fourthOctet;
				break;

			}
			if (octval == ANY) {
				retVal += "*";
			} else {
				retVal += octval;
			}
			if (i != 3) {
				retVal += ".";
			}
		}
		
		return retVal;
	}
	
	public boolean isAny() {
		return (firstOctet == ANY);
	}
	
	public byte[] getBytes() throws UtilityException {
		
		if (isAny())  return null;
		
		byte[] result = new byte[maskBits/8];
		result[0] = Utility.integerToOneByte(firstOctet);
		
		if (maskBits == 8) {
			return result;
		}
		
		result[1] = Utility.integerToOneByte(secondOctet);	
		if (maskBits == 16) {
			return result;
		}
		
		result[2] = Utility.integerToOneByte(thirdOctet);
		if (maskBits == 24) {
			return result;
		}
		
		result[3] = Utility.integerToOneByte(fourthOctet);
		return result;
	}
	
	public InetAddress getInetAddress() throws UtilityException, UnknownHostException {
		byte[] address = new byte[4];
		if (isAny()) {
			return null;
		}
		address[0] = Utility.integerToOneByte(firstOctet);
		address[1] = Utility.integerToOneByte(secondOctet);
		address[2] = Utility.integerToOneByte(thirdOctet);
		address[3] = Utility.integerToOneByte(fourthOctet);
		return InetAddress.getByAddress(address);
	}
	
	public int getFirstOctet() {
		return firstOctet;
	}

	public int getSecondOctet() {
		return secondOctet;
	}

	public int getThirdOctet() {
		return thirdOctet;
	}

	public int getFourthOctet() {
		return fourthOctet;
	}

	public int getMaskBits() {
		return maskBits;
	}

	public void setFirstOctet(int firstOctet) {
		this.firstOctet = firstOctet;
	}

	public void setSecondOctet(int secondOctet) {
		this.secondOctet = secondOctet;
	}

	public void setThirdOctet(int thirdOctet) {
		this.thirdOctet = thirdOctet;
	}

	public void setFourthOctet(int fourthOctet) {
		this.fourthOctet = fourthOctet;
	}

	public void setMaskBits(int maskBits) {
		this.maskBits = maskBits;
	}

	public boolean equals(Object obj) {
		if (obj == null) return false;
		try {
			byte[] data1 = this.getBytes();
			byte[] data2 = ((Address) obj).getBytes();
			if (data1 != null && data2 != null && 
					data1.length == data2.length) {
				if ((data1[0] == data2[0]) && (data1[1] == data2[1]) &&
						(data1[2] == data2[2]) && (data1[3] == data2[3])) 
					return true;
			}
			return false;
		} catch (UtilityException ue) {
			return false;
		}
	}
	
	public static Address next(Address  a) 
			throws UtilityException {
		
		Address n = null;
		
		if (a.isAny() || a.firstOctet == ANY) 
			return null;
		
		if (a.fourthOctet <  255) {
			n = new Address(a.firstOctet, a.secondOctet, a.thirdOctet, a.fourthOctet+1);		
			return n;
		}
		if (a.fourthOctet == ANY) {
			n = new Address(a.firstOctet, a.secondOctet, a.thirdOctet, 1);		
			return n;
		}
		if (a.thirdOctet < 255) {
			n = new Address(a.firstOctet, a.secondOctet, a.thirdOctet+1, 0);
			return n;
		}
		if (a.thirdOctet == ANY) {
			n = new Address(a.firstOctet, a.secondOctet, 1, 0);		
			return n;
		}
		if (a.secondOctet < 255) {
			n = new Address(a.firstOctet, a.secondOctet+1, 0, 0);
			return n;
		}
		if (a.secondOctet == ANY) {
			n = new Address(a.firstOctet, 1, 0, 0);		
			return n;
		}
		if (a.firstOctet < 255 && a.firstOctet > 0) {
			n = new Address(a.firstOctet+1, 0, 0, 0);
		}
		return null;
	}
	
	public int hashCode() {
		if (isAny()) {
			return new Integer(ANY).hashCode();
		}
		return (firstOctet << 24) + (secondOctet << 16) + (thirdOctet << 8) + fourthOctet; 
	}
}
