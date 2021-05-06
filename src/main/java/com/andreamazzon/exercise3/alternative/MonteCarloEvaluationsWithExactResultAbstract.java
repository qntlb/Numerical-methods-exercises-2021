package com.andreamazzon.exercise3.alternative;

import com.andreamazzon.exercise3.montecarloevaluations.MonteCarloEvaluationsWithExactResultInterface;
import com.andreamazzon.usefulmethodsmatricesandvectors.UsefulMethodsMatricesAndVectors;

/**
 * This is an abstract class inheriting from MonteCarloEvaluationsAbstract, so
 * also implementing the interface MonteCarloEvaluationsInterface. Note that it
 * is identical to the one in com.andreamazzon.exercise3.montecarloevaluations.
 * The only difference is that it extends the MonteCarloEvaluationsAbstract of
 * this package.
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