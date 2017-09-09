/*
 * Adapted from jsflow and add support for VG Counter
 */
package com.sflow.packet.header.countersample;

import java.math.BigInteger;

import com.sflow.util.HeaderBytesException;
import com.sflow.util.HeaderParseException;
import com.sflow.util.Utility;

// 100 BaseVG interface counters - see RFC 2020
public class VgCounterHeader {
    // enterprise = 0, format = 4

    private long dot12InHighPriorityFrames;
    private BigInteger dot12InHighPriorityOctets;
    private long dot12InNormPriorityFrames;
    private BigInteger dot12InNormPriorityOctets;
    private long dot12InIPMErrors;
    private long dot12InOversizeFrameErrors;
    private long dot12InDataErrors;
    private long dot12InNullAddressedFrames;
    private long dot12OutHighPriorityFrames;
    private BigInteger dot12OutHighPriorityOctets;
    private long dot12TransitionIntoTrainings;
    private BigInteger dot12HCInHighPriorityOctets;
    private BigInteger dot12HCInNormPriorityOctets;
    private BigInteger dot12HCOutHighPriorityOctets;

    private CounterData counterData;

    public long getDot12InHighPriorityFrames() {
        return dot12InHighPriorityFrames;
    }

    public void setDot12InHighPriorityFrames(long dot12InHighPriorityFrames) {
        this.dot12InHighPriorityFrames = dot12InHighPriorityFrames;
    }

    public BigInteger getDot12InHighPriorityOctets() {
        return dot12InHighPriorityOctets;
    }

    public void setDot12InHighPriorityOctets(BigInteger dot12InHighPriorityOctets) {
        this.dot12InHighPriorityOctets = dot12InHighPriorityOctets;
    }

    public long getDot12InNormPriorityFrames() {
        return dot12InNormPriorityFrames;
    }

    public void setDot12InNormPriorityFrames(long dot12InNormPriorityFrames) {
        this.dot12InNormPriorityFrames = dot12InNormPriorityFrames;
    }

    public BigInteger getDot12InNormPriorityOctets() {
        return dot12InNormPriorityOctets;
    }

    public void setDot12InNormPriorityOctets(BigInteger dot12InNormPriorityOctets) {
        this.dot12InNormPriorityOctets = dot12InNormPriorityOctets;
    }

    public long getDot12InIPMErrors() {
        return dot12InIPMErrors;
    }

    public void setDot12InIPMErrors(long dot12InIPMErrors) {
        this.dot12InIPMErrors = dot12InIPMErrors;
    }

    public long getDot12InOversizeFrameErrors() {
        return dot12InOversizeFrameErrors;
    }

    public void setDot12InOversizeFrameErrors(long dot12InOversizeFrameErrors) {
        this.dot12InOversizeFrameErrors = dot12InOversizeFrameErrors;
    }

    public long getDot12InDataErrors() {
        return dot12InDataErrors;
    }

    public void setDot12InDataErrors(long dot12InDataErrors) {
        this.dot12InDataErrors = dot12InDataErrors;
    }

    public long getDot12InNullAddressedFrames() {
        return dot12InNullAddressedFrames;
    }

    public void setDot12InNullAddressedFrames(long dot12InNullAddressedFrames) {
        this.dot12InNullAddressedFrames = dot12InNullAddressedFrames;
    }

    public long getDot12OutHighPriorityFrames() {
        return dot12OutHighPriorityFrames;
    }

    public void setDot12OutHighPriorityFrames(long dot12OutHighPriorityFrames) {
        this.dot12OutHighPriorityFrames = dot12OutHighPriorityFrames;
    }

    public BigInteger getDot12OutHighPriorityOctets() {
        return dot12OutHighPriorityOctets;
    }

    public void setDot12OutHighPriorityOctets(BigInteger dot12OutHighPriorityOctets) {
        this.dot12OutHighPriorityOctets = dot12OutHighPriorityOctets;
    }

