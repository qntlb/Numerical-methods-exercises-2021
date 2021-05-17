package com.andreamazzon.exercise5.discrepancy;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;

/**
 * The goal of this class is to compute the discrepancy and the star discrepancy
 * of a Van der Corput sequence of a given length and given base, and to plot
 * the values for increasing length of the sequence. In the case of the star
 * discrepancy, we also plot the value of log(n)/n for increasing n, in order to
 * compare the two.
 *
 * @author Andrea Mazzon
 *
 */
public class VanDerCorputDiscrepancy {

	/**
	 * It computes and returns the star discrepancy of the Van der Corput sequence
	 * of length sequenceLength and base base, i.e., of the first sequenceLength
	 * numbers of the Van der Corput sequence of base base.
	 *
	 * @param sequenceLength, the length of the sequence, i.e., the number of points
	 *                        considered
	 * @param base,           the base of the sequence
	 * @return the star discrepancy
	 */
	public static double getVanDerCorputStarDiscrepancy(int sequenceLength, int base) {
		// your implementation here
		return 0;
	}

	/**
	 * It computes and returns the discrepancy of the Van der Corput sequence of
	 * length sequenceLength and base base, i.e., of the first sequenceLength
	 * numbers of the Van der Corput sequence of base base.
	 *
	 * @param sequenceLength, the length of the sequence, i.e., the number of points
	 *                        considered
	 * @param base,           the base of the sequence
	 * @return the discrepancy
	 */
	public static double getVanDerCorputDiscrepancy(int sequenceLength, int base) {
		// your implementation here
		return 0;
	}

	/**
	 * It plots the star discrepancy of Van der Corput sequences of increasing
	 * length n and base base, together with the values of log(n)/n.
	 *
	 * @param maxSequenceLength, the length of the longest sequence whose value is
	 *                           plotted
	 * @param base,              the base of the sequences
	 */
	public static void plotVanDerCorputStarDiscrepancy(int maxSequenceLength, int base) throws Exception {

		/*
		 * This is a function mapping the length of a Van der Corput sequence of the
		 * given base to the star discrepancy of the sequence. Note that the function
		 * maps doubles to doubles, so sequenceLength is considered to be a double, and
		 * must be downcasted if you want to call the getVanDerCorputStarDiscrepancy
		 * method.
		 */
		DoubleUnaryOperator starDiscrepancyFunction = (sequenceLength) -> {
			return 0;
			// your implementation here
		};

		/*
		 * a function returning log(x)/x for a double n. In the plot, x will be the size
		 * of the Van der Corput sequences.
		 */
		DoubleUnaryOperator logNOverN = (x) -> {

			return Math.log(x) / x;
		};

		Plot2D plot = new Plot2D(2 /* min value on the x-axis */, maxSequenceLength, /* max value */
				maxSequenceLength - 2, /* number of plotted points: length 1 and 2 are not considered */
				Arrays.asList(new Named<DoubleUnaryOperator>("Star discrepancy", starDiscrepancyFunction),
						new Named<DoubleUnaryOperator>("Log(n)/n", logNOverN))/* functions plotted */);
		plot.setIsLegendVisible(true);
		plot.setTitle("Star discrepancy of Van der Corput sequences of length n vs log(n)/n");
		plot.setXAxisLabel("length");
		plot.setYAxisLabel("Star discrepancy");
		plot.show();
	}

	/**
	 * It plots the discrepancy of Van der Corput sequences of increasing length n
	 * and base base
	 *
	 * @param maxSequenceLength, the length of the longest sequence whose value is
	 *                           plotted
	 * @param base,              the base of the sequences
	 */
	public void plotVanDerCorputDiscrepancy(int maxSequenceLength, int base) throws Exception {
		/*
		 * a function mapping the length of a Van der Corput sequence of the given base
		 * to the discrepancy of the sequence. Note that the function maps doubles to
		 * doubles, so sequenceLength is considered to be a double, and must be
		 * downcasted when we call the getVanDerCorputStarDiscrepancy method.
		 */
		DoubleUnaryOperator discrepancyFunction = (sequenceLength) -> {
			// your implementation here
			return 0;
		};

		Plot2D plot = new Plot2D(2 /* min value on the x-axis */, maxSequenceLength, /* max value */
				maxSequenceLength - 2, /* number of plotted points: length 1 and 2 are not considered */
				Arrays.asList(
						new Named<DoubleUnaryOperator>("Star discrepancy", discrepancyFunction))/* function plotted */);
		plot.setTitle("Discrepancy of Van der Corput sequences");
		plot.setXAxisLabel("length");
		plot.setYAxisLabel("Discrepancy");
		plot.show();
	}
}
