package com.xavier.calculator.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xavier.calculator.R
import com.xavier.calculator.api.*
import com.xavier.calculator.api.Compute.COMPUTE_EXCEPTION_MESSAGE
import com.xavier.calculator.db.ComputeDao
import com.xavier.calculator.db.ComputeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// TODO Formula processing.
// TODO Outcome processing.
// TODO MVI 改造

class CalculatorViewModel(
    private val computeDao: ComputeDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initFormula: String = savedStateHandle[KEY_FORMULA] ?: ""
    private val initOutcome: String = savedStateHandle[KEY_OUTCOME] ?: ""

    private val _formula = MutableStateFlow(initFormula)
    val formula: StateFlow<String> get() = _formula

    private val _outcome = MutableStateFlow(initOutcome)
    val outcome: StateFlow<String> get() = _outcome
    /*.map {
        Compute.format(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = initOutcome
    )*/

    private val _completion = MutableStateFlow(false)
    val completion: StateFlow<Boolean> get() = _completion

    val history = computeDao.history()

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            computeDao.clear()
        }
    }

    fun onEntry(text: String) {
        if (_completion.value) {
            if (text == EQUAL_TOKEN) return
            if (text in OPERATOR_TOKEN_ARRAY || text == DELETE_TOKEN) {
                _formula.value = _outcome.value
                _outcome.value = ""
                _completion.value = false
            } else {
                clear()
            }
        }
        when (text) {
            in KEY_IN -> keyIn(text)
            in ONLY_ACTION -> action(text)
            else -> {}
        }
    }

    fun onItemClicked(entity: ComputeEntity) {
        _formula.value = entity.formula
        _outcome.value = entity.outcome
        _completion.value = false
    }

    private fun keyIn(text: String) {
        var value = _formula.value
        val textIsOperator = text in OPERATOR_TOKEN_ARRAY
        if (value.isNotEmpty() && value.last()
                .toString() in OPERATOR_TOKEN_ARRAY && textIsOperator
        ) {
            // ) ) 丢弃
            // ) 其他 放行 附加
            // 其他 其他 替换
            // 其他 ) 放行 附加 提示错误
            val last = value.last().toString()
            if (last == RIGHT_TOKEN && text == RIGHT_TOKEN) return
            if (last != RIGHT_TOKEN && text != RIGHT_TOKEN) {
                value = value.replaceRange(value.lastIndex, value.length, text)
                _formula.value = value
                return
            }
        }
        if (!textIsOperator) {
            val split = value.split(
                LEFT_TOKEN, RIGHT_TOKEN, DIVIDE_TOKEN, MULTIPLY_TOKEN, MINUS_TOKEN, PLUS_TOKEN
            )
            if (split.isNotEmpty() && split.last().length >= 16) {
                R.string.maximum_number_digits_message.toast()
                return
            }
        }
        _formula.value = value + text
        autoCalculate()
    }

    private fun action(text: String) {
        when (text) {
            AC_TOKEN -> clear()
            DELETE_TOKEN -> delete()
            EQUAL_TOKEN -> confirmFormula()
            else -> {}
        }
    }

    private fun confirmFormula() {
        calculate()
        if (_outcome.value == COMPUTE_EXCEPTION_MESSAGE) return
        val value = _formula.value
        _formula.value = ""
        _completion.value = true
        insert(value, _outcome.value)
    }

    private fun insert(formulaValue: String, outcomeValue: String) {
        viewModelScope.launch {
            computeDao.insert(
                ComputeEntity(
                    formula = formulaValue,
                    outcome = outcomeValue,
                )
            )
            computeDao.deleteOld()
        }
    }

    private fun clear() {
        _formula.value = ""
        _outcome.value = ""
        _completion.value = false
    }

    private fun delete() {
        val value = _formula.value
        if (value.isNotEmpty()) {
            _formula.value = value.removeSuffix(value.last().toString())
            autoCalculate()
        }
    }

    private fun autoCalculate() {
        val value = _formula.value
        if (value.isEmpty()) return
        val last = value.last().toString()
        if (
            (value.contains(DIVIDE_TOKEN) || value.contains(MULTIPLY_TOKEN) || value.contains(
                MINUS_TOKEN
            ) || value.contains(PLUS_TOKEN))
            && (last == RIGHT_TOKEN || last !in OPERATOR_TOKEN_ARRAY)
        ) {
            calculate()
        }
    }

    private fun calculate() {
        val calculate = Compute.calculate(_formula.value)
        _outcome.value = calculate
    }

    override fun onCleared() {
        savedStateHandle[KEY_FORMULA] = _formula.value
        savedStateHandle[KEY_OUTCOME] = _outcome.value
        super.onCleared()
    }
}

private const val KEY_FORMULA = "formula"
private const val KEY_OUTCOME = "outcome"