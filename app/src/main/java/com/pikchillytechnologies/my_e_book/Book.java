package com.pikchillytechnologies.my_e_book;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{

    private String mTitle;
    private String mAuthor;
    private String mInfoUrl;
    private String mImageUrl;
    private String mPublisher;

    public Book(String title, String author, String infoUrl, String imageUrl, String publisher){
        this.mTitle = title;
        this.mAuthor = author;
        this.mInfoUrl = infoUrl;
        this.mImageUrl = imageUrl;
        this.mPublisher = publisher;
    }

    private Book(Parcel in) {
        mTitle = in.readString();
        mAuthor = in.readString();
        mInfoUrl = in.readString();
        mImageUrl = in.readString();
        mPublisher = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmInfoUrl() {
        return mInfoUrl;
    }

    public String getmImageUrl() { return mImageUrl; }

    public String getmPublisher() {
        return mPublisher;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);
        parcel.writeString(mInfoUrl);
        parcel.writeString(mImageUrl);
        parcel.writeString(mPublisher);
    }
}
