package com.andreamazzon.exercise9.brownianmotion;

import java.util.function.DoubleUnaryOperator;

import com.andreamazzon.exercise6.randomvariables.NormalRandomVariable;

import net.finmath.plots.Plot2D;

/**
 * This class provides the discretization and the implementation of a
 * one-dimensional Brownian motion. The Brownian motion is simulated for a time
 * discretization (t_0,t_1,..,t_n), supposing t_i-t_{i-1} = Δ, i.e., constant
 * for any i = 1, .., n, and is represented by a one-dimensional array of
 * objects of type RandomVariableFromArray. In order to simulate the process
 * itself, we simulate the Brownian increments ΔB_j, j = 1,...,n, having
 * distribution N(0, Δ) with Δ:= t_j-t_{j-1}. Then we simply go forward putting
 * B_{t_j}= B_{t_{j-1}} + ΔB_j.
 *
 * @author Andrea Mazzon
 *
 */
public class BrownianMotion {

	private final double timeStepLength;
	private final int numberOfTimeSteps;
	private final double finalTime;

	private final int numberOfPaths;
	/*
	 * The Brownian motion starts at zero. Anyway, it's better to create a field
	 * initialValue and set it to be zero because this helps the readability of the
	 * code
	 */
	private final double initialValue = 0;

	/*
	 * Array of RandomVariableFromArray types: such objects can be seen as vectors,
	 * so these arrays can be seen as matrices of doubles, the second dimension
	 * being the number of simulated paths. It gets filled in
	 * generateBrownianMotion()
	 */
	private RandomVariableFromArray[] brownianPaths;

	/**
	 * It creates an object of type Brownian motion
	 *
	 * @param timeStepLength,    the size of the time steps in the discretization
	 * @param numberOfTimeSteps, the number of the time steps in the discretization
	 * @param numberOfPaths,     the number of simulated paths
	 */
	public BrownianMotion( // Constructor
			double timeStepLength, int numberOfTimeSteps, int numberOfPaths) {
		this.timeStepLength = timeStepLength;
		this.numberOfTimeSteps = numberOfTimeSteps;
		this.numberOfPaths = numberOfPaths;
		this.finalTime = numberOfTimeSteps * timeStepLength;
	}

	/**
	 * It creates an object of type Brownian motion
	 *
	 * @param timeStepLength, the size of the time steps in the discretization
	 * @param finalTime,      the final time t_n in the time discretization
	 * @param numberOfPaths,  the number of simulated paths
	 */
	public BrownianMotion( // Overloaded constructor
			double timeStepLength, double finalTime, int numberOfPaths) {
		this.timeStepLength = timeStepLength;
		this.numberOfPaths = numberOfPaths;
		this.finalTime = finalTime;
		// the initial time is zero: B_0 = 0
		numberOfTimeSteps = (int) (finalTime / timeStepLength);
	}

	/*
	 * It generates a Brownian motion, i.e., it fills the entries of brownianPaths.
	 * In particular, it first creates a matrix of doubles and then wraps it in
	 * one-dimensional matrices of objects of type RandomVariableFromArray.
	 */
	private void generateBrownianMotion() {

		// the paths have numberOfTimeSteps + 1 points
		final int numberOfTimes = numberOfTimeSteps + 1;

		final double[][] brownianPathsArray = new double[numberOfTimes][numberOfPaths];

		// we need it in order to simulate the increments
		final NormalRandomVariable normalRv = new NormalRandomVariable(0.0, 1.0);

		final double incrementsVolatility = Math.sqrt(timeStepLength);

		// to be filled every time, for every simulated path
		double brownianIncrement;

		// loop:at every iteration we generate uncorrelated Brownian increments
		for (int pathIndex = 0; pathIndex < numberOfPaths; pathIndex++) {
			// first we fill the entries of the matrix of doubles
			brownianPathsArray[0][pathIndex] = initialValue;
			for (int timeIndex = 0; timeIndex < numberOfTimeSteps; timeIndex++) {
				brownianIncrement = normalRv.generate() * incrementsVolatility;
				// we sum the increment
				brownianPathsArray[timeIndex + 1][pathIndex] = brownianPathsArray[timeIndex][pathIndex]
						+ brownianIncrement;
			}
		}

		/*
		 * At this point, we have a two-dimensional matrix whose entries are the values
		 * of the Brownian motion, for every time and for every simulated path. Now we
		 * want to wrap into a 1-dimensional array of instances of
		 * RandomVariableFromArray: a vector containing all the realizations at a given
		 * time for a given Brownian factor is wrapped into an object of type
		 * RandomVariableFromArray.
		 */

		// First, we allocate memory for RandomVariableFromArray wrapper objects.
		brownianPaths = new RandomVariableFromArray[numberOfTimes];

		// Then we wrap the values in RandomVariable objects
		for (int timeIndex = 0; timeIndex <= numberOfTimeSteps; timeIndex++) {
			brownianPaths[timeIndex] = new RandomVariableFromArray(brownianPathsArray[timeIndex]);
		}
	}

