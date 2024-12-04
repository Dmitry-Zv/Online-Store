package com.vc.onlinestore.presentation.common

interface Event<E> {

    fun onEvent(event: E)
}