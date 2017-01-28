package com.myownapps.yasser.moviesapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class moviedetailFragment extends Fragment {


    String title,poster_path,relreas_date,overview,user_rating,id;
    Button add_movie;
    boolean at_favorite;
    Databease_movies db;
    LinearLayout lin_rev;


    ArrayList <String> movie_reviews;
    ArrayList <String> movie_trailers_names,movie_trailers_path;
    ListView triview;
    ArrayAdapter arr_tri;

    public moviedetailFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview= inflater.inflate(R.layout.fragment_moviedetail, container, false);
        final Bundle arg=getArguments();
        title=arg.getString("title");
        poster_path=arg.getString("poster path");
        relreas_date=arg.getString("releas date");
        lin_rev=(LinearLayout)rootview.findViewById(R.id.reviews_view);
        overview= arg.getString("overview");
        user_rating= arg.getString("user rating");
        id=arg.getString("id");
        ((TextView)rootview.findViewById(R.id.movie_title_detail)).setText(title);
        ((TextView)rootview.findViewById(R.id.release_date_detail)).setText(relreas_date);
        ((TextView)rootview.findViewById(R.id.over_view_detail)).setText(overview);
        ((TextView)rootview.findViewById(R.id.user_rating_detail)).setText(user_rating+"/10");
        ImageView poster=(ImageView)rootview.findViewById(R.id.movie_poster_detail);
        add_movie=(Button)rootview.findViewById(R.id.add_to_favorite) ;
        db=new Databease_movies(getActivity());



        movie_reviews = new ArrayList<>();
        getreviwes(id);

        movie_trailers_names = new ArrayList<>();
        movie_trailers_path = new ArrayList<>();


        triview = (ListView) rootview.findViewById(R.id.trailers_list);
        gettrailers(id);
        triview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movie_trailers_path.get(position))));
            }
        });



        Picasso.with( getActivity()).load(poster_path).into(poster);



        ArrayList if_exe=db.show_title();

        for (int i = 0; i <if_exe.size() ; i++) {
            if(title.equals(if_exe.get(i).toString())){

                add_movie.setText("Remove from favorite");
                at_favorite=true;
                break;
            }

        }

        add_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(at_favorite){
                    db.delete_movie(id);
                    Toast.makeText(getActivity(), title + " removed from the Favorites movies", Toast.LENGTH_SHORT).show();

                    add_movie.setText("Add to favorite");
                    at_favorite=false;

                }
                else {
                    add_movie.setText("Remove from favorite");
                    db.Add_movies_to_favorite(title, overview, user_rating, relreas_date, poster_path,id);
                    Toast.makeText(getActivity(), title + " Added to the Favorites movies", Toast.LENGTH_SHORT).show();
                    at_favorite=true;
                }

            }
        });

        return rootview;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void getreviwes(String id ){
        String first_base="http://api.themoviedb.org/3/movie/";
        String last_base_review="/reviews?api_key=cbc4dcf1540ca46e94a63ac9fea166d7";

        Ion.with(this)
                .load(first_base+id+last_base_review)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e == null)
                        {
                            JsonArray j = result.getAsJsonArray("results");
                            String auth,rev;
                            movie_reviews.clear();
                            for(int i=0 ; i< j.size() ; i++) {
                                auth = ((JsonObject) j.get(i)).get("author").toString();
                                auth=auth.substring(1,auth.length()-2);
                                rev= ((JsonObject) j.get(i)).get("content").toString();
                                rev=rev.substring(1,rev.length()-2);
                                movie_reviews.add(i,auth +"  SAY:  "+rev);
                            }

                            for(String review:movie_reviews)
                            {

                                View view =LayoutInflater.from(getActivity()).inflate(R.layout.reviews,null);
                                TextView tex=(TextView)view.findViewById(R.id.auther_text);
                                tex.setText(review);

                                lin_rev.addView(view);


                            }

                        }
                    }

                });



    }


    public void gettrailers(String id)
    {
        String first_base="http://api.themoviedb.org/3/movie/";
        String last_base_video="/videos?api_key=cbc4dcf1540ca46e94a63ac9fea166d7";

        Ion.with(this)
                .load(first_base+id+last_base_video)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(e == null)
                        {
                            JsonArray j = result.getAsJsonArray("results");
                            String keys_of_trailer, trailer_names;


                            for(int i=0 ; i< j.size() ; i++) {
                                keys_of_trailer=((JsonObject) j.get(i)).get("key").toString();
                                keys_of_trailer=keys_of_trailer.substring(1,keys_of_trailer.length()-1);
                                trailer_names=((JsonObject) j.get(i)).get("name").toString();
                                trailer_names=trailer_names.substring(1,trailer_names.length()-1);
                                movie_trailers_names.add(i,trailer_names);
                                movie_trailers_path.add(i,keys_of_trailer);


                            }
                            arr_tri=new ArrayAdapter(getActivity(),R.layout.trailers,R.id.trailer_text,movie_trailers_names);
                            triview.setAdapter(arr_tri);
                            setListViewHeightBasedOnItems(triview);
                        }
                    }
                });
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom() ;

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding+50;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }
    }

}
