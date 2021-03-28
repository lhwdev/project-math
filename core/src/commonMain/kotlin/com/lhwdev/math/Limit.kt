package com.lhwdev.math


sealed class Infinity : MathEntity {
	object Positive : Infinity() {
		override fun SerializeScope.serialize() {
			identifier("Infinity")
		}
	}
	
	object Negative : Infinity() {
		override fun SerializeScope.serialize() {
			operator("-")
			identifier("Infinity")
		}
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitEntity(this, data)
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {}
}


/**
 * [Approach] is what used to describe the state of variables in limit.
 *
 * This class can be described as: `target -> to`.
 *
 * @see Limit
 */
sealed class Approach : Condition {
	abstract val target: Expression
	abstract val to: MathEntity
	
	
	data class Convergence(override val target: Expression, override val to: Expression) : Approach()
	data class Divergence(override val target: Expression, override val to: Infinity) : Approach()
	
	override fun SerializeScope.serialize() {
		entity(target)
		operator("->")
		entity(to)
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R =
		visitor.visitApproach(this, data)
	
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		target.accept(visitor, data)
		to.accept(visitor, data)
	}
	
	override fun MathScope.evaluate(extraCondition: Set<Condition>): MathValue? = this@Approach
}


data class Limit(val condition: Approach, val value: Expression) : WrappingPolyExpression {
	override val wrappedExpression: Expression get() = value
	
	/**
	 * `lim_ (condition) (value)`
	 */
	override fun SerializeScope.serialize() {
		operator("lim")
		operator("_")
		groupedEntity(condition)
		groupedEntity(value)
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R =
		visitor.visitLimit(this, data)
	
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		condition.accept(visitor, data)
		value.accept(visitor, data)
	}
	
	override fun MathScope.evaluate(extraCondition: Set<Condition>): MathValue? {
	}
}
