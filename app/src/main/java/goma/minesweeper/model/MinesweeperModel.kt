package goma.minesweeper.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import goma.minesweeper.SettingsActivity

object MinesweeperModel{
    private lateinit var model: Array<Array<Cell?>>

    const val OPENED: Byte = 0
    const val FLAG: Byte = 1
    const val BOMB: Byte = 2

    fun resetModel(tableSize: Int) {
        model = Array(tableSize) {
            Array(tableSize) {
                Cell(bomb = true, flag = false)
            }
        }
    }

    fun getCellContent(x: Int, y: Int): Byte {
       if(model[x][y]?.flag == true)
           return FLAG
        else if(model[x][y]?.opened == true){
            return OPENED
       }else
           return BOMB
    }

    fun setCell(x: Int, y: Int, flag: Boolean, opened: Boolean) {
        model[x][y]!!.opened = opened
        model[x][y]!!.flag = flag
    }

}