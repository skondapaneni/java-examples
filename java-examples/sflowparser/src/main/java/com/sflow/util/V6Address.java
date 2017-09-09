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

import java.lang.System;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.google.common.net.InetAddresses;

public class V6Address {
	byte[]  v6 = new byte[16];
	
	public V6Address(byte[] address) throws UtilityException {
		if (address.length < 16) {
			throw new UtilityException("16 bytes are required.");
		}
		try {
			System.arraycopy(address, 0, v6, 0, 16);
		} catch (Exception e) {
		}
	}

	public V6Address(byte[] address, int offset) throws UtilityException {
		if ((address.length - offset) < 16) {
			throw new UtilityException("16 bytes are required.");
		}
		try {
			System.arraycopy(address, offset, v6, 0, 16);
		} catch (Exception e) {
		}
	}
	
	public String toString() {
		try {
			return InetAddresses.toUriString(getInetAddress());
		} catch (Exception e) {
			e.printStackTrace();
			return new String(v6);
		}
	}
	
	public byte[] getBytes() {
		return v6;
	}
	
	public InetAddress getInetAddress() throws UtilityException, UnknownHostException {
		return InetAddresses.fromLittleEndianByteArray(v6);
	}
	
	public boolean equals(Object obj) {
		if (obj == null) 
			return false;
		if (obj instanceof V6Address) {
			return (Arrays.equals(v6, ((V6Address)obj).v6));
		}
		return false;
	}
	
	public int hashCode() {
		try {
			return getInetAddress().hashCode(); 
		} catch (Exception e) {
			e.printStackTrace();
			return new String(v6).hashCode();
		}
	}
}
