package com.sflow.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import reactor.bus.EventBus;
import reactor.spring.context.config.EnableReactor;
import reactor.Environment;

@SpringBootApplication
@EnableAutoConfiguration
@EnableReactor
@ComponentScan("com.sflow")
public class SFlowApplication extends SpringBootServletInitializer {
	
	@Bean
	Environment env() {
		return Environment.initializeIfEmpty().assignErrorJournal();
	}

	@Autowired
	public void setSFlowCollector(SFlowCollector collector) {
		collector.startCollector();
	}

	// sflow udp packet enters on eventBus, forked into flowEvent, and
	// counterEvent,
	// and dispatched onto classifierBus and counterRecordBus respectively after
	// the
	// full parsing is done.
	@Bean(name = "eventBus")
	EventBus createEventBus(Environment env) {
		return EventBus.create(env, Environment.SHARED);
	}

	@Bean(name = "flowEventBus")
	EventBus createFlowEventBus(Environment env) {
		return EventBus.create(env, Environment.SHARED);
	}

	@Bean(name = "counterEventBus")
	EventBus createCounterEventBus(Environment env) {
		return EventBus.create(env, Environment.SHARED);
	}

	@Bean(name = "counterRecordBus")
	EventBus createCounterRecordBus(Environment env) {
		return EventBus.create(env, Environment.SHARED);
	}

	@Bean(name = "rawPacketRecordBus")
	EventBus createRawPacketRecordBus(Environment env) {
		return EventBus.create(env, Environment.SHARED);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SFlowApplication.class);
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(SFlowApplication.class).headless(false).web(true).run(args);
	}
}
