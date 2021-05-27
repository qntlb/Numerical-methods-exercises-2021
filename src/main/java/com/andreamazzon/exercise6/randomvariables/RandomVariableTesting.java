package com.andreamazzon.exercise6.randomvariables;

/**
 * This class tests some methods of classes NormalRandomVariable and
 * ExponentialRandomVariable, which implement RandomVariableInterface.
 *
 * @author Andrea Mazzon
 *
 */
public class RandomVariableTesting {

	public static void main(String[] args) {
		double lambda = 0.2;
		int numberOfSimulations = 100000;
		ExponentialRandomVariable exponentialSampler = new ExponentialRandomVariable(lambda);

		System.out.println("Exponential random variable: comparing Empirical mean and std dev to mu and sigma");

		System.out.println();

		System.out.println("Sample mean: " + exponentialSampler.getSampleMean(numberOfSimulations));
		System.out.println("Mu: " + exponentialSampler.getAnalyticMean());

		System.out.println();

		System.out.println("Sample std dev: " + exponentialSampler.getSampleStdDeviation(numberOfSimulations));
		System.out.println("Sigma: " + exponentialSampler.getAnalyticStdDeviation());

		System.out.println();

		System.out.println("_".repeat(80) + "\n");

		double mu = 1;
		double sigma = 2.0;
		NormalRandomVariable normalSampler = new NormalRandomVariable(mu, sigma);

		System.out.println("Normal random variable: comparing Empirical mean and std dev to mu and sigma");

		System.out.println();

		System.out.println("Sample mean: " + normalSampler.getSampleMean(numberOfSimulations));
		System.out.println("Mu: " + normalSampler.getAnalyticMean());

		System.out.println();

		System.out.println("Sample std dev: " + normalSampler.getSampleStdDeviation(numberOfSimulations));
		System.out.println("Sigma: " + normalSampler.getAnalyticStdDeviation());

		System.out.println();
		System.out.println("_".repeat(80) + "\n");
		System.out.println();

		double prob = 0.95;

		double y = normalSampler.getQuantileFunction(prob);
		System.out.println("The quantile of a probability of " + prob + " of a  normal random variable of mean " + mu
				+ " and std deviation " + sigma + " is " + y);

		System.out.println("\n");

		System.out.println("The cumulative distribution  function at " + y + " is "
				+ normalSampler.getCumulativeDistributionFunction(y));

	}
}