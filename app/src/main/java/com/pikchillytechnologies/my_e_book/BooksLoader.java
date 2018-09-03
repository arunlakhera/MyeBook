package com.pikchillytechnologies.my_e_book;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<Book>> {
    public static List<Book> arrayList = null;
    String url;

    public BooksLoader(Context context, String url) {
        super(context);
        if (url == null) {
            return;
        }
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (url == null) {
            return null;
        }
        arrayList = QueryUtils.fetchBooksData(url);
        return arrayList;
    }
}