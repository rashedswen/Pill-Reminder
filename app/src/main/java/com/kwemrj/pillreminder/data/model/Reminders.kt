package com.kwemrj.pillreminder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminder", foreignKeys = arrayOf(
        ForeignKey(
            entity = Drug::class,
            parentColumns = ["id"],
            childColumns = ["pill_id"],
            onDelete = CASCADE
        )
    )
)
data class Reminders(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "pill_id", index = true)
    val pillId: Long,
    val timestamp: Long,
    val take: TakeStatus
)
