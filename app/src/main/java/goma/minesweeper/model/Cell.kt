package goma.minesweeper.model

data class Cell(
    var flag: Boolean = false,
    var bomb: Boolean = false,
    var opened: Boolean = false,
    var bombsNear: Int,
    var draw: Boolean = false
)

