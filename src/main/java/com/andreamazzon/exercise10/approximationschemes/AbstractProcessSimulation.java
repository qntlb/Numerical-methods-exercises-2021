package com.andreamazzon.exercise10.approximationschemes;

import java.util.function.DoubleUnaryOperator;

import net.finmath.montecarlo.BrownianMotion;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * This is an abstract class for the discretization and simulation of a
 * continuous Itô process. The only two abstract methods are the ones that
 * return the drift mu(X_{i-1}, t_i)(t_i-t_{i-1}) and the diffusion term
 * sigma(X_{i-1}, t_i)(t_i-t_{i-1}) of the process, since they can be
 * implemented in different ways depending on the scheme (for example, Euler,
 * log-Euler or Milstein) and on the dynamics of the process. All the other
 * methods are implemented in this class, as they only depend on the generation
 * of the process. We want to simulate dX_t=\mu(t,X_t)dt+\sigma(t,X_t)dW_t
 *
 * @author Andrea Mazzon
 *
 */
public abstract class AbstractProcessSimulation {
	// it will contain the paths of the process
	private RandomVariable[] paths;// not yet initialized: default value is null.

	private int numberOfSimulations;

	private TimeDiscretization times;

	// the initial value of the process: path[0] has to be filled with this value
	private double initialValue;

	// used in order to generate the Brownian motion
	private int seed;

	// used as the stochastic driver of the process
	private BrownianMotion brownianMotion;

	/*
	 * They are not the identity if it can be useful to use Itô's formula in order
	 * to simulate a convenient function of the process. In particular, transform
	 * gives the function we have to apply to the trajectories of the process we
	 * have simulated in order to get the one we want to return. For example, in the
	 * log Euler method this is the exponential function: we simulate the logarithm
	 * and we want to return anyway the process.
	 */
	protected DoubleUnaryOperator transform;
	protected DoubleUnaryOperator inverseTransform;// log for log Euler

	/*
	 * they of course depend on the process and on the scheme. We suppose that the
	 * drift and the diffusion are functions of the process and of the time.
	 */
	protected abstract RandomVariable getDrift(RandomVariable lastRealization, int timeIndex);

	protected abstract RandomVariable getDiffusion(RandomVariable lastRealization, int timeIndex);

	protected AbstractProcessSimulation(int numberOfSimulations, double initialValue, int seed,
			TimeDiscretization times) {
		this.numberOfSimulations = numberOfSimulations;
		this.initialValue = initialValue;
		this.seed = seed;
		this.times = times;
	}

	/*
	 * This method generates the process. Here you have to write a for loop, with
	 * respect to the time, such that at every iteration you fill the entry
	 * path[timeIndex] of path by adding the drift and the diffusion to
	 * path[timeIndex-1], also taking into consideration the possible transform and
	 * inverse transform. Note that, in order to deal with the diffusion term, you
	 * also have to create an object of type BrownianMotion (according to the fields
	 * of this class) and exploit one of its methods.
	 */
	private void generate() {
		// IMPLEMENT IT!
	}

	// getters

	/**
	 * It gets the initial value of the process, as a double
	 *
	 * @return the initial value of the process
	 */
	public double getInitialValue() {
		return initialValue;
	}

	/**
	 * It returns the seed by which the Brownian motion is generated
	 *
	 * @return the seed by which the Brownian motion is generated
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * It returns the Brownian motion driving the process
	 *
	 * @return the Brownian motion driving the process, as a BrownianMotion object
	 */
	public BrownianMotion getStochasticDriver() {
		return brownianMotion;
	}

	/**
	 * It returns the time discretization of the process
	 *
	 * @return the time discretization of the process
	 */
	public TimeDiscretization getTimeDiscretization() {
		return times;
	}

	/**
	 * It returns the number of times in the time discretization
	 *
	 * @return the number of times in the time discretization
	 */
	public int getNumberOfTimes() {
		return times.getNumberOfTimes();
	}

	/**
	 * It returns the last time of the time discretization
	 *
	 * @return the last time of the time discretization
	 */
	public double getTimeHorizon() {
		// implement it
		return 0;
	}

	/**
	 * It returns the number of paths of the process, i.e., the number of
	 * simulations
	 *
	 * @return the number of paths of the process
	 */
	public int getNumberOfSimulations() {
		return numberOfSimulations;
	}

	/**
	 * It returns the vector of random variables with the realizations of the
	 * process. It generates the process only if this has not already done.
	 *
	 * @return paths, vector of random variables with the realizations of the
	 *         process.
	 */
	public RandomVariable[] getPaths() {
		if (paths == null) {
			generate();
		}
		return paths;
	}

	/**
	 * It returns a random variable with the realizations of the process at a give
	 * time index. It generates the process only if this has not already done.
	 *
	 * @param timeInstant, index of the time considered
	 * @return paths, vector of random variables with the realizations of the
	 *         process.
	 */
	public RandomVariable getProcessAtGivenTimeIndex(int timeIndex) { // return the whole realisation at time t
		if (paths == null) {
			generate();
		}
		return paths[timeIndex];
	}

	/**
	 * It returns a random variable with the realizations of the process at a given
	 * time.
	 *
	 * @param time, the value of the time considered
	 * @return a random variable with the realizations of the process.
	 */
	public RandomVariable getProcessAtGivenTime(double time) {
		// implement it
		return null;
	}

	/**
	 * It returns a vector of doubles representing a path of the process for a given
	 * simulation.
	 *
	 * @param pathNumber, index of the simulation we consider
	 * @return the path of the process for the given simulation index
	 */
	public double[] getPathForGivenSimulation(int pathNumber) {
		// implement it
		return null;
	}

	/**
	 * It prints a vector of doubles representing a path of the process for a given
	 * simulation.
	 *
	 * @param pathNumber, index of the simulation we consider
	 */
	public void printAPath(int pathNumber) {
		final double[] samplePath = getPathForGivenSimulation(pathNumber);
		for (final double realization : samplePath) {
			System.out.println(realization);
		}
	}

	/**
	 * It returns the final value of the process
	 *
	 * @return random variable holding the realizations of the process at final time
	 */
	public RandomVariable getFinalValue() {
		// implement it
		return null;
	}
}