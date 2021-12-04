package goma.minesweeper

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import goma.minesweeper.data.GameResultDatabase
import goma.minesweeper.databinding.ActivityMainBinding
import java.lang.Double.parseDouble

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var progressDialog: ProgressDialog? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PreferenceManager.setDefaultValues(this, R.xml.preferencescreen, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        binding.btStartGame.setOnClickListener {
            configureStartButton()
        }
        binding.btRanking.setOnClickListener {
            startActivity(Intent(this, RankingActivity::class.java))
        }
        binding.btSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun configureStartButton() {
        val tableSize = sharedPreferences.getString("tableSize", "-1")!!.toInt()
        val bomNumberString = sharedPreferences.getString("bombNumber", "-1")

        var bombNumber: Int = 0

        //Bombaszám ellenőrzése hogy szám-e
        var numeric = true
        try {
            val num = parseDouble(bomNumberString?:"_")
            bombNumber = bomNumberString!!.toInt()
        } catch (e: NumberFormatException) {
            numeric = false
        }

        if (bombNumber > (tableSize * tableSize) || bombNumber < 0 || !numeric) {
            binding.tvError.text = "Number of bombs should not be set higher than number of cells and should be a numeric value!"
            binding.tvError.visibility = View.VISIBLE
        }
        else{
            showProgressDialog()
            startActivity(Intent(this, GameActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        hideProgressDialog()
        binding.tvError.visibility = View.GONE
    }

    private fun hideProgressDialog() {
        progressDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        progressDialog = null
    }

    private fun showProgressDialog() {
        if (progressDialog != null) {
            return
        }

        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage("Preparing the game...")
            show()
        }
    }


}