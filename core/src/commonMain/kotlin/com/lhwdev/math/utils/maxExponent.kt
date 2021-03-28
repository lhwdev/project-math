package com.lhwdev.math.utils

import com.lhwdev.math.*


private object MaxExponentResolver : MathVisitor<Unit, List<Expression>> {
	override fun visitEntity(entity: MathEntity, data: Unit): List<Expression> = emptyList()
	
	override fun visitExponentiation(expression: Exponentiation, data: Unit): List<Expression> {
	
	}
	
	override fun visitLiteral(expression: Literal, data: Unit): List<Expression> = emptyList()
	
	override fun visitCondition(condition: Condition, data: Unit): List<Expression> {
		return super.visitCondition(condition, data)
	}
	
	override fun visitApproach(condition: Approach, data: Unit): List<Expression> {
		return super.visitApproach(condition, data)
	}
	
	override fun visitStatement(statement: Statement, data: Unit): List<Expression> {
		return super.visitStatement(statement, data)
	}
}


fun Expression.resolveMaxExponent(): List<Expression> = accept(MaxExponentResolver, Unit)
