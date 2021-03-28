package com.lhwdev.math


interface MathVisitor<D, R> {
	fun visitEntity(entity: MathEntity, data: D): R
	
	fun visitValue(value: MathValue, data: D): R = visitEntity(value, data)
	
	fun visitExpression(expression: Expression, data: D): R = visitValue(expression, data)
	fun visitExponentiation(expression: Exponentiation, data: D): R = visitExpression(expression, data)
	fun visitTerm(expression: Term, data: D): R = visitExpression(expression, data)
	fun visitPolynomial(expression: Polynomial, data: D): R = visitExpression(expression, data)
	fun visitLiteral(expression: Literal, data: D): R = visitExpression(expression, data)
	
	fun visitPolyExpression(expression: PolyExpression, data: D): R = visitExpression(expression, data)
	fun visitLimit(expression: Limit, data: D): R = visitPolyExpression(expression, data)
	
	fun visitCondition(condition: Condition, data: D): R = visitExpression(condition, data)
	fun visitApproach(condition: Approach, data: D): R = visitCondition(condition, data)
	
	fun visitStatement(statement: Statement, data: D): R = visitValue(statement, data)
}


// interface MathTransformer<D> : MathVisitor<D, MathEntity> {
// 	override fun visitEntity(entity: MathEntity, data: D): MathEntity =
// 		entity.also { it.transformChildren(this, data) }
//
// 	override fun visitValue(value: MathValue, data: D): MathValue =
// 		value.also { it.transformChildren(this, data) }
//
// 	override fun visitExpression(expression: Expression, data: D): Expression =
// 		visitValue(expression, data) as Expression
//
// 	override fun visitApproach(condition: Approach, data: D): Approach =
// 		visitCondition(condition, data) as Approach
//
// 	override fun visitCondition(condition: Condition, data: D): Condition =
// 		visitExpression(condition, data) as Condition
//
// 	override fun visitStatement(statement: Statement, data: D): Statement =
// 		visitValue(statement, data) as Statement
// }
