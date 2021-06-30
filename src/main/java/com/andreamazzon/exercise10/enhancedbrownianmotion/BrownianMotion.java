package com.andreamazzon.exercise10.enhancedbrownianmotion;

import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class provides the implementation of a discrete time possibly
 * multi-dimensional Brownian motion. All the simulated Brownian motions are
 * supposed to be independent. A one-dimensional Brownian motion is simulated
 * for a time discretization (t_0,t_1,..,t_n), identified by a
 * TimeDiscretization object, and is represented by a one-dimensional array of
 * objects of type RandomVariableFromDoubleArray. In order to simulate the
 * process itself, we simulate the brownian increments ΔB_j, j = 1,...,n, having
 * distribution N(0, Δ_j) with Δ_j:= t_j-t_{j-1}. Then we simply go forward
 * putting B_{t_j}= B_{t_{j-1}} + ΔB_j.
 *
 * @author Andrea Mazzon
 *
 */
public class BrownianMotion {

	private TimeDiscretization times;// look at the TimeDiscretization interface and TimeDiscretizationFromArray
										// class of the Finmath library

	private final int numberOfFactors; // more than one if this is a multi-dimensional Brownian motion
	private final int numberOfPaths; // number of simulations
	private final double initialValue = 0;

	/*
	 * Matrices of RandomVariable types: dimensions are time length and number of
	 * factors. A random variable can be seen as a vector, so these matrices can be
	 * seen as 3-dimensional matrices of doubles, the third dimension being the
	 * number of simulations.
	 */
	private RandomVariable[][] brownianIncrements;
	private RandomVariable[][] brownianPaths;

	public BrownianMotion( // Constructor
			TimeDiscretization timeDiscretization, int numberOfFactors, int numberOfPaths) {
		this.times = timeDiscretization;
		this.numberOfFactors = numberOfFactors;
		this.numberOfPaths = numberOfPaths;
	}

	// Overloaded constructor generating a time discretization internally from the
	// given data
	public BrownianMotion(double initialTimeValue, int numberOfTimeSteps, // number of time steps of the time
																			// discretization
			double deltaT, // length of the time step (supposed IN THIS CASE to be constant)
			int numberOfFactors, int numberOfPaths) {
		this.times = new TimeDiscretizationFromArray(initialTimeValue, numberOfTimeSteps, deltaT);
		this.numberOfFactors = numberOfFactors;
		this.numberOfPaths = numberOfPaths;
	}

	// let's say numberOfTimeSteps = 1000 if not given
	public BrownianMotion(double initialTimeValue, // number of time steps of the time
			// discretization
			double deltaT, // length of the time step (supposed IN THIS CASE to be constant)
			int numberOfFactors, int numberOfPaths) {
		this(initialTimeValue, 1000, // number of time steps of the time
				deltaT, numberOfFactors, numberOfPaths);
	}

	/*
	 * It generates a Brownian motion, i.e., it fills the entries of
	 * brownianIncrements and brownianPaths. In particular, it first creates
	 * 3-dimensional matrices of doubles and then wraps them in 2-dimensional
	 * matrices of objects of type RandomVariable.
	 */
	private void generateBrownianMotion() {
		/*
		 * number of time steps: we get it through the getNumberOfTimeSteps method of
		 * TimeDiscretizationFromArray
		 */
		final int numberOfTimeSteps = times.getNumberOfTimeSteps();
		final int numberOfTimes = times.getNumberOfTimes(); // number of times = numberOfTimesteps + 1

		// dimensions are: number of time steps, number of factors and number of paths
		final double[][][] brownianIncrements3Array = new double[numberOfTimeSteps][numberOfFactors][numberOfPaths];

		// paths have numberOfTimeSteps + 1 points
		final double[][][] brownianPaths3Array = new double[numberOfTimes][numberOfFactors][numberOfPaths];

		/*
		 * Better to define it here once for always, with volatility 1, instead of
		 * inside the for loop, every time with different volatility
		 * Math.sqrt(times.getTimeStep(i))
		 */
		final NormalRandomVariable normalRv = new NormalRandomVariable(0.0, 1.0);

		final double[] volatilities = new double[numberOfTimeSteps]; // allocate space for volatilities array
		for (int i = 0; i < numberOfTimeSteps; i++) {
			/*
			 * here is where the structure of the TimeDiscretisation is used: the mesh may
			 * not be equally sized.
			 */
			volatilities[i] = Math.sqrt(times.getTimeStep(i)); // other method of TimeDiscretizationFromArray!
		}

		// loop: Generate uncorrelated Brownian increments
		for (int pathIndex = 0; pathIndex < numberOfPaths; pathIndex++) {
			for (int factorIndex = 0; factorIndex < numberOfFactors; factorIndex++) {
				// first we fill the entries of the 3-dimensional matrix of doubles
				brownianPaths3Array[0][factorIndex][pathIndex] = initialValue;
				for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
					brownianIncrements3Array[timeIndex][factorIndex][pathIndex] = normalRv.generate()
							* volatilities[timeIndex];
					// we sum the increment
					brownianPaths3Array[timeIndex
							+ 1][factorIndex][pathIndex] = brownianPaths3Array[timeIndex][factorIndex][pathIndex]
									+ brownianIncrements3Array[timeIndex][factorIndex][pathIndex];
				}
			}
		}

