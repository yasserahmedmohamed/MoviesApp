package com.myownapps.yasser.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yasser on 8/13/2016.
 */

public class movies implements Parcelable {
    String original_title,plot_synopsis,user_rating,release_date,poster_path,id;



    public movies( String original_title,String plot_synopsis,String user_rating,String release_date,String poster_path,String id){

        this.original_title =original_title;
        this.plot_synopsis=plot_synopsis;
        this.user_rating=user_rating;
        this.release_date=release_date;
       this.poster_path=poster_path;
        this.id=id;


    }
    public static final Parcelable.Creator<movies> CREATOR = new Parcelable.Creator<movies>() {
        public movies createFromParcel(Parcel in) {
            return new movies(in);
        }

        public movies[] newArray(int size) {
            return new movies[size];
        }
    };
    private movies(Parcel in) {
        original_title = in.readString();
        plot_synopsis = in.readString();
        user_rating=in.readString();
        release_date=in.readString();
        poster_path=in.readString();
        id=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(plot_synopsis);
        dest.writeString(user_rating);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(id);

    }


}


