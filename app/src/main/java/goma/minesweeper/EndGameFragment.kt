package goma.minesweeper

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import android.R
import android.content.Context
import android.util.Log
import goma.minesweeper.data.GameResult
import goma.minesweeper.data.GameResultDatabase
import goma.minesweeper.databinding.FragmentEndGameBinding
import kotlin.concurrent.thread


class EndGameFragment : DialogFragment() {


    interface DialogClosed{
        fun onDialogClosed()
    }

    private lateinit var binding: FragmentEndGameBinding
    private lateinit var listenerOnClosed: DialogClosed

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    public fun setCloseListener(listenerOnClosed: DialogClosed){
        this.listenerOnClosed = listenerOnClosed
    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentEndGameBinding.inflate(LayoutInflater.from(context))

        val bundle = arguments

        binding.tvPlayer.text = bundle?.getString("Player")
        binding.tvBombs.text = bundle?.getInt("NumberOfBombs").toString()
        binding.tvTime.text = bundle?.getInt("Time").toString()
        binding.tvSummary.text = bundle?.getString("Summary")

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton("ok") { _, _ ->
                run {
                    listenerOnClosed.onDialogClosed()
                }
            }
            .create()


    }


    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}