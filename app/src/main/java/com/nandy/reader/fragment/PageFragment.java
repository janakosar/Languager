package com.nandy.reader.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softes.clickabletextview.ClickableTextView;
import com.nandy.reader.R;
import com.nandy.reader.adapter.DictionaryAdapter;
import com.nandy.reader.core.ActionModeCallback;
import com.nandy.reader.core.TranslationCallback;
import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.translator.yandex.Translator;
import com.nandy.reader.utils.ReaderPrefs;
import com.nandy.reader.view.Fragment;
import com.nandy.reader.view.PopupFragment;
import com.nandy.reader.view.StyleCallback;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Single book page content is displayed there.
 * <p>
 * Created by yana on 01.01.17.
 */

public class PageFragment extends Fragment implements ClickableTextView.OnWordClickListener, ActionModeCallback {

    @Bind(R.id.item_content)
    ClickableTextView textView;

    private TranslationCallback translationCallback;

    public static final String ARG_PAGE = "page";
    public static final String ARG_CONTENT = "content";
    private CharSequence content;

    private View popView;
    private PopupFragment popupFragment;

    private Language primaryLanguage;
    private Language translationLangusage;


    public static PageFragment create(int pageNumber, CharSequence content, Language primaryLang,
                                      Language translationLang, TranslationCallback translationCallback) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putCharSequence(ARG_CONTENT, content);
        args.putString("primary_lang", primaryLang.toString());
        args.putString("trans_lang", translationLang.toString());
        fragment.setArguments(args);

        fragment.setTranslationCallback(translationCallback);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        content = getArguments().getCharSequence(ARG_CONTENT);
        primaryLanguage = Language.getLanguage(getArguments().getString("primary_lang"));
        translationLangusage = Language.getLanguage(getArguments().getString("trans_lang"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page, container, false);
        ButterKnife.bind(this, view);

        popupFragment = new PopupFragment(getActivity(), view, R.layout.pop_view);

        ReaderPrefs prefs = ReaderPrefs.getPreferences(getContext());
        textView.setPadding(prefs.getPaddingHorizontal(), prefs.getPaddingVertical(), prefs.getPaddingHorizontal(), 0);

        TextPaint paint = prefs.getTextPaint();
        textView.setTextSize(prefs.getTextSize());
        textView.setTextColor(paint.getColor());
        textView.setTypeface(paint.getTypeface());
        textView.setLineSpacing(prefs.getLineSpacingExtra(), prefs.getLineSpacingMultiplier());
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setCustomSelectionActionModeCallback(new StyleCallback(textView, this));
        textView.setTextIsSelectable(true);
        textView.setOnWordClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView.setText(content.toString());
    }

    @Override
    public void onTranslateOptionSelected(CharSequence text) {
        onTranslate(text);
    }

    @Override
    public void onClick(final String word) {
        onTranslate(word);
    }

    public void showPopup() {
        if (!popupFragment.isShowing()) {
            popView = popupFragment.showPopup();
        }
        popView.findViewById(R.id.item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFragment.hidePopup();
            }
        });

    }

    private void setUpDictionary(List<DictionaryItem> dictionary) {
        RecyclerView recyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DictionaryAdapter adapter = new DictionaryAdapter(dictionary);
        recyclerView.setAdapter(adapter);
    }

    private void onTranslate(CharSequence text) {
        showPopup();
        TextView textOrigin = (TextView) popView.findViewById(R.id.item_origin);
        textOrigin.setTextColor(Color.WHITE);
        textOrigin.setText(text);
        translate(text);
    }

    /**
     * Request translation in Yandex Translator API.
     *
     * @param original - word or phrase to translate.
     */
    private void translate(final CharSequence original) {
        Translator.translate(original, new Language[]{primaryLanguage, translationLangusage},
                new Translator.Callback<Word>() {
                    @Override
                    public void translationSuccess(Word word) {

                        if (popView == null) {
                            showPopup();
                        }


                        popView.findViewById(R.id.layout_translation).setVisibility(View.VISIBLE);

                        TextView textView = (TextView) popView.findViewById(R.id.item_translation);

                        String primary = original + "\n";
                        primary = primary.toUpperCase(Locale.getDefault());

                        SpannableString text = new SpannableString(primary + word.getTranslationsAsString());
                        text.setSpan(new UnderlineSpan(), 0, primary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        text.setSpan(new StyleSpan(Typeface.BOLD), 0, primary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        text.setSpan(new RelativeSizeSpan(0.85f), primary.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        text.setSpan(new StyleSpan(Typeface.ITALIC), primary.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorSecondaryText)),
                                primary.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        textView.setText(text);

                        if (text.toString().split(" ").length < 3) {
                            lookup(word);
                        } else {
                            translationCallback.newTranslation(word);
                        }


                    }

                    @Override
                    public void translationFailure(Call call, Response response) {

                    }

                    @Override
                    public void translationError(Call call, Throwable error) {

                    }
                });


    }


    /**
     * Request translation from Yandex Dictionary.
     */
    private void lookup(final Word word) {
        Translator.getDictionary(word.getText(), new Language[]{primaryLanguage, translationLangusage},
                new Translator.Callback<Dictionary>() {
                    @Override
                    public void translationSuccess(Dictionary dictionary) {

                        if (popView == null) {
                            showPopup();
                        }

                        setUpDictionary(dictionary.getItems());
//                DictionaryTable.insert(getContext(), items, item);

                        word.setDictionary(dictionary);
                        translationCallback.newTranslation(word);

                    }

                    @Override
                    public void translationFailure(Call call, Response response) {

                    }

                    @Override
                    public void translationError(Call call, Throwable error) {

                    }
                });

    }


    public void setTranslationCallback(TranslationCallback translationCallback) {
        this.translationCallback = translationCallback;
    }
}
