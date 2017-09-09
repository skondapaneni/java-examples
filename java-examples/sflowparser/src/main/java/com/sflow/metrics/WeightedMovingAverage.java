package com.sflow.metrics;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sflow.metrics.PeakDetectedException;

public class WeightedMovingAverage {

	private DescriptiveStatistics ds;
	private DescriptiveStatistics dsAvg;
	private String				  tag;
	private long				  id;
	
	private int rolling_sample_size;
	
	private static final Logger log = LogManager.getLogger(WeightedMovingAverage.class.getName());

	public WeightedMovingAverage(String tag, int rolling_sample_size, long id) {
		this.rolling_sample_size = rolling_sample_size;
		this.setTag(tag);
		this.id = id;
		ds = new DescriptiveStatistics(this.rolling_sample_size);
		dsAvg = new DescriptiveStatistics(this.rolling_sample_size);
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public double weightedMovAverage(double m) {
		double sum = 0;
		double den = 0;
		double alpha = 0.7; // Higher alpha discounts older observations faster..
		
		dsAvg.addValue(m);

		if (dsAvg.getValues().length < rolling_sample_size) {
			return m;
		}
		
		int i = Math.min(dsAvg.getValues().length, rolling_sample_size);
		
		for (int j = 0; j < i; j++) {
			double var = Math.pow((1-alpha), j);
			sum += dsAvg.getElement(i-j-1) * var;	
			den += var;
		} 
		sum = alpha * sum;		
		return sum/den;
	}
	

	public double getSumSq(double m) {

		if (dsAvg.getValues().length < rolling_sample_size) {
			dsAvg.addValue(m);
			return m;
		}
		
		dsAvg.addValue(m);
		
		return dsAvg.getSumsq()/rolling_sample_size;

	}
	
	
	public void addValue(double value) throws PeakDetectedException {
		double m, expM, sd;
		double k=2.0;

		ds.addValue(value); 
		synchronized(dsAvg) {
			m = ds.getMean();
			expM = getSumSq(m);
			if (ds.getValues().length == rolling_sample_size) {
				sd = dsAvg.getStandardDeviation();
				if (m > expM && sd != 0.0 &&
							m - expM > k * sd) {
					throw new PeakDetectedException(tag, value, expM, sd);
				}
				log.warn("metric " + tag + " " +  id + " " + expM + " " + m + " " + sd + " " + rolling_sample_size);

			}
		}
	}	
}
