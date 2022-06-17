package com.kwemrj.pillreminder.ui.add_medicine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwemrj.pillreminder.data.local.DrugDao
import com.kwemrj.pillreminder.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddMedicineViewModel @Inject constructor(
    val drugDao: DrugDao
) : ViewModel() {


    private var medicineName: String = ""
    private var medicineForm: MedicineForm = MedicineForm.Inhalation

    // 3 variables for the date start and end
    private val _medicineStartDate = MutableLiveData<Long>(null)
    private val _medicineEndDate = MutableLiveData<Long>(null)
    private val _medicineStartTime = MutableLiveData<Long>(0)

    private val _numberOfDosagePerTime = MutableLiveData<Int>(0)
    private var _timeInterval: Long = 0
    private var drugId: Long = 0

    val medicineStartDate: LiveData<Long> get() = _medicineStartDate
    val medicineEndDate: LiveData<Long> get() = _medicineEndDate
    val medicineStartTime: LiveData<Long> get() = _medicineStartTime
    val numberOfDosagePerTime: LiveData<Int> get() = _numberOfDosagePerTime


    //get the medicine (start and end date and start time in Long) and number of dosage per time from fragment
    fun createMedicineWithIntervalTime(
        medicineStartTime: Long,
        numberOfDosagePerTime: Int,
        timeInterval: Long,
    ) {
        this._medicineStartTime.value = medicineStartTime
        this._numberOfDosagePerTime.value = numberOfDosagePerTime
        _timeInterval = timeInterval
        createDrug()
    }

    fun createMedicineWithSpecificTimes(
        medicineStartTime: List<Long>,
        numberOfDosagePerTime: Int,
    ) {
        this._numberOfDosagePerTime.value = numberOfDosagePerTime
        createDrugSpecific(medicineStartTime)
    }

    private fun createDrugSpecific(list: List<Long>) {

        val drug = Drug(
            name = medicineName,
            form = medicineForm,
            duration = differenceInDays().toInt(),
            status = Status.Active,
            isNotificationOn = true,
            numberOfDoes = generateMedicineWithSpecificTimes(list).size,
            numberOfDoesPerTime = numberOfDosagePerTime.value!!,
            startDate = medicineStartDate.value!!,
            finishDate = medicineEndDate.value!!
        )
        viewModelScope.launch {
            val listOfReminders = mutableListOf<Reminders>()
            drugId = drugDao.insertDrug(drug)
            generateMedicineWithSpecificTimes(list).forEach { time ->
                listOfReminders.add(
                    Reminders(
                        pillId = drugId,
                        timestamp = time,
                        take = TakeStatus.Pending
                    )
                )
            }
            drugDao.insertReminders(listOfReminders)
            Log.d("Hello", drugId.toString())
        }
    }

    private fun generateMedicineWithSpecificTimes(list: List<Long>): MutableList<Long> {
        Log.d("Hello", "time: ${medicineStartTime.value} \n date: ${medicineStartDate.value} ")
        val listOfTimes = mutableListOf<Long>()

        // هنا عملت list عشان اخزن فيها القيم حقت ال list الأصلية
        val mutableListOfTimes = mutableListOf<Long>()
        mutableListOfTimes.addAll(list)
        val time24IntervalInMilleseconds = 24 * 60 * 60 * 1000
        for (i in 0..differenceInDays()) {
            repeat(list.size) { j ->
                if (i <= 0) {
                    mutableListOfTimes[j] = list[j]
                } else {
                    mutableListOfTimes[j] = time24IntervalInMilleseconds + mutableListOfTimes[j]
                }
/*                } else {
//                    j += timeIntervalInMilleseconds
//                    if (startTime <= (medicineEndDate.value!! + 60 * 60 * 24 * 1000)) {
//                        listOfTimes.add(
//                            startTime
//                        )
//                    }
//                }*/
            }
            listOfTimes.addAll(mutableListOfTimes)
        }

        return listOfTimes
    }

    private fun createDrug() {

        val drug = Drug(
            name = medicineName,
            form = medicineForm,
            duration = differenceInDays().toInt(),
            status = Status.Active,
            isNotificationOn = true,
            numberOfDoes = generateMedicineDates().size,
            numberOfDoesPerTime = numberOfDosagePerTime.value!!,
            startDate = medicineStartDate.value!!,
            finishDate = medicineEndDate.value!!
        )
        viewModelScope.launch {
            val listOfReminders = mutableListOf<Reminders>()
            drugId = drugDao.insertDrug(drug)
            generateMedicineDates().forEach { time ->
                listOfReminders.add(
                    Reminders(
                        pillId = drugId,
                        timestamp = time,
                        take = TakeStatus.Pending
                    )
                )
            }
            drugDao.insertReminders(listOfReminders)
            Log.d("Hello", drugId.toString())
            resetValues()
        }
    }

    private fun resetValues() {
        _medicineStartDate.value = 0
        _medicineEndDate.value = 0
        _medicineStartTime.value = 0
        _numberOfDosagePerTime.value = 0
        _timeInterval = 0
        drugId = 0
    }


    fun setMedicineStartDate(startDate: Long) {
        _medicineStartDate.value = startDate
    }

    fun setMedicineEndDate(endDate: Long) {
        _medicineEndDate.value = endDate
    }

    fun setMedicineStartTime(time: Long) {
        _medicineStartTime.value = time
    }

    private fun generateMedicineDates(): MutableList<Long> {
        Log.d("Hello", "time: ${medicineStartTime.value} \n date: ${medicineStartDate.value} ")
        val listOfTimes = mutableListOf<Long>()
        val calendar = Calendar.getInstance()
        var startTime: Long = medicineStartTime.value!!
        val numberOfTimesInDay = 24 / _timeInterval
        listOfTimes.add(
            startTime
        )
        val timeIntervalInMilleseconds = _timeInterval * 60 * 60 * 1000
        for (i in 0 until differenceInDays()) {
            for (j in 0..numberOfTimesInDay) {
                startTime += timeIntervalInMilleseconds
                if (startTime <= (medicineEndDate.value!! + 60 * 60 * 24 * 1000)) {
                    listOfTimes.add(
                        startTime
                    )
                }
            }
        }


        return listOfTimes
    }

//
//    private fun generateDatesBetweenDates() {
//val datesBetweenDates = mutableListOf<Long>()
//        val startDate = Date(medicineStartDate)
//        val endDate = Date(medicineEndDate)
//        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val startDateString = sdf.format(startDate)
//        val endDateString = sdf.format(endDate)
//        val startDateCalendar = Calendar.getInstance()
//        val endDateCalendar = Calendar.getInstance()
//        startDateCalendar.time = startDate
//        endDateCalendar.time = endDate
//        while (startDateCalendar.before(endDateCalendar)) {
//            datesBetweenDates.add(startDateCalendar.timeInMillis)
//            startDateCalendar.add(Calendar.DATE, 1)
//        }
//        datesBetweenDates.add(endDateCalendar.timeInMillis)
//        datesBetweenDates.forEach {
//            val drug = Drug(
//                medicineName,
//                medicineForm,
//                it,
//                medicineStartTime,
//                numberOfDosagePerTime
//            )
//            drugDao.insert(drug)
//        }
//    }


    fun addMedicineName(medicineN: String, medicineF: MedicineForm) {
        this.medicineName = medicineN
        this.medicineForm = medicineF
    }

    // function to get difference in days between two timestamps
    private fun differenceInDays(): Long {
        val diff = medicineEndDate.value!! - medicineStartDate.value!!
        return diff / (24 * 60 * 60 * 1000) + 1
    }


}