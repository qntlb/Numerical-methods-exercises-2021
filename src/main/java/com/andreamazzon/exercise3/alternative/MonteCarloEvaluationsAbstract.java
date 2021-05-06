package com.andreamazzon.exercise3.alternative;

import com.andreamazzon.exercise3.montecarloevaluations.MonteCarloEvaluationsInterface;
import com.andreamazzon.usefulmethodsmatricesandvectors.UsefulMethodsMatricesAndVectors;

/**
 * This is an abstract class implementing MonteCarloEvaluationsInterface. It
 * provides the implementation of methods that can be called in order to get the
 * vector of several Monte-Carlo approximations of a given quantity, the average
 * and the standard deviation of the vector, as well as its minimum and maximum
 * value and an histogram of its elements. Note that all the methods are based
 * on the implementation of the abstract method getComputations(), returning an
 * array of doubles representing the Monte-Carlo results. In the sub-classes,
 * the Monte-Carlo results are computed and directly returned. This is a
 * difference with respect to the implementation in
 * com.andreamazzon.exercise3.montecarloevaluations, where the results were
 * stored in a field which was initialized only once.
 *
 * @author Andrea Mazzon
 *
 */
public abstract class MonteCarloEvaluationsAbstract implements MonteCarloEvaluationsInterface {

	/*
	 * These fields are protected because they are used in the sub-classes. Another
	 * solution could be to let them private and create some protected setters.
	 */

	/*
	 * The number of times we perform the Monte-Carlo computations, i.e., the length
	 * of monteCarloComputations
	 */
	protected int numberOfMonteCarloComputations;

	// the number of drawings for the single Monte-Carlo computation
	protected int numberOfDrawings;

	// it will be called by the sub-classes
	public MonteCarloEvaluationsAbstract(int numberOfMonteCarloComputations, int numberOfDrawings) {
		this.numberOfMonteCarloComputations = numberOfMonteCarloComputations;
		this.numberOfDrawings = numberOfDrawings;
	}

	// The Javadoc documentation is already given in the interface
	@Override
	public abstract double[] getComputations();

	@Override
	public double getAverageComputations() {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.getAverage
		 */
		double average = UsefulMethodsMatricesAndVectors.getAverage(getComputations());
		return average;
	}

	@Override
	public double getStandardDeviationComputations() {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.getStandardDeviation
		 */
		double standardDeviation = UsefulMethodsMatricesAndVectors.getStandardDeviation(getComputations());
		return standardDeviation;
	}

	@Override
	public double[] getMinAndMaxComputations() {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.getMin and UsefulMethodsMatricesVectors.getMax.
		 * Here we create the array computations not to call the method
		 * getComputations() twice
		 */
		double[] computations = getComputations();
		double min = UsefulMethodsMatricesAndVectors.getMin(computations);
		double max = UsefulMethodsMatricesAndVectors.getMax(computations);
		double[] minAndMax = { min, max };
		return minAndMax;
	}

	@Override
	public int[] getHistogramComputations(double leftPointOfInterval, double rightPointOfInterval, int numberOfBins) {
		/*
		 * you get the vector of computations and you pass it to
		 * UsefulMethodsMatricesVectors.buildHistogram.
		 */
		int[] histogram = UsefulMethodsMatricesAndVectors.buildHistogram(getComputations(), leftPointOfInterval,
				rightPointOfInterval, numberOfBins);
		return histogram;
	}

}