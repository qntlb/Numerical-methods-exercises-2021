package com.andreamazzon.exercise3.overflowlinearcongruentialgenerator;

import static com.andreamazzon.session3.useful.Print.printn;

import java.util.Arrays;

import com.andreamazzon.session3.encapsulation.lazyinitialization.LinearCongruentialGenerator;

/**
 * This class contains one main method that calls method of the class
 * LinearCongruentialGenerator.
 *
 * @author Andrea Mazzon
 *
 */

public class PseudoRandomNumbersTesting {
	public static void main(String[] args) {

		long firstSeed = 2814749763100L;// the seed is the first entry of the sequence of pseudo random numbers
		int numberOfPseudoRandomNumbers = 5;

		AdjustedLinearCongruentialGenerator firstGenerator = new AdjustedLinearCongruentialGenerator(
				numberOfPseudoRandomNumbers, firstSeed);

		long[] sequenceGeneratedByTheFirstObject = firstGenerator.getRandomNumberSequence();

		System.out.println();

		System.out.println("Simulation of " + numberOfPseudoRandomNumbers + " integers with seed " + firstSeed
				+ " using the Java specifications: " + Arrays.toString(sequenceGeneratedByTheFirstObject));

		System.out.println();

		System.out.println("First four number of the random sequence, excluded the seed:");
		// maybe the user is not interested to have all the sequence, but only in the
		// first numbers
		for (int i = 0; i < numberOfPseudoRandomNumbers - 1; i++) {
			printn(firstGenerator.getNextInteger());
		}

		System.out.println();

		long secondSeed = 8L;

		LinearCongruentialGenerator secondGenerator = new LinearCongruentialGenerator(numberOfPseudoRandomNumbers,
				secondSeed);

		long[] sequenceGeneratedByTheSecondObject = secondGenerator.getRandomNumberSequence();

		System.out.println("Simulation of " + numberOfPseudoRandomNumbers + " integers with seed " + secondSeed
				+ " using the Java specifications: " + Arrays.toString(sequenceGeneratedByTheSecondObject));
	}
}