package com.andreamazzon.exercise11.products;

import java.text.DecimalFormat;
import java.util.function.DoubleUnaryOperator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This test class compares the value of a call option with the one computed by
 * EuropeanOption with the one we get giving the specific payoff function to
 * GeneralOption.
 *
 * @author Andrea Mazzon
 *
 */
class GeneralOptionTest {

	private final static DecimalFormat formatterValue = new DecimalFormat("0.0000");
	private final static DecimalFormat formatterValuePrecision = new DecimalFormat("0.000000000000000");

	@Test
	void testCall() {
		try {

			// model parameters

			final double initialPrice = 100.0;
			final double volatility = 0.25;
			final double riskFreeRate = 0;

			// simulation and time discretization parameters
			final int numberOfSimulations = 100000;// number of paths

			final double initialTime = 0;
			final double maturity = 1.0;
			final int numberOfTimeSteps = 100;
			final double timeStep = maturity / numberOfTimeSteps;
			final TimeDiscretization times = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps, timeStep);

			// option parameters
			final double strike = 100.0;

			// parameter for the test
			final double tolerance = 1e-15;

			// DoubleUnaryOperator specifying the payoff function of an european call.
			final DoubleUnaryOperator payoffFunction = (x) -> Math.max(x - strike, 0);

			final double analyticValue = AnalyticFormulas.blackScholesOptionValue(initialPrice, riskFreeRate,
					volatility, maturity, strike);

			/*
			 * Look at our scheme: MonteCarloBlackScholesModel extends MonteCarloAssetModel
			 * which implements AssetModelMonteCarloSimulationModel
			 */
			final AssetModelMonteCarloSimulationModel bsModel = new MonteCarloBlackScholesModel(times,
					numberOfSimulations, initialPrice, riskFreeRate, volatility);

			final AbstractAssetMonteCarloProduct ourOption = new GeneralOption(maturity, payoffFunction);
			final AbstractAssetMonteCarloProduct europeanOption = new EuropeanOption(maturity, strike);

			final double valueWithGeneralOption = ourOption.getValue(bsModel);
			final double valueWithSpecificEuropeanCall = europeanOption.getValue(bsModel);

			System.out.println("Value with general option: " + formatterValuePrecision.format(valueWithGeneralOption)
					+ "\n" + "value with specific european call class: "
					+ formatterValuePrecision.format(valueWithSpecificEuropeanCall) + "\n" + "analytical value: "
					+ formatterValue.format(analyticValue));

			Assert.assertEquals(valueWithGeneralOption, valueWithSpecificEuropeanCall, tolerance);
		} catch (CalculationException e) {
			Assert.fail();
		}
	}
}