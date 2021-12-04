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

        Log.d("bomb", sharedPreferences.getString("bombNumber", "-1").toString())

        binding.btStartGame.setOnClickListener {
            val tableSize = sharedPreferences.getString("tableSize","-1")!!.toInt()
            val bombNumber =sharedPreferences.getString("bombNumber","-1")!!.toInt()
            if(bombNumber > (tableSize*tableSize) || bombNumber<0){
                binding.tvError.text = "Number of bombs should not be set higher than number of cells"
                binding.tvError.visibility = View.VISIBLE
            }
            else{
                showProgressDialog()
                startActivity(Intent(this, GameActivity::class.java))
            }


        }
        binding.btRanking.setOnClickListener {
            startActivity(Intent(this,RankingActivity::class.java))
        }
        binding.btSettings.setOnClickListener{
            startActivity(Intent(this,SettingsActivity::class.java))

        }



       // val database =  GameResultDatabase.getDatabase(applicationContext)
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