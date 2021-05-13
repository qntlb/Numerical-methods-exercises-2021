package com.andreamazzon.exercise4.montecarlopihypersphere;

import java.util.Arrays;

/**
 * This class has a main method where we compare the approximation error of pi
 * from the volume of an hypesphere when the points are sampled by Monte-Carlo
 * and when they are sampled by an Halton sequence. The comparison may be done
 * for different dimensions.
 *
 * @author Andrea Mazzon
 *
 */
public class ComparisonPiFromHypersphere {

	public static void main(String[] args) {

		int numberOfIntegrations = 100;// number of Monte Carlo executions
		int numberOfSamplePoints = 100000;
		int dimension = 3;

		MonteCarloPiFromHypersphere monteCarlo = new MonteCarloPiFromHypersphere(numberOfIntegrations,
				numberOfSamplePoints, dimension);

		/*
		 * For the Monte-Carlo implementation, we compute the average of the error with
		 * respect to the value of Pi produced by Java for 100 different computations.
		 * In this way the result is not too affected by the seeds.
		 */

		double averageAbsoluteError = monteCarlo.getAverageAbsoluteError();
		System.out.println("Average of the errors in the computation of Pi with " + numberOfSamplePoints
				+ " sample points and dimension " + dimension + " by the Monte-Carlo method: " + averageAbsoluteError);
		// now we sample the points with the Halton sequence:
		int[] base = { 2, 3, 5 };

		HaltonSequencePiFromHypersphere haltonSequencePi = new HaltonSequencePiFromHypersphere(numberOfSamplePoints,
				base);

		System.out.println("Error in the computation of Pi with " + numberOfSamplePoints
				+ " sample points and dimension " + dimension + " and base " + Arrays.toString(base)
				+ " by the method with Halton sequence: " + haltonSequencePi.getError());
	}

}
