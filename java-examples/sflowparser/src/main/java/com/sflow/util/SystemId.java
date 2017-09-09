/*
 * Adapted from jsflow
 */
package com.sflow.util;

import java.util.StringTokenizer;

public class SystemId {
	private int firstOctet;
	private int secondOctet;
	private int thirdOctet;
	private int fourthOctet;
	private int fifthOctet;
	private int sixthOctet;
	private int seventhOctet;
	private int eighthOctet;
	
	public SystemId() {
	}
	
	public SystemId(String address) throws UtilityException {
		StringTokenizer st = new StringTokenizer(address, ":");
		if (st.countTokens() != 8) {
			throw new UtilityException("8 octets in a System ID  string are required.");
		}
		int i = 0;
		while (st.hasMoreTokens()) {
			int temp = Integer.parseInt(st.nextToken(), 16);
			if ((temp < 0) || (temp > 255)) {
				throw new UtilityException("Address is in incorrect format.");
			}
			switch (i) {
			case 0: firstOctet = temp; ++i; break;
			case 1: secondOctet = temp; ++i; break;
			case 2: thirdOctet = temp; ++i; break;
			case 3: fourthOctet = temp; ++i; break;
			case 4: fifthOctet = temp; ++i; break;
			case 5: sixthOctet = temp; ++i; break;
			case 6: seventhOctet = temp; ++i; break;
			case 7: eighthOctet = temp; ++i; break;

			}
		}
	}
	
	public SystemId(byte[] address) throws UtilityException {
		if (address.length < 8) {
			throw new UtilityException("8 bytes are required.");
		}
		firstOctet = Utility.oneByteToInteger(address[0]);
		secondOctet = Utility.oneByteToInteger(address[1]);
		thirdOctet = Utility.oneByteToInteger(address[2]);
		fourthOctet = Utility.oneByteToInteger(address[3]);
		fifthOctet = Utility.oneByteToInteger(address[4]);
		sixthOctet = Utility.oneByteToInteger(address[5]);
		seventhOctet = Utility.oneByteToInteger(address[6]);
		eighthOctet = Utility.oneByteToInteger(address[7]);

	}
	
	public SystemId(byte[] address, int offset) throws UtilityException {
		if (address.length - offset < 8) {
			throw new UtilityException("8 bytes are required.");
		}
		firstOctet = Utility.oneByteToInteger(address[offset]);
		secondOctet = Utility.oneByteToInteger(address[1+offset]);
		thirdOctet = Utility.oneByteToInteger(address[2+offset]);
		fourthOctet = Utility.oneByteToInteger(address[3+offset]);
		fifthOctet = Utility.oneByteToInteger(address[4+offset]);
		sixthOctet = Utility.oneByteToInteger(address[5+offset]);
		seventhOctet = Utility.oneByteToInteger(address[6+offset]);
		eighthOctet = Utility.oneByteToInteger(address[7+offset]);

	}
	
	public String toString() {
		return Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(firstOctet)) + ":" + 
				Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(secondOctet)) + ":" + 
				Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(thirdOctet)) + ":" + 
				Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(fourthOctet)) + ":" + 
				Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(fifthOctet)) + ":" + 
				Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(sixthOctet)) + ":" + 
				Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(seventhOctet)) + ":" + 
				Utility.prependZeroIfNeededForMacAddress(Integer.toHexString(eighthOctet));
	}
	
	public byte[] getBytes() throws UtilityException {
		byte[] result = new byte[8];
		result[0] = Utility.integerToOneByte(firstOctet);
		result[1] = Utility.integerToOneByte(secondOctet);
		result[2] = Utility.integerToOneByte(thirdOctet);
		result[3] = Utility.integerToOneByte(fourthOctet);
		result[4] = Utility.integerToOneByte(fifthOctet);
		result[5] = Utility.integerToOneByte(sixthOctet);
		result[6] = Utility.integerToOneByte(seventhOctet);
		result[7] = Utility.integerToOneByte(eighthOctet);

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SystemId other = (SystemId) obj;
		if (firstOctet != other.firstOctet) return false;
		if (secondOctet != other.secondOctet) return false;
		if (thirdOctet != other.thirdOctet) return false;
		if (fourthOctet != other.fourthOctet) return false;
		if (fifthOctet != other.fifthOctet) return false;		
		if (sixthOctet != other.sixthOctet) return false;
		if (seventhOctet != other.seventhOctet) return false;
		if (eighthOctet != other.eighthOctet) return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		return (firstOctet << 56) + (secondOctet << 48) + (thirdOctet << 40) + (fourthOctet << 32) + 
				(fifthOctet << 24) + (sixthOctet << 16) + (seventhOctet << 8) + (eighthOctet); 
	}
}
