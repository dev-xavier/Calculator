package com.xavier.calculator.api

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

object Compute {

    const val COMPUTE_EXCEPTION_MESSAGE = "错误"

    fun calculate(formula: String): String = try {
        val list = readToList(formula)
        val queue = convertToSuffix(list)
        // BigDecimal(calculateFromSuffix(queue)).stripTrailingZeros().toString()
        val value = calculateFromSuffix(queue).toString()
        val bigDecimal = BigDecimal(value)
        val double = value.toDouble()
        if (double <= 1000_0000_0000_0000) { // 不使用科学计数法
            // bigDecimal.setScale(10, ROUND_DOWN).stripTrailingZeros().toPlainString()
            bigDecimal.stripTrailingZeros().toPlainString()
        } else { // 使用科学计数法
            // bigDecimal.setScale(10, ROUND_DOWN).stripTrailingZeros().toString()
            bigDecimal.stripTrailingZeros().toString()
        }
    } catch (e: Exception) {
        COMPUTE_EXCEPTION_MESSAGE
    }

    // 将中缀表达式读入集合
    private fun readToList(formula: String): List<Any> {
        val mutableList = mutableListOf<Any>()
        var begin = 0
        for (index in formula.indices) {
            val char = formula[index]
            if (OPERATOR_TOKEN_ARRAY.contains(char.toString())) { // 是运算符
                val substring = formula.substring(begin, index) // 数值
                if (substring.isNotEmpty()) {
                    mutableList.add(substring.toDouble()) // 有可能抛出 NumberFormatException
                }
                mutableList.add(Operator.create(char.toString())) // 运算符
                begin = index + 1
            }
        }
        // 最后一个数值
        val substring = formula.substring(begin)
        if (substring.isNotEmpty()) {
            mutableList.add(substring.toDouble()) // 有可能抛出 NumberFormatException
        }
        return mutableList
    }

    // 调度场算法 将表示中缀表达式的集合转换为表示后缀表达式的队列
    private fun convertToSuffix(list: List<Any>): ArrayDeque<Any> {
        val operatorStack: Stack<Operator> = Stack()
        val arrayDeque = ArrayDeque<Any>()
        for (any in list) {
            when (any) {
                is Double -> arrayDeque.offerLast(any) // 数值进入队列
                is Operator -> {
                    when (any) {
                        Operator.LEFT -> operatorStack.push(any) // 左括号直接入栈
                        Operator.RIGHT -> {
                            // 右括号 持续弹出运算符 直到遇到左括号
                            while (operatorStack.peek() != Operator.LEFT) {
                                arrayDeque.offerLast(operatorStack.pop()) // 弹出运算符进入队列
                            }
                            operatorStack.pop() // 弹出左括号
                        }
                        else -> {
                            // 栈不为空 && 栈顶不是左括号 && 当前运算符优先级小于等于栈顶运算符优先级
                            while (
                                !operatorStack.isEmpty()
                                && operatorStack.peek() != Operator.LEFT
                                && any.priority <= operatorStack.peek().priority
                            ) {
                                arrayDeque.offerLast(operatorStack.pop()) // 弹出运算符进入队列
                            }
                            operatorStack.push(any) // 不满足上述条件 将运算符压入栈内
                        }
                    }
                }
                else -> throw IllegalArgumentException("使用了未支持的操作类型") // 显式抛出异常
            }
        }
        // 将剩余的运算符全部弹出
        while (!operatorStack.isEmpty()) {
            arrayDeque.offerLast(operatorStack.pop())
        }
        return arrayDeque
    }

    // 对表示后缀表达式的队列进行计算
    private fun calculateFromSuffix(expressionQueue: Queue<Any>): Double {
        val calculateStack: Stack<Double> = Stack()
        var any: Any?
        while (expressionQueue.poll().also { any = it } != null) {
            when (any) {
                is Double -> calculateStack.push(any as Double)
                is Operator -> {
                    val number1 = calculateStack.pop()
                    val number2 = calculateStack.pop()
                    calculateStack.push(computeNode(number1, number2, any as Operator))
                }
                else -> throw IllegalArgumentException("使用了未支持的操作类型") // 显式抛出异常
            }
        }
        // 弹出运算结果
        return calculateStack.pop()
    }

    // 运算节点
    private fun computeNode(
        number1: Double, number2: Double, operator: Operator
    ): Double {
        return when (operator) {
            Operator.PLUS -> /*number2.plus(number1)*/ number2 + number1
            Operator.MINUS -> /*number2.minus(number1)*/ number2 - number1
            Operator.MULTIPLY -> /*number2.times(number1)*/ number2 * number1
            Operator.DIVIDE -> /*number2.div(number1)*/ number2 / number1
            else -> throw IllegalArgumentException("使用了未支持的操作符类型")
        }
    }

    fun format(value: String): String {
        if (value.isEmpty()) return ""
        return try {
            val bigDecimal = BigDecimal(value)
            val double = value.toDouble()
            if (double <= 1000_0000_0000_0000) { // 不使用科学计数法
                // bigDecimal.setScale(10, ROUND_DOWN).stripTrailingZeros().toPlainString()
                bigDecimal.stripTrailingZeros().toPlainString()
            } else { // 使用科学计数法
                // bigDecimal.setScale(10, ROUND_DOWN).stripTrailingZeros().toString()
                bigDecimal.stripTrailingZeros().toString()
            }
        } catch (e: Exception) {
            // TODO almost never execute
            COMPUTE_EXCEPTION_MESSAGE
        }
    }
}

fun main() {
    val number = 3.141592653589793
    val numberFormat = NumberFormat.getInstance()
    numberFormat.maximumFractionDigits = 10
    numberFormat.isGroupingUsed = true
    println(numberFormat.format(number))

    val diameter = 16
    val formula = "$number*$diameter"
    val split = formula.split("*")
    assert(split.isNotEmpty())
    val first = split.first()
    first.toDouble()
    val showFirst = Compute.format(first)
    val showFormula = formula.replaceRange(0, first.length, showFirst)
    println("$formula -> $showFormula")
}