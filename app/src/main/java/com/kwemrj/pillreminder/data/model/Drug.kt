package com.kwemrj.pillreminder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Drug")
data class Drug(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    val name: String,
    val form: MedicineForm,
    val duration: Int,
    val status: Status = Status.Active,
    val isNotificationOn: Boolean = true,
    val numberOfDoseTaken: Int = 0,
    val numberOfDoes: Int,
    val numberOfDoesPerTime: Int,
    val startDate: Long,
    val finishDate: Long,
)