		/*
		 * At this point, we have two 3-dimensional matrices whose entries are the
		 * Brownian increments and values, respectively. Now we want to wrap them into
		 * 2-dimensional matrices of types RandomVariableFromDoubleArray: a vector
		 * containing all the realizations at a given time for a given Brownian factor
		 * is wrapped into an object of type RandomVariableFromDoubleArray.
		 */
		// First, we allocate memory for RandomVariableFromDoubleArray wrapper objects.
		brownianIncrements = new RandomVariable[numberOfTimeSteps][numberOfFactors];
		brownianPaths = new RandomVariable[numberOfTimeSteps + 1][numberOfFactors];

		// Wrap the values in RandomVariable objects
		for (int factorIndex = 0; factorIndex < numberOfFactors; factorIndex++) {
			/*
			 * The entries for time equal to zero are actually non stochastic: we use an
			 * overload version of the constructor of RandomVariableFromDoubleArray that
			 * builds non stochastic random variables (i.e., all the realizations are the
			 * same).
			 */
			brownianPaths[0][factorIndex] = new RandomVariableFromDoubleArray(times.getTime(0), // filtration time
					initialValue);
			for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
				brownianIncrements[timeIndex][factorIndex] = new RandomVariableFromDoubleArray(times.getTime(timeIndex),
						brownianIncrements3Array[timeIndex][factorIndex]);// vector: realizations of the rv
				brownianPaths[timeIndex + 1][factorIndex] = new RandomVariableFromDoubleArray(
						times.getTime(timeIndex + 1), brownianPaths3Array[timeIndex + 1][factorIndex]);
			}
		}
	}

	/**
	 * It gets and returns the time discretization used for the process
	 *
	 * @return the time discretization used for the process
	 */
	public TimeDiscretization getTimeDiscretization() {
		/*
		 * fine as long as the implementation of the class implementing
		 * TimeDiscretization used here is immutable: immutable means there are no ways
		 * to modify an object of such a class, so even if we return a reference to the
		 * object we are safe
		 */
		return times;
	}

	/**
	 * It gets and returns the two-dimensional array of random variables
	 * representing the brownian increments. Dimensions are the number of factors
	 * and the number of times.
	 *
	 * @return the two-dimensional array of random variables representing the
	 *         brownian increments
	 */
	public RandomVariable[][] getBrownianIncrements() {
		// lazy initialization: brownianIncrements gets initialized only when needed
		if (brownianIncrements == null) { // generated only once
			generateBrownianMotion();
		}
		/*
		 * NOTE: here we have to return a clone of our array object. If we return the
		 * object itself, it might be accessed and modified from the outside. For
		 * example, modifying the n-th element.
		 */
		RandomVariable[][] brownianIncrementsClone = brownianIncrements.clone();
		return brownianIncrementsClone;
	}

	/**
	 * It gets and returns the two-dimensional array of random variables
	 * representing the brownian realized paths. Dimensions are the number of
	 * factors and the number of times.
	 *
	 * @return the two-dimensional array of random variables representing the
	 *         brownian paths
	 */
	public RandomVariable[][] getAllThePaths() {
		// lazy initialization: brownianPaths gets initialized only when needed
		if (brownianPaths == null) { // generated only once
			generateBrownianMotion();
		}
		// same thing here
		return brownianPaths.clone();
	}

	/**
	 * It gets and returns a one-dimensional array of doubles representing the given
	 * increments of the Brownian motion for a given factor
	 *
	 * @param factor, index for the factor
	 * @param path,   index for the path (i.e., a given simulation)
	 * @return a vector of doubles with the values of the increments over time
	 */
	public RandomVariable getBrownianIncrement(int timeIndex, int factorIndex) {

		RandomVariable[][] allTheIncrements = getBrownianIncrements();

		return allTheIncrements[timeIndex][factorIndex];
	}

	/**
	 * It gets and returns a random variable which stands for the Brownian motion
	 * for a given factor at a given time index
	 *
	 * @param factor,    index for the factor
	 * @param timeIndex, index for the time at which the Brownian motion is
	 *                   considered
	 * @return a random variable which stands for the Brownian motion for a given
	 *         factor at a given time
	 */
	public RandomVariable getSimulationForGivenTimeIndex(int timeIndex, int factorIndex) {

		RandomVariable[][] allThePaths = getAllThePaths();

		return allThePaths[timeIndex][factorIndex];
	}

	/**
	 * It gets and returns a random variable which stands for the Brownian motion
	 * for a given factor at a given time
	 *
	 * @param factor, index for the factor
	 * @param time,   the time at which the Brownian motion is considered
	 * @return a random variable which stands for the Brownian motion for a given
	 *         factor at a given time
	 */
	public RandomVariable getSimulationForGivenTime(double time, int factorIndex) {

		RandomVariable[][] allThePaths = getAllThePaths();

		return allThePaths[times.getTimeIndex(time)][factorIndex];
	}

	/**
	 * It gets and returns a one-dimensional array of random variables representing
	 * the paths of the Brownian motion for a given factor.
	 *
	 * @param factor, index for the factor
	 * @return one-dimensional array of RandomVariableFromDoubleArray objects
	 *         representing the evolution in time of the given indexed factor
	 */
	public RandomVariable[] getPathsForFactor(int factorIndex) {

		final int numberOfTimes = times.getNumberOfTimes();
		final RandomVariable[] paths = new RandomVariableFromDoubleArray[numberOfTimes];

		RandomVariable[][] allThePaths = getAllThePaths();

		for (int timeIndex = 0; timeIndex < numberOfTimes; timeIndex++) {
			paths[timeIndex] = allThePaths[timeIndex][factorIndex];
		}
		return paths;
	}

	/**
	 * It gets and returns a one-dimensional array of doubles representing the given
	 * path of the Brownian motion for a given factor
	 *
	 * @param factor, index for the factor
	 * @param path,   index for the path (i.e., a given simulation)
	 * @return a vector of doubles with the values of the path over time
	 */
	public double[] getSpecificPathForFactor(int factorIndex, int pathIndex) {
		// first we get the vector of random variables, for the given factor
		final RandomVariable[] paths = getPathsForFactor(factorIndex);
		final int numberOfTimes = paths.length;
		final double[] specificPath = new double[numberOfTimes];
		// the we extrapolate the path for the specific path
		for (int timeIndex = 0; timeIndex < numberOfTimes; timeIndex++) {
			specificPath[timeIndex] = paths[timeIndex].get(pathIndex); // get method of RandomVariableFromArray
		}
		return specificPath;
	}

	/**
	 * It prints the path of the Brownian motion for a given simulation and a given
	 * factor
	 *
	 * @param factor, index for the factor
	 * @param path,   index for the path (i.e., a given simulation)
	 */
	public void printSpecificPath(int factorIndex, int pathIndex) {
		final double[] specificPath = getSpecificPathForFactor(factorIndex, pathIndex);
		final int numberOfTimes = specificPath.length;

		for (int timeIndex = 0; timeIndex < numberOfTimes; timeIndex++) {
			System.out.println(specificPath[timeIndex]);
		}
	}

	/**
	 * It prints the increment of the Brownian motion for a given simulation and a
	 * given factor at a given time
	 *
	 * @param timeindex, time at which the increment is considered
	 * @param factor,    index for the factor
	 * @param path,      index for the path (i.e., a given simulation)
	 */
	public void printIncrement(int timeindex, int factorIndex, int pathIndex) {
		// lazy initialization: brownianPaths gets initialized only when needed
		if (brownianIncrements == null) { // generated only once
			generateBrownianMotion();
		}
		System.out.println(brownianIncrements[timeindex][factorIndex].get(pathIndex));
	}
}
