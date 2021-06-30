package com.andreamazzon.exercise10.calloption;

import java.text.DecimalFormat;
import java.util.Random;

import com.andreamazzon.exercise10.approximationschemes.AbstractProcessSimulation;
import com.andreamazzon.exercise10.approximationschemes.EulerSchemeForBlackScholes;
import com.andreamazzon.exercise10.approximationschemes.LogEulerSchemeForBlackScholes;
import com.andreamazzon.exercise10.approximationschemes.MilsteinSchemeForBlackScholes;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class has a main method which computes the average percentage error in
 * the computation of the price of a call option under the Black-Scholes model,
 * for three different methods used to simulate the underlying process: Euler
 * scheme, "log Euler" scheme (i.e., simulating the logarithm of the underlying
 * by an Euler scheme) and Milstein scheme.
 *
 * @author Andrea Mazzon
 *
 */
public class CallChecks {

	private final static DecimalFormat formatterPercentage = new DecimalFormat("0.0000 %");
	private final static DecimalFormat formatterValue = new DecimalFormat("0.0000");

	public static void main(String[] args) throws CalculationException {

		final double initialPrice = 100.0;

		// this will be the drift: we simulate under the risk neutral probability
		// measure
		final double riskFreeRate = 0.4;
		final double volatility = 0.25;

		final int numberOfSimulations = 10000;// number of simulated paths

		// time discretization
		final double initialTime = 0;
		final double timeHorizon = 1.0;// it is also the maturity of the option
		final int numberOfTimeSteps = 100;
		final double delta = timeHorizon / numberOfTimeSteps;
		final TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, delta);

		final double strike = 100.0;

		// note: analytic value given in the Finmath library
		final double analyticCallValue = AnalyticFormulas.blackScholesOptionValue(initialPrice, riskFreeRate,
				volatility, timeHorizon, strike);

		final int numberOfTests = 100;

		double errorEulerCalls;
		double errorLogEulerCalls;
		double errorMilsteinCalls;

		double sumErrorEuler = 0;
		double sumErrorLogEuler = 0;
		double sumErrorMilstein = 0;

		final CallOption callOption = new CallOption(strike, timeHorizon, riskFreeRate);

		final Random randomGenerator = new Random();

		for (int i = 0; i < numberOfTests; i++) {

			// randomized seed; for every test we will have (very probably) a different seed
			final int seed = randomGenerator.nextInt();

			// the three simulations:
			final AbstractProcessSimulation euler = new EulerSchemeForBlackScholes(numberOfSimulations, volatility,
					riskFreeRate, initialPrice, seed, times);

			final AbstractProcessSimulation logEuler = new LogEulerSchemeForBlackScholes(numberOfSimulations,
					volatility, riskFreeRate, initialPrice, seed, times);

			final AbstractProcessSimulation milstein = new MilsteinSchemeForBlackScholes(numberOfSimulations,
					volatility, riskFreeRate, initialPrice, seed, times);

			// the three values of the error computing the price of the call option
			errorEulerCalls = Math.abs(callOption.priceCall(euler) - analyticCallValue) / analyticCallValue;
			errorLogEulerCalls = Math.abs(callOption.priceCall(logEuler) - analyticCallValue) / analyticCallValue;
			errorMilsteinCalls = Math.abs(callOption.priceCall(milstein) - analyticCallValue) / analyticCallValue;

			// we update the sum
			sumErrorEuler += errorEulerCalls;
			sumErrorLogEuler += errorLogEulerCalls;
			sumErrorMilstein += errorMilsteinCalls;

		}

		// we compute the average
		double averageErrorEuler = sumErrorEuler / numberOfTests;
		double averageErrorLogEuler = sumErrorLogEuler / numberOfTests;
		double averageErrorMilstein = sumErrorMilstein / numberOfTests;

		System.out.println("Analytical price of the call: " + formatterValue.format(analyticCallValue));
		System.out.println("Average error for Euler scheme: " + formatterPercentage.format(averageErrorEuler));
		System.out.println("Average error for log Euler scheme: " + formatterPercentage.format(averageErrorLogEuler));
		System.out.println("Average error for Milstein scheme: " + formatterPercentage.format(averageErrorMilstein));

	}
}