package com.vc.onlinestore.domain.model

sealed class Category(val category: String) {
    data object Chair : Category("Chair")
    data object Cupboard : Category("Cupboard")
    data object Table : Category("Table")
    data object Accessory : Category("Accessory")
    data object Instrument : Category("Instrument")
    data object Material : Category("Material")
}