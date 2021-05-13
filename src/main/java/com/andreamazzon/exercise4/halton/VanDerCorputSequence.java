package com.andreamazzon.exercise4.halton;

/**
 * This class is used in order to compute the elements of a Van der Corput
 * sequence with a given base.
 *
 * @author Andrea Mazzon, from the implementation of Christian Fries
 *
 */
public class VanDerCorputSequence {

	/**
	 * @param index The index of the sequence starting with 0
	 * @param base  The base.
	 * @return The van der Corput number
	 */
	public static double getVanDerCorputNumber(long index, int base) {

		index = index + 1;// because we start from zero

		double x = 0.0;
		// first refinement
		double refinementFactor = 1.0 / base;

		while (index > 0) {
			/*
			 * refinementFactor is what you call b^(-j) at page 147 of the script, in the
			 * formula for x_i. On the other hand, index % base is alpha_{i,j}, that is, the
			 * number you get when you write the original index in base base.
			 */
			x += (index % base) * refinementFactor;

			index = index / base; // in order to write the original index in base base

			refinementFactor = refinementFactor / base;
		}

		/*
		 * Take index = 5, base = 2. You write 5 = 1 * 1 + 0 * 2 + 1 * 4. So you get
		 * alpha_{5,0}=1, alpha_{5,1}=0, alpha_{5,2}=1. So x_5 = 1 * 1/2 + 1 * 1/8 = 5/8
		 * Look at the while loop: first you have index = 5, refinementFactor=1/2, x=0.
		 * First iteration: x = 0 + 1 * 1/2, index = 5/2 = 2, refinementFactor = 1/4.
		 * Second iteration: x = 1/2 + 0 * 1/4, index = 2/2 = 1, refinementFactor = 1/8.
		 * Third iteration: x = 1/2 + 1 * 1/8, index = 1/2 = 0, refinementFactor = 1/16.
		 * And you stop here because index = 0. So, x = 1/2 + 1/8 = 5/8, and you return
		 * it.
		 */

		return x;
	}

	/**
	 * @param maxIndex The length of the sequence starting with 0
	 * @param base     The base.
	 * @return The van der Corput sequence up to the index n
	 */
	public static double[] getVanDerCorputSequence(int n, int base) {
		double[] sequence = new double[n + 1];
		for (int i = 0; i <= n; i++) {
			sequence[i] = getVanDerCorputNumber(i, base);
		}
		return sequence;
	}
}