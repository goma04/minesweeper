package goma.minesweeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameResult::class], version = 1)
abstract class GameResultDatabase : RoomDatabase(){
    abstract fun gameResultDao(): GameResultDao

    companion object {
        fun getDatabase(applicationContext: Context): GameResultDatabase {
            return Room.databaseBuilder(
                applicationContext,
                GameResultDatabase::class.java,
                "results"
            ).build();
        }
    }
}