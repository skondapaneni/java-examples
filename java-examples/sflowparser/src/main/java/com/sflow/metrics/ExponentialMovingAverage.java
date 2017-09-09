package com.sflow.metrics;

/** This class implements an exponential moving average, using the algorithm described 
 *  at: <a href="http://en.wikipedia.org/wiki/Moving_average">http://en.wikipedia.org/wiki/Moving_average</a>.
 *  The average does not sample itself; it merely computes the new average when updated with
 *  a sample by an external mechanism. */

public class ExponentialMovingAverage {
    private long windowMillis;
    private long lastMillis;
    private double average;

    /** Construct a {@link ExponentialMovingAverage}, providing the time window
     *  we want the average over. For example, providing a value of
     *  3,600,000 provides a moving average over the last hour.
     *  @param windowMillis the length of the sliding window in
     *    milliseconds */
    public ExponentialMovingAverage(long windowMillis) {
        this.windowMillis = windowMillis;
    }

    /** Updates the average with the latest measurement.
     *  @param sample the latest measurement in the rolling average */
    public synchronized void update(double sample) {
         long now = System.currentTimeMillis();

         if (lastMillis == 0) {  // first sample
             average = sample;
             lastMillis = now;
             return;
         }
         long deltaTime = now - lastMillis;
         double coeff = Math.exp(-1.0 * ((double)deltaTime / windowMillis));
         average = (1.0 - coeff) * sample + coeff * average;
        
         lastMillis = now;
    }

    
    /** Updates the average with the latest measurement.
     *  @param sample the latest measurement in the rolling average */
    public synchronized void update(long now, double sample) {
    	// long now = System.currentTimeMillis();

    	if (lastMillis == 0) {  // first sample
    		average = sample;
    		lastMillis = now;
    		return;
    	}
    	long deltaTime = now - lastMillis;
    	//double coeff = Math.exp(-1.0 * ((double)deltaTime / windowMillis));
    	double coeff = Math.exp(-2.3 * ((double)deltaTime / windowMillis));

    	average = (1.0 - coeff) * sample + coeff * average;

    	lastMillis = now;
    }
    
    /** Updates the average with the latest measurement.
     *  @param sample the latest measurement in the rolling average */
    public synchronized void update(long last, long now, double sample) {
    	// long now = System.currentTimeMillis();

    	if (this.lastMillis == 0) {  // first sample
    		average = sample;
    		this.lastMillis = last;
    		return;
    	}
    	long deltaTime = now - last;
    	//double coeff = Math.exp(-1.0 * ((double)deltaTime / windowMillis));
    	double coeff = Math.exp(-2.3 * ((double)deltaTime / windowMillis));

    	average = (1.0 - coeff) * sample + coeff * average;

		this.lastMillis = last;
    }
    
    
    /** Returns the last computed average value. */
    public double getAverage() { 
    	return average; 
    }
    
    public static void main(String[] args) {
    	ExponentialMovingAverage ema = new ExponentialMovingAverage(5*60*1000);
    	
    	ema.update(1000*60, 40);
    	System.out.println("average 1000 " + ema.getAverage());

    	ema.update(2000*60, 50);
    	System.out.println("average 2000 " + ema.getAverage());

    	ema.update(3000*60, 60);
    	System.out.println("average 3000 " + ema.getAverage());

    	ema.update(4000*60, 100);
    	
    	System.out.println("average 4000 " + ema.getAverage());

    	ema.update(5000*60, 100);
    	
    	System.out.println("average 5000 " + ema.getAverage());

    	ema.update(6000*60, 100);
    	
    	System.out.println("average 6000 " + ema.getAverage());
    	
    	ema.update(7000*60, 100);
   	
    	System.out.println("average 7000 " + ema.getAverage());
    	
       	ema.update(8000*60, 100);
       	
    	System.out.println("average 8000 " + ema.getAverage());
    	
       	ema.update(9000*60, 50);
       	
    	System.out.println("average 9000 " + ema.getAverage());
    	
       	ema.update(10000*60, 50);
       	
    	System.out.println("average 10000 " + ema.getAverage());
    	
       	ema.update(11000*60, 50);
       	
    	System.out.println("average 11000 " + ema.getAverage());

    	ema.update(12000*60, 50);
       	
    	System.out.println("average 12000 " + ema.getAverage());
    	
    	ema.update(13000*60, 50);
       	
    	System.out.println("average 13000 " + ema.getAverage());
    	
    	ema.update(14000*60, 50);
       	
    	System.out.println("average 14000 " + ema.getAverage());
    	
    	ema.update(15000*60, 80);
       	
    	System.out.println("average 14000 " + ema.getAverage());
    	
    	ema.update(16000*60, 80);
       	
    	System.out.println("average 14000 " + ema.getAverage());
 
    	ema.update(17000*60, 80);
       	
    	System.out.println("average 14000 " + ema.getAverage());
 
    	ema.update(18000*60, 80);
       	
    	System.out.println("average 14000 " + ema.getAverage());
 
    	ema.update(19000*60, 80);
       	
    	System.out.println("average 14000 " + ema.getAverage());
 
    	
//    	average 1000 40.0
//    	average 2000 41.81269246922018
//    	average 3000 43.2967995396436
//    	average 4000 53.57534598516064
//    	average 5000 61.990708057045815
//    	average 6000 68.88062378358426
//    	average 7000 74.5216096750169
//    	average 8000 70.07659595591092
//    	average 9000 66.4373265262253
//    	average 10000 63.457744725405135
//    	average 11000 61.01826947376219
//    	average 12000 59.02099606386946
//    	average 13000 57.38576690088536
    	
   }
}
