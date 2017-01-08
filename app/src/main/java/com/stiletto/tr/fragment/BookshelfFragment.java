package com.stiletto.tr.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BooksAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.utils.FileSeeker;
import com.stiletto.tr.view.Fragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.yuweiguocn.lib.squareloading.SquareLoading;

/**
 * Created by yana on 02.01.17.
 */

public class BookshelfFragment extends Fragment implements OnListItemClickListener<Book>, View.OnClickListener {

    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.button_load)
    FloatingActionButton buttonLoad;
    @Bind(R.id.item_progress)
    SquareLoading progressBar;
    @Bind(R.id.item_alert)
    TextView itemAlert;

    private BooksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bookshelf, container, false);
        ButterKnife.bind(this, view);
        itemAlert.setText(getString(R.string.scan_files));
        buttonLoad.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchForFiles();
    }

    private void searchForFiles() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                List<Book> bookList = FileSeeker.getBooks();
                adapter = new BooksAdapter(getContext(), bookList);
                adapter.setOnItemClickListener(BookshelfFragment.this);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                progressBar.setVisibility(View.GONE);
                itemAlert.setVisibility(View.GONE);
                buttonLoad.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.VISIBLE);
                gridView.setAdapter(adapter);
            }
        }.execute();
    }

    @Override
    public void onListItemClick(Book item, int position) {
        openBook(item);
    }

    private void openBook(Book book) {

        NavigationManager.addFragment(getActivity(), PageViewerFragment.create(book));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_load:
                break;
        }

    }
}