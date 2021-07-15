package com.andreamazzon.exercise13.sensitivities;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.montecarlo.model.ProcessModel;
import net.finmath.stochastic.RandomVariable;

/**
 * This class implements the computation of the delta of a European call option
 * for a Black Scholes model, through the Pathwise Differentation Method. Note
 * that the only overridden method is getValue.
 *
 * @author Andrea Mazzon
 */
public class EuropeanOptionDeltaPathwise extends AbstractAssetMonteCarloProduct {

	private final double maturity;
	private final double strike;

	public EuropeanOptionDeltaPathwise(double maturity, double strike) {
		super();
		this.maturity = maturity;
		this.strike = strike;
	}

	/**
	 * This method returns the approximated value of the delta of an European call
	 * option through the Likelihood Ratio Method, as a RandomVariable. Note that
	 * the model has to be a Black Scholes model.
	 *
	 * @param evaluationTime   The time on which this products value should be
	 *                         observed.
	 * @param monteCarloModel, object of type AssetModelMonteCarloSimulationModel
	 *                         that represents the udnerlying of the option. Its
	 *                         model has to be Black Scholes, otherwise an exception
	 *                         is thrown.
	 * @return The random variable representing the approximated value of the delta
	 *         discounted to evaluation time
	 * @throws net.finmath.exception.CalculationException Thrown if the valuation
	 *                                                    fails, specific cause may
	 *                                                    be available via the
	 *                                                    <code>cause()</code>
	 *                                                    method.
	 */
	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel monteCarloModel)
			throws CalculationException {
		/*
		 * The model has to be Black Scholes! The implementation of the method (i.e.,
		 * the computation of the derivative of the density) is specific to the Black
		 * Scholes model. So we want to check this. We first have to get the model of
		 * AssetModelMonteCarloSimulationModel. In order to do this, we can call the
		 * getModel() method. However, this is a method defined in the class
		 * MonteCarloAssetModel, which implements AssetModelMonteCarloSimulationModel,
		 * but not in this interface itself. So we have to donwcast.
		 */
		ProcessModel modelForTheSimulation = ((MonteCarloAssetModel) monteCarloModel).getModel();

		/*
		 * Now we want modelForTheSimulation to be an instance of the class
		 * BlackScholesModel. If it is not, we throw a ClassCastException.
		 */
		if (!(modelForTheSimulation instanceof BlackScholesModel)) {
			throw new ClassCastException(
					"This method requires a Black-Scholes type model" + "(MonteCarloBlackScholesModel).");
		}

		/*
		 * Now we know that in the following we can safely downcast, and apply the
		 * formulas at page 530 and 535 of the script.
		 */

		// first step: get the underlying at current time and at evaluation time
		final RandomVariable underlyingAtEvalTime = monteCarloModel.getAssetValue(evaluationTime, 0);
		;
		final RandomVariable underlyingAtMaturity = monteCarloModel.getAssetValue(maturity, 0);

		// now we apply the formulas at page 535 of the script
		RandomVariable values = underlyingAtMaturity.apply(x -> x >= strike ? x : 0.0).div(underlyingAtEvalTime);

		// Discounting...
		final RandomVariable numeraireAtMaturity = monteCarloModel.getNumeraire(maturity);
		final RandomVariable monteCarloWeights = monteCarloModel.getMonteCarloWeights(maturity);
		values = values.div(numeraireAtMaturity).mult(monteCarloWeights);

		// ...to evaluation time.
		final RandomVariable numeraireAtEvalTime = monteCarloModel.getNumeraire(evaluationTime);
		final RandomVariable monteCarloProbabilitiesAtEvalTime = monteCarloModel.getMonteCarloWeights(evaluationTime);
		values = values.mult(numeraireAtEvalTime).div(monteCarloProbabilitiesAtEvalTime);

		return values;
	}
}