package com.kwemrj.pillreminder.data.local

import androidx.room.TypeConverter
import com.kwemrj.pillreminder.data.model.MedicineForm
import com.kwemrj.pillreminder.data.model.Status
import com.kwemrj.pillreminder.data.model.TakeStatus

class Converters {

    @TypeConverter
    fun fromStatus(status: Status) : Int {
        return status.ordinal
    }

    @TypeConverter
    fun toStatus(value: Int) : Status {
        return enumValues<Status>()[value]
    }

    @TypeConverter
    fun fromMedicineForm(medicineForm: MedicineForm) : Int {
        return medicineForm.ordinal
    }

    @TypeConverter
    fun toMedicineForm(value: Int) : MedicineForm {
        return enumValues<MedicineForm>()[value]
    }


    @TypeConverter
    fun fromTakeStatus(takeStatus: TakeStatus) : Int {
        return takeStatus.ordinal
    }

    @TypeConverter
    fun toTakeStatus(value: Int) : TakeStatus {
        return enumValues<TakeStatus>()[value]
    }

}