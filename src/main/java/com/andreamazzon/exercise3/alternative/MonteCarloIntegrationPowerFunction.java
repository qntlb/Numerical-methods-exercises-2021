package com.andreamazzon.exercise3.alternative;

/**
 * This class takes care of the computation of the integral between 0 and 1 of
 * x^a, for a positive.
 *
 * @author Andrea Mazzon
 *
 */
public class MonteCarloIntegrationPowerFunction extends MonteCarloEvaluationsWithExactResultAbstract {

	private double exponent;

	// note again the call to the super constructor
	public MonteCarloIntegrationPowerFunction(double exponent, int numberOfMonteCarloComputations,
			int numberOfDrawings) {
		super(numberOfMonteCarloComputations, numberOfDrawings, 1 / (1 + exponent));
		this.exponent = exponent;
	}

	/**
	 * It computes the Monte Carlo approximation of the integral of integrand in
	 * [0,1].
	 *
	 * @return the approximated value of the integral
	 */
	public double computeIntegral() {
		double integralValue = 0;
		for (int i = 0; i < numberOfDrawings; i++) {
			// every time with a different seed
			integralValue += Math.pow(Math.random(), exponent);
		}
		integralValue /= numberOfDrawings;
		return integralValue;
	}

	@Override
	public double[] getComputations() {
		double[] monteCarloComputations = new double[numberOfMonteCarloComputations];
		for (int i = 0; i < numberOfMonteCarloComputations; i++) {
			monteCarloComputations[i] = computeIntegral();// specific computation
		}
		return monteCarloComputations;
	}
}
