package com.vc.onlinestore.presentation.shopping.homescreen.categories.maincategory

sealed class MainCategoryEvent {
    data object SpecialProducts : MainCategoryEvent()
    data object BestDealsProducts : MainCategoryEvent()
    data object BestProducts : MainCategoryEvent()
}