package com.andreamazzon.exercise7.confidenceintervals;

import com.andreamazzon.exercise6.randomvariables.ExponentialRandomVariable;
import com.andreamazzon.exercise6.randomvariables.RandomVariable;

/**
 * This class provides some experiment for the calculation of confidence
 * intervals based on the Central Limit Theorem and on the Chebychev inequality
 *
 * @author Andrea Mazzon
 *
 */
public class ConfidenceIntervalsTesting {

	public static void main(String[] args) {
		double lambda = 0.2;
		int numberOfMeanComputations = 10000;
		int sampleSize = 100000;
		double confidenceLevel = 0.9;
		/*
		 * exponentially distributed random variable: we want to compute the confidence
		 * intervals of its sample mean for a sample size given by sampleSize.
		 */
		RandomVariable exponential = new ExponentialRandomVariable(lambda);
		// with Chebychev inequality
		ChebychevMeanConfidenceInterval chebychevInterval = new ChebychevMeanConfidenceInterval(exponential,
				sampleSize);
		// and with the Central Limit Theorem
		CLTMeanConfidenceInterval cLTInterval = new CLTMeanConfidenceInterval(exponential, sampleSize);

		System.out.println("The Chebyshev confidence interval boundaries at a " + confidenceLevel * 100
				+ "% confidence level  for lambda  after " + sampleSize + " drawings are \n"
				+ chebychevInterval.getLowerBoundConfidenceInterval(confidenceLevel) + " and "
				+ chebychevInterval.getUpperBoundConfidenceInterval(confidenceLevel));

		System.out.println("\n");

		System.out.println("The CLT confidence interval boundaries at a " + confidenceLevel * 100
				+ "% confidence level for lambda after " + sampleSize + " drawings are \n"
				+ cLTInterval.getLowerBoundConfidenceInterval(confidenceLevel) + " and "
				+ cLTInterval.getUpperBoundConfidenceInterval(confidenceLevel));

		System.out.println("The frequence of lambda being in the Chebyshev  confidence interval" + "is "
				+ chebychevInterval.frequenceOfInterval(numberOfMeanComputations, confidenceLevel) * 100 + "%");
		System.out.println("The frequence of lambda being in the CLT confidence interval is "
				+ cLTInterval.frequenceOfInterval(numberOfMeanComputations, confidenceLevel) * 100 + "%");

	}
}