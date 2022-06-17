package com.kwemrj.pillreminder.ui.med_date

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kwemrj.pillreminder.R
import com.kwemrj.pillreminder.databinding.FragmentMedicineForSelectetDateBinding
import java.text.SimpleDateFormat
import java.util.*

class MedicineForSelectedDateFragment : Fragment() {

    lateinit var binding: FragmentMedicineForSelectetDateBinding
    private val viewModel: MedicineViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    private var twoDaysAgo: Long = 0
    private var startOfDay: Long = 0
    private var endOfDay: Long = 0
    private var oneDay: Long = 24 * 60 * 60 * 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMedicineForSelectetDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetCalendar()
        generateListOfTimes()

    }

    private fun changeDay() {
        val adapter = SelectedDateMedicineAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.allDrugList.observe(viewLifecycleOwner) { drugs ->
            drugs?.let {
                adapter.submitList(drugs.filter { it.dateTimestamp in startOfDay until endOfDay }
                    .sortedBy { it.dateTimestamp })
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun resetCalendar() {
        Log.d("Hello", calendar.timeInMillis.toString())
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)

        Log.d("Hello", calendar.timeInMillis.toString())
        twoDaysAgo = calendar.timeInMillis - (24 * 60 * 60 * 1000 * 2)
    }

    private fun generateListOfTimes() {
        val oneDay: Long = 60 * 60 * 24 * 1000
        var loopDateIncrement = twoDaysAgo
        startOfDay = twoDaysAgo
        endOfDay = twoDaysAgo + oneDay
        for (i in 0..10) {
            val inflater = LayoutInflater.from(this.context).inflate(R.layout.date_radio, null)
            val radioDate = inflater.findViewById<RadioButton>(R.id.date)
            radioDate.apply {
                id = View.generateViewId()
                hint = loopDateIncrement.toString()
                text = SimpleDateFormat("EEE\nd", Locale.getDefault()).format(loopDateIncrement)
                setOnClickListener {
                    calendar.timeInMillis = radioDate.hint.toString().toLong()
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    startOfDay = calendar.timeInMillis
                    calendar.set(Calendar.HOUR_OF_DAY, 23)
                    calendar.set(Calendar.MINUTE, 59)
                    calendar.set(Calendar.SECOND, 59)
                    endOfDay = calendar.timeInMillis
                    changeDay()
                }
            }
            if(i == 2){
                calendar.timeInMillis = radioDate.hint.toString().toLong()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                startOfDay = calendar.timeInMillis
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                endOfDay = calendar.timeInMillis
            }
            binding.monthRadioGroup.addView(radioDate, binding.monthRadioGroup.childCount)
            loopDateIncrement += oneDay
        }
        changeDay()
        binding.monthRadioGroup.check(binding.monthRadioGroup.getChildAt(2).id)
    }

}