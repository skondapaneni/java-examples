package com.sflow.collector;

import java.util.Date;

public final class TimestampedData {
     byte[] data;
     Date   timestamp;

     public TimestampedData(byte[] data) {
          this.data = data;
          this.timestamp = new Date();
     }

     public byte[] getData() {
          return data;
     }
     
     public Date getTimestamp() {
          return timestamp;
     }
};
     
