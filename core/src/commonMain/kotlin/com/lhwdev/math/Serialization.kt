package com.lhwdev.math


interface SerializeScope {
	fun symbol(name: String)
	fun identifier(name: String)
	fun entity(value: MathEntity)
	fun groupedEntity(value: MathEntity)
	fun operator(text: String)
	fun beginGroup(type: String = "(")
	fun endGroup(type: String = ")")
	fun number(number: Double)
	fun number(number: Long)
}

inline fun SerializeScope.group(beginType: String = "(", endType: String = ")", block: () -> Unit) {
	beginGroup(beginType)
	try {
		block()
	} finally {
		endGroup(endType)
	}
}
