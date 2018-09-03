package com.pikchillytechnologies.my_e_book;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

//LoaderManager.LoaderCallbacks<List<Book>>
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String TAG = "BooksActivity";
    private static final String bookGoogleUrl = "https://www.googleapis.com/books/v1/volumes";
    private static final int BOOKS_LOADER_ID = 1;
    public static ArrayList<Book> listBook = null;
    public BooksAdapter adapterBooks;
    private RecyclerView recyclerViewBook;
    private EditText editTextSearch;
    private ProgressBar progressBarBooks;
    private TextView empty_state;

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int i, @Nullable Bundle bundle) {

        editTextSearch = (EditText) findViewById(R.id.editText_Search);
        ///BUT How to convert spaces
        String query = editTextSearch.getText().toString();

        if (query.isEmpty() || query.length() == 0) {
            editTextSearch.setError(String.valueOf(R.string.search_msg));
            return new BooksLoader(this, null);
        }

        Uri baseUri = Uri.parse(bookGoogleUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", query);

        // Hide keyboard once Find button is clicked
        hideKeyboard();

        editTextSearch.setText("");

        //Returning a new Loader Object
        return new BooksLoader(this, String.valueOf(uriBuilder));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        progressBarBooks.setVisibility(View.GONE);

        if (books != null && !books.isEmpty()) {
            prepareBooks(books);
            Log.i(TAG, "onLoadFinished: ");
        } else {
            checkNetwork();
            empty_state.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        Log.i(QueryUtils.TAG, "onLoaderReset: ");
        if (adapterBooks == null) {
            return;
        }
        listBook.clear();
        adapterBooks.notifyDataSetChanged();
        Log.i(TAG, "onLoaderReset: " + listBook);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarBooks = findViewById(R.id.progressBar_Books);
        progressBarBooks.setIndeterminate(true);
        progressBarBooks.setVisibility(View.GONE);

        empty_state =  findViewById(R.id.empty_state);
        checkNetwork();

        recyclerViewBook = findViewById(R.id.recyclerView_Books);

        if (savedInstanceState == null || !savedInstanceState.containsKey("booksList")) {
            listBook = new ArrayList<>();
            adapterBooks = new BooksAdapter(this, listBook);

            //log Statement
            Log.i(TAG, "onCreate: " + listBook);
        } else {
            listBook.addAll(savedInstanceState.<Book>getParcelableArrayList("booksList"));

            //log statement
            Log.i(TAG, "onCreate: under else" + listBook);
            adapterBooks = new BooksAdapter(this, listBook);
            //this will reLoad the adapter
            adapterBooks.notifyDataSetChanged();

        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewBook.setLayoutManager(mLayoutManager);
        recyclerViewBook.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBook.setAdapter(adapterBooks);

    }

    /**
     * Function to Hide keyboard
     */
    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("booksList", listBook);
        super.onSaveInstanceState(outState);
    }

    public void searchButton(View view) {
        progressBarBooks.setVisibility(View.VISIBLE);
        listBook.clear();
        adapterBooks.notifyDataSetChanged();
        getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, this);
        getLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);
        Log.i(TAG, "searchButton: " + listBook);
    }

    private void prepareBooks(List<Book> booksList) {

        listBook.addAll(booksList);
        Log.i(TAG, "prepareBooks: " + listBook);

        //notifiying the recycleradapter that data has been changed
        adapterBooks.notifyDataSetChanged();
    }

    public void checkNetwork(){

        //Checking the Network State
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            empty_state.setText(R.string.no_internet);
            empty_state.setVisibility(View.VISIBLE);
            (findViewById(R.id.button_Search)).setEnabled(false);
        }
    }
}
