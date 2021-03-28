package com.lhwdev.math


private val sOkayRegex = Regex("[a-zA-Z]")


class MathSymbol(val name: String) : MathEntity {
	override fun SerializeScope.serialize() {
		symbol(name)
	}
}


data class Get(val symbol: MathSymbol) : MathEntity {
	override fun SerializeScope.serialize() {
		entity(symbol)
	}
}
