package com.stiletto.tr.widget.categorized_recycler_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.stiletto.tr.R;

import java.util.ArrayList;

/**
 * Created by yana on 08.03.17.
 */

public class CategoryBarView extends View {

    // index bar margin
    float indexbarMargin;

    // user touched Y axis coordinate value
    float sideIndexY;

    // flag used in touch events manipulations
    boolean isIndexing = false;

    // holds current section position selected by user
    int currentSectionPosition = -1;

    // array list to store section positions
//    public ArrayList<Integer> listSections;

    // array list to store listView data
    ArrayList<String> listItems;

    Paint indexPaint;

    Context context;

    // interface object used as bridge between list view and index bar view for
    // filtering list view content on touch event
    CategorizedListView categoryFilter;


    public CategoryBarView(Context context) {
        super(context);
        this.context = context;
    }


    public CategoryBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public CategoryBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }


    public void setData(CategorizedListView listView, ArrayList<String> listItems) {
        this.listItems = listItems;

        // list view implements categoryFilter interface
        categoryFilter = listView;

        // set index bar margin from resources
        indexbarMargin = context.getResources().getDimension(R.dimen.index_bar_view_margin);

        // index bar item color and text size
        indexPaint = new Paint();
        indexPaint.setColor(ContextCompat.getColor(context, R.color.black));
        indexPaint.setAntiAlias(true);
        indexPaint.setTextSize(context.getResources().getDimension(R.dimen.index_bar_view_text_size));
    }


    // draw view content on canvas using paint
    @Override
    protected void onDraw(Canvas canvas) {
//        if (listSections != null && listSections.size() > 1) {
//            float sectionHeight = (getMeasuredHeight() - 2 * indexbarMargin)/ listSections.size();
//            float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
//
//            for (int i = 0; i < listSections.size(); i++) {
//                float paddingLeft = (getMeasuredWidth() - indexPaint.measureText(getSectionText(listSections.get(i)))) / 2;
//
//                canvas.drawText(getSectionText(listSections.get(i)),
//                        paddingLeft,
//                        indexbarMargin + (sectionHeight * i) + paddingTop + indexPaint.descent(),
//                        indexPaint);
//            }
//        }
//        super.onDraw(canvas);

        if (listItems != null && listItems.size() > 1) {
            int itemCount = listItems.size();
            float sectionHeight = (getMeasuredHeight() - 2 * indexbarMargin)/ itemCount;
            float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;

            for (int i = 0; i < itemCount; i++) {
                float paddingLeft = (getMeasuredWidth() - indexPaint.measureText(listItems.get(i))) / 2;

                canvas.drawText(listItems.get(i),
                        paddingLeft,
                        indexbarMargin + (sectionHeight * i) + paddingTop + indexPaint.descent(),
                        indexPaint);
            }
        }
        super.onDraw(canvas);
    }




    boolean contains(float x, float y) {
        // Determine if the point is in index bar region, which includes the
        // right margin of the bar
        return (x >= getLeft() && y >= getTop() && y <= getTop() + getMeasuredHeight());
    }


    void filterListItem(float sideIndexY) {
        this.sideIndexY = sideIndexY;

        // filter list items and get touched section position with in index bar
        currentSectionPosition = (int) (((this.sideIndexY) - getTop() - indexbarMargin) /
                ((getMeasuredHeight() - (2 * indexbarMargin)) / listItems.size()));

        if (currentSectionPosition >= 0 && currentSectionPosition < listItems.size()) {
            int position = currentSectionPosition;
            String previewText = listItems.get(position);
//            categoryFilter.filterList(this.sideIndexY, position, previewText);
        }
    }


    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // If down event occurs inside index bar region, start indexing
                if (contains(ev.getX(), ev.getY())) {
                    // It demonstrates that the motion event started from index
                    // bar
                    isIndexing = true;
                    // Determine which section the point is in, and move the
                    // list to
                    // that section
                    filterListItem(ev.getY());
                    return true;
                }
                else {
                    currentSectionPosition = -1;
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if (isIndexing) {
                    // If this event moves inside index bar
                    if (contains(ev.getX(), ev.getY())) {
                        // Determine which section the point is in, and move the
                        // list to that section
                        filterListItem(ev.getY());
                        return true;
                    }
                    else {
                        currentSectionPosition = -1;
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isIndexing) {
                    isIndexing = false;
                    currentSectionPosition = -1;
                }
                break;
        }
        return false;
    }
}