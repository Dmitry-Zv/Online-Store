package com.vc.onlinestore.presentation.shopping.homescreen.categories

sealed class CategoryEvent {
    data object OfferProducts:CategoryEvent()
    data object BestProducts:CategoryEvent()
}