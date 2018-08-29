package com.framgia.music_29.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String mArtworkUrl;
    private String mId;
    private String mTitle;
    private String mUri;
    private String mPermalink;
    private boolean mDownloadable;
    private boolean mStreamable;
    private String mDownloadUrl;
    private String mUserFullName;

    protected Song(Parcel in) {
        mArtworkUrl = in.readString();
        mId = in.readString();
        mTitle = in.readString();
        mUri = in.readString();
        mPermalink = in.readString();
        mDownloadable = in.readByte() != 0;
        mStreamable = in.readByte() != 0;
        mDownloadUrl = in.readString();
        mUserFullName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mArtworkUrl);
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mUri);
        dest.writeString(mPermalink);
        dest.writeByte((byte) (mDownloadable ? 1 : 0));
        dest.writeByte((byte) (mStreamable ? 1 : 0));
        dest.writeString(mDownloadUrl);
        dest.writeString(mUserFullName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public String getPermalink() {
        return mPermalink;
    }

    public void setPermalink(String permalink) {
        mPermalink = permalink;
    }

    public boolean isDownloadable() {
        return mDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        mDownloadable = downloadable;
    }

    public boolean isStreamable() {
        return mStreamable;
    }

    public void setStreamable(boolean streamable) {
        mStreamable = streamable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public String getUserFullName() {
        return mUserFullName;
    }

    public void setUserFullName(String userFullName) {
        mUserFullName = userFullName;
    }
}
