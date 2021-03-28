package com.lhwdev.math


interface Condition : Expression

interface InlineCondition : PolyExpression, Condition

object True : Condition {
	override fun SerializeScope.serialize() {
		identifier("true")
	}
}

object False : Condition {
	override fun SerializeScope.serialize() {
		identifier("false")
	}
}


data class Equation(val lhs: Expression, val rhs: Expression) : Condition {
	override fun SerializeScope.serialize() {
		entity(lhs)
		operator("=")
		entity(rhs)
	}
}

data class Inequality(val lhs: Expression, val rhs: Expression, val operator: Operator) : Condition {
	enum class Operator(val text: String) {
		notEqual("!="), lt("<"), gt(">"), ltOrEqual("<="), gtOrEqual(">=")
	}
	
	override fun SerializeScope.serialize() {
		entity(lhs)
		operator(operator.text)
		entity(rhs)
	}
}


data class All(val value: Expression) : InlineCondition {
	override fun SerializeScope.serialize() {
		identifier("all")
		entity(value)
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitExpression(this, data)
	
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		value.accept(visitor, data)
	}
}

data class Exist(val value: Expression) : InlineCondition {
	override fun SerializeScope.serialize() {
		identifier("exist")
		entity(value)
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitExpression(this, data)
	
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		value.accept(visitor, data)
	}
}

data class ExistOne(val value: Expression) : InlineCondition {
	override fun SerializeScope.serialize() {
		identifier("existOne")
		entity(value)
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitExpression(this, data)
	
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		value.accept(visitor, data)
	}
}

