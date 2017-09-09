package com.sflow.packet.header.countersample.reactor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sflow.packet.header.reactor.CounterSampleHeader;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.ObjectSelector;
import reactor.fn.Consumer;

@Service
public class ReactorCounterSampleHeaderConsumer  implements  
Consumer<Event<CounterSampleHeader>> {

	@Autowired
	EventBus 				counterRecordBus;

	@Autowired
//	CounterEventListener	counterEventListener;

	public static String counterSampleHeaderEvent = new String("sflowDatagram.counterSampleHeaderEvent");

	@PostConstruct
	public void init() {
		ObjectSelector<String, String> selector = new ObjectSelector<String, String>(counterSampleHeaderEvent);
		counterRecordBus.on(selector, this);
	}

	public void accept(Event<CounterSampleHeader> event) {
		CounterSampleHeader csh = (CounterSampleHeader)event.getData();	
//		counterEventListener.addCounterRecords(csh);
	}

}
