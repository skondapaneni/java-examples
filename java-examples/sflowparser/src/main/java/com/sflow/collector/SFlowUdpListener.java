package com.sflow.collector;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.sflow.packet.reactor.SFlowEventConsumer;

import reactor.bus.Event;
import reactor.bus.EventBus;

public class SFlowUdpListener implements Runnable {
	
    private static final Logger log = LogManager.getLogger(SFlowCollector.class.getName());

    private DatagramSocket      ds;
    private EventBus  			eventBus;

    @Value("${appx.sflow.port}")
    int                         port=6343;
    
    public SFlowUdpListener(EventBus eventBus) {
    	this.eventBus = eventBus;
    }
    
    public void run() {  
        log.info("SFLowCollector Started.... Waiting for data....");

         try {
              ds = new DatagramSocket(port);
              while (true) {
                   byte[] data = new byte[65536];
                   DatagramPacket dp = new DatagramPacket(data, data.length);
                   ds.receive(dp);     
                   eventBus.notify(SFlowEventConsumer.event, 
                             Event.wrap(new TimestampedData(dp.getData())) );
              }
         } catch (SocketException se) {
              se.printStackTrace();
         } catch (IOException ioe) {
              ioe.printStackTrace();
         }
    }
}
