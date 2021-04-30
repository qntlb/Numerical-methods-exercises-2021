package com.andreamazzon.exercise2;

import java.util.stream.DoubleStream;

import com.andreamazzon.session4.usefulmatrices.UsefulMethodsMatricesVectors;
import com.andreamazzon.session5.abstractclasses.simulators.StochasticProcessSimulatorInterface;

/**
 * This class implements a Digital option, i.e., an option that for an
 * underlying S at maturity time i has payoff 1 if S(i) − K > 0 and 0 vice
 * versa, where K > 0 is a given strike.
 *
 * @author Andrea Mazzon
 *
 */
public class DigitalOption implements EuropeanTypeOptionMonteCarlo {

	/*
	 * The underlying is supposed to be discrete, that's why we say that maturity is
	 * an integer: we basically suppose t_0 = 0, t_1 = 1, t_2 = 2, etc. That is, we
	 * identify the index for the maturity with the maturity itself.
	 */
	private int maturity; // we use it to get the realizations at time T
	private double strike;// the strike K

	public DigitalOption(int maturity, double strike) {
		this.maturity = maturity;
		this.strike = strike;
	}

	/**
	 * It returns a one-dimensional array whose entries are the realizations of the
	 * payoff of the digital option for the underlying represented by
	 * underlyingProcess
	 *
	 * @param underlyingProcess, an object of type StochasticProcessSimulator,
	 *                           representing the realizations of the underlying
	 *                           process
	 * @return the realizations of the payoff, as a one-dimensional array
	 */
	@Override
	public double[] getPayoff(StochasticProcessSimulatorInterface underlyingProcess) {
		// realizations of the process at time maturity
		double[] realizations = underlyingProcess.getRealizationsAtGivenTime(maturity);
		int numberOfSimulations = realizations.length;// this is the length of the array we return
		double[] payoff = new double[numberOfSimulations];
		for (int simulationIndex = 0; simulationIndex < numberOfSimulations; simulationIndex++) {
			// note: this is the ternary if-else operator
			payoff[simulationIndex] = (realizations[simulationIndex] > strike) ? 1 : 0;
		}
		return payoff;
	}

	/**
	 *
	 * @param underlyingProcess, an object of type StochasticProcessSimulator,
	 *                           representing the realizations of the underlying
	 *                           process
	 * @return the realizations of the payoff, as a DoubleStream
	 */
	public DoubleStream getPayoffWithDoubleStream(StochasticProcessSimulatorInterface underlyingProcess) {
		DoubleStream realizations = DoubleStream.of(underlyingProcess.getRealizationsAtGivenTime(maturity));
		// other way to do it:

		// DoubleStream realizations =
		// Arrays.stream(underlyingProcess.getRealizationsAtGivenTime(maturity));

		return realizations;
	}

	/**
	 * It returns the Monte-Carlo price of the digital option for the underlying
	 * represented by stochasticProcessUser. This is done by taking the average of
	 * the one-dimensional array returned by getPayoff.
	 *
	 * @param underlyingProcess, an object of type StochasticProcessSimulator,
	 *                           representing the realizations of the underlying
	 *                           process
	 * @return the price of the option, i.e., the average of the realizations of the
	 *         payoff;
	 */
	@Override
	public double getPrice(StochasticProcessSimulatorInterface underlyingProcess) {

		// possible implementation with DoubleStream:

		// DoubleStream realizationsAtMaturity =
		// getPayoffWithDoubleStream(underlyingProcess);
		// DoubleStream payoff = realizationsAtMaturity.map(x -> x > strike ? 1 : 0);
		// return payoff.average().getAsDouble();

		// or, in short:

		// return getPayoffWithDoubleStream(underlyingProcess).map(x -> x > strike ? 1 :
		// 0).average().getAsDouble();
		return UsefulMethodsMatricesVectors.getAverage(getPayoff(underlyingProcess));
	}

}
