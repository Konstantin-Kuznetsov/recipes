package com.example.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.core.AppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // SafeArgs and Jetpack Navigation is a little bit complicated thing with multi-module projects when
    // we want to implement navigation between feature modules with arguments. Also navigation is more
    // suitable in ViewModel. There are some workarounds about this but lets keep is simple in test task and
    // implement it as is.
    override fun toDetails(recipeId: String) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_recipesList_to_recipeDetails,
            Bundle().apply { putString("RECIPE_ID", recipeId) }
        )
    }
}