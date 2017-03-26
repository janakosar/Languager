package com.stiletto.tr.adapter;

import android.support.v4.app.FragmentManager;

import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.fragment.BookExpandingFragment;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.ExpandingViewPagerAdapter;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 19.03.17.
 */

public class BookShelfAdapter extends ExpandingViewPagerAdapter {

    private List<BookExpandingFragment> fragments;
    private List<Book> books = new ArrayList<>();

    public BookShelfAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
    }

    public void addBook(Book book, BookItemListener listener) {

        if (!books.contains(book)) {
            int position = fragments.size();
            fragments.add(BookExpandingFragment.newInstance(book, position, listener));
            books.add(book);
            notifyDataSetChanged();
        }
    }

    public boolean removeItem(Book book, int position){

        books.remove(book);
        fragments.remove(position);
        notifyDataSetChanged();

        return fragments.size() > 0;
    }
    @Override
    public Fragment getItem(int position) {
        return
                getContent(position);
    }

    private BookExpandingFragment getContent(int position){
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}