package com.andreamazzon.exercise7.importancesampling;

import java.text.DecimalFormat;
import java.util.function.DoubleUnaryOperator;

import com.andreamazzon.exercise7.randomvariables.NormalRandomVariable;

/**
 * In this class we want to test importance sampling with weighted Monte-Carlo
 * by considering the approximation of P(X > 2.5) where X is a standard normal
 * random variable. Since this is an "extreme" event and the variance is very
 * high, the quality of the approximation with standard sampling might be very
 * poor. We see that by sampling with weighted Monte-Carlo where Y is normal
 * with mean 2.5 the result is much better.
 *
 * @author Andrea Mazzon
 *
 */
public class ImportanceSamplingTesting {

	private final static DecimalFormat formatterDouble = new DecimalFormat("0.0000");
	private final static DecimalFormat formatterPercentage = new DecimalFormat("0.0000 %");

	public static void main(String[] args) {

		final int numberOfDrawings = 10000;
		/*
		 * We do 1000 tests in order to get a better idea of the difference between
		 * standard sampling and importance sampling
		 */
		final int numberOfTests = 1000;

		final double barrier = 2.5;// we want compute P(X > barrier)

		final NormalRandomVariable standardNormal = new NormalRandomVariable(0.0, 1.0);
		// the mean is the barrier itself!
		final NormalRandomVariable shiftedNormal = new NormalRandomVariable(barrier, 1);
		final DoubleUnaryOperator indicatorIntegrand = x -> (x > barrier) ? 1.0 : 0.0; // 1_{X > barrier}

		final double analyticResult = 1 - standardNormal.getCumulativeDistributionFunction(barrier);

		// we now compare the two methods

		// number of times when the error of standard sampling is lower
		int numberOffWinsStandardSampling = 0;
		// number of times when the error of importance sampling is lower
		int numberOffWinsImportanceSampling = 0;

		double sumPercentualErrorStandardSampling = 0.0;
		double sumPercentualErrorImportanceSampling = 0.0;

		for (int i = 0; i < numberOfTests; i++) {

			// standard sampling
			final double resultStandardSampling = standardNormal.getSampleMean(numberOfDrawings, indicatorIntegrand);
			// importance sampling(weighted Monte-Carlo)
			final double resultImportanceSampling = standardNormal.getSampleMeanWithWeightedMonteCarlo(numberOfDrawings,
					indicatorIntegrand, shiftedNormal);

			// percentage errors
			final double errorStandardSampling = Math.abs(analyticResult - resultStandardSampling) / analyticResult;
			final double errorImportanceSampling = Math.abs(analyticResult - resultImportanceSampling) / analyticResult;

			// we update the average
			sumPercentualErrorStandardSampling += errorStandardSampling;
			sumPercentualErrorImportanceSampling += errorImportanceSampling;

			// we check the winner
			if (errorStandardSampling > errorImportanceSampling) {
				numberOffWinsImportanceSampling++;
			} else {
				numberOffWinsStandardSampling++;
			}
		}
		double averagePercentualErrorStandardSampling = sumPercentualErrorStandardSampling / numberOfTests;
		double averagePercentualErrorImportanceSampling = sumPercentualErrorImportanceSampling / numberOfTests;

		// We print the results

		System.out.println("Analytic percentage probability of a standard double exponential variable being more than "
				+ barrier + ": " + formatterPercentage.format(analyticResult));

		System.out.println();

		System.out.println("The average percentage error of standard sampling is "
				+ formatterPercentage.format(averagePercentualErrorStandardSampling));

		System.out.println("The average percentage error of importance sampling is "
				+ formatterPercentage.format(averagePercentualErrorImportanceSampling));

		System.out.println();

		System.out.println("Number of times when standard sampling is better: " + numberOffWinsStandardSampling);
		System.out.println("Number of times when importance sampling is better: " + numberOffWinsImportanceSampling);

	}
}