    public long getDot12TransitionIntoTrainings() {
        return dot12TransitionIntoTrainings;
    }

    public void setDot12TransitionIntoTrainings(long dot12TransitionIntoTrainings) {
        this.dot12TransitionIntoTrainings = dot12TransitionIntoTrainings;
    }

    public BigInteger getDot12HCInHighPriorityOctets() {
        return dot12HCInHighPriorityOctets;
    }

    public void setDot12HCInHighPriorityOctets(
            BigInteger dot12hcInHighPriorityOctets) {
        dot12HCInHighPriorityOctets = dot12hcInHighPriorityOctets;
    }

    public BigInteger getDot12HCInNormPriorityOctets() {
        return dot12HCInNormPriorityOctets;
    }

    public void setDot12HCInNormPriorityOctets(
            BigInteger dot12hcInNormPriorityOctets) {
        dot12HCInNormPriorityOctets = dot12hcInNormPriorityOctets;
    }

    public BigInteger getDot12HCOutHighPriorityOctets() {
        return dot12HCOutHighPriorityOctets;
    }

    public void setDot12HCOutHighPriorityOctets(
            BigInteger dot12hcOutHighPriorityOctets) {
        dot12HCOutHighPriorityOctets = dot12hcOutHighPriorityOctets;
    }

    public CounterData getCounterData() {
        return counterData;
    }

    public void setCounterData(CounterData counterData) {
        this.counterData = counterData;
    }

    public static VgCounterHeader parse(byte[] data) throws HeaderParseException {
        try {
            if (data.length < 80) throw new HeaderParseException("Data array too short.");
            VgCounterHeader eic = new VgCounterHeader();

            eic.setDot12InHighPriorityFrames(Utility.fourBytesToLong(data, 0));
            eic.setDot12InHighPriorityOctets(Utility.eightBytesToBigInteger(data, 4));
            eic.setDot12InNormPriorityFrames(Utility.fourBytesToLong(data, 12));
            eic.setDot12InNormPriorityOctets(Utility.eightBytesToBigInteger(data, 16));
            eic.setDot12InIPMErrors(Utility.fourBytesToLong(data, 24));
            eic.setDot12InOversizeFrameErrors(Utility.fourBytesToLong(data, 28));
            eic.setDot12InDataErrors(Utility.fourBytesToLong(data, 32));
            eic.setDot12InNullAddressedFrames(Utility.fourBytesToLong(data, 36));
            eic.setDot12OutHighPriorityFrames(Utility.fourBytesToLong(data, 40));
            eic.setDot12OutHighPriorityOctets(Utility.eightBytesToBigInteger(data, 44));
            eic.setDot12TransitionIntoTrainings(Utility.fourBytesToLong(data, 52));
            eic.setDot12HCInHighPriorityOctets(Utility.eightBytesToBigInteger(data, 56));
            eic.setDot12HCInNormPriorityOctets(Utility.eightBytesToBigInteger(data, 64));
            eic.setDot12HCOutHighPriorityOctets(Utility.eightBytesToBigInteger(data, 72));

            // counter data
            if (data.length > 80) {
                byte[] subData = new byte[data.length - 80]; 
                System.arraycopy(data, 80, subData, 0, data.length - 80);
                CounterData cd = CounterData.parse(subData);
                eic.setCounterData(cd);
            }

            return eic;
        }  catch (Exception e) {
            throw new HeaderParseException("Parse error: " + e.getMessage());
        }
    }

    public byte[] getBytes() throws HeaderBytesException {
        try {
            byte[] counterDataBytes = null;
            int counterDataLen = 0;
            if (counterData != null) {
                counterDataBytes = counterData.getBytes();
                counterDataLen = counterDataBytes.length;
            }
            byte[] data = new byte[80 + counterDataLen ];

            // dot12InHighPriorityFrames
            System.arraycopy(Utility.longToFourBytes(dot12InHighPriorityFrames), 0, data, 0, 4);

            // dot12InHighPriorityOctets
            System.arraycopy(Utility.BigIntegerToEightBytes(dot12InHighPriorityOctets), 0, data, 4, 8);

            // dot12InNormPriorityFrames
            System.arraycopy(Utility.longToFourBytes(dot12InNormPriorityFrames), 0, data, 12, 4);

            // dot12InNormPriorityOctets
            System.arraycopy(Utility.BigIntegerToEightBytes(dot12InNormPriorityOctets), 0, data, 16, 8);

            // dot12InIPMErrors
            System.arraycopy(Utility.longToFourBytes(dot12InIPMErrors), 0, data, 24, 4);

            // dot12InOversizeFrameErrors
            System.arraycopy(Utility.longToFourBytes(dot12InOversizeFrameErrors), 0, data, 28, 4);

            // dot12InDataErrors
            System.arraycopy(Utility.longToFourBytes(dot12InDataErrors), 0, data, 32, 4);

            // dot12InNullAddressedFrames
            System.arraycopy(Utility.longToFourBytes(dot12InNullAddressedFrames), 0, data, 36, 4);

            // dot12OutHighPriorityFrames
            System.arraycopy(Utility.longToFourBytes(dot12OutHighPriorityFrames), 0, data, 40, 4);

            // dot12OutHighPriorityOctets
            System.arraycopy(Utility.BigIntegerToEightBytes(dot12OutHighPriorityOctets), 0, data, 44, 8);

            // dot12TransitionIntoTrainings
            System.arraycopy(Utility.longToFourBytes(dot12TransitionIntoTrainings), 0, data, 52, 4);

            // dot12HCInHighPriorityOctets
            System.arraycopy(Utility.BigIntegerToEightBytes(dot12HCInHighPriorityOctets), 0, data, 56, 8);

            // dot12HCInNormPriorityOctets
            System.arraycopy(Utility.BigIntegerToEightBytes(dot12HCInNormPriorityOctets), 0, data, 64, 8);

            // dot12HCOutHighPriorityOctets
            System.arraycopy(Utility.BigIntegerToEightBytes(dot12HCOutHighPriorityOctets), 0, data, 72, 8);
            
            if (counterDataLen != 0) {
                // counter data
                System.arraycopy(counterDataBytes, 0, data, 80, counterDataLen);
            }

            return data;
        } catch (Exception e) {
            throw new HeaderBytesException("Error while generating the bytes: " + e.getMessage());
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[VgCounterHeader]:");

        sb.append("InHighPriorityFrames: ");
        sb.append(getDot12InHighPriorityFrames());

        sb.append(", InHighPriorityOctets: " + this.getDot12InHighPriorityOctets());

        sb.append(", InNormPriorityFrames: ");
        sb.append(getDot12InNormPriorityFrames());

        sb.append(", InNormPriorityOctets: ");
        sb.append(getDot12InNormPriorityOctets());

        sb.append(", InIPMErrors: ");
        sb.append(getDot12InIPMErrors());

        sb.append(", InOversizeFrameErrors: ");
        sb.append(getDot12InOversizeFrameErrors());

        sb.append(", InDataErrors: ");
        sb.append(getDot12InDataErrors());

        sb.append(", InNullAddressedFrames: ");
        sb.append(getDot12InNullAddressedFrames());

        sb.append(", OutHighPriorityFrames: ");
        sb.append(getDot12OutHighPriorityFrames());

        sb.append(", OutHighPriorityOctets: ");
        sb.append(getDot12OutHighPriorityOctets());

        sb.append(", TransitionIntoTrainings: ");
        sb.append(getDot12TransitionIntoTrainings());

        sb.append(", HCInHighPriorityOctets: ");
        sb.append(getDot12HCInHighPriorityOctets());

        sb.append(", HCInNormPriorityOctets: ");
        sb.append(getDot12HCInNormPriorityOctets());

        sb.append(", HCOutHighPriorityOctets: ");
        sb.append(getDot12HCOutHighPriorityOctets());
        
        return sb.toString();
    }
}
