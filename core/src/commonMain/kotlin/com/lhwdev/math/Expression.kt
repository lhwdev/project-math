package com.lhwdev.math


interface Expression : MathValue {
	override fun MathScope.evaluate(extraCondition: Set<Condition>): MathValue?
}
