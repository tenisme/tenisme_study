package com.tenisme.draganddrop

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import android.view.View.DRAG_FLAG_OPAQUE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

class MainActivity : AppCompatActivity(), View.OnDragListener, View.OnLongClickListener,
    View.OnClickListener {

    // todo : 몇가지 더 테스트해보고 옮기기

    companion object {
        private const val IMAGE_VIEW_TAG = "icon bitmap"
    }

    private lateinit var layoutGoal: LinearLayout
    private lateinit var imgFront: ImageView
    private lateinit var btnUndo: Button
    private val width = 250
    private val height = 250

    private lateinit var bitFront: Bitmap

    private lateinit var beforeView: View
    private lateinit var beforeViewGroup: ViewGroup
    private lateinit var beforeNewParent: ViewGroup

    private val sp: SharedPreferences by lazy { getSharedPreferences("TEST", MODE_PRIVATE) }
    private val editor: SharedPreferences.Editor by lazy { sp.edit() }

//    private val binding by lazy {
//        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//    }
//    private var fromX = 0f
//    private var fromY = 0f

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DragListener 등록
        layoutGoal = findViewById(R.id.layoutGoal)
        layoutGoal.setOnDragListener(this)

//        // BitmapFactory Option inSampleSize 지정
//        bitOption = BitmapFactory.Options()
//        bitOption.inSampleSize = 1

        // ImageView Scale
        bitFront =
            BitmapFactory.decodeResource(resources, android.R.drawable.btn_star_big_on, null)
        bitFront = Bitmap.createScaledBitmap(bitFront, width, height, true)

        // ImageView Listener 등록 (LongClickListener)
        imgFront = findViewById<ImageView>(R.id.imgFront).apply {
            // Set ImageView
            setImageBitmap(bitFront)
//            tag = IMAGE_VIEW_TAG
        }

        imgFront.setOnLongClickListener(this)

        btnUndo = findViewById(R.id.btnUndo)

        btnUndo.setOnClickListener(this)
        if(sp.getBoolean("isMoving", false)) {
            btnUndo.setBackgroundColor(Color.GREEN)
            findViewById<ImageView>(R.id.imgGoal).setImageBitmap(bitFront)
            imgFront.visibility = View.GONE
            editor.putBoolean("isMoving", true)
            editor.apply()
        } else {
            btnUndo.setBackgroundColor(Color.parseColor("#aaaaaa"))
            editor.putBoolean("isMoving", false)
            editor.apply()
        }

    }

    override fun onDrag(v: View, dragEvent: DragEvent): Boolean {
        if(v == layoutGoal) {
            val viewState = dragEvent.localState as View

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    // Drag 가 시작되면 Listener 를 등록한 모든 뷰에게 event 전달
                    Log.d("TEST", "ACTION_DRAG_STARTED")
                    return true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // view 의 경계 안으로 들어옴
                    Log.d("TEST", "ACTION_DRAG_ENTERED")
                    return true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
//                    Log.d("TEST", "ACTION_DRAG_LOCATION")
                    // 경계 안에서 이동중
                    return true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    // 경계를 벗어남
                    Log.d("TEST", "ACTION_DRAG_EXITED")
                    return true
                }
                DragEvent.ACTION_DROP -> {
                    Log.d("TEST", "ACTION_DROP")
                    // Drop 됨
                    // Drop Down 되는 순간 event state 가 일어난 시점의 view(Image View)를 저장하고
                    // viewGroup 을 구한 후 view 를 remove 하고, Drop Down 되는 state 의 ViewGroup 을 구한 후
                    // 이동시킬 view(Image View) 를 add 하면 자연스럽게 view 를 이동시킬 수 있음.

                    val viewParent = viewState.parent as ViewGroup
                    viewParent.removeView(viewState)
                    val container = v as LinearLayout
                    container.addView(viewState)
//                    viewState.x = dragEvent.x
//                    viewState.y = dragEvent.y
                    v.visibility = View.VISIBLE

                    // 기존 layout 상태로 되돌리기 위해 view/viewGroup 을 저장하고 필요한 시점에 사용함
                    beforeView = viewState
                    beforeViewGroup = viewParent
                    beforeNewParent = container

                    beforeNewParent.removeView(beforeView)
                    beforeViewGroup.addView(beforeView)

                    return true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d("TEST", "ACTION_DRAG_ENDED")
                    // Drag & Drop 종료
                    when (dragEvent.result) {
                        true -> {
                            findViewById<ImageView>(R.id.imgGoal).setImageBitmap(bitFront)
                            editor.putBoolean("isMoving", true)
                            editor.apply()
                            btnUndo.setBackgroundColor(Color.GREEN)
                        }
                        false -> (dragEvent.localState as View).visibility = View.VISIBLE
                    }
                    return true
                }
                else -> return false
            }
        }
        return true
    }

    override fun onLongClick(v: View): Boolean {
        if(v == imgFront) {
//            val clipData = ClipData(
//                v.tag as CharSequence,
//                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
//                ClipData.Item(v.tag as CharSequence)
//            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(null, View.DragShadowBuilder(v), v, DRAG_FLAG_OPAQUE)
            } else {
                v.startDrag(null, View.DragShadowBuilder(v), v, 0)
            }
            v.visibility = View.GONE
            return true
        }

        return true
    }

    override fun onClick(v: View?) {
        if(v == btnUndo) {
            if(sp.getBoolean("isMoving", true)) {
                findViewById<ImageView>(R.id.imgGoal).setImageResource(android.R.drawable.btn_star_big_off)
                imgFront.visibility = View.VISIBLE

                editor.putBoolean("isMoving", false)
                editor.apply()
                btnUndo.setBackgroundColor(Color.parseColor("#aaaaaa"))
            }
        }
    }

//    // 다른 방법의 Drag & Drop
//        binding.lifecycleOwner = this
//        binding.mainActivity = this
//
//        val drag = View.OnDragListener { view, dragEvent ->
//            val id = view.id
//            when (dragEvent.action) {
//                DragEvent.ACTION_DROP -> {
//                    if (id == R.id.motherView) {
//                        val x = dragEvent.x - (binding.moveTxtView.width / 2)
//                        val y = dragEvent.y - (binding.moveTxtView.height / 2)
//                        binding.moveTxtView.x = x
//                        binding.moveTxtView.y = y
//                    }
//                }
//            }
//            true
//        }
//
//        binding.moveTxtView.tag = "good"
//        binding.motherView.setOnDragListener(drag)
//
//        binding.moveTxtView.setOnTouchListener { view, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    if (view != null) {
//                        val data = ClipData(
//                            view.tag.toString(),
//                            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
//                            ClipData.Item(view.tag as CharSequence)
//                        )
//                        fromX = event.x
//                        fromY = event.y
//                        val builder = View.DragShadowBuilder(view)
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            view.startDragAndDrop(data, builder, null, 0)
//                        } else {
//                            view.startDrag(data, builder, null, 0)
//                        }
//                    }
//                }
//            }
//            true
//        }
//    }

}

// API 11 이상
class CustomDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    private val shadow = ColorDrawable(Color.LTGRAY)

    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)
//            // 보여질 shadow size
//            outShadowSize?.set(width, height)
//            // touchPoint 가 shadow 의 중심 좌표로 따라 이동됨
//            outShadowTouchPoint?.set(x, y)

        // Sets the width of the shadow to half the width of the original View
        val width: Int = view.width

        // Sets the height of the shadow to half the height of the original View
        val height: Int = view.height

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        shadow.setBounds(0, 0, width, height)

        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        outShadowSize?.set(width, height)

        // Sets the touch point's position to be in the middle of the drag shadow
        outShadowTouchPoint?.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas?) {
        shadow.draw(canvas!!)
        // Shadow 를 새롭게 Draw 해야할 때
    }
}

class CustomShadowBuilder: View.DragShadowBuilder() {
    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        outShadowSize?.set(1, 1)
        outShadowTouchPoint?.set(0, 0)
    }
}