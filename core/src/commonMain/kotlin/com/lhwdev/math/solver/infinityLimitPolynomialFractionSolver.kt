package com.lhwdev.math.solver

import com.lhwdev.math.*


fun solveInfinityLimitPolynomialFraction(limit: Limit, extraCondition: Set<Condition>): Expression? {
	val condition = limit.condition as? Approach.Convergence ?: return null
	val fraction = limit.value.asFraction() ?: return null
	fraction.numerator
}
