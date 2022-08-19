package com.xavier.calculator.api

const val LOGIC_INT_INIT_VALUE = -1

// 符号
const val AC_TOKEN = "AC"
const val LEFT_TOKEN = "("
const val RIGHT_TOKEN = ")"
const val DIVIDE_TOKEN = "/"
const val MULTIPLY_TOKEN = "*"
const val MINUS_TOKEN = "-"
const val PLUS_TOKEN = "+"
const val EQUAL_TOKEN = "＝"
const val DELETE_TOKEN = "←"
const val POINT_TOKEN = "."

// 数字
const val ZERO = "0"
const val ONE = "1"
const val TWO = "2"
const val THREE = "3"
const val FOUR = "4"
const val FIVE = "5"
const val SIX = "6"
const val SEVEN = "7"
const val EIGHT = "8"
const val NINE = "9"

const val LEVEL_LOW = 10
const val LEVEL_MEDIUM = 100
const val LEVEL_HIGH = 1000

// 字典
val KEYBOARD_DICT = arrayOf(
    arrayOf(AC_TOKEN, LEFT_TOKEN, RIGHT_TOKEN, DIVIDE_TOKEN),
    arrayOf(SEVEN, EIGHT, NINE, MULTIPLY_TOKEN),
    arrayOf(FOUR, FIVE, SIX, MINUS_TOKEN),
    arrayOf(ONE, TWO, THREE, PLUS_TOKEN),
    arrayOf(POINT_TOKEN, ZERO, DELETE_TOKEN, EQUAL_TOKEN)
)

// 数字区
val DIGITAL_AREA = arrayOf(
    SEVEN, EIGHT, NINE,
    FOUR, FIVE, SIX,
    ONE, TWO, THREE,
    POINT_TOKEN, ZERO, DELETE_TOKEN
)

// 符号区
val SYMBOL_AREA = arrayOf(
    AC_TOKEN, LEFT_TOKEN, RIGHT_TOKEN, DIVIDE_TOKEN,
    MULTIPLY_TOKEN, MINUS_TOKEN, PLUS_TOKEN,
    POINT_TOKEN, DELETE_TOKEN
)

// 运算符 Token 数组
val OPERATOR_TOKEN_ARRAY = arrayOf(
    LEFT_TOKEN, RIGHT_TOKEN, DIVIDE_TOKEN,
    MULTIPLY_TOKEN, MINUS_TOKEN, PLUS_TOKEN
)

val KEY_IN = arrayOf(
    LEFT_TOKEN, RIGHT_TOKEN, DIVIDE_TOKEN,
    SEVEN, EIGHT, NINE, MULTIPLY_TOKEN,
    FOUR, FIVE, SIX, MINUS_TOKEN,
    ONE, TWO, THREE, PLUS_TOKEN,
    POINT_TOKEN, ZERO
)

val ONLY_ACTION = arrayOf(
    AC_TOKEN, DELETE_TOKEN, EQUAL_TOKEN
)

