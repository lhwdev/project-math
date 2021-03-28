package com.lhwdev.math


interface MathEntity {
	fun SerializeScope.serialize()
	fun <D, R> accept(visitor: MathVisitor<D, R>, data: D): R
	fun <D, R> acceptChildren(visitor: MathVisitor<D, R>, data: D)
}
