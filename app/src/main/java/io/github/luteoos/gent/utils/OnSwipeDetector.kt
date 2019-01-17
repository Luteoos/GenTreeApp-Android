package io.github.luteoos.gent.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.lang.Exception

//        iv_profile_pic.setOnTouchListener(object: OnSwipeDetector(context!!){
//            override fun onSwipeBottom() {
//                Toasty.info(context!!,"xDD").show()
//            }
//        })

open class OnSwipeDetector(val ctx: Context) : View.OnTouchListener {

    private val detector: GestureDetector

    init {
        detector = GestureDetector(ctx, GestureListener(this))
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        return detector.onTouchEvent(event)
    }

    open fun onSwipeTop(){
        Toasty.info(ctx, "top").show()
    }

    open fun onSwipeBottom(){
        Toasty.info(ctx, "bot").show()
    }

    open fun onSwipeLeft(){
        Toasty.info(ctx, "left").show()
    }

    open fun onSwipeRight(){
        Toasty.info(ctx, "right").show()
    }

    private class GestureListener(val owner: OnSwipeDetector) : GestureDetector.SimpleOnGestureListener(){

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            var result = false

            try {
                val diffY = e2!!.y - e1!!.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            owner.onSwipeRight()
                        } else {
                            owner.onSwipeLeft()
                        }
                        result = true
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        owner.onSwipeBottom()
                    } else {
                        owner.onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                Timber.e("%s Trace %s", exception.message, exception.stackTrace)
            }
            return result
        }
    }
}