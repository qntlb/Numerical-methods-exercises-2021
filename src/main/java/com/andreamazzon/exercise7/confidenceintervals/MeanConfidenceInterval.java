package com.andreamazzon.exercise7.confidenceintervals;

import com.andreamazzon.exercise6.randomvariables.RandomVariable;

/**
 * This is an abstract class providing methods for the computation of upper and
 * lower bounds of confidence intervals for the sample mean of given size a
 * given random variable. The size of the sample and the random variable are
 * fields of the class. It also has a method frequenceOfInterval which returns
 * the frequency at which the effective mean of the sample falls in the
 * confidence interval for a given confidence level. It will be inherited by
 * CLTMeanConfidenceInterval, computing the intervals basing on the Central
 * Limit Theorem, and by ChebychevMeanConfidenceInterval, which uses Chebychev
 * inequality.
 *
 * @author Andrea Mazzon
 *
 */
public abstract class MeanConfidenceInterval {
	protected RandomVariable randomVariable;// it will be inherited and initialized
	protected int sampleSize;// it will be inherited and initialized

	/*
	 * two abstract methods. The confidence interval is specific of the limit
	 * theorem used to compute it.
	 */
	public abstract double getLowerBoundConfidenceInterval(double level);

	public abstract double getUpperBoundConfidenceInterval(double level);

	// pre implemented at abstract level
	/**
	 * It computes the frequency with which the mean of the sample falls inside the
	 * confidence interval for a given confidence level.
	 *
	 * @param numberOfMeanComputations, the number of the computations of the sample
	 *                                  mean
	 * @param level,                    the level of the confidence interval
	 * @return the frequency: the number of mean samples within the interval divided
	 *         by the number of mean computations
	 */
	public double frequenceOfInterval(int numberOfMeanComputations, double confidenceLevel) {
		double numberOfTimesInsideTheInterval = 0;
		// computed with CLT or Chebychev depending on the object calling
		double lowerBound = getLowerBoundConfidenceInterval(confidenceLevel);
		// computed with CLT or Chebychev depending on the object calling
		double upperBound = getUpperBoundConfidenceInterval(confidenceLevel);

		double sampleMean;
		for (int i = 0; i < numberOfMeanComputations; i++) {
			sampleMean = randomVariable.getSampleMean(sampleSize); // sample mean
			if (sampleMean > lowerBound && sampleMean < upperBound) {
				numberOfTimesInsideTheInterval++; // sample mean within the confidence interval
			}
		}
		return numberOfTimesInsideTheInterval / numberOfMeanComputations;
	}
}