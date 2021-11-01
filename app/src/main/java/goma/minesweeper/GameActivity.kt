package goma.minesweeper

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceManager
import goma.minesweeper.databinding.ActivityGameBinding
import goma.minesweeper.model.MinesweeperModel
import java.lang.Thread as Thread

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var numberOfBombs = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this)
        numberOfBombs = sharedPreferences.getString("bombNumber", "-1")!!
        val tableSize = sharedPreferences.getString("tableSize", "-1")!!.toInt()

        binding.tvBombNumber.text = numberOfBombs
        MinesweeperModel.resetModel(tableSize, numberOfBombs.toInt())
    }


}