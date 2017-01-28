package com.myownapps.yasser.moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static    List<movies> movies_content;
    public   moviesAdapter adp;
    public  RecyclerView recview;
    boolean has_data;



    String kind_of_sort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
         } else {
            mTwoPane = false;
        }
        if( savedInstanceState!=null&&mTwoPane)
        {

             has_data=true ;
            int x=savedInstanceState.getInt("position");
            Bundle argo = new Bundle();
            argo.putString("title", movies_content.get(x).original_title);
            argo.putString("poster path", movies_content.get(x).poster_path);
            argo.putString("releas date", movies_content.get(x).release_date);
            argo.putString("overview", movies_content.get(x).plot_synopsis);
            argo.putString("user rating", movies_content.get(x).user_rating);
            argo.putString("id", movies_content.get(x).id);
            argo.putString("kind_of_sort",kind_of_sort);
            argo.putBoolean("mTwoPane",mTwoPane);

moviedetailFragment fram=new moviedetailFragment();
            fram.setArguments(argo);
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fram)
                    .commit();





      /* fragment.setArguments(savedInstanceState);
         getFragmentManager().beginTransaction()
                 .replace(R.id.movie_detail_container, fragment)
                 .commit();
*/
        }




        movies_content =new ArrayList<>();
        adp =new moviesAdapter(this,movies_content);
        recview=(RecyclerView)findViewById(R.id.rec_movies);


    }
    public  boolean mTwoPane;

    @Override
    protected void onResume() {
        super.onResume();

        final String sorting[]={"/movie/popular","/movie/top_rated","favorite"};
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
         kind_of_sort=pref.getString(getString(R.string.pref_movie_key),getString(R.string.pref_defult_sorting));
        String sort_now="/movie/popular";
        recview.setLayoutManager(new GridLayoutManager(this, 2));

        if(kind_of_sort.equals("most popular"))
        {
if(mTwoPane&&getFragmentManager().findFragmentById(R.id.movie_detail_container)!=null&&has_data!=true)
{
    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.movie_detail_container)).commit();


}

            sort_now=sorting[0];
            getdata(sort_now,movies_content);





        }

        else if(kind_of_sort.equals("highest rated"))
        {
            if(mTwoPane&&getFragmentManager().findFragmentById(R.id.movie_detail_container)!=null&&has_data!=true)
            {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.movie_detail_container)).commit();


            }


            sort_now=sorting[1];
            getdata(sort_now,movies_content);



        }
        else if(kind_of_sort.equals("favorites")) {
            if(mTwoPane&&getFragmentManager().findFragmentById(R.id.movie_detail_container)!=null&&has_data!=true)
            {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.movie_detail_container)).commit();


            }
            Databease_movies db=new Databease_movies(this);
            ArrayList title=db.show_title();
            ArrayList plot_syn=db.show_plot_syn();
            ArrayList vote_avg=db.show_vote_avg();
            ArrayList release_date=db.show_release_date();
            ArrayList poster=db.show_poster_path();
            ArrayList id=db.show_id();
            movies_content.clear();

            for (int i = 0; i <title.size(); i++) {
                movies_content.add(i,new movies(title.get(i).toString(),//title
                        plot_syn.get(i).toString(),//plot_syn
                        vote_avg.get(i).toString(),//vote_avg
                        release_date.get(i).toString(),//release_date
                        poster.get(i).toString(),id.get(i).toString()));//poster path

            }
            //db.clear_all();
            adp =new moviesAdapter(this,movies_content);
            recview.setAdapter(adp);


        }

    }
    public void getdata(String sort_now, final List<movies> movies_content) {
        String first_base="http://api.themoviedb.org/3";
        String last_base="?api_key=cbc4dcf1540ca46e94a63ac9fea166d7";
        Ion.with(this)

                .load(first_base+sort_now+last_base)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String or, ov, v_a, r_d, poster_path,id;
                        movies_content.clear();
                        if (e == null) {
                            JsonArray j = result.getAsJsonArray("results");
                            for (int i = 0; i < j.size(); i++) {
                                poster_path = "http://image.tmdb.org/t/p/w185/" + ((JsonObject) j.get(i)).get("poster_path").toString().replace("\"", "");
                                or = ((JsonObject) j.get(i)).get("original_title").toString();
                                or=or.substring(1,or.length()-1);
                                ov = ((JsonObject) j.get(i)).get("overview").toString();
                                ov=ov.substring(1,ov.length()-1);
                                v_a = ((JsonObject) j.get(i)).get("vote_average").toString();

                                r_d = (((JsonObject) j.get(i)).get("release_date").toString());
                                r_d=r_d.substring(1,5);
                                id=(((JsonObject) j.get(i)).get("id").toString());

                                movies_content.add(i, new movies(or, ov, v_a, r_d, poster_path,id));
                            }
                        }
                        adp =new moviesAdapter(MainActivity.this,movies_content);
                        recview.setAdapter(adp);


                    }
                });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           startActivity(new Intent(this,settingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void onClick(int adapterPosition, List<movies> movie) {

        if(mTwoPane==true){
moviedetailFragment fragment=new moviedetailFragment();

        Bundle arg = new Bundle();
            arg.putString("title", movie.get(adapterPosition).original_title);
            arg.putString("poster path", movie.get(adapterPosition).poster_path);
            arg.putString("releas date", movie.get(adapterPosition).release_date);
            arg.putString("overview", movie.get(adapterPosition).plot_synopsis);
            arg.putString("user rating", movie.get(adapterPosition).user_rating);
            arg.putString("id", movie.get(adapterPosition).id);
            arg.putString("kind_of_sort",kind_of_sort);
            arg.putBoolean("mTwoPane",mTwoPane);


            fragment.setArguments(arg);
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();


        } else {
            Intent in = new Intent(this, moviedetail.class)
                    .putExtra("title", movie.get(adapterPosition).original_title)
                    .putExtra("poster path", movie.get(adapterPosition).poster_path)
                    .putExtra("releas date", movie.get(adapterPosition).release_date)
                    .putExtra("overview", movie.get(adapterPosition).plot_synopsis)
                    .putExtra("user rating", movie.get(adapterPosition).user_rating)
                    .putExtra("id", movie.get(adapterPosition).id);

            this.startActivity(in);
        }
    }


   @Override
    protected void onSaveInstanceState(Bundle outState) {
if(mTwoPane){

    outState.putInt("position",adp.pos);
 /*   outState.putString("title",movies_content.get(adp.pos).original_title);
    outState.putString("poster",movies_content.get(adp.pos).poster_path);
    outState.putString("releas date",movies_content.get(adp.pos).release_date);
    outState.putString(  "overview"   , movies_content.get(adp.pos).plot_synopsis);
    outState.putString( "user rating"     ,movies_content.get(adp.pos).user_rating);
    outState.putString( "id" ,   movies_content.get(adp.pos).id);
    super.onSaveInstanceState(outState);*/
   // outState.putParcelableArrayList("movies_list", (ArrayList<? extends Parcelable>) movies_content);
}


    }

 /*  @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

     if(mTwoPane){
        movies x=new movies(savedInstanceState.getString("title"),
                savedInstanceState.getString("title"),savedInstanceState.getString("title"),savedInstanceState.getString("title"),savedInstanceState.getString("title"),savedInstanceState.getString("title")
                );
        super.onRestoreInstanceState(savedInstanceState);
    }}
*/
}
