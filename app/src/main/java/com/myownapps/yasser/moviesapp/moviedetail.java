package com.myownapps.yasser.moviesapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

public class moviedetail extends AppCompatActivity {



    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);
        Intent in=getIntent();
        Bundle arg=new Bundle();
        arg.putString("title", in.getStringExtra("title"));
        arg.putString("poster path",in.getStringExtra("poster path"));
        arg.putString("releas date",in.getStringExtra("releas date"));
        arg.putString("overview",in.getStringExtra("overview"));
        arg.putString("user rating",in.getStringExtra("user rating"));
        arg.putString("id",in.getStringExtra("id"));


        if (savedInstanceState==null){
moviedetailFragment  fragment=new moviedetailFragment();
            fragment.setArguments(arg);
            getFragmentManager().beginTransaction().add(R.id.movie_detail_container,fragment).commit();
        }

    }

}
