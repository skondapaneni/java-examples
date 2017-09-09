package com.sflow.packet.header.flowsample.reactor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sflow.packet.header.reactor.FlowSampleHeader;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.ObjectSelector;
import reactor.fn.Consumer;

@Service
public class FlowSampleHeaderConsumer  implements  
          Consumer<Event<FlowSampleHeader>> {

     @Autowired
     EventBus                     rawPacketRecordBus;
     
     @Autowired
//     FlowEventListener          flowEventListener;
     

     public static String flowSampleHeaderEvent = new String("sflowDatagram.flowSampleHeaderEvent");
     
     @PostConstruct
     public void init() {
          ObjectSelector<String, String> selector = new ObjectSelector<String, String>(flowSampleHeaderEvent);
          rawPacketRecordBus.on(selector, this);
     }

     public void accept(Event<FlowSampleHeader> event) {
          FlowSampleHeader fsh = (FlowSampleHeader)event.getData(); 
          System.out.println("== " + fsh);
          // notify to inventory
//          flowEventListener.addFlowSample(fsh);
     }
          
}
