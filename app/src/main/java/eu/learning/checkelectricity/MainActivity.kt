package eu.learning.checkelectricity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import eu.learning.checkelectricity.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.combine
import org.jsoup.Jsoup
import java.io.IOException


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "CheckElectricity"
        binding.btnFlexActivity.setOnClickListener {
            val intent = Intent(this, FlexActivity::class.java)
            startActivity(intent)
        }
        binding.btnComboActivity.setOnClickListener {
            val intent = Intent(this, ComboActivity::class.java)
            startActivity(intent)
        }
        binding.btnPoolActivity.setOnClickListener {
            val intent = Intent(this, PoolActivity::class.java)
            startActivity(intent)
        }
    }
}