package com.vc.onlinestore.presentation.shopping.homescreen.categories.maincategory

sealed class MainCategoryEvent {
    data object GetAllProducts : MainCategoryEvent()
}