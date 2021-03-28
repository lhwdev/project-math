package com.lhwdev.math


fun componentIdentifierFor(dimension: Int): Char = 'i' + dimension


fun Literal(components: DoubleArray): Literal = when(components.size) {
	1 -> Literal.Real(components[0])
	in 2..Int.MAX_VALUE -> Literal.Dimensional(components)
	else -> error("unsupported components size ${components.size}")
}

fun Literal(number: Long): Literal.Integer = Literal.Integer(number)

fun Literal(numerator: Literal, denominator: Literal): Literal =
	Literal.Fraction(numerator, denominator).asReduced()

fun Literal(numerator: Long, denominator: Long): Literal.Fraction {
	val gcd = gcd(numerator, denominator)
	return Literal.Fraction(Literal(numerator / gcd), Literal(denominator / gcd))
}


sealed class Literal : Expression {
	abstract val value: DoubleArray
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitLiteral(this, data)
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {}
	
	override fun MathScope.evaluate(extraCondition: Set<Condition>): MathValue? = this@Literal // literal: as-is
	
	
	data class Real(val number: Double) : Literal() {
		override val value: DoubleArray get() = doubleArrayOf(number)
		
		override fun SerializeScope.serialize() {
			number(number)
		}
	}
	
	data class Integer(val number: Long) : Literal() {
		override val value: DoubleArray get() = doubleArrayOf(number.toDouble())
		
		override fun SerializeScope.serialize() {
			number(number)
		}
	}
	
	data class Dimensional(val components: List<Literal>) : Literal() {
		init {
			fun isDimensional(literal: Literal): Boolean = when(literal) {
				is Dimensional -> true
				is Fraction -> isDimensional(literal.numerator) || isDimensional(literal.denominator)
				else -> false
			}
			components.forEach {
				if(isDimensional(it)) error("dimensional literal in components: $it")
			}
		}
		
		override val value: DoubleArray get() = DoubleArray(components.size) { components[it].realValue }
		
		override fun SerializeScope.serialize() {
			for(i in components.indices) {
				if(i != 0) operator("+")
				entity(components[i])
				if(i != 0) {
					operator("*")
					identifier(componentIdentifierFor(i - 1) + "")
				}
			}
		}
		
		override fun equals(other: Any?): Boolean = when {
			this === other -> false
			other !is Dimensional -> false
			else -> components.contentEquals(other.components)
		}
		
		override fun hashCode(): Int = components.contentHashCode()
	}
	
	data class Fraction(val numerator: Literal, val denominator: Literal) : Literal() {
		override val value: DoubleArray get() =
		val realValue: Double get() = numerator.realValue / denominator.realValue
		
		override fun SerializeScope.serialize() {
		}
	}
}


val Literal.realValue: Double
	get() = when(this) {
		is Literal.Real -> number
		is Literal.Integer -> number.toDouble()
		is Literal.Fraction ->
	}

fun Literal.asInteger(): Long? = when(this) {
	is Literal.Real, is Literal.Dimensional -> null
	is Literal.Integer -> number
	is Literal.Fraction -> when {
		numerator !is Literal.Integer || denominator !is Literal.Integer -> null
		numerator.number % denominator.number != 0L -> null
		else -> numerator.number / denominator.number
	}
}

fun Literal.contentEquals(other: Literal): Boolean? = when(this) {
	is Literal.Real, is Literal.Dimensional -> null
	is Literal.Integer -> this.number == other.asInteger()
	is Literal.Fraction -> when {
		numerator !is Literal.Integer || denominator !is Literal.Integer -> null
		other is Literal.Real || other is Literal.Dimensional -> null
		
	}
}


fun Literal.Fraction.asReduced(): Literal = when {
	numerator is Literal.Integer && denominator is Literal.Integer -> {
		val gcd = gcd(numerator.number, denominator.number)
		if(gcd == 1L) this
		else Literal(numerator.number / gcd, denominator.number / gcd)
	}
	numerator.contentEquals(denominator) -> Literal(1)
	else -> this // TODO: fraction / number, etc.
}




