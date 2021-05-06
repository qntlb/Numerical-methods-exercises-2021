package com.andreamazzon.exercise3.montecarloevaluations;

import com.andreamazzon.usefulmethodsmatricesandvectors.UsefulMethodsMatricesAndVectors;

/**
 * This is an abstract class inheriting from MonteCarloEvaluationsAbstract, so
 * also implementing the interface MonteCarloEvaluationsInterface. Therefore, it
 * provides the implementation of methods that can be called in order to get the
 * vector of several Monte-Carlo approximations of a given quantity, the average
 * and the standard deviation of the vector, as well as its minimum and maximum
 * value and an histogram of its elements. Moreover, here we suppose that we
 * already know the exact value of the quantity we approximate by Monte-Carlo.
 * So we experiment on the quality of our approximation by getting the vector of
 * the absolute errors and the average of the vector. This is done by the
 * methods double[] getAbsoluteErrorsOfComputations() and double
 * getAverageAbsoluteError(), respectively. Note that all the methods are based
 * on the protected double[] field monteCarloComputations, inherited from
 * MonteCarloExperiments, that hosts all the elements of the vector. This vector
 * is initialized and filled by the protected abstract void method
 * generateMonteCarloComputations(), also inherited from MonteCarloExperiments,
 * whose implementation depends on the kind of Monte-Carlo approximation
 * considered.
 *
 * @author Andrea Mazzon
 *
 */
public abstract class MonteCarloEvaluationsWithExactResultAbstract extends MonteCarloEvaluationsAbstract
		implements MonteCarloEvaluationsWithExactResultInterface {

	private double exactResult;// hosts the exact value of the quantity we approximate

	/*
	 * It will be called by the constructor of the sub-classes. In turn, it calls
	 * itself the constructor of the parent class MonteCarloEvaluationsAbstract
	 */
	public MonteCarloEvaluationsWithExactResultAbstract(int numberOfMonteCarloComputations, int numberOfDrawings,
			double exactResult) {
		super(numberOfMonteCarloComputations, numberOfDrawings);
		this.exactResult = exactResult;
	}

	/**
	 * It returns the vector of the absolute errors of the Monte-Carlo
	 * approximations
	 *
	 * @return the vector of the absolute errors of the Monte-Carlo approximations,
	 *         computed as the difference between the array of the approximation and
	 *         the exact result.
	 */
	@Override
	public double[] getAbsoluteErrorsOfComputations() {
		double[] errors = UsefulMethodsMatricesAndVectors.sumVectorAndDouble(getComputations(), -exactResult);
		double[] absoluteErrors = UsefulMethodsMatricesAndVectors.absVector(errors);
		return absoluteErrors;
	}

	/**
	 * It returns the average of the vector of the absolute errors of the
	 * Monte-Carlo approximations
	 *
	 * @return the average of the vector of the absolute errors of the Monte-Carlo
	 *         approximations. The vector is computed as the difference between the
	 *         array of the approximation and the exact result.
	 */
	@Override
	public double getAverageAbsoluteError() {
		return UsefulMethodsMatricesAndVectors.getAverage(getAbsoluteErrorsOfComputations());
	}

}
