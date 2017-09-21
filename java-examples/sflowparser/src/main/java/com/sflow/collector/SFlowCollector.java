package com.sflow.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.bus.EventBus;

@Service
public class SFlowCollector {

	@Autowired
	EventBus eventBus;

	@Autowired
	EventBus rawPacketRecordBus;

	@Autowired
	EventBus counterRecordBus;

	// @Value("${sflow.port}")
	int port = 6343;

	public void startCollector() {
		Thread t = new Thread(new SFlowUdpListener(eventBus));
		t.start();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public EventBus getRawPacketRecordBus() {
		return rawPacketRecordBus;
	}

	public void setRawPacketRecordBus(EventBus rawPacketRecordBus) {
		this.rawPacketRecordBus = rawPacketRecordBus;
	}

	public EventBus getCounterRecordBus() {
		return counterRecordBus;
	}

	public void setCounterRecordBus(EventBus counterRecordBus) {
		this.counterRecordBus = counterRecordBus;
	}

	public SFlowCollector() {
	}

}
