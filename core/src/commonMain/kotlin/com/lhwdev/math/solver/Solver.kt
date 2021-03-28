package com.lhwdev.math.solver

import com.lhwdev.math.Condition
import com.lhwdev.math.Expression
import com.lhwdev.math.MathScope


interface Solver {
	fun matches(expression: Expression, extraCondition: Set<Condition>): Boolean
	fun MathScope.solve(expression: Expression, extraCondition: Set<Condition>): Expression?
}
