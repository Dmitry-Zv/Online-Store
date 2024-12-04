package com.vc.onlinestore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vc.onlinestore.databinding.SpecialRvItemBinding
import com.vc.onlinestore.domain.model.Product

class SpecialProductsAdapter :
    RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    var onClick: ((Product) -> Unit)? = null

    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgSpecialRvItem)
                tvSpecialProductName.text = product.name
                tvSpecialProductPrice.text = product.price.toString()
            }
            itemView.setOnClickListener {
                onClick?.invoke(product)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        val binding =
            SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialProductsViewHolder(binding)
    }

    override fun getItemCount(): Int = listDiffer.currentList.size


    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product = listDiffer.currentList[position]
        holder.bind(product)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, diffCallback)

    fun setProducts(products: List<Product>) {
        listDiffer.submitList(products)
    }
}