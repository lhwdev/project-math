package com.lhwdev.math


interface PolyExpression : Expression


interface WrappingPolyExpression : PolyExpression {
	val wrappedExpression: Expression
}
