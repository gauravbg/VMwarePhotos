package com.chikeandroid.tutsplus_glide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chike on 2/11/2017.
 */

public class SpacePhoto implements Parcelable {

    private String mUrl;
    private String mTitle;
    private List<String> mTags;
    private static SpacePhoto[] allPhotos =  new SpacePhoto[]{
                new SpacePhoto("https://i.imgur.com/eA7JeTJ.jpg", "1"),
                new SpacePhoto("https://i.imgur.com/52sUKRr.jpg", "2"),
//                new SpacePhoto("https://i.imgur.com/cl2aNUB.jpg", "3"),
//                new SpacePhoto("https://i.imgur.com/kekFp5l.jpg", "4"),
//                new SpacePhoto("https://i.imgur.com/ZBlwxSi.jpg", "5"),
//                new SpacePhoto("https://i.imgur.com/tMOTzqr.jpg", "6"),
//                new SpacePhoto("https://i.imgur.com/KkAfQv7.jpg", "7"),
                new SpacePhoto("https://i.imgur.com/SrQYwjG.jpg", "8"),
                new SpacePhoto("https://i.imgur.com/AeyMXoi.jpg", "9"),
//                new SpacePhoto("https://i.imgur.com/jTIjFek.jpg", "10"),
//                new SpacePhoto("https://i.imgur.com/sRsk8.jpg", "11"),
//                new SpacePhoto("https://i.imgur.com/OFtsM4F.jpg", "12"),
//                new SpacePhoto("https://i.imgur.com/SULVQoU.jpg", "13"),
//                new SpacePhoto("https://i.imgur.com/3SuU8.jpg", "14"),
                new SpacePhoto("https://i.imgur.com/2wwWa48.jpg", "15"),
                new SpacePhoto("https://i.imgur.com/yZtxKts.jpg", "16"),
//                new SpacePhoto("https://i.imgur.com/0ZByC.jpg", "17"),
//                new SpacePhoto("https://i.imgur.com/NPF5rNP.jpg", "18"),
//                new SpacePhoto("https://i.imgur.com/4NLElhE.jpg", "19"),
//                new SpacePhoto("https://i.imgur.com/B8Y55PO.jpg", "20"),
//                new SpacePhoto("https://i.imgur.com/xFgoaPL.jpg", "21"),
                new SpacePhoto("https://i.imgur.com/NALwjzlb.jpg", "22"),
                new SpacePhoto("https://i.imgur.com/1knse.jpg", "23")

    };

    public SpacePhoto(String url, String title) {
        mUrl = url;
        mTitle = title;
        mTags = new ArrayList<>();
    }

    protected SpacePhoto(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
        mTags = new ArrayList<>();
    }

    public static final Creator<SpacePhoto> CREATOR = new Creator<SpacePhoto>() {
        @Override
        public SpacePhoto createFromParcel(Parcel in) {
            return new SpacePhoto(in);
        }

        @Override
        public SpacePhoto[] newArray(int size) {
            return new SpacePhoto[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public List<String> getTags() {
        return mTags;
    }

    public void setTags(List<String> tags) {
        mTags = tags;
    }

    public static  SpacePhoto[] getSpacePhotos() {

        return allPhotos;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
        parcel.writeStringList(mTags);
    }
}
