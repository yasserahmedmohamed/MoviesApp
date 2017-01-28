package com.myownapps.yasser.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Yasser on 9/10/2016.
 */

public class Databease_movies extends SQLiteOpenHelper {
    public static final String DB_movies="my_favorite_movies.db";
    public static final int version=1;
    public Databease_movies(Context context) {
        super(context, DB_movies, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL("create table IF NOT EXISTS favorite_movies (title TEXT,plot_syn TEXT,vote_avg TEXT,release_date TEXT,poster_path TEXT,id TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if EXISTS favorite_movies");
        onCreate(db);

    }
    public void Add_movies_to_favorite(  String title,String plot_syn,String vote_avg,String release_date,String poster_path,String id)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues conval=new ContentValues();
        conval.put("title",title);
        conval.put("plot_syn",plot_syn);
        conval.put("vote_avg",vote_avg);
        conval.put("release_date",release_date);
        conval.put("poster_path",poster_path);
        conval.put("id",id);


        db.insert("favorite_movies",null,conval);
    }
    public ArrayList show_title()
    {
        ArrayList arr_list=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select title from favorite_movies",null);
        rec.moveToFirst();
        while (rec.isAfterLast()==false)
        {
            arr_list.add(rec.getString(rec.getColumnIndex("title")));
            rec.moveToNext();
        }


        return arr_list;
    }

    public ArrayList show_plot_syn()
    {
        ArrayList arr_list=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select plot_syn from favorite_movies",null);
        rec.moveToFirst();
        while (rec.isAfterLast()==false)
        {
            arr_list.add(rec.getString(rec.getColumnIndex("plot_syn")));


            rec.moveToNext();
        }


        return arr_list;
    }
    public ArrayList show_vote_avg()
    {
        ArrayList arr_list=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select vote_avg from favorite_movies",null);
        if(rec.moveToFirst())
        while (rec.isAfterLast()==false)
        {
            arr_list.add(rec.getString(rec.getColumnIndex("vote_avg")));


            rec.moveToNext();
        }


        return arr_list;
    }
    public ArrayList show_release_date()
    {
        ArrayList arr_list=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select release_date from favorite_movies",null);
        rec.moveToFirst();
        while (rec.isAfterLast()==false)
        {
           arr_list.add(rec.getString(rec.getColumnIndex("release_date")));


            rec.moveToNext();
        }


        return arr_list;
    }
    public ArrayList show_poster_path()
    {
        ArrayList arr_list=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select poster_path from favorite_movies",null);
        rec.moveToFirst();
        while (rec.isAfterLast()==false)
        {
            arr_list.add(rec.getString(rec.getColumnIndex("poster_path")));


            rec.moveToNext();
        }


        return arr_list;
    }

    public ArrayList show_id()
    {
        ArrayList arr_list=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select id from favorite_movies",null);
        rec.moveToFirst();
        while (rec.isAfterLast()==false)
        {
            arr_list.add(rec.getString(rec.getColumnIndex("id")));


            rec.moveToNext();
        }


        return arr_list;
    }

    /*  public ArrayList show_movies()
    {
        ArrayList arr_list=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select * from favorite_movies",null);
        rec.moveToFirst();
        while (rec.isAfterLast()==false)
        {
            arr_list.add(rec.getString(rec.getColumnIndex("title")));
            arr_list.add(rec.getString(rec.getColumnIndex("plot_syn")));
            arr_list.add(rec.getString(rec.getColumnIndex("vote_avg")));
            arr_list.add(rec.getString(rec.getColumnIndex("release_date")));
            arr_list.add(rec.getString(rec.getColumnIndex("poster_path")));


            rec.moveToNext();
        }


        return arr_list;
    }


*/

    public void delete_movie(String del)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM favorite_movies WHERE id = "+del);
    }
    public void clear_all(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favorite_movies", null, null);
    }
}
