package goma.minesweeper.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.preference.PreferenceManager
import goma.minesweeper.model.MinesweeperModel
import kotlin.math.min
import goma.minesweeper.R

class TableView : View {

    private val paintBg = Paint()
    private val paintLine = Paint()
    private val paintEmpty = Paint()
    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private var tableSize: Int = sharedPreferences.getString("tableSize", "-1")!!.toInt()
    private var canvas: Canvas? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paintBg.color = resources.getColor(goma.minesweeper.R.color.tableBackgroundColor)
        paintBg.style = Paint.Style.FILL

        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5F

        paintEmpty.color = Color.GREEN
        paintLine.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintBg)

        drawGameArea(canvas)
        drawCells(canvas)
    }

    private fun drawGameArea(canvas: Canvas) {
        val widthFloat: Float = width.toFloat()
        val heightFloat: Float = height.toFloat()

        for (i in 0 until tableSize + 1) {
            canvas.drawLine(
                0F,
                i * heightFloat / tableSize,
                widthFloat,
                i * widthFloat / tableSize,
                paintLine
            )

            canvas.drawLine(
                i * widthFloat / tableSize,
                0F,
                i * widthFloat / tableSize,
                heightFloat,
                paintLine
            )
        }

    }


    private fun drawCells(canvas: Canvas) {
        var flag = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.monkey
        )
        val cellSize = width / tableSize

        flag = Bitmap.createScaledBitmap(flag, cellSize, cellSize, true);

        for (i in 0 until tableSize) {
            for (j in 0 until tableSize) {
                val current = MinesweeperModel.getCellContent(i, j)
                val x = (i * width / tableSize).toFloat()
                val y = (j * height / tableSize).toFloat()

                when(current){
                    MinesweeperModel.OPENED ->{
                        canvas.drawRect(x+paintLine.strokeWidth/2,y+paintLine.strokeWidth/2, x+cellSize.toFloat()-paintLine.strokeWidth/2, y+cellSize.toFloat()-paintLine.strokeWidth/2,paintEmpty)
                    }
                    MinesweeperModel.FLAG ->{
                        canvas.drawBitmap(flag, x, y, null)
                    }
                }
            }
        }
    }

    //Azért kell, hogy négyzet legyen a tábla
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)

        val d: Int = when {
            w == 0 -> {
                h
            }
            h == 0 -> {
                w
            }
            else -> {
                min(w, h)
            }
        }

        setMeasuredDimension(d, d)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val tX: Int = (event.x / (width / tableSize)).toInt()
                val tY: Int = (event.y / (height / tableSize)).toInt()
                val current = MinesweeperModel.getCellContent(tX, tY)
                if (current != MinesweeperModel.FLAG) {
                    MinesweeperModel.setCell(tX, tY, false, true)
                    invalidate()
                }

                true
            }

            else -> super.onTouchEvent(event)
        }
    }

}