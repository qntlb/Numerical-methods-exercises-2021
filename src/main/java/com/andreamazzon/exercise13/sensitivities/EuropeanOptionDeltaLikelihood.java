package com.andreamazzon.exercise13.sensitivities;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.montecarlo.model.ProcessModel;
import net.finmath.stochastic.RandomVariable;

/**
 * This class implements the approximation of the delta of an European call
 * option for a Black Scholes model, through the Likelihood Ratio Method. Note
 * that the only overridden method is getValue.
 *
 * @author Andrea Mazzon
 */
public class EuropeanOptionDeltaLikelihood extends AbstractAssetMonteCarloProduct {

	private final double maturity;
	private final double strike;

	public EuropeanOptionDeltaLikelihood(double maturity, double strike) {
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
		 * formulas at page 539 and 543 of the script.
		 */

		/*
		 * first step: get the underlying at maturity S(T), the current value of the
		 * underlying, the risk free rate and the volatility sigma. This can be done
		 * from modelForTheSimulation.
		 */
		// Get underlying and numeraire
		final RandomVariable underlyingAtMaturity = monteCarloModel.getAssetValue(maturity, 0);
		final RandomVariable underlyingToday = monteCarloModel.getAssetValue(evaluationTime, 0);

		final RandomVariable riskFree = ((BlackScholesModel) modelForTheSimulation).getRiskFreeRate();
		final RandomVariable sigma = ((BlackScholesModel) modelForTheSimulation).getVolatility();

		/*
		 * second step: use the quantities found above in order to compute the delta.
		 * Use the formulas at page 543 of the script, together with page 539: : we have
		 * to multiply the payoff by the weight at page 539.
		 */

		/*
		 * Look at page 539: the weight we have to compute is d Phi_{S(T)}(S(T))/dS_0
		 * divided by Phi_{S(T)}(S(T)), (here we suppose evaluation time = 0) where
		 * Phi_{S(T)}(S(T)) = 1/(sigma sqrt(T))Phi_{std}(1/(sigma sqrt(T))
		 * (log(S_T/S_0)-rT+1/2 sigma^2T )/S_T ), see page 543 of the script. Here
		 * Phi_{std}(x) = 1/sqrt(2 pi)*exp(-x^2/2), so that we easily get the derivative
		 * d/dx Phi_{std}(x) = - x Phi_{std}(x). Computing now the whole derivative
		 * dPhi_{S(T)}(S(T))/dS_0 and dividing it by Phi_{S(T)}(S(T)), we then get
		 * 1/(sigma sqrt(T)) * (-1/(sigma sqrt(T))(log(S_T/S_t)-rT+1/2
		 * sigma^2T)/S_T)(-1/S_t). Also look at the PDF with computations
		 */

		final RandomVariable likelihoodRatioWeight = underlyingAtMaturity.div(underlyingToday).log()
				.sub(riskFree.mult(maturity)).add(sigma.squared().mult(0.5 * maturity)).div(sigma).div(sigma)
				.div(maturity).div(underlyingToday);

		// now we multiply the weight by the payoff
		RandomVariable values = underlyingAtMaturity.sub(strike).floor(0.0).mult(likelihoodRatioWeight);

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
