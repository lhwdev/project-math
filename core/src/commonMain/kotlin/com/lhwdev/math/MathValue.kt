package com.lhwdev.math


interface MathValue : MathEntity {
	fun MathScope.evaluate(extraCondition: Set<Condition>): MathValue?
}
