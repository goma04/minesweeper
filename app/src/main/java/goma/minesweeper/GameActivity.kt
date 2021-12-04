package goma.minesweeper

import android.content.SharedPreferences
import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import goma.minesweeper.data.GameResult
import goma.minesweeper.data.GameResultDatabase
import goma.minesweeper.databinding.ActivityGameBinding
import goma.minesweeper.model.MinesweeperModel
import goma.minesweeper.view.TableView
import java.util.*
import kotlin.concurrent.thread


class GameActivity : AppCompatActivity(), TableView.GameEndedListener,
    TableView.BombNumberChangedListener,
    EndGameFragment.DialogClosed {
    private lateinit var binding: ActivityGameBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var numberOfBombs = 0
    private var t: Timer = Timer()
    private var seconds = 0

    private lateinit var database: GameResultDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tableView.setListeners(this, this)

        database = GameResultDatabase.getDatabase(applicationContext)

        Log.d("1", "inflate után");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        numberOfBombs = sharedPreferences.getString("bombNumber", "-1")!!.toInt()
        val tableSize = sharedPreferences.getString("tableSize", "-1")!!.toInt()

        binding.tvBombNumber.text = numberOfBombs.toString()

        MinesweeperModel.resetModel(tableSize, numberOfBombs.toInt())

        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                seconds++

                runOnUiThread { binding.tvTime.text = seconds.toString() }
            }
        }, 1000, 1000)
    }

    override fun onGameEnded(won: Boolean) {
        t.cancel()
        t.purge()

        if (won) {
            thread {
                database.gameResultDao().insert(
                    GameResult(
                        numberOfBombs = numberOfBombs,
                        player = sharedPreferences.getString("player", "-1"),
                        time = seconds
                    )
                )
                var list = database.gameResultDao().getAll()
                for (item in list)
                    Log.d("list", item.player.toString())
            }
        }
        val summary = if (won) {
            "Game won"
        } else {
            "Game lost"
        }
        val endGameFragment = EndGameFragment()

        val bundle = Bundle()
        bundle.putInt("NumberOfBombs", sharedPreferences.getString("bombNumber", "-1")!!.toInt())
        bundle.putString("Player", sharedPreferences.getString("player", "-1"))
        bundle.putInt("Time", seconds)
        bundle.putString("Summary", summary)
        endGameFragment.arguments = bundle

        endGameFragment.show(supportFragmentManager, "EndGame")
        endGameFragment.setCloseListener(this)
    }

    override fun onBombNumberChanged(i: Int) {
        numberOfBombs = numberOfBombs + i
        binding.tvBombNumber.text = numberOfBombs.toString()
    }

    override fun onDialogClosed() {
        this.finish()
    }
}