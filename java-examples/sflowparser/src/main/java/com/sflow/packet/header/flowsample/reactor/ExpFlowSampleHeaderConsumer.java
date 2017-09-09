package com.sflow.packet.header.flowsample.reactor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sflow.packet.header.reactor.ExpandedFlowSampleHeader;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.ObjectSelector;
import reactor.fn.Consumer;

@Service
public class ExpFlowSampleHeaderConsumer implements  
     Consumer<Event<ExpandedFlowSampleHeader>> {

     @Autowired
     EventBus                   rawPacketRecordBus;

     @Autowired
     FlowEventListener          flowEventListener;

     public static String expandedFlowSampleEvent = new String("sflowDatagram.expandedFlowSampleEvent");

     @PostConstruct
     public void init() {
          ObjectSelector<String, String> selector = new ObjectSelector<String, String>(expandedFlowSampleEvent);
          rawPacketRecordBus.on(selector, this);
     }

     public void accept(Event<ExpandedFlowSampleHeader> event) {
          flowEventListener.addExpandedFlowSample((ExpandedFlowSampleHeader)event.getData());
     }
}

