package com.andreamazzon.exercise13.sensitivities;

import java.text.DecimalFormat;

import com.andreamazzon.exercise12.finitedifferences.EuropeanOptionDeltaCentralDifferences;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * In this class we test and compare central finite differences, the pathwise
 * differentiation method and the likelihood ratio method in order to
 * approximate the delta of a call option written on a Black-Scholes model.
 *
 * @author Andrea Mazzon
 *
 */
public class SensitivitiesBlackScholesTest {

	private final static DecimalFormat formatterValue = new DecimalFormat("0.0000000");
	private final static DecimalFormat formatterPercentage = new DecimalFormat("0.0000 %");

	public static void main(String[] args) throws CalculationException {
		// option parameters
		final double maturity = 1.0;
		final double strike = 3.0;

		// model parameters
		final double initialPrice = 3.0;
		final double riskFreeRate = 0.2;
		final double volatility = 0.5;

		// our benchmark
		double derivativeAnalytic = AnalyticFormulas.blackScholesOptionDelta(initialPrice, riskFreeRate, volatility,
				maturity, strike);

		System.out.println(
				"The analytic value of the delta of the call option is " + formatterValue.format(derivativeAnalytic));

		System.out.println();

		// simulation and time discretization parameters
		final int numberOfSimulations = 10000;// number of paths

		final double initialTime = 0;
		final double finalTime = maturity;
		final int numberOfTimeSteps = 100;
		final double timeStep = finalTime / numberOfTimeSteps;
		final TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

		/*
		 * We construct our underlying: we pass it to the getValue methods of our
		 * classes
		 */
		final AssetModelMonteCarloSimulationModel bsModel = new MonteCarloBlackScholesModel(times, numberOfSimulations,
				initialPrice, riskFreeRate, volatility);

		final double stepForCentralDifferences = Math.pow(10, -3);

		// we construct the objects for the three methods
		final AbstractAssetMonteCarloProduct centralFiniteDifferences = new EuropeanOptionDeltaCentralDifferences(
				maturity, strike, stepForCentralDifferences);
		final AbstractAssetMonteCarloProduct pathwise = new EuropeanOptionDeltaPathwise(maturity, strike);
		final AbstractAssetMonteCarloProduct likelihood = new EuropeanOptionDeltaLikelihood(maturity, strike);

		// we compute the approximated values with the three methods
		double approximationWithFiniteDifferences = centralFiniteDifferences.getValue(bsModel);
		double approximationWithPathwiseDifferentiation = pathwise.getValue(bsModel);
		double approximationWithLikelihoodRatio = likelihood.getValue(bsModel);

		// we print the values
		System.out.println("Approximated value with central finite differences: "
				+ formatterValue.format(approximationWithFiniteDifferences));
		System.out.println("Approximated value with pathwise differentiation: "
				+ formatterValue.format(approximationWithPathwiseDifferentiation));
		System.out.println(
				"Approximated value with likelihood ratio: " + formatterValue.format(approximationWithLikelihoodRatio));

		System.out.println();

		// and the errors
		System.out.println("Percentual error with central finite differences: " + formatterPercentage
				.format((approximationWithFiniteDifferences - derivativeAnalytic) / derivativeAnalytic));
		System.out.println("Percentual error with pathwise differentiation: " + formatterPercentage
				.format((approximationWithPathwiseDifferentiation - derivativeAnalytic) / derivativeAnalytic));
		System.out.println("Percentual error with likelihood ratio: " + formatterPercentage
				.format((approximationWithLikelihoodRatio - derivativeAnalytic) / derivativeAnalytic));

	}

}
