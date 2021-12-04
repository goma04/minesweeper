package goma.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import goma.minesweeper.adapter.ResultAdapter
import goma.minesweeper.data.GameResult
import goma.minesweeper.data.GameResultDatabase
import goma.minesweeper.databinding.ActivityRankingBinding
import kotlin.concurrent.thread

class RankingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding
    private lateinit var database: GameResultDatabase
    private lateinit var adapter: ResultAdapter
    private lateinit var items: MutableList<GameResult>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database  = GameResultDatabase.getDatabase(applicationContext)
        adapter = ResultAdapter()
        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter

        loadItemsInBackground()

        binding.btnSortBomb.setOnClickListener {

            adapter.update(items.sortedByDescending { it.numberOfBombs })

        }

        binding.btnSortTime.setOnClickListener{
            adapter.update(items.sortedBy { it.time })
        }


    }

    private fun loadItemsInBackground() {
        thread{
            items = database.gameResultDao().getAll()
            adapter.update(items)
        }
    }


}