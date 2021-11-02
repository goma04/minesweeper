package goma.minesweeper

import android.app.ProgressDialog
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import goma.minesweeper.databinding.ActivityGameBinding
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.core.graphics.createBitmap
import androidx.preference.PreferenceManager
import goma.minesweeper.model.MinesweeperModel


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