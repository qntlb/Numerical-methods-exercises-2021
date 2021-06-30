package com.andreamazzon.exercise10.calloption;

import com.andreamazzon.exercise10.approximationschemes.AbstractProcessSimulation;

import net.finmath.stochastic.RandomVariable;

/**
 * In this class we compute the price of a discounted call option with a given
 * underlying stochastic process, represented by an object of type
 * AbstractSimulation
 *
 * @author Andrea Mazzon
 *
 */
public class CallOption {

	private final double strike, maturity, riskFreeRate;

	public CallOption(double strike, double maturity, double riskFreeRate) {
		this.strike = strike;
		this.maturity = maturity;
		this.riskFreeRate = riskFreeRate;
	}

	/**
	 * Computes and returns the price of the discounted call option.
	 *
	 * @param strike,       the strike of the option
	 * @param maturity,     the maturity of the option
	 * @param riskFreeRate, the risk free rate
	 * @return the price of the option
	 */
	public double priceCall(AbstractProcessSimulation underlying) {
		// (S_T-K)^+
		final RandomVariable payoff = underlying.getProcessAtGivenTime(maturity).sub(strike).floor(0.0);

		// e^{-rT}(S_T-K)^+
		final RandomVariable discountedPayoff = payoff.mult(Math.exp(-riskFreeRate * maturity));

		final double price = discountedPayoff.getAverage();

		return price;
	}

}