package com.vc.onlinestore.presentation.shopping.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vc.onlinestore.domain.usecases.network.GetProductsByName
import com.vc.onlinestore.domain.usecases.network.GetToken
import com.vc.onlinestore.presentation.shopping.homescreen.categories.ProductsCategoryState
import com.vc.onlinestore.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsByName: GetProductsByName,
    private val getToken: GetToken
) : ViewModel() {

    private val _state = MutableStateFlow(ProductsCategoryState())
    val state = _state.asStateFlow()

    fun search(name: String) {
        viewModelScope.launch {
            _state.value = ProductsCategoryState(
                product = null,
                isLoading = true,
                errorMessage = null
            )
            val token = getToken()
            when (val result = getProductsByName(name, token)) {
                is Resource.Error -> {
                    _state.value = ProductsCategoryState(
                        product = null,
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Success -> {
                    _state.value = ProductsCategoryState(
                        product = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
        _state.value = ProductsCategoryState()
    }
}