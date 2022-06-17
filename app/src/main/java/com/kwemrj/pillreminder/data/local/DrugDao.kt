package com.kwemrj.pillreminder.data.local

import androidx.room.*
import com.kwemrj.pillreminder.data.model.Drug
import com.kwemrj.pillreminder.data.model.DrugForSelectedDate
import com.kwemrj.pillreminder.data.model.Reminders
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDrug(drug: Drug): Long

    @Delete
    suspend fun deleteDrug(drug: Drug)

    @Update
    suspend fun updateDrug(drug: Drug)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminders(reminders: List<Reminders>)

    @Query(
        "Select drug.id as pill_id , reminder.id as timestamp_id, drug.name as drugName," +
                " drug.numberOfDoesPerTime as numberOfDose, reminder.timestamp as dateTimestamp, " +
                "drug.status as status, drug.form as form, reminder.take as takeStatus " +
                " From drug join reminder where drug.id = reminder.pill_id"
    )
    fun getDrugsWithList(): Flow<List<DrugForSelectedDate>>
}