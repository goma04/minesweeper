package goma.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import goma.minesweeper.SettingsActivity

object MinesweeperModel {
    private lateinit var model: Array<Array<Cell?>>

    const val OPENED: Byte = 0
    const val FLAG: Byte = 1
    const val BOMB: Byte = 2
    private const val CLOSED: Byte = 3



     fun checkWin(): Boolean{

        for (row in model){
            for(cell in row)
            {
                //Ha nincs nyitva és nincs benne bomba, még nincs vége
                if(!cell!!.opened && !cell.bomb)
                    return false
            }
        }

        return true
    }

    fun resetModel(tableSize: Int, numberOfBombs: Int) {
        model = Array(tableSize) {
            Array(tableSize) {
                Cell(bomb = false, flag = false, bombsNear = 0)
            }
        }

        var bombCounter = 0
        while (bombCounter != numberOfBombs) {
            val bombI = (model.indices).random()
            val bombJ = (model.indices).random()

            val current = model[bombI][bombJ]
            if (!current!!.bomb) {
                current.bomb = true
                bombCounter++

                for (i in -1 until 2) {
                    for (j in -1 until 2)
                        if (bombI + i >= 0 && bombJ + j >= 0 && bombI + i < model.size && bombJ + j < model.size)
                            model[bombI + i][bombJ + j]!!.bombsNear++
                }
            }
        }
    }

    fun getNumberBombsNear(i: Int, j: Int): Int {
        return model[i][j]!!.bombsNear
    }


    fun getCellContent(x: Int, y: Int): Byte {
        return when {
            model[x][y]?.flag == true -> FLAG
            model[x][y]?.opened == true -> {
                OPENED
            }
            model[x][y]?.bomb == true -> {
                BOMB
            }
            else -> CLOSED
        }
    }



    fun getCell(x: Int, y: Int):Cell?{
        return model[x][y]
    }


fun setCell(x: Int, y: Int, flag: Boolean, opened: Boolean) {
    model[x][y]!!.opened = opened
    model[x][y]!!.flag = flag
}

}