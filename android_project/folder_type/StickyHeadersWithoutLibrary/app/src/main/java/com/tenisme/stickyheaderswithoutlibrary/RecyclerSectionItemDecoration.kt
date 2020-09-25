package com.tenisme.stickyheaderswithoutlibrary

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


/**
 * Created By Lonnie on 2020/05/22
 *
 */

// ItemDecoration 을 상속받은 RecyclerSectionItemDecoration 을 생성한다.
// onDrawOver 를 Override 해준다.
class RecyclerSectionItemDecoration(sectionCallback: SectionCallback) : ItemDecoration() {
    private val sectionCallback: SectionCallback = sectionCallback

    // onDrawOver 는 recyclerView 가 그려진 뒤에 호출 된다. 그래서 RecyclerView 위에 그릴 수 있다.
    // 이 점을 이용해서 상단에 Sticky Header View 를 그릴 것이다.
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        // topChild 에 현재 recyclerView 에 보이는 뷰의 0번째를 가져와 넣는다.
        val topChild = parent.getChildAt(0) ?: return

        // getChildAdapterPosition 로 topChild 에 해당하는 position 을 가져온다.
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        // position 값이 RecyclerView.NO_POSITION 인지 확인한다. -> MainActivity 로
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        // MainAdapter 에서 넘어옴 -> getHeaderLayoutView 를 이용해 topChildPosition 에 해당하는 뷰를 찾아온다.
        // fixLayoutSize 에서 가져온 뷰를 측정해준다.
        val currentHeader: View =
            sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return
        fixLayoutSize(parent, currentHeader)

        // contactPoint 로 현재 topChildPosition 에 해당하는 뷰의 bottom 값을 구하고
        val contactPoint = currentHeader.bottom
        // getChildInContact 로 인접한 뷰를 구한다.
        val childInContact: View = (getChildInContact(parent, contactPoint) ?: return)

        // 그 인접한 뷰 childInContact 에 해당하는 포지션을 가져온다.
        val childAdapterPosition = parent.getChildAdapterPosition(childInContact)
        if (childAdapterPosition == -1) {
            return
        }
        // 이 childAdapterPosition 이 리스트 뷰의 최 상위에 있을 때 moveHeader 로 밀려나는 것처럼 그리고
        // 그 외엔 상단에 고정되어 있는 것처럼 보이도록 drawHeader 로 그린다.
        if (sectionCallback.isSection(childAdapterPosition)) {
            moveHeader(c, currentHeader, childInContact)
            return
        }
        drawHeader(c, currentHeader)
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }


    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.UNSPECIFIED
        )
        val childWidth: Int = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight: Int = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    // ItemDecoration 에서는 adapter 에 직접 접근하지 않아야 한다.
    // 따라서 Interface 를 만들어서 adapter 에서 필요한 정보를 가져온다.
    // 인터페이스로 SectionCallback 을 만들고 isSection 과 getHeaderLayoutView 를 선언했다.
    // isSection 은 해당 포지션이 고정될 뷰 인지 판단하고
    // getHeaderLayoutView 는 해당 포지션에 해당하는 뷰를 가져온다.
    interface SectionCallback {
        fun isSection(position: Int): Boolean
        fun getHeaderLayoutView(list: RecyclerView, position: Int): View?
    }

}