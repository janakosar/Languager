package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandy.reader.fragment.WordDetailsFragment;
import com.nandy.reader.mvp.contract.DictionaryContract;
import com.nandy.reader.mvp.model.DictionaryModel;
import com.nandy.reader.mvp.presenter.DictionaryPresenter;
import com.softes.categorizedlistview.CategorizedListView;
import com.nandy.reader.R;
import com.nandy.reader.adapter.BaseDictionaryAdapter;
import com.nandy.reader.core.DictionaryItemListener;
import com.nandy.reader.manager.NavigationManager;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.view.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 21.05.17.
 */

public class DictionaryFragment extends Fragment
        implements BaseDictionaryAdapter.OnItemClickListener, DictionaryItemListener,
        DictionaryContract.View{

    @Bind(R.id.recycler_view)
    CategorizedListView recyclerView;

    private BaseDictionaryAdapter adapter;
    private DictionaryContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BaseDictionaryAdapter();
        adapter.setOnListItemClickListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        presenter.start();

//        HashSet<String> set = new HashSet<>();
//        for (Word item : list) {
//            String word = item.getText().substring(0, 1).toUpperCase();
//            set.add(word);
//        }
//
//        ArrayList<String> items = new ArrayList<>(set);
//        Collections.sort(items);
//        recyclerView.setIndexBarItems(items);
//
//        if (adapter.getItemCount() <= 10) {
//            recyclerView.setIndexBarVisibility(View.GONE);
//        }
    }

    @Override
    public void setPresenter(DictionaryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onItemClick(String key, Word word, int position) {
        NavigationManager.addFragment(getActivity(), WordDetailsFragment.getInstance(word, position, this));
    }

    @Override
    public void onDictionaryItemRemoved(int position) {
        adapter.remove(position);
    }

    public static DictionaryFragment newInstance(Pair<String, String> languages) {

        DictionaryFragment fragment = new DictionaryFragment();

        DictionaryPresenter presenter = new DictionaryPresenter(fragment);
        presenter.setDictionaryModel(new DictionaryModel(languages.first, languages.second));

        fragment.setPresenter( presenter);

        return fragment;
    }

    @Override
    public void addItem(Word word) {

    }
}