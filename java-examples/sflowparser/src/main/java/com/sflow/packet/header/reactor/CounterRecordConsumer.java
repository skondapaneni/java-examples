package com.sflow.packet.header.reactor;


import javax.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.ObjectSelector;
import reactor.fn.Consumer;

import com.sflow.collector.SFlowCollector;
import com.sflow.packet.reactor.SFlowHeader;
import com.sflow.util.HeaderParseException;

@Service
public class CounterRecordConsumer implements  
	Consumer<Event<CounterRecordConsumer.CounterEventData>> {

	@Autowired
	SFlowCollector 	collector;
	
	@Autowired
	EventBus		counterEventBus;
	
	public static String counterEvent = new String("sflowDatagram.counterEvent");
	

	public final static class CounterEventData {
		SFlowHeader header;
		byte[] data;
		int offset;
		long len;
		long format;
		
		public CounterEventData(SFlowHeader header, 
				long format, byte[] data, int offset, long sampleLength) {
			this.data = data;
			this.header = header;
			this.offset = offset;
			this.format = format;
			this.len = sampleLength;
		}
		
		public byte[] getData() {
			return data;
		}
		
		public SFlowHeader getSFlowHeader() {
			return header;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public long getLen() {
			return len;
		}

		public void setLen(long len) {
			this.len = len;
		}

		public long getFormat() {
			return format;
		}

		public void setFormat(long format) {
			this.format = format;
		}
	};
		
	@PostConstruct
	public void init() {
		ObjectSelector<String, String> selector = new ObjectSelector<String, String>(counterEvent);
		counterEventBus.on(selector, this);
	}

	public void accept(Event<CounterRecordConsumer.CounterEventData> event) {

		try {
			SampleDataHeader sdh = SampleDataHeader.parse(event.getData().getFormat(),
					event.getData().data, event.getData().getOffset(),
					event.getData().getLen(), event.getData().header);
			
			event.getData().header.addSampleDataHeader(sdh);
		} catch (HeaderParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
		
}
