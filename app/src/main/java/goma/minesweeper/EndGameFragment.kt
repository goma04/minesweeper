package goma.minesweeper

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import goma.minesweeper.databinding.FragmentEndGameBinding


class EndGameFragment : DialogFragment() {


    interface DialogClosed{
        fun onDialogClosed()
    }

    private lateinit var binding: FragmentEndGameBinding
    private lateinit var listenerOnClosed: DialogClosed



    fun setCloseListener(listenerOnClosed: DialogClosed){
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



}