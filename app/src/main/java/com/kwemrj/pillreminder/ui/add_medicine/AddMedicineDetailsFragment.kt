package com.kwemrj.pillreminder.ui.add_medicine

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.kwemrj.pillreminder.R
import com.kwemrj.pillreminder.databinding.FragmentAddMedicineDetailsBinding
import java.text.SimpleDateFormat
import java.util.*


class AddMedicineDetailsFragment : Fragment() {

    lateinit var binding: FragmentAddMedicineDetailsBinding

    private val viewModel: AddMedicineViewModel by activityViewModels()
    private val startDatePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Start Date")
            .setSelection(
                MaterialDatePicker.thisMonthInUtcMilliseconds()
            )

    private var startDate: Long = 0
    private var hour: Int = 0
    private var minute: Int = 0

    private val calendar = Calendar.getInstance()

    private val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .setHour(10)
        .setMinute(0)
        .setTitleText("Select Time")
        .build()

    private var currentChip = -1

    private var isEveryXHour = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_medicine_details,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.medicineViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        resetCalendar()

        viewModel.medicineStartDate.observe(viewLifecycleOwner) {
            binding.startDate.editText?.setText(
                if (it != null && it != 0L) {
                    SimpleDateFormat(
                        "yyyy/MM/dd",
                        Locale.getDefault()
                    ).format(it)
                } else {
                    null
                }
            )
        }

        viewModel.medicineEndDate.observe(viewLifecycleOwner) {
            binding.endDate.editText?.setText(
                if (it != null && it != 0L) {
                    SimpleDateFormat(
                        "yyyy/MM/dd",
                        Locale.getDefault()
                    ).format(it)
                } else {
                    null
                }
            )
        }

        binding.startDate.editText?.setOnClickListener {
            val startDate = startDatePicker
                .setTitleText("Start Date")
                .build()
            startDate.show(childFragmentManager, "DATE_PICKER_Start")
            startDate.addOnPositiveButtonClickListener { selection ->
                Log.d("Hello", selection.toString())
                Log.d("Hello", (selection * 1000).toString())
                viewModel.setMedicineStartDate(selection)
                this.startDate = selection
                binding.startDate.isErrorEnabled = false
            }
        }

        binding.endDate.editText?.setOnClickListener {
            val endDate = startDatePicker.setTitleText("End date")
                .build()
            endDate.show(childFragmentManager, "DATE_PICKER_End")
            endDate.addOnPositiveButtonClickListener { selection ->
                viewModel.setMedicineEndDate(selection)
                binding.endDate.isErrorEnabled = false
            }
        }

        binding.addTime.setOnClickListener {
            timePicker.show(childFragmentManager, "tag")
            currentChip = -1
        }

        timePicker.addOnPositiveButtonClickListener {
            // get time in milliseconds
            val time = (timePicker.hour * 60 * 60 * 1000) + (timePicker.minute * 60 * 1000)

            // layout inflater for generate new chip
            val inflater = LayoutInflater.from(this.context).inflate(R.layout.time_chip, null)

            // find view and store it in variables
            val timeChipText = inflater.findViewById<Chip>(R.id.time_chip_set)
            val timeChipValue = inflater.findViewById<TextView>(R.id.chip_text_value)
            val linearLayout = inflater.findViewById<LinearLayout>(R.id.time_chip_group)

            // set text for chip
            timeChipText.text = SimpleDateFormat(
                "h:mm aa", Locale.getDefault()
            ).format((calendar.timeInMillis + time))

            // set value to restore the time when needed to submit to viewModel
            timeChipValue.text = "${timePicker.hour}:${timePicker.minute}"
            Log.d("Hello", "${calendar.timeInMillis}")

            // onClick listener for chip to change time
            timeChipText.setOnClickListener { timeIt ->
                Toast.makeText(this.context, "v", Toast.LENGTH_LONG).show()
                currentChip = binding.timeChipsAdd.indexOfChild(timeIt.parent as View)
                Log.d("Hello", "chip count ${timeIt.parent}")

                timePicker.show(childFragmentManager, "hhh")
                timePicker.addOnPositiveButtonClickListener {
                    val specificTimeChipView = binding.timeChipsAdd.getChildAt(currentChip)
                        ?: return@addOnPositiveButtonClickListener
                    val timeChip = specificTimeChipView.findViewById<Chip>(R.id.time_chip_set)
                    val timeChipValueE =
                        specificTimeChipView.findViewById<TextView>(R.id.chip_text_value)
                    hour = timePicker.hour
                    minute = timePicker.minute
                    timeChip.text =
                        SimpleDateFormat(
                            "h:mm aa", Locale.getDefault()
                        ).format((timePicker.hour * 60 * 60 * 1000) + (timePicker.minute * 60 * 1000) + calendar.timeInMillis)
                    timeChipValueE.text = "${timePicker.hour}:${timePicker.minute}"
                }
            }
            if (currentChip < 0) {
                binding.timeChipsAdd.addView(linearLayout, binding.timeChipsAdd.childCount)
            }
            if (isEveryXHour) {
                checkChildCountAndHide()
            }
        }

        binding.frequentTimes.setOnCheckedChangeListener { _, itemChecked ->
            when (itemChecked) {
                R.id.ever_x_times -> {
                    isEveryXHour = true
                    checkChildCountAndHide()
                }
                R.id.specific_hour_of_day -> {
                    isEveryXHour = false
                    binding.addTime.visibility = View.VISIBLE
                    binding.timeChipsAdd.visibility = View.VISIBLE
                    binding.interval.visibility = View.GONE
                }
                R.id.as_needed -> {
                    isEveryXHour = false
                    binding.addTime.visibility = View.GONE
                    binding.timeChipsAdd.visibility = View.GONE
                    binding.interval.visibility = View.GONE
                }
            }
        }

        binding.addMedicine.setOnClickListener {
            if (isValid()) {
                when (binding.frequentTimes.checkedRadioButtonId) {
                    R.id.ever_x_times -> {
                        val v = binding.timeChipsAdd.getChildAt(0)
                        val chip = v.findViewById<TextView>(R.id.chip_text_value)
                        Log.d("Hello", chip.text.toString())

                        val c = chip.text.split(":")
                        Log.d("Hello", c.toString())
                        calendar.timeInMillis = this.startDate
                        calendar.set(Calendar.HOUR_OF_DAY, c[0].toInt())
                        calendar.set(Calendar.MINUTE, c[1].toInt())
                        viewModel.createMedicineWithIntervalTime(
                            calendar.timeInMillis,
                            binding.doesNumber.editText?.text.toString().toInt(),
                            binding.interval.editText?.text.toString().toLong()
                        )
                    }
                    R.id.specific_hour_of_day -> {
                        val listOfTimes = mutableListOf<Long>()
                        binding.timeChipsAdd.children.iterator().forEach {
                            val chipValue = it.findViewById<TextView>(R.id.chip_text_value)
                            val c = chipValue.text.split(":")
                            calendar.timeInMillis = this.startDate
                            calendar.set(Calendar.HOUR_OF_DAY, c[0].toInt())
                            calendar.set(Calendar.MINUTE, c[1].toInt())
                            listOfTimes.add(calendar.timeInMillis)
                        }
                        viewModel.createMedicineWithSpecificTimes(
                            listOfTimes,
                            binding.doesNumber.editText?.text.toString().toInt()
                        )
                    }
                }
                Toast.makeText(context, "Medicine Reminder Added Successfully", Toast.LENGTH_LONG)
                    .show()
                val action =
                    AddMedicineDetailsFragmentDirections.actionAddMedicineDetailsFragment2ToMedicineForSelectetDateFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun checkChildCountAndHide() {
        binding.interval.visibility = View.VISIBLE
        if (binding.timeChipsAdd.childCount > 0) {
            binding.addTime.visibility = View.GONE
            binding.timeChipsAdd.visibility = View.VISIBLE
            Log.d("Hello", binding.timeChipsAdd.childCount.toString())
            if (binding.timeChipsAdd.childCount > 1) {
                for (i in binding.timeChipsAdd.childCount downTo 1) {
                    if (binding.timeChipsAdd.getChildAt(i) != null)
                        binding.timeChipsAdd.removeViewAt(i)
                }
            }
        } else {
            binding.addTime.visibility = View.VISIBLE
        }
    }

    private fun isValid(): Boolean {
        return if (!binding.doesNumber.editText?.text.isNullOrEmpty() && !binding.startDate.editText?.text.isNullOrEmpty() && !binding.endDate.editText?.text.isNullOrEmpty()) {

            when (binding.frequentTimes.checkedRadioButtonId) {
                R.id.ever_x_times -> {
                    if (!binding.interval.editText?.text.isNullOrEmpty()) {
                        binding.interval.isErrorEnabled = false
                    }
                }
            }
            true
        } else {
            if (binding.doesNumber.editText?.text.isNullOrEmpty()) {
                binding.doesNumber.isErrorEnabled = true
                binding.doesNumber.error = "Write the number of dosage"
            }
            if (binding.startDate.editText?.text.isNullOrEmpty()) {
                binding.startDate.isErrorEnabled = true
                binding.startDate.error = "please select start Date"
            }
            if (binding.endDate.editText?.text.isNullOrEmpty()) {
                binding.endDate.isErrorEnabled = true
                binding.endDate.error = "please select start Date"
            }
            when (binding.frequentTimes.checkedRadioButtonId) {
                R.id.ever_x_times -> {
                    if (binding.interval.editText?.text.isNullOrEmpty()) {
                        binding.interval.isErrorEnabled = true
                        binding.interval.error = "please select time between dosage"
                    } else {
                        binding.interval.isErrorEnabled = false
                    }
                }
            }
            false
        }
    }


    private fun resetCalendar() {
        Log.d("Hello", calendar.timeInMillis.toString())
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)

        Log.d("Hello", calendar.timeInMillis.toString())
    }

}