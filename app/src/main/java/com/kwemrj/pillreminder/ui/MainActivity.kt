package com.kwemrj.pillreminder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kwemrj.pillreminder.R
import com.kwemrj.pillreminder.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.fabAddMedicineReminder.setOnClickListener {
            navController.navigate(R.id.addMedicineName)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment, R.id.medicineForSelectetDateFragment, R.id.profileFragment, R.id.statusFragment -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fabAddMedicineReminder.visibility = View.VISIBLE
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fabAddMedicineReminder.visibility = View.GONE
                }
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

val appBarConfiguration = AppBarConfiguration.Builder(
    R.id.medicineForSelectetDateFragment,
    R.id.settingsFragment,
    R.id.profileFragment,
    R.id.statusFragment,
).build()