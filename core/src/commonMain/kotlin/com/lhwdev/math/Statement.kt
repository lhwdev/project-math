package com.lhwdev.math


data class Statement(val condition: Condition) : MathValue {
	override fun SerializeScope.serialize() {
		entity(condition)
	}
	
	override fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R = visitor.visitStatement(this, data)
	override fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D) {
		condition.accept(visitor, data)
	}
	
	override fun MathScope.evaluate(extraCondition: Set<Condition>): MathValue? =
		(evaluate(condition, extraCondition) as? Condition)?.let { Statement(it) }
}
