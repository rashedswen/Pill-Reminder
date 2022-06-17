package com.kwemrj.pillreminder.ui.med_date

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kwemrj.pillreminder.data.local.DrugDao
import com.kwemrj.pillreminder.data.model.DrugForSelectedDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    val drugDao: DrugDao
) : ViewModel() {

    val allDrugList: LiveData<List<DrugForSelectedDate>> = drugDao.getDrugsWithList().asLiveData()

}