package goma.minesweeper

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import goma.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btStartGame.setOnClickListener {
            showProgressDialog()
            startActivity(Intent(this, GameActivity::class.java))

        }
        binding.btRanking.setOnClickListener {
            startActivity(Intent(this,RankingActivity::class.java))
        }
        binding.btSettings.setOnClickListener{
            startActivity(Intent(this,SettingsActivity::class.java))

        }
    }

    override fun onResume() {
        super.onResume()
        hideProgressDialog()
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