package com.andreamazzon.exercise7.confidenceintervals;

import java.text.DecimalFormat;

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

	static DecimalFormat formatterValue = new DecimalFormat("#0.00000");

	static DecimalFormat formatterPercentage = new DecimalFormat("#0.00%");

	public static void main(String[] args) {
		double lambda = 0.2;
		int numberOfMeanComputations = 10000;
		int sampleSize = 100000;
		double confidenceLevel = 0.9;
		/*
		 * exponentially distributed random variables: we want to compute the confidence
		 * intervals for the sample mean of size given by sampleSize.
		 */
		RandomVariable exponential = new ExponentialRandomVariable(lambda);
		// with Chebychev inequality
		ChebychevMeanConfidenceInterval chebychevInterval = new ChebychevMeanConfidenceInterval(exponential,
				sampleSize);
		// and with the Central Limit Theorem
		CLTMeanConfidenceInterval cLTInterval = new CLTMeanConfidenceInterval(exponential, sampleSize);

		System.out.println("The Chebyshev confidence interval boundaries at a " + confidenceLevel * 100
				+ "% confidence level  for lambda  after " + sampleSize + " drawings are \n"
				+ formatterValue.format(chebychevInterval.getLowerBoundConfidenceInterval(confidenceLevel)) + " and "
				+ formatterValue.format(chebychevInterval.getUpperBoundConfidenceInterval(confidenceLevel)));

		System.out.println("\n");
		System.out.println("_".repeat(80) + "\n");
		System.out.println("\n");

		System.out.println("The CLT confidence interval boundaries at a " + confidenceLevel * 100
				+ "% confidence level for lambda after " + sampleSize + " drawings are \n"
				+ formatterValue.format(cLTInterval.getLowerBoundConfidenceInterval(confidenceLevel)) + " and "
				+ formatterValue.format(cLTInterval.getUpperBoundConfidenceInterval(confidenceLevel)));

		System.out.println("\n");
		System.out.println("_".repeat(80) + "\n");

		System.out.println("\n");

		System.out
				.println("The frequence of lambda being in the Chebyshev  confidence interval" + "is "
						+ formatterPercentage.format(
								chebychevInterval.frequenceOfInterval(numberOfMeanComputations, confidenceLevel))
						+ "%");
		System.out.println("The frequence of lambda being in the CLT confidence interval is "
				+ formatterPercentage.format(cLTInterval.frequenceOfInterval(numberOfMeanComputations, confidenceLevel))
				+ "%");

	}
}