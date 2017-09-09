package com.sflow.metrics;

public class CumulativeMovingAverage {
	  
	  private double constantK;
	  private long n;
	  private double ex;
	  private double ex2;

	  
	  public CumulativeMovingAverage() {
		  n = 0;
		  constantK = 0.0;
		  ex = 0.0;
		  ex2 = 0.0;
	  }
	  
	  public synchronized void update(double sample) {
		  
		  if (n == 0) {
			  constantK = sample;
		  }
		  
		  n += 1;
		  
		  ex +=  (sample - constantK);
		  ex2 += (sample - constantK) * (sample - constantK);
		  
		 // average = (sample + average *  n)/(n+1);
		 // n = n+1;
	  }
	  
	  public double getAverage() {
		  return constantK + ex/n;
	  }

	public double getVariance() {
		return (ex2 - (ex*ex)/n)/(n-1);
	}

}
