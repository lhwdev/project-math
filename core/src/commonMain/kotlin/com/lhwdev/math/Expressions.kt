package com.lhwdev.math


abstract class UnaryOperatorExpression : Expression {
	abstract val value: Expression
	
	override fun SerializeScope.serialize() {
		entity(value)
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitExpression(this, data)
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		value.accept(visitor, data)
	}
}

data class Minus(override val value: Expression) : UnaryOperatorExpression() {
	override fun MathScope.evaluate(extraCondition: Set<Condition>): MathValue? = when(value) {
		is Literal -> value.map {}
	}
}


abstract class BinaryOperatorExpression : Expression {
	abstract val lhs: Expression
	abstract val rhs: Expression
	abstract val operator: String
	
	override fun SerializeScope.serialize() {
		entity(lhs)
		operator(operator)
		entity(rhs)
	}
	
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		lhs.accept(visitor, data)
		rhs.accept(visitor, data)
	}
}

data class Add(override val lhs: Expression, override val rhs: Expression) : BinaryOperatorExpression() {
	override val operator: String get() = "+"
}

data class Sub(override val lhs: Expression, override val rhs: Expression) : BinaryOperatorExpression() {
	override val operator: String get() = "-"
}

data class Mul(override val lhs: Expression, override val rhs: Expression) : BinaryOperatorExpression() {
	override val operator: String get() = "*"
}

data class Div(override val lhs: Expression, override val rhs: Expression) : BinaryOperatorExpression() {
	override val operator: String get() = "/"
}

data class Exponentiation(val base: Expression, val exponent: Expression) : BinaryOperatorExpression() {
	override val lhs: Expression get() = base
	override val rhs: Expression get() = exponent
	override val operator: String get() = "^"
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitExponentiation(this, data)
}

data class Root(val base: Expression, val degree: Expression) : Expression {
	override fun SerializeScope.serialize() {
		identifier("root")
		if(degree !is Literal || !degree.isReal(2.0)) {
			identifier("_")
			groupedEntity(degree)
		}
		entity(base)
	}
}


/**
 * `numerator / denominator`
 */
data class Fraction(
	val numerator: Expression, val denominator: Expression,
	val type: Type = Type.fraction
) : Expression {
	enum class Type { fraction, ratio }
	
	override fun SerializeScope.serialize() {
		entity(numerator)
		val operator = when(type) {
			Type.fraction -> "/"
			Type.ratio -> ":"
		}
		operator(operator)
		entity(denominator)
	}
}

fun Expression.asFraction(): Fraction? = when(this) {
	is Fraction -> this
	// is Mul && // TODO: constant * fraction
	else -> null
}

fun Expression.toFraction(): Fraction = asFraction() ?: when(this) {
	// TODO: solve common denominator if possible
	else -> Fraction(this, Literal.Real(1))
}


data class GetValue(val symbol: MathSymbol) : Expression {
	override fun SerializeScope.serialize() {
		entity(symbol)
	}
}

