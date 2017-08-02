package cav.pdst.utils;

import android.view.MotionEvent;
import android.view.View;

/**
 * взято от сюда: http://snowpard-android-ru.blogspot.ru/2013/01/blog-post_20.html
 * автор Artem Kirillov
 */

public class SwipeTouchListener implements View.OnTouchListener{
    private float downX, downY, upX, upY; // Координаты

    public static enum Action {
        LR, // Слева направо
        RL, // Справа налево
        TB, // Сверху вниз
        BT, // Снизу вверх
        None // не обнаружено действий
    }
    private static final int HORIZONTAL_MIN_DISTANCE = 100; // Минимальное расстояние для свайпа по горизонтали
    private static final int VERTICAL_MIN_DISTANCE = 80; // Минимальное расстояние для свайпа по вертикали
    private Action mSwipeDetected = Action.None; // Последнее дейтсвие

    public boolean swipeDetected() {
        return mSwipeDetected != Action.None;
    }
    public Action getAction() {
        return mSwipeDetected;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                mSwipeDetected = Action.None;
                return false;
            }
            case MotionEvent.ACTION_UP:{
                upX = motionEvent.getX();
                upY = motionEvent.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // Обнаружение горизонтального свайпа
                if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                    // Слева направо
                    if (deltaX < 0) {
                        mSwipeDetected = Action.LR;
                        return true;
                    }
                    // Справа налево
                    if (deltaX > 0) {
                        mSwipeDetected = Action.RL;
                        return true;
                    }
                }else if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) { // Обнаружение вертикального свайпа
                    // Сверху вниз
                    if (deltaY < 0) {
                        mSwipeDetected = Action.TB;
                        return false;
                    }
                    // Снизу вверх
                    if (deltaY > 0) {
                        mSwipeDetected = Action.BT;
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
