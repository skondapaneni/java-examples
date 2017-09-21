package com.sflow.packet.reactor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sflow.collector.SFlowCollector;
import com.sflow.collector.TimestampedData;
import com.sflow.util.HeaderParseException;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.ObjectSelector;
import reactor.fn.Consumer;

/**
 * Main role for this service is to remove sflow data from the event queue and hand
 * it off to the 2 processing queues (flowEventBus and counterEventBus)
 * 
 * udp packets are drained from the udp socket as fast as possible.
 * 
 * @author sriharikondapaneni
 *
 */
@Service
public class SFlowEventConsumer implements Consumer<Event<TimestampedData>> {

	@Autowired
	SFlowCollector collector;

	@Autowired
	EventBus eventBus;

	@Autowired
	EventBus flowEventBus;

	@Autowired
	EventBus counterEventBus;

	public static String event = new String("sflowDatagram");

	@PostConstruct
	public void init() {
		ObjectSelector<String, String> selector = new ObjectSelector<String, String>(event);
		eventBus.on(selector, this);
	}

	public void accept(Event<TimestampedData> event) {
		try {
			SFlowHeader.parse(event.getData().getTimestamp(), event.getData().getData(), collector, counterEventBus,
					flowEventBus);

		} catch (HeaderParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
