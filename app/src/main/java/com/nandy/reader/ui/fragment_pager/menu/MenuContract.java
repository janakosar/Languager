package com.nandy.reader.ui.fragment_pager.menu;

import com.nandy.reader.BasePresenter;
import com.nandy.reader.BaseView;

/**
 * Created by yana on 20.08.17.
 */

public class MenuContract {

    public interface Model{

        String getPrimaryLanguage();

        String getTranslationLanguage();

        String getTitle();

        String getAuthor();

        int getBookmark();

        void setPagesCount(int pagesCount);

        int getPagesCount();

        String getBookId();
    }
    public interface View extends BaseView<Presenter>{

        void setLanguages(String languages);

        void setTitle(String title);

        void setAuthor(String author);

        void setCurrentPage(String page);

        void setPagesCount(String count);

        void setItemsInDictionary(String text);

        void setItemsToLearn(String text);

        void setNoItemsInTheDictionary();

        void setCurrentPagesProgress(int progress);

        void setMaxPagesProgress(int progress);

        void setFooterPagesText(String text);

        void onPageChanged(int page);

        void onBookParsingFinished(int pagesCount);

        void show();

        void hide();

    }

    public interface Presenter extends BasePresenter{

        void onMenuClick();

        void onSettingsClick();

        void onDictionaryClick();

        void onInfoClick();

        void onCloseClick();

        void onBackgroundClick();

        void onPageChanged(int page);

        void onPagesProgressChanged(int progress, boolean byUser);

        void onBookParsingFinished(int pagesCount);

    }
}