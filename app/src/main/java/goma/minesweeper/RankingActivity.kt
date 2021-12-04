package goma.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import goma.minesweeper.adapter.ResultAdapter
import goma.minesweeper.data.GameResultDatabase
import goma.minesweeper.databinding.ActivityRankingBinding
import kotlin.concurrent.thread

class RankingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding
    private lateinit var database: GameResultDatabase
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database  = GameResultDatabase.getDatabase(applicationContext)
        adapter = ResultAdapter()
        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter
        loadItemsInBackground()


    }

    private fun loadItemsInBackground() {
        thread{
            val items = database.gameResultDao().getAll()
            adapter.update(items)
        }
    }


}