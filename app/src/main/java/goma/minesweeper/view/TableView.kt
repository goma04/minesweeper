package goma.minesweeper.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.preference.PreferenceManager
import goma.minesweeper.model.MinesweeperModel
import kotlin.math.min
import goma.minesweeper.R
import goma.minesweeper.model.Cell

class TableView : View {

    private val paintBg = Paint()
    private val paintLine = Paint()
    private val paintEmpty = Paint()
    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private var tableSize: Int = sharedPreferences.getString("tableSize", "-1")!!.toInt()
    private var canvas: Canvas? = null
    private lateinit var flag: Bitmap
    private lateinit var bomb: Bitmap
    private var numberImages: ArrayList<Bitmap> = ArrayList()

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

    private var firstDraw: Boolean = true
    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintBg)

        //Betölti a képeket a memóriába
        if(firstDraw){
            flag = createBitmap(context.resources, R.drawable.monkey)
            bomb = createBitmap(context.resources, R.drawable.bomb)
            numberImages.add(createBitmap(context.resources, R.drawable.number1))
            numberImages.add(createBitmap(context.resources, R.drawable.number2))
            numberImages.add(createBitmap(context.resources, R.drawable.number3))
            numberImages.add(createBitmap(context.resources, R.drawable.number4))
            numberImages.add(createBitmap(context.resources, R.drawable.number5))
            numberImages.add(createBitmap(context.resources, R.drawable.number6))
            numberImages.add(createBitmap(context.resources, R.drawable.number7))
            numberImages.add(createBitmap(context.resources, R.drawable.number8))
            numberImages.add(createBitmap(context.resources, R.drawable.number9))
            firstDraw = false
        }

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

    private fun createBitmap(res: Resources, id: Int): Bitmap {

        val cellSize = width / tableSize
        return Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(
                res,
                id
            ), cellSize, cellSize, true
        )
    }


    private fun drawCells(canvas: Canvas) {

        /*var flag = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.monkey
        )*/
        val cellSize = width / tableSize


        //flag = Bitmap.createScaledBitmap(flag, cellSize, cellSize, true);



        //A cellákhoz kirajzolja a megfelelő képet/felületet
        for (i in 0 until tableSize) {
            for (j in 0 until tableSize) {
                val current = MinesweeperModel.getCellContent(i, j)
                val x = (i * width / tableSize).toFloat()
                val y = (j * height / tableSize).toFloat()

                when (current) {
                    MinesweeperModel.OPENED -> {
                        canvas.drawRect(
                            x + paintLine.strokeWidth / 2,
                            y + paintLine.strokeWidth / 2,
                            x + cellSize.toFloat() - paintLine.strokeWidth / 2,
                            y + cellSize.toFloat() - paintLine.strokeWidth / 2,
                            paintEmpty
                        )
                        drawNumber(MinesweeperModel.getNumberBombsNear(i, j), x, y)
                    }
                    MinesweeperModel.FLAG -> canvas.drawBitmap(flag, x, y, null)

                    MinesweeperModel.BOMB -> canvas.drawBitmap(bomb, x, y, null)
                }
            }
        }
    }

    private fun drawNumber(bombsNumber: Int, x: Float, y: Float) {
        var numberImg: Bitmap? = null

        when (bombsNumber) {
            1 -> numberImg = numberImages[0]
            2 -> numberImg = numberImages[1]
            3 -> numberImg = numberImages[2]
            4 -> numberImg = numberImages[3]
            5 -> numberImg = numberImages[4]
            6 -> numberImg = numberImages[5]
            7 -> numberImg = numberImages[6]
            8 -> numberImg = numberImages[7]
            9 -> numberImg = numberImages[8]
        }

        numberImg?.let { canvas?.drawBitmap(it, x, y, null) }
    }

    //Azért kell, hogy négyzet legyen a tábla
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
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