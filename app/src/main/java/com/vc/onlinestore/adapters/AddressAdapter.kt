package com.vc.onlinestore.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vc.onlinestore.R
import com.vc.onlinestore.data.firebase.dto.Address
import com.vc.onlinestore.databinding.AddressRvItemBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    var onClick: ((Address) -> Unit)? = null
    var selectedAddress = -1

    inner class AddressViewHolder(private val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, isSelected: Boolean, position: Int) {
            binding.apply {
                buttonAddress.text = address.addressTitle
                if (isSelected) {
                    buttonAddress.background =
                        ColorDrawable(ContextCompat.getColor(itemView.context, R.color.g_blue))
                } else {
                    buttonAddress.background =
                        ColorDrawable(ContextCompat.getColor(itemView.context, R.color.g_white))
                }

                buttonAddress.setOnClickListener {
                    if (selectedAddress >= 0) {
                        notifyItemChanged(0)
                    }
                    selectedAddress = position
                    notifyItemChanged(selectedAddress)
                    onClick?.invoke(address)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun getItemCount(): Int = listDiffer.currentList.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = listDiffer.currentList[position]
        holder.bind(address, selectedAddress == position, position)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean =
            oldItem.addressTitle == newItem.addressTitle


        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean =
            oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, diffCallback)

    init {
        listDiffer.addListListener{_,_->
            notifyItemChanged(selectedAddress)
        }
    }

    fun setAddress(addresses: List<Address>) {
        listDiffer.submitList(addresses)
    }
}