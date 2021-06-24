package com.andreamazzon.exercise10.approximationschemes;

import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This class simulates the trajectories of a geometric Brownian motion (i.e.,
 * Black-Scholes model) by using a log Euler scheme: in this case, we simulate
 * the trajectories of the logarithm of the geometric Brownian motion. We use
 * the exponential transform in order to return the right values. This class
 * extends AbstractSimulation by giving the implementation of getDrift and
 * getDiffusion.
 *
 * @author Andrea Mazzon
 */
public class LogEulerSchemeForBlackScholes extends AbstractProcessSimulation {

	private final double muDrift;// mu
	private final double sigmaVolatility;// sigma

	public LogEulerSchemeForBlackScholes(int numberOfSimulations, double sigmaVolatility, double muDrift,
			double initialValue, int seed, TimeDiscretization times) {
		super(numberOfSimulations, initialValue, seed, times);
		this.muDrift = muDrift;
		this.sigmaVolatility = sigmaVolatility;
		/*
		 * in AbstractSimulation, the drift and diffusion of the logarithm computed here
		 * are added to the last realization of the logarithm process, and the value
		 * obtained is exponentiated, by this transform.
		 */
		this.transform = (x -> Math.exp(x));
		/*
		 * the inverse transform is needed to sum the drift and diffusion of the
		 * logarithm computed here to the last realization of the logarithm
		 */
		this.inverseTransform = (x -> Math.log(x));
	}

	/*
	 * It gets and returns the drift of the logarithm of a geometric Brownian
	 * motion, computed with the Euler scheme. That is, it simply returns
	 * (mu-sigma^2/2)*(T_k-t_{k-1})
	 */
	@Override
	protected RandomVariable getDrift(RandomVariable lastRealization, int timeIndex) {
		TimeDiscretization times = getTimeDiscretization();
		return new RandomVariableFromDoubleArray(times.getTime(timeIndex),
				(muDrift - 0.5 * sigmaVolatility * sigmaVolatility) * (times.getTimeStep(timeIndex - 1)));
	}

	/*
	 * It gets and returns the diffusion of the logarithm of a geometric Brownian
	 * motion computed with the Euler scheme. That is, it simply returns
	 * sigma*(W_{t_k}-W_{t_{k-1}).
	 */
	@Override
	protected RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex) {
		BrownianMotion brownianMotion = getStochasticDriver();
		final RandomVariable brownianIncrement = brownianMotion.getBrownianIncrement(timeIndex - 1, 0);
		return brownianIncrement.mult(sigmaVolatility);
	}
}