package goma.minesweeper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goma.minesweeper.data.GameResult
import goma.minesweeper.databinding.ItemResultListBinding

class ResultAdapter():RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(val binding: ItemResultListBinding): RecyclerView.ViewHolder(binding.root)

    private val items = mutableListOf<GameResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ResultViewHolder(
       ItemResultListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ResultAdapter.ResultViewHolder, position: Int) {
       val gameResult = items[position]

        holder.binding.tvBombNumber.text = gameResult.numberOfBombs.toString()
        holder.binding.tvName.text = gameResult.player
        holder.binding.tvTime.text = gameResult.time.toString()

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(gameResults: List<GameResult>){
        items.clear()
        items.addAll(gameResults)
        notifyDataSetChanged()
    }
}