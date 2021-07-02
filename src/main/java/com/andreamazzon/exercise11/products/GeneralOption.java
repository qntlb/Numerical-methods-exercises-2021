package com.andreamazzon.exercise11.products;

import java.util.function.DoubleUnaryOperator;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;

/**
 * This class represents a general payoff function, through the use of the apply
 * method of RandomVariable
 *
 * @author Andrea Mazzon
 *
 */
public class GeneralOption extends AbstractAssetMonteCarloProduct {

	private final double maturity;
	private final DoubleUnaryOperator payoffFunction;

	/**
	 * Construct a product representing a general option on an asset S (where S the
	 * asset with index 0 from the model - single asset case).
	 * 
	 * @param maturity            The maturity T in the option payoff
	 * @param DoubleUnaryOperator payoffFunction, representing the function of the
	 *                            underlying which gives the payoff
	 */
	public GeneralOption(double maturity, DoubleUnaryOperator payoffFunction) {
		this.maturity = maturity;
		this.payoffFunction = payoffFunction;
	}

	/**
	 * This method returns the value random variable of the product within the
	 * specified model, evaluated at a given evalutationTime.
	 *
	 * @param evaluationTime The time on which this products value should be
	 *                       observed.
	 * @param model          The model used to price the product.
	 * @return The random variable representing the value of the product discounted
	 *         to evaluation time
	 * @throws net.finmath.exception.CalculationException Thrown if the valuation
	 *                                                    fails, specific cause may
	 *                                                    be available via the
	 *                                                    <code>cause()</code>
	 *                                                    method.
	 */
	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
			throws CalculationException {
		// Get underlying and numeraire

		// Get S(T)
		final RandomVariable underlyingAtMaturity = model.getAssetValue(maturity, 0);

		// The payoff: given by the apply method of RandomVariable
		RandomVariable values = underlyingAtMaturity.apply(payoffFunction);// f(S_T)

		// Discounting...
		final RandomVariable numeraireAtMaturity = model.getNumeraire(maturity);
		final RandomVariable monteCarloWeights = model.getMonteCarloWeights(maturity);
		values = values.div(numeraireAtMaturity).mult(monteCarloWeights);

		// ...to evaluation time.
		final RandomVariable numeraireAtEvalTime = model.getNumeraire(evaluationTime);
		final RandomVariable monteCarloProbabilitiesAtEvalTime = model.getMonteCarloWeights(evaluationTime);
		values = values.mult(numeraireAtEvalTime).div(monteCarloProbabilitiesAtEvalTime);

		return values;
	}
}