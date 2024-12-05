package com.vc.onlinestore.adapters

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.vc.onlinestore.databinding.BestDealsRvItemBinding
import com.vc.onlinestore.domain.model.Product
import com.vc.onlinestore.helper.getProductPrice

class BestDealAdapter : RecyclerView.Adapter<BestDealAdapter.BestDealViewHolder>() {

    var onClick: ((Product) -> Unit)? = null

    inner class BestDealViewHolder(private val binding: BestDealsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                val firstImage = product.images.takeIf { it.isNotEmpty() }?.get(0)
                Glide.with(itemView).setDefaultRequestOptions(
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(10000) // Set timeout to 10 seconds
                ).load(firstImage).error(ColorDrawable(Color.GRAY)).into(imgBestDeal)
                tvDealProductName.text = product.name
                tvOldPrice.text = product.price.toString()
                val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                tvNewPrice.text = "BYN ${String.format("%.2f", priceAfterOffer)}"
                tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                if (product.offerPercentage == null) {
                    tvNewPrice.visibility = View.GONE
                }
            }
            itemView.setOnClickListener {
                onClick?.invoke(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealViewHolder {
        val binding =
            BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestDealViewHolder(binding)
    }

    override fun getItemCount(): Int =
        listDiffer.currentList.size

    override fun onBindViewHolder(holder: BestDealViewHolder, position: Int) {
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