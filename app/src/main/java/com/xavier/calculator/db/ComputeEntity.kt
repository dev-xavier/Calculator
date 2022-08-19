package com.xavier.calculator.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ComputeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val formula: String = "",
    val outcome: String = ""
) {
    override fun toString(): String {
        return "$formula\n$outcome"
    }

    /*fun findOutcome() = "$EQUAL_TOKEN ${Compute.format(outcome)}"*/
}
