package eu.learning.checkelectricity

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import eu.learning.checkelectricity.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var intentFlex: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        title = "CheckElectricity"
        intentFlex = Intent(this, FlexActivity::class.java)
        setContentView(binding.root)

        startActivity(intentFlex)
    }
}