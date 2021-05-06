package com.andreamazzon.exercise3.montecarlointegration;

import java.util.stream.DoubleStream;

import com.andreamazzon.exercise3.montecarloevaluations.MonteCarloEvaluationsWithExactResultAbstract;

import net.finmath.randomnumbers.MersenneTwister;

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
		/*
		 * Note the implementation with DoubleStream! Here of course we don't give the
		 * seed to MersenneTwister, since otherwise teh result would be always the same
		 */
		DoubleStream randomNumbers = DoubleStream.generate(new MersenneTwister()).limit(numberOfDrawings);
		return randomNumbers.map(x -> Math.pow(x, exponent)).average().getAsDouble();
	}

	@Override
	protected void generateMonteCarloComputations() {
		monteCarloComputations = new double[numberOfMonteCarloComputations];
		for (int i = 0; i < numberOfMonteCarloComputations; i++) {
			monteCarloComputations[i] = computeIntegral();// specific computation
		}
	}
}
