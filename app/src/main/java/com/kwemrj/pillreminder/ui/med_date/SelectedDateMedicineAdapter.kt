package com.kwemrj.pillreminder.ui.med_date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kwemrj.pillreminder.R
import com.kwemrj.pillreminder.data.model.Drug
import com.kwemrj.pillreminder.data.model.DrugForSelectedDate
import com.kwemrj.pillreminder.data.model.MedicineForm
import com.kwemrj.pillreminder.databinding.FragmentMedicineForSelectetDateBinding
import com.kwemrj.pillreminder.databinding.MedicineListItemBinding
import java.text.SimpleDateFormat
import java.util.*

class SelectedDateMedicineAdapter :
    ListAdapter<DrugForSelectedDate, SelectedDateMedicineAdapter.SelectedDateMedicineViewHolder>(
        DiffCallBack
    ) {

    class SelectedDateMedicineViewHolder(private var binding: MedicineListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(drug: DrugForSelectedDate) {
            binding.apply {
                medicineForm.setImageResource(
                    when (drug.form) {
                        MedicineForm.Tablet -> R.drawable.ic_pill_24
                        MedicineForm.Capsule -> R.drawable.ic_capsule_24
                        MedicineForm.Drops -> R.drawable.ic_drop_24
                        MedicineForm.Cream -> R.drawable.ic_cream_skin
                        MedicineForm.Solution -> R.drawable.ic_medicine_syrum_24
                        MedicineForm.Injection -> R.drawable.ic_injection_24
                        MedicineForm.Inhalation -> R.drawable.ic_inhalator_24
                    }
                )
                nameOfMedicine.text = drug.drugName
                numberOfDose.text = drug.numberOfDose.toString()
                medicineTime.text =
                    SimpleDateFormat("h:mm a", Locale.getDefault()).format(drug.dateTimestamp).uppercase()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedDateMedicineViewHolder {
        return SelectedDateMedicineViewHolder(
            MedicineListItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: SelectedDateMedicineViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<DrugForSelectedDate>() {
            override fun areItemsTheSame(
                oldItem: DrugForSelectedDate,
                newItem: DrugForSelectedDate
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DrugForSelectedDate,
                newItem: DrugForSelectedDate
            ): Boolean {
                return oldItem.pill_id == newItem.pill_id
            }

        }
    }

}