package com.vc.onlinestore.presentation.shopping

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vc.onlinestore.R
import com.vc.onlinestore.databinding.ActivityShoppingBinding
import com.vc.onlinestore.presentation.shopping.cartscreen.CartViewModel
import com.vc.onlinestore.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private var _binding: ActivityShoppingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShoppingShareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        collectCartProductSate()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun collectCartProductSate() {
        collectLatestLifecycleFlow(viewModel.cartProducts) { cartProducts ->
            val count = cartProducts.size
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                number = count
                backgroundColor = ContextCompat.getColor(this@ShoppingActivity, R.color.g_blue)
            }
        }
    }
}