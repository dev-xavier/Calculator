package com.xavier.calculator.api

// 运算符
enum class Operator(private val value: String, val priority: Int) {
    PLUS(PLUS_TOKEN, LEVEL_LOW), MINUS(MINUS_TOKEN, LEVEL_LOW),
    MULTIPLY(MULTIPLY_TOKEN, LEVEL_MEDIUM), DIVIDE(DIVIDE_TOKEN, LEVEL_MEDIUM),
    LEFT(LEFT_TOKEN, LEVEL_HIGH), RIGHT(RIGHT_TOKEN, LEVEL_HIGH);

    override fun toString(): String {
        return "Operator(value='$value', priority=$priority)"
    }

    companion object {
        // Token To 运算符
        fun create(value: String): Operator {
            return when (value) {
                PLUS_TOKEN -> PLUS
                MINUS_TOKEN -> MINUS
                MULTIPLY_TOKEN -> MULTIPLY
                DIVIDE_TOKEN -> DIVIDE
                LEFT_TOKEN -> LEFT
                RIGHT_TOKEN -> RIGHT
                else -> throw IllegalArgumentException("使用了未支持的操作符类型") // 显式抛出异常
            }
        }
    }
}