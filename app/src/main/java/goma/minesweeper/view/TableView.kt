package goma.minesweeper.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.preference.PreferenceManager
import goma.minesweeper.GameActivity
import goma.minesweeper.model.MinesweeperModel
import kotlin.math.min
import goma.minesweeper.R
import android.content.ContextWrapper
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.bumptech.glide.Glide
import goma.minesweeper.model.Cell
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class TableView : View {


    private var lastDraw: Boolean = false
    private lateinit var bombNumberChangedListener: TableView.BombNumberChangedListener
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
    private lateinit var gameEndedListener: GameEndedListener

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paintBg.color = resources.getColor(R.color.tableBackgroundColor)
        paintBg.style = Paint.Style.FILL

        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5F

        paintEmpty.color = Color.GREEN
        paintLine.style = Paint.Style.STROKE


        flag = BitmapFactory.decodeResource(resources, R.drawable.monkey)
        bomb = if (tableSize > 12) {
            BitmapFactory.decodeResource(resources, R.drawable.bomb_small)
        } else
            BitmapFactory.decodeResource(resources, R.drawable.bomb)

        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number1));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number2));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number3));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number4));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number5));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number6));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number7));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number8));
        numberImages.add(BitmapFactory.decodeResource(resources, R.drawable.number9));
    }

    private var firstDraw: Boolean = true
    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintBg)

        //Betölti a képeket a memóriába
        if (firstDraw) {
            val cellSize = width / tableSize
            flag = Bitmap.createScaledBitmap(flag, cellSize, cellSize, false);
            bomb = Bitmap.createScaledBitmap(bomb, cellSize, cellSize, false);

            for (i: Int in 0 until numberImages.size) {
                numberImages[i] =
                    Bitmap.createScaledBitmap(numberImages[i], cellSize, cellSize, false);
            }

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

    private fun drawCells(canvas: Canvas) {

        val cellSize = width / tableSize

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


                    MinesweeperModel.BOMB -> {
                        if (lastDraw)
                            canvas.drawBitmap(bomb, x, y, null)
                    }

                }
            }
        }
        val i = 0
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

    private val mScaleFactor = 1.0000000f
    private val LONG_PRESS_TIME = 500.0;
    private var mStartClickTime: Long = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return true;

        val tX: Int = (event.x / (width / tableSize)).toInt()
        val tY: Int = (event.y / (height / tableSize)).toInt()
        return when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                mStartClickTime = Calendar.getInstance().timeInMillis
                true
            }
            MotionEvent.ACTION_UP -> {
                actionUp(tX, tY)
                invalidate()
                true
            }

            else -> super.onTouchEvent(event)
        }
    }

    private fun actionUp(tX: Int, tY: Int) {
        val clickDuration = Calendar.getInstance().timeInMillis - mStartClickTime
        val current = MinesweeperModel.getCellContent(tX, tY)

        Log.d("touch", "$clickDuration");

        if (clickDuration < LONG_PRESS_TIME) {
            Log.d("touch", "short")

            when {
                current == MinesweeperModel.FLAG -> {
                    MinesweeperModel.setCell(tX, tY, false, opened = false)
                    bombNumberChangedListener.onBombNumberChanged(1)
                }
                current != MinesweeperModel.BOMB -> {



                    if (MinesweeperModel.getNumberBombsNear(tX, tY) == 0) {
                        openEmptyNeighbours(tX, tY)
                    }else{
                        MinesweeperModel.setCell(tX, tY, false, opened = true)
                    }

                    if (MinesweeperModel.checkWin())
                        gameEndedListener.onGameEnded(true)
                }
                current == MinesweeperModel.BOMB -> {
                    lastDraw = true
                    invalidate()
                    gameEndedListener.onGameEnded(false)
                }
                else -> {
                    //TODO vége fragment meghívása
                }
            }
        } else {
            Log.d("touch", "long");
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            MinesweeperModel.setCell(tX, tY, flag = true, opened = false)
            bombNumberChangedListener.onBombNumberChanged(-1)
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        20,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else vibrator.vibrate(200)

        }
    }

    private fun openEmptyNeighbours(tX: Int, tY: Int) {
        Log.v("q", "$tX $tY")
        if (tX < 0 || tY < 0 || tX > tableSize - 1 || tY > tableSize-1) {
            Log.d("q", "Returned")
            return
        }
        /*if (MinesweeperModel.getNumberBombsNear(tX, tY) != 0) {
            Log.d("q", "Returned")
            return
        }*/

        if(MinesweeperModel.getCellContent(tX,tY) != MinesweeperModel.BOMB && MinesweeperModel.getCellContent(tX,tY) != MinesweeperModel.OPENED && MinesweeperModel.getNumberBombsNear(tX,tY) == 0 ){
            MinesweeperModel.setCell(tX, tY, false, opened = true)
            MinesweeperModel.getCell(tX,tY)!!.draw = true

            openEmptyNeighbours(tX, tY - 1)
            openEmptyNeighbours(tX, tY + 1)
             openEmptyNeighbours(tX + 1, tY)
            openEmptyNeighbours(tX - 1, tY)
        }else return

    }


    public fun setListeners(
        gameEndedListener: GameEndedListener,
        bombNumberChangedListener: BombNumberChangedListener
    ) {
        this.bombNumberChangedListener = bombNumberChangedListener
        this.gameEndedListener = gameEndedListener
    }

    interface GameEndedListener {
        fun onGameEnded(won: Boolean)
    }

    interface BombNumberChangedListener {
        fun onBombNumberChanged(i: Int)
    }

}