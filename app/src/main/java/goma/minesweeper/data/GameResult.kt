package goma.minesweeper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gameResult")
data class GameResult(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "player") var player: String?,
    @ColumnInfo(name = "time") var time: Int?,
    @ColumnInfo(name = "numberOfBombs") var numberOfBombs: Int?
)