	/**
	 * It gets and returns the one-dimensional array of random variables
	 * representing the brownian realized paths.
	 *
	 * @return the one-dimensional array of random variables representing the
	 *         brownian paths. The length of the array is given by the the number of
	 *         times.
	 */
	public RandomVariableFromArray[] getPaths() {
		if (brownianPaths == null) { // generated only once
			// lazy initialization: brownianPaths gets initialized only when needed
			generateBrownianMotion();
		}
		return brownianPaths;
	}

	/**
	 * It gets and returns a random variable which stands for the Brownian motion at
	 * a given time
	 *
	 * @param timeIndex, index for the time at which the Brownian motion is
	 *                   considered
	 * @return a random variable which stands for the Brownian motion for a given
	 *         factor at a given time
	 */
	public RandomVariableFromArray getProcessAtGivenTimeIndex(int timeIndex) {
		if (brownianPaths == null) { // generated only once
			// lazy initialization: brownianPaths gets initialized only when needed
			generateBrownianMotion();
		}
		return brownianPaths[timeIndex];
	}

	/**
	 * It gets and returns a one-dimensional array of doubles representing the given
	 * path of the Brownian motion for the given simulation index
	 *
	 * @param pathIndex, index for the path (i.e., the given simulation)
	 * @return a vector of doubles with the values of the path over time
	 */
	public double[] getSpecificPath(int pathIndex) {

		final double[] specificPath = new double[numberOfTimeSteps + 1];
		if (brownianPaths == null) { // generated only once
			// lazy initialization: brownianPaths gets initialized only when needed
			generateBrownianMotion();
		}
		// then we extrapolate the path for the specific path index
		for (int timeIndex = 0; timeIndex <= numberOfTimeSteps; timeIndex++) {
			// note how we use the method of RandomVariableFromArray
			specificPath[timeIndex] = brownianPaths[timeIndex].getSpecificRealization(pathIndex);
		}
		return specificPath;
	}

	/**
	 * It gets and returns double representing the realization of the Brownian
	 * motion for the given simulation index at the given time index
	 *
	 * @param pathIndex, index for the path (i.e., the given simulation)
	 * @param timeIndex, index for the time
	 *
	 * @return a double value representing the value of the Brownian motion for the
	 *         given simulation index at the given time index
	 */
	public double getSpecificRealizationAtGivenTimeIndex(int pathIndex, int timeIndex) {

		final double[] specificPath = getSpecificPath(pathIndex);

		return specificPath[timeIndex];
	}

	/**
	 * It prints the one-dimensional array of doubles representing the given path of
	 * the Brownian motion for the given simulation index
	 *
	 * @param pathIndex, index for the path (i.e., the given simulation)
	 */
	public void printSpecificPath(int pathIndex) {
		final double[] specificPath = getSpecificPath(pathIndex);

		for (int timeIndex = 0; timeIndex <= numberOfTimeSteps; timeIndex++) {
			System.out.println(specificPath[timeIndex]);
		}
	}

	/**
	 * It plots the one-dimensional array of doubles representing the given path of
	 * the Brownian motion for the given simulation index. The path is plotted
	 * against the indices
	 *
	 * @param pathIndex, index for the path (i.e., the given simulation)
	 */
	public void plotSpecificPath(int pathIndex) {
		double[] path = getSpecificPath(pathIndex);

		/*
		 * Note that we associate the value of the time to the value of the process at
		 * that time. We do it by calling the method
		 * getSpecificRealizationAtGivenTimeIndex and then giving it the time index
		 * corresponding to the time. It would be nicer to write a specific method to
		 * get the value of the Brownian motion directly for the specific time. Next
		 * time..
		 */
		DoubleUnaryOperator trajectory = (time) -> {
			return getSpecificRealizationAtGivenTimeIndex(pathIndex, (int) (time / timeStepLength));
		};

		Plot2D plot = new Plot2D(0 /* min value on the x-axis */, finalTime, /* max value */
				path.length, // number of plotted points
				trajectory/* function plotted */);
		plot.setTitle("Discretized Brownian motion");
		plot.setXAxisLabel("Time");
		plot.setYAxisLabel("Brownian value");
		plot.show();
	}
}
