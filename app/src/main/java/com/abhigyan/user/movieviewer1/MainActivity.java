package com.abhigyan.user.movieviewer1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    LinearLayout flowLayout;
    LayoutInflater layoutInflater;
    private static String APIKEY="17d99bf38e7ffbebabfc4d9713b679a8";
    String BASE_URL = "https://api.themoviedb.org";
    View myView;
    String posterURL, posterpth;
    int moviedID;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query1) {

                flowLayout.removeAllViews();
                retro(query1);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.settingsOption:
                Toast.makeText(this, "settings has been selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.favOption:
                Toast.makeText(this, "favourites have been selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.latestOption:
                return true;

            default:
                return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       flowLayout = findViewById(R.id.flowLayout);
    }

    public void retro(String query)
    {
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

        movieAPI MovieAPI = retrofit.create(movieAPI.class);
        Call<MovieInformation> call = MovieAPI.getMovies(APIKEY,query);
        call.enqueue(new Callback<MovieInformation>() {
            @Override
            public void onResponse(Call<MovieInformation> call, Response<MovieInformation> response) {

                MovieInformation movie = response.body();
                List<MovieInformation.Result> list = movie.getResults();
                for(int i=0;i<list.size();i++) {
                    MovieInformation.Result cinema = list.get(i);
                    String title = cinema.getTitle();
                    Log.i("name-********************** ", title);
                    String releaseDate = cinema.getReleaseDate();
                    String lan = cinema.getOriginalLanguage();
                    String overview = cinema.getOverview();
                    Boolean a = cinema.getAdult();
                    moviedID = cinema.getId();
                    String adult;
                    if (a == true) {
                        adult = "18+";
                    } else {
                        adult = "0+";
                    }
                    posterURL = cinema.getPosterPath();
                    Log.i("poster url-********************** ", posterURL);
                    showLayout(title, releaseDate, lan, overview, adult, posterpth);
                }
            }
            @Override
            public void onFailure(Call<MovieInformation> call, Throwable t) {

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("abhigyans error", t.getMessage());
            }
        });
    }

    public void showLayout(String title, String releaseDate, String language, String overview, String Adult, String posterpth) {
        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myView = layoutInflater.inflate(R.layout.test_ui, flowLayout, false);

        //setting data for each layout
        TextView movieTitle = myView.findViewById(R.id.titleText);
        movieTitle.setText("Title- " + title);
        TextView languageText = myView.findViewById(R.id.languageText);
        languageText.setText("Language- " + language);
        TextView ReleaseDate = myView.findViewById(R.id.genreText);
        ReleaseDate.setText("Release Date- " + releaseDate);
        TextView overviewText = myView.findViewById(R.id.overviewText);
        overviewText.setText("Overview- " + overview);
        TextView adultText = myView.findViewById(R.id.adultInfo);
        adultText.setText("Adult- " + Adult);
        ImageView img = myView.findViewById(R.id.posterView);
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+posterURL).fit().into(img);

        LinearLayout linearLayout= myView.findViewById(R.id.clickLinearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.i("done", "done");
               Intent completeInfoIntent = new Intent(getApplicationContext(), CompleteInformationActivity.class);
               completeInfoIntent.putExtra("movieid", moviedID);
               startActivity(completeInfoIntent);
           }
        });
        flowLayout.addView(myView);
    }
    public void searchFunction(View view)
    {

    }
}

