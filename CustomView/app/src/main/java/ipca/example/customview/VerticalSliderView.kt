package ipca.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class VerticalSliderView : View {

    private var positionY : Float? = null

    private var _percentage = 50
    var percentage : Int
        get() {
            return _percentage
        }
        set(value) {
            _percentage = value
            positionY = ((100 - _percentage)/100f) * height
            invalidate()
        }

    private var valueChanged : ((Int)->Unit)? = null
    fun setOnValueChanged (callback:(Int)->Unit) {
        valueChanged = callback
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val paint = Paint()
        paint.color = Color.RED

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = Color.YELLOW
        canvas.drawRect(0f, positionY?:(height/2.0f), width.toFloat(), height.toFloat(), paint)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        super.onTouchEvent(e)

        val y: Float? = e?.y

        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                positionY = y
                _percentage = 100 - (((y?:0f)/height.toFloat())*100f).toInt()
                if (_percentage > 100) _percentage = 100
                if (_percentage < 0) _percentage = 0
                valueChanged?.invoke(_percentage)
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                positionY = y
                _percentage = 100 - (((y?:0f)/height.toFloat())*100f).toInt()
                if (_percentage > 100) _percentage = 100
                if (_percentage < 0) _percentage = 0
                valueChanged?.invoke(_percentage)
                invalidate()
                return true
            }
        }

        return false

    }
}