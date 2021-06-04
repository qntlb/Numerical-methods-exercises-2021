package com.andreamazzon.exercise7.bivariatenormal;

import java.text.DecimalFormat;
import java.util.concurrent.Callable;

import com.andreamazzon.exercise7.randomvariables.NormalRandomVariable;

/**
 * In this class we test the precision and the efficiency of inversion sampling
 * and acceptance rejection to generate a pair of independent normal random
 * variables with expectation mu and standard deviation sigma. The test is to
 * compute the Monte-Carlo approximation of P(X_1<mu, X_2 <mu) where X_1, X_2
 * are independent, normal random variables with expectation mu. For every
 * method we compute and print the average percentage error with respect to the
 * exact probability 0.25 and the time needed in order to generate the drawings
 * of (X_1,X_2) and do the computations.
 *
 * @author Andrea Mazzon
 *
 */
public class BivariateNormalTesting {

	// format in order to print a number with 5 decimal digits
	static DecimalFormat formatterValue = new DecimalFormat("#0.00000");

	// These fields are initialized here.
	private double exactResult = 0.25;// because Z_1 and Z_2 are independent
	private double sumElapsedTime = 0;
	private double sumError = 0;

	/*
	 * expected value of the two normal random variables Z_1,Z_2 (they are
	 * independent and have same distribution). It will be initialized in
	 * testMethod(NormalRandomVariable normalTestSampler, GenerationMethods method),
	 * once we know our NormalRandomVariable object.
	 */
	private double mu;

	// they will be initialized in the constructor
	private int numberOfComputations;
	private int numberOfDrawings;

	/**
	 * It generates an object of the class to test the implementation of the two
	 * generation methods
	 *
	 * @param numberOfDrawings     the number of drawings we simulate every time we
	 *                             want to compute the approximate probability
	 * @param numberOfComputations the number of times we compute the probability
	 */
	public BivariateNormalTesting(int numberOfDrawings, int numberOfComputations) {
		this.numberOfDrawings = numberOfDrawings;
		this.numberOfComputations = numberOfComputations;
	}

	/**
	 * It tests the precision and the efficiency of a selected method to generate a
	 * pair of independent normal random variables with expectation mu and standard
	 * deviation sigma. The method (inversion sampling or acceptance rejection) is
	 * selected by means of a switch statement based on an enum type containing the
	 * names of the methods. The test is to compute the Monte-Carlo approximation of
	 * P(X_1<mu, X_2 <mu) where X_1, X_2 are independent, normal random variables
	 * with expectation mu. For every method we compute and print the average
	 * percentage error with respect to the exact probability 0.25 and the time
	 * needed in order to generate the drawings of (X_1,X_2) and do the
	 * computations.
	 *
	 * @param normalTestSampler, object of type NormalRandomVariable. It calls the
	 *                           generation methods
	 * @param method,            enum type whose value is the name of one of the two
	 *                           generation methods.
	 * @throws Exception, if unable to compute a result given by the call to the
	 *                    method Callable<double[]> functionToEvaluate
	 */
	public void testMethodLengthy(NormalRandomVariable normalTestSampler, GenerationMethods method) throws Exception {

		/*
		 * expected value of the two normal random variables Z_1,Z_2 (they are
		 * independent and have same distribution)
		 */
		mu = normalTestSampler.getAnalyticMean();// field of the class now!

		/*
		 * Here we have a switch based on the name of the four methods, see the enum
		 * type GenerationMethods
		 */
		switch (method) {// name of the method
		case BIVARIATENORMAL:
			System.out.println("Bivariate Normal");
			/*
			 * for every Monte-Carlo approximation, we compute the percentage error and the
			 * time needed to do the computation. Then we compute the average.
			 */
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * We compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					double[] generatedPair = normalTestSampler.generateBivariate();
					if (generatedPair[0] < mu && generatedPair[1] < mu) {
						numberOfTimesBothSmallerThanMu++;
					}
				}
				/*
				 * number of generated pairs for which both the values are smaller then the mean
				 * divided by the number of simulations: you expect the result to be close to
				 * 0.25
				 */
				double frequence = numberOfTimesBothSmallerThanMu / numberOfDrawings;
				long lEndTime = System.currentTimeMillis();// time when the computation ends: it depends on the method.
				double elapsedTime = lEndTime - lStartTime;
				sumElapsedTime += elapsedTime;
				double error = Math.abs(frequence - exactResult) / exactResult * 100;
				sumError += error;
			}
			break;

		case ACCEPTANCEREJECTION:
			System.out.println("Acceptance rejection");
			/*
			 * for every Monte-Carlo approximation, we compute the percentage error and the
			 * time needed to do the computation. Then we compute the average.
			 */
			for (int i = 0; i < numberOfComputations; i++) {
				/*
				 * We compute for how many generated pairs both the values are smaller than mu
				 */
				double numberOfTimesBothSmallerThanMu = 0.0;
				long lStartTime = System.currentTimeMillis();// time when the computations starts
				for (int j = 0; j < numberOfDrawings; j++) {
					double[] generatedPair = normalTestSampler.generateBivariateNormalAR();
					if (generatedPair[0] < mu && generatedPair[1] < mu) {
						numberOfTimesBothSmallerThanMu++;
					}
				}
				/*
				 * number of generated pairs for which both the values are smaller then the mean
				 * divided by the number of simulations: you expect the result to be close to
				 * 0.25
				 */
				double frequence = numberOfTimesBothSmallerThanMu / numberOfDrawings;
				long lEndTime = System.currentTimeMillis();// time when the computation ends: it depends on the method.
				double elapsedTime = lEndTime - lStartTime;
				sumElapsedTime += elapsedTime;
				double error = Math.abs(frequence - exactResult) / exactResult * 100;
				sumError += error;
			}
			break;
		}

		double averageElapsedTime = sumElapsedTime / numberOfComputations;
		double averageError = sumError / numberOfComputations;

		System.out.println("Average elapsed time: " + formatterValue.format(averageElapsedTime));
		System.out.println("Average percentage error: " + formatterValue.format(averageError));
		System.out.println();
	}

	/**
	 * It tests the precision and the efficiency of a selected method to generate a
	 * pair of independent normal random variables with expectation mu and standard
	 * deviation sigma. The method (inversion sampling or acceptance rejection) is
	 * selected by means of a switch statement based on an enum type containing the
	 * names of the methods. The test is to compute the Monte-Carlo approximation of
	 * P(X_1<mu, X_2 <mu) where X_1, X_2 are independent, normal random variables
	 * with expectation mu. For every method we compute and print the average
	 * percentage error with respect to the exact probability 0.25 and the time
	 * needed in order to generate the drawings of (X_1,X_2) and do the
	 * computations.
	 *
	 * @param normalTestSampler, object of type NormalRandomVariable. It calls the
	 *                           generation methods
	 * @param method,            enum type whose value is the name of one of the two
	 *                           generation methods.
	 * @throws Exception, if unable to compute a result given by the call to the
	 *                    method Callable<double[]> functionToEvaluate
	 */
	public void testMethod(NormalRandomVariable normalTestSampler, GenerationMethods method) throws Exception {

		/*
		 * expected value of the two normal random variables Z_1,Z_2 (they are
		 * independent and have same distribution)
		 */
		mu = normalTestSampler.getAnalyticMean();// field of the class now!

		double[] values = null;// we have to initialize it before the switch
		Callable<double[]> functionToEvaluate;
		/*
		 * Here we have a switch based on the name of the four methods, see the enum
		 * type GenerationMethods
		 */
		switch (method) {// name of the method
		case BIVARIATENORMAL:
			System.out.println("Bivariate Normal");
			// we now say what must be returned by functionToEvaluate in this case
			functionToEvaluate = () -> normalTestSampler.generateBivariate();
			values = testMethod(functionToEvaluate);
			break;

		case ACCEPTANCEREJECTION:
			System.out.println("Acceptance rejection");
			// we now say what must be returned by functionToEvaluate in this case
			functionToEvaluate = () -> normalTestSampler.generateBivariateNormalAR();
			values = testMethod(functionToEvaluate);
			break;

		}

		final double averageError = values[0];
		final double averageElapsedTime = values[1];

		System.out.println("Average elapsed time: " + formatterValue.format(averageElapsedTime));
		System.out.println("Average percentage error: " + formatterValue.format(averageError));
		System.out.println();
	}

	/*
	 * This method computes and returns the average percentage error and the average
	 * time needed to compute the approximated probability P(Z_1 <= mu, Z_2 <= mu),
	 * where (Z_1,Z_2) are independent normal random variables with mean mu, only
	 * basing on a Callable<double[]> object that we call function. You can see it
	 * as a placeholder for the specific method that we will call depending on the
	 * chosen generation method. For now, we only know that it will return an array
	 * of doubles (as we specify between angular brackets).
	 */
	private double[] testMethod(Callable<double[]> function) throws Exception {
		/*
		 * For every Monte-Carlo approximation, we compute the percentage error and the
		 * time needed to do the computation. Then we compute the averages.
		 */
		for (int i = 0; i < numberOfComputations; i++) {
			// We compute for how many generated pairs both the values are smaller than mu
			int numberOfTimesBothSmallerThanMu = 0;
			final long lStartTime = System.currentTimeMillis();// time when the computations starts
			for (int j = 0; j < numberOfDrawings; j++) {
				/*
				 * NOTE!!! here our Callable<double[]> comes into play: generatedPair is the
				 * array of doubles returned by function when called (we know that it returns an
				 * array of doubles)
				 */
				final double[] generatedPair = function.call();// note the syntax
				if (generatedPair[0] < mu && generatedPair[1] < mu) {
					numberOfTimesBothSmallerThanMu++;
				}
			}
			/*
			 * number of generated pairs for which both the values are smaller than the mean
			 * divided by the number of simulations: you expect the result to be close to
			 * 0.25
			 */
			final double frequence = numberOfTimesBothSmallerThanMu / (double) numberOfDrawings;
			// time when the computation ends: it depends on the method.
			final long lEndTime = System.currentTimeMillis();
			final double elapsedTime = lEndTime - lStartTime;
			sumElapsedTime += elapsedTime;
			final double error = Math.abs(frequence - exactResult) / exactResult * 100;
			sumError += error;
		}
		double averageElapsedTime = sumElapsedTime / numberOfComputations;
		double averageError = sumError / numberOfComputations;

		return new double[] { averageError, averageElapsedTime };
	}

	public static void main(String[] args) throws Exception {

		final double mu = 2;
		final double sigma = 1.0;
		final int numberOfDrawings = 10000;
		final int numberOfComputations = 10000;
		final NormalRandomVariable normalTestSampler = new NormalRandomVariable(mu, sigma);
		final BivariateNormalTesting tester = new BivariateNormalTesting(numberOfDrawings, numberOfComputations);
		/*
		 * we test the method for the two choices: the vector of the names of the method
		 * is given by the static values() method of GenerationMethods: this is
		 * implemented in Java
		 */
		for (final GenerationMethods modelSelector : GenerationMethods.values()) {// foreach syntax
			tester.testMethod(normalTestSampler, modelSelector);
		}
	}
}