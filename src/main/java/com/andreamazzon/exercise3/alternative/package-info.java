/**
 * In this package we show an alternative implementation of the interface
 * com.andreamazzon.exercise3.montecarloevaluations.MonteCarloEvaluationsInterface.
 * Indeed, in the class MonteCarloEvaluationsAbstract we make getComputations()
 * abstract, and we implement it in the sub-classes. In this method we generate
 * the Monte-Carlo computations and we directly return them, without filling a
 * field as in com.andreamazzon.exercise3.montecarloevaluations. In this way,
 * checking that the such Monte-Carlo computations are generated only once is
 * much more difficult, and actually we don't care/we don't want. For example,
 * in this way if the same object calls twice getAverageComputations() difefrent
 * results might be obtained. This is just another way to proceed.
 */
package com.andreamazzon.exercise3.alternative;