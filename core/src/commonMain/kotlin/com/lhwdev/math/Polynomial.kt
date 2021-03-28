package com.lhwdev.math


data class Term(val coefficient: Literal, val values: List<Exponentiation>) : Expression {
	override fun SerializeScope.serialize() {
		entity(coefficient)
		for(value in values) {
			operator("*")
			entity(value)
		}
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitTerm(this, data)
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		coefficient.accept(visitor, data)
		values.forEach { it.accept(visitor, data) }
	}
}

data class Polynomial(val terms: List<Term>) : Expression {
	override fun SerializeScope.serialize() {
		terms.forEachIndexed { i, term ->
			if(i != 0) operator("+")
			entity(term)
		}
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitPolynomial(this, data)
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		terms.forEach { it.accept(visitor, data) }
	}
}
