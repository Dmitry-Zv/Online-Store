package com.vc.onlinestore.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vc.onlinestore.R
import com.vc.onlinestore.data.firebase.dto.order.Order
import com.vc.onlinestore.data.firebase.dto.order.OrderStatus
import com.vc.onlinestore.data.firebase.dto.order.getOrderStatus
import com.vc.onlinestore.databinding.OrderItemBinding

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrdersAdapterViewHolder>() {

    var onClick: ((Order) -> Unit)? = null

    inner class OrdersAdapterViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val colorDrawable = when (getOrderStatus(order.orderStatus)) {
                    OrderStatus.Canceled -> {
                        ColorDrawable(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.g_red
                            )
                        )
                    }

                    OrderStatus.Confirmed -> {
                        ColorDrawable(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.g_green
                            )
                        )
                    }

                    OrderStatus.Delivered -> {
                        ColorDrawable(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.g_green
                            )
                        )
                    }

                    OrderStatus.Ordered -> {
                        ColorDrawable(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.g_orange_yellow
                            )
                        )
                    }

                    OrderStatus.Returned -> {
                        ColorDrawable(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.g_red
                            )
                        )
                    }

                    OrderStatus.Shipped -> {
                        ColorDrawable(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.g_green
                            )
                        )
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }
            itemView.setOnClickListener {
                onClick?.invoke(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapterViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int =
        listDiffer.currentList.size

    override fun onBindViewHolder(holder: OrdersAdapterViewHolder, position: Int) {
        val order = listDiffer.currentList[position]
        holder.bind(order)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem.products == newItem.products

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
    }

    private val listDiffer = AsyncListDiffer(this, diffCallback)

    fun setOrders(orders: List<Order>) {
        listDiffer.submitList(orders)
    }
}