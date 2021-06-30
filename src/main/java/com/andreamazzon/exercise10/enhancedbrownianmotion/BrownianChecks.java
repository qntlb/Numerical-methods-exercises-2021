package com.andreamazzon.exercise10.enhancedbrownianmotion;

import java.text.DecimalFormat;

import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * This class tests some features of Brownian motion through Monte Carlo
 * simulation
 *
 * @author Andrea Mazzon
 *
 */
public class BrownianChecks {

	public static void main(String[] args) {
		final DecimalFormat formatterValue = new DecimalFormat(" ##0.00000;" + "-##0.00000");
		final DecimalFormat formatterTime = new DecimalFormat(" ##0.00;");

		final int numberOfFactors = 2;
		final int numberOfPaths = 100000;

		// time discretization parameters
		final double initialTime = 0.0;
		final int numberOfTimeSteps = 100;
		final double timeHorizon = 1.0;
		final double deltaT = timeHorizon / numberOfTimeSteps;

		// time discretization from the Finmath library
		final TimeDiscretization timeDiscretization = new TimeDiscretizationFromArray(initialTime, numberOfTimeSteps,
				deltaT);

		// we construct the Brownian motion object
		final BrownianMotion brownianMotion = new BrownianMotion(timeDiscretization, numberOfFactors, numberOfPaths);
		// or
		// final BrownianMotion brownianMotion = new BrownianMotion(initialTime,
		// numberOfTimeSteps, deltaT,
		// numberOfFactors, numberOfPaths);

		/*
		 * We first want to compute an approximation of the correlation between the two
		 * factors. This is the approximation of
		 * E[B_{t_j}^1B_{t_j}^2]-E[B_{t_j}^1]E[B_{t_j}^2] = E[B_{t_j}^1B_{t_j}^2]. So we
		 * have to compute the approximated expectation of B_{t_j}^1B_{t_j}^2.
		 */
		double averagesOfTheProduct;

		/*
		 * we compute the product across all the paths, yielding a new RandomVariable
		 * object, and then compute its average. We use the mult(RandomVariable
		 * randomVariable) and getAverage methods of the RandomVariableFromDoubleArray
		 * class
		 */
		for (int timeIndex = 0; timeIndex < numberOfTimeSteps + 1; timeIndex += 10) {

			averagesOfTheProduct = brownianMotion.getSimulationForGivenTimeIndex(timeIndex, 0)
					.mult(brownianMotion.getSimulationForGivenTimeIndex(timeIndex, 1)).getAverage();

			System.out.println("The correlation of the two factors at time "
					+ formatterTime.format(timeDiscretization.getTime(timeIndex)) + " is " + " "
					+ formatterValue.format(averagesOfTheProduct));
		}

		System.out.println();

		// we now compute the cross correlation E[B^1_{t_j}B^1_{t_i}], for t_i != t_j

		/*
		 * Suppose t>s. Then it holds E[B_tB_s] = E[(B_t-B_s)B_s+B_s^2] = E[B_t -
		 * B_s]E[B_s] + E[B_s^2] = s, where the second equality comes from the
		 * independence of the increments of Brownian motion.
		 */

		final double firstTime = 0.1;
		final double secondTime = 0.2;

		// computes the cross correlation at certain times for the first Factor
		final double crossCorrelationTest = brownianMotion.getSimulationForGivenTime(firstTime, 0)
				.mult(brownianMotion.getSimulationForGivenTime(secondTime, 0)).getAverage();

		System.out.println("The serial correlation at times " + firstTime + " and " + secondTime + " is " + " "
				+ crossCorrelationTest);

	}
}