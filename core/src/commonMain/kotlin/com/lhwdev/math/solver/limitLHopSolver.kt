package com.lhwdev.math.solver

import com.lhwdev.math.*


object LimitLHopSolver : Solver {
	override fun matches(expression: Expression, extraCondition: Set<Condition>): Boolean = expression is Limit
	override fun MathScope.solve(expression: Expression, extraCondition: Set<Condition>): Expression? {
		if(expression !is Limit) return null
		return solveLimitLHop(expression, extraCondition)
	}
}


/**
 * `lim_ (x -> a) f(x) = lim_ (x -> a) g(x) = 0: lim_(x -> a) (f(x)/g(x)) = lim_(x -> a) (f'(x)/g'(x))`
 */
fun MathScope.solveLimitLHop(limit: Limit, extraCondition: Set<Condition>): Expression? {
	val solved = solvedLimitLHop(limit) ?: return null
	
	return when(limit.condition) {
		is Approach.Convergence ->
			evaluate(solved, extraCondition + Equation(limit.condition.target, limit.condition.to))
		is Approach.Divergence ->
			solveInfinityLimitPolynomialFraction(Limit(limit.condition, solved), extraCondition)
	}
}

fun MathScope.solvedLimitLHop(limit: Limit): Expression? {
	var fraction = limit.value.asFraction() ?: return null
	fraction = solveLimitLHopOnce(fraction, limit.condition) ?: return null
	
	while(true) {
		fraction = solveLimitLHopOnce(fraction, limit.condition) ?: return fraction
	}
}

fun MathScope.solveLimitLHopOnce(fraction: Fraction, condition: Approach): Fraction? {
	val denominator = fraction.denominator
	val numerator = fraction.numerator
	// check if `lim_ (x -> a) f(x) = lim_ (x -> a) g(x) = 0`
	val denominatorValue = evaluateLiteral(Limit(condition = condition, value = denominator)) ?: return null
	if(denominatorValue.isNotZero()) return null
	val numeratorValue = evaluateLiteral(Limit(condition = condition, value = numerator)) ?: return null
	if(numeratorValue.isNotZero()) return null
	
	// derivative
	val denominatorDerivative = derivative(denominator, forSymbol = condition.target)
	val numeratorDerivative = derivative(numerator, forSymbol = condition.target)
	
	return Fraction(numeratorDerivative, denominatorDerivative)
}
