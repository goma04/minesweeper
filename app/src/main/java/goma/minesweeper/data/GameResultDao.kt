package goma.minesweeper.data

import androidx.room.*

@Dao
interface GameResultDao{
    @Query("SELECT * FROM gameResult")
    fun getAll(): List<GameResult>

    @Insert
    fun insert(gameResult: GameResult):Long

    @Update
    fun update(gameResult: GameResult)

    @Delete
    fun deleteItem(gameResult: GameResult)
}