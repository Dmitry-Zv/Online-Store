package com.vc.onlinestore.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vc.onlinestore.databinding.BillingProductsRvItemBinding
import com.vc.onlinestore.domain.model.CartProduct
import com.vc.onlinestore.helper.getProductPrice

class BillingProductsAdapter :
    RecyclerView.Adapter<BillingProductsAdapter.BillingProductsViewHolder>() {

    inner class BillingProductsViewHolder(private val binding: BillingProductsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(billingProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = billingProduct.product.name
                tvBillingProductQuantity.text = billingProduct.quantity.toString()
                val priceAfterPercentage =
                    billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
                tvProductCartPrice.text = "$ ${String.format("%.2f", priceAfterPercentage)}"
                imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        billingProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
                tvCartProductSize.text = billingProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductsViewHolder {
        val binding =
            BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillingProductsViewHolder(binding)
    }

    override fun getItemCount(): Int = listDiffer.currentList.size


    override fun onBindViewHolder(holder: BillingProductsViewHolder, position: Int) {
        val billingProduct = listDiffer.currentList[position]
        holder.bind(billingProduct)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
            oldItem.cartProductId == newItem.cartProductId


        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean =
            oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, diffCallback)

    fun setBillingProducts(billingProducts: List<CartProduct>) {
        listDiffer.submitList(billingProducts)
    }
}