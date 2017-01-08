package com.stiletto.tr.fragment;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.PagesListAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.utils.PDFBookParser;
import com.stiletto.tr.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 30.12.16.
 */

public class MainFragment extends Fragment {
    @Bind(R.id.item_page)
    TextView itemPage;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);


        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        int density = (int) metrics.density;

        final int viewWidth = w - 30 * density;
        final int viewHeight = h - 30 * density;

        Log.d("PAGE_", "w=" + w + ", viewWidth=" + viewWidth + " | density=" + density);
        Log.d("PAGE_", "h=" + h + ", viewHeight=" + viewHeight + " | density=" + density);


        itemPage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    itemPage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    itemPage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                itemPage.setWidth(viewWidth);
                itemPage.setHeight(viewHeight);

                String content = PDFBookParser.getBookContent(PDFBookParser.ZA_FARENHEITOM);

                Pagination pagination = new Pagination(content, itemPage);

                itemPage.setVisibility(View.GONE);
                setupPagesAdapter(pagination);

            }
        });
    }

    private void setupPagesAdapter(Pagination pagination) {

        PagesListAdapter adapter = new PagesListAdapter(pagination.getPages());

        adapter.setOnListItemClick(new OnListItemClickListener<CharSequence>() {
            @Override
            public void onListItemClick(CharSequence item, int position) {

                Log.d("LIST_", "open page fragment");
//                NavigationManager.addFragment(getActivity(), PageFragment.newInstance(PageFragment.RIGHT));
            }
        });
        recyclerView.setAdapter(adapter);
    }


    public static MainFragment getInstance() {
        return new MainFragment();
    }
}