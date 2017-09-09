package com.sflow.packet.header.countersample.reactor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sflow.packet.header.reactor.ExpandedCounterSampleHeader;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.ObjectSelector;
import reactor.fn.Consumer;

@Service
public class ReactorExpCounterSampleHeaderConsumer  implements  
Consumer<Event<ExpandedCounterSampleHeader>> {

	@Autowired
	EventBus 				counterRecordBus;

	@Autowired
//	CounterEventListener	counterEventListener;

	public static String expandedCounterSampleHeaderEvent = 
			new String("sflowDatagram.expanedCounterSampleHeaderEvent");

	@PostConstruct
	public void init() {
		ObjectSelector<String, String> selector = 
				new ObjectSelector<String, String>(expandedCounterSampleHeaderEvent);
		counterRecordBus.on(selector, this);
	}

	public void accept(Event<ExpandedCounterSampleHeader> event) {
		ExpandedCounterSampleHeader ecsh = (ExpandedCounterSampleHeader)event.getData();	
//		counterEventListener.addCounterRecords(ecsh);
	}
}
