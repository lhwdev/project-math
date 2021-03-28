package com.lhwdev.math

import com.lhwdev.math.solver.Solver


interface MathScope {
	val parent: MathScope?
	val solvers: List<Solver>
	
	fun findSymbol(name: String): MathSymbol?
	fun hasSymbol(symbol: MathSymbol): Boolean
	fun addSymbol(symbol: MathSymbol)
	fun removeSymbol(symbol: MathSymbol)
	
	fun evaluate(expression: Expression, extraCondition: Set<Condition> = emptySet()): Expression?
	fun evaluateLiteral(expression: Expression, extraCondition: Set<Condition> = emptySet()): Literal?
	fun derivative(expression: Expression, forSymbol: Expression): Expression
}

fun MathScope.evaluate(expression: Expression, vararg extraCondition: Condition): Expression? =
	evaluate(expression, setOf(*extraCondition))

fun MathScope.evaluateLiteral(expression: Expression, vararg extraCondition: Condition): Literal? =
	evaluateLiteral(expression, setOf(*extraCondition))


fun MathScope.symbol(name: String): MathSymbol {
	val symbol = MathSymbol(name)
	addSymbol(symbol)
	return symbol
}


class MathScopeImpl(override val parent: MathScope?) : MathScope {
	private val symbols = mutableListOf<MathSymbol>()
	
	override fun findSymbol(name: String): MathSymbol? = symbols.find { it.name == name }
	
	override fun hasSymbol(symbol: MathSymbol): Boolean = symbol in symbols
	
	override fun addSymbol(symbol: MathSymbol) {
		symbols += symbol
	}
	
	override fun removeSymbol(symbol: MathSymbol) {
		symbols.remove(symbol)
	}
	
	
}


inline fun math(block: MathScope.() -> Unit) {
	MathScopeImpl(parent = null).block()
}
