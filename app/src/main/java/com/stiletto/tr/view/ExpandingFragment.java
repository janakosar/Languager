package com.stiletto.tr.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;

/**
 * Created by yana on 19.03.17.
 */

public abstract class ExpandingFragment extends Fragment
        implements OnExpandableItemClickListener {

    private static final float SCALE_OPENED = 1.2f;
    private static final int SCALE_CLOSED = 1;
    private static final int ELEVATION_OPENED = 40;

    Fragment fragmentFront;
    Fragment fragmentBottom;

    private CardView back;
    private CardView front;
    private CardView layout3;

    private float startY;

    float defaultCardElevation;
    private ObjectAnimator frontAnimator;
    private ObjectAnimator backAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expanding_fragment, container, false);
    }

    @Override
    public void onExpandableItemClick() {
        toggle();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fragmentFront = getFragmentTop();
        this.fragmentBottom = getFragmentBottom();

        if (fragmentFront != null && fragmentBottom != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.front, fragmentFront)
                    .replace(R.id.bottomLayout, fragmentBottom)
                    .commit();
        }

        back = (CardView) view.findViewById(R.id.back);
        front = (CardView) view.findViewById(R.id.front);
        layout3 = (CardView) view.findViewById(R.id.bottomLayout);

//        setupDownGesture(view);

        defaultCardElevation = front.getCardElevation();
    }


    private void setupDownGesture(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float my = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        my = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isOpened() && event.getY() - startY > 0) {
                            close();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

    }

    public abstract Fragment getFragmentTop();

    public abstract Fragment getFragmentBottom();

    public boolean isClosed() {
        return ViewCompat.getScaleX(back) == SCALE_CLOSED;
    }

    public boolean isOpened() {
        return ViewCompat.getScaleX(back) == SCALE_OPENED;
    }

    public void toggle() {
        if (isOpened()) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        ViewGroup.LayoutParams layoutParams = layout3.getLayoutParams();
        layoutParams.height = (int) (front.getHeight() * SCALE_OPENED / 4 * SCALE_OPENED);
        layout3.setLayoutParams(layoutParams);


        ViewCompat.setPivotY(back, 0);

        PropertyValuesHolder front1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, -front.getHeight() / 4);
        PropertyValuesHolder front2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1);
        frontAnimator = ObjectAnimator.ofPropertyValuesHolder(front, front1, front2);
        PropertyValuesHolder backX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f);
        PropertyValuesHolder backY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f);
        backAnimator = ObjectAnimator.ofPropertyValuesHolder(back, backX, backY);
        back.setPivotY(0);
        frontAnimator.start();
        backAnimator.start();

        front.setCardElevation(ELEVATION_OPENED);
    }

    public void close() {
        if (frontAnimator != null) {
            frontAnimator.reverse();
            backAnimator.reverse();
            backAnimator = null;
            frontAnimator = null;
        }
        front.setCardElevation(defaultCardElevation);
    }

}