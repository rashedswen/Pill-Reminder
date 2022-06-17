//package com.kwemrj.pillreminder.ui.add_medicine
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.kwemrj.pillreminder.databinding.MedicineListItemBinding
//import com.kwemrj.pillreminder.databinding.TimeChipBinding
//
//class MedicineTimeAdapter : ListAdapter<String , MedicineTimeAdapter.MedicineTimeViewHolder>(DiffCallBack) {
//
//    class MedicineTimeViewHolder(private var binding: TimeChipBinding): RecyclerView.ViewHolder(binding.root){
//
//        fun bind(time: String){
//            binding.time.text = time
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineTimeViewHolder {
//        return MedicineTimeViewHolder(
//            TimeChipBinding.inflate(
//                LayoutInflater.from(parent.context)
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: MedicineTimeViewHolder, position: Int) {
//        val current = getItem(position)
//
//    }
//
//    companion object{
//        val DiffCallBack = object : DiffUtil.ItemCallback<String>(){
//            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
//                return oldItem == newItem
//            }
//
//        }
//    }
//
//}
//
