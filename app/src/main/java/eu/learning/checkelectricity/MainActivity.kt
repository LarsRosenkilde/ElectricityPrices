package eu.learning.checkelectricity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.learning.checkelectricity.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "CheckElectricity"
        val intentFlex = Intent(this, FlexActivity::class.java)
        val intentCombo = Intent(this, ComboActivity::class.java)
        val intentPool = Intent(this, PoolActivity::class.java)
        binding.flexButton.setOnClickListener {
            startActivity(intentFlex)
        }
        binding.comboButton.setOnClickListener {
            startActivity(intentCombo)
        }
        binding.poolButton.setOnClickListener {
            startActivity(intentPool)
        }
    }
}