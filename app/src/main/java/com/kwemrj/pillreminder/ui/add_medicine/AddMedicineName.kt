package com.kwemrj.pillreminder.ui.add_medicine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kwemrj.pillreminder.R
import com.kwemrj.pillreminder.data.model.MedicineForm
import com.kwemrj.pillreminder.databinding.FragmentAddMedicineNameBinding


class AddMedicineName : Fragment() {

    lateinit var binding: FragmentAddMedicineNameBinding
    private val viewModel: AddMedicineViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMedicineNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.next.setOnClickListener {
            submitNameAndForm()
        }
    }

    private fun submitNameAndForm(){
        if(!binding.medicineName.editText?.text.isNullOrEmpty()){
            val form = when(binding.medicineFormGroup.checkedRadioButtonId){
                R.id.tablet -> MedicineForm.Tablet
                R.id.capsule -> MedicineForm.Capsule
                R.id.drops -> MedicineForm.Drops
                R.id.cream -> MedicineForm.Cream
                R.id.solution -> MedicineForm.Solution
                R.id.injection -> MedicineForm.Injection
                else -> MedicineForm.Inhalation
            }
            viewModel.addMedicineName(binding.medicineName.editText?.text.toString(), form)
            val action = AddMedicineNameDirections.actionAddMedicineNameToAddMedicineDetailsFragment2()
            findNavController().navigate(action)
        }

    }

}