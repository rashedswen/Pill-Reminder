package com.kwemrj.pillreminder.data.model


data class DrugForSelectedDate(
    val pill_id: Int,
    val timestamp_id: Int,
    val drugName: String,
    val numberOfDose: Int,
    val dateTimestamp: Long,
    val status: Status,
    val form: MedicineForm,
    val takeStatus: TakeStatus
)