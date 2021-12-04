package goma.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import goma.minesweeper.SettingsActivity

object MinesweeperModel {
    private lateinit var model: Array<Array<Cell?>>
    private lateinit var sharedPreferences: SharedPreferences

    const val OPENED: Byte = 0
    const val FLAG: Byte = 1
    const val BOMB: Byte = 2
    const val CLOSED: Byte = 3

    fun getTableSize(): Int{
        return model.size
    }

    public fun checkWin(): Boolean{

        for (row in model){
            for(cell in row)
            {
                //Ha nincs nyitva és nincs benne bomba, még nincs vége
                if(!cell!!.opened && !cell!!.bomb)
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
        if (model[x][y]?.flag == true)
            return FLAG
        else if (model[x][y]?.opened == true) {
            return OPENED
        } else if (model[x][y]?.bomb == true) {
            return BOMB
        }
        return CLOSED
    }

    public fun getAdjacentCells(x: Int, y: Int): MutableList<Cell?> {
        val res: MutableList<Cell?> = mutableListOf()
        for (i in -1 until 2) {
            for (j in -1 until 2)
                if (x + i >= 0 && y + j >= 0 && x + i < model.size && y + j < model.size)
                    res.add(model[x+i][y+j])
        }
        res.remove(model[x][y])

        return res
    }

    //TODO remove this
    fun getCell(x: Int, y: Int):Cell?{
        return model[x][y]
    }


fun setCell(x: Int, y: Int, flag: Boolean, opened: Boolean) {
    model[x][y]!!.opened = opened
    model[x][y]!!.flag = flag
}

}