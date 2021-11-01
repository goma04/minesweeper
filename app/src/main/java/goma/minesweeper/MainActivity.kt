package goma.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import goma.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btStartGame.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        binding.btRanking.setOnClickListener {
            startActivity(Intent(this,RankingActivity::class.java))
        }
        binding.btSettings.setOnClickListener{
            startActivity(Intent(this,SettingsActivity::class.java))

        }
    }
}