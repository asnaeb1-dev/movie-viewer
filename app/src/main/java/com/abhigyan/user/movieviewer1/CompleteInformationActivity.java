package com.abhigyan.user.movieviewer1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//https://api.themoviedb.org/3/movie/343611?api_key={api_key} ****Query with ID (343611 is the ID)
public class CompleteInformationActivity extends AppCompatActivity {

    private static String BASE_URL = "https://api.themoviedb.org";
    private static String APIKEY = "17d99bf38e7ffbebabfc4d9713b679a8";
    String movieID;
    String posterpath;

    TextView titleinfoText, languageinfoText, ratinginfoText, overviewinfoText, adultinfoText, releaseinfoText, reviewText;
    ImageView posterinfoView;

    public void addFavourite(View view)
    {
        Intent gotofavourite = new Intent(getApplicationContext(),FavouritesActivity.class);
        Snackbar.make(view, "FloatingActionButton is clicked", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
        startActivity(gotofavourite);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_information);

        titleinfoText = findViewById(R.id.infotitleText);
        languageinfoText = findViewById(R.id.infoLanguageText);
        ratinginfoText = findViewById(R.id.ratingText);
        overviewinfoText = findViewById(R.id.infoOverviewText);
        adultinfoText = findViewById(R.id.infoAdultText);
        releaseinfoText = findViewById(R.id.inforeleaseDate);
        reviewText = findViewById(R.id.reviewText);
        posterinfoView = findViewById(R.id.infoPosterView);

        Intent movieIDgetter = getIntent();
        movieID = String.valueOf(movieIDgetter.getIntExtra("movieid",0));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        completeInfoInterfaceAPI CompleteInfoInterfaceAPI = retrofit.create(completeInfoInterfaceAPI.class);

        Call<Example> call = CompleteInfoInterfaceAPI.getMovie(APIKEY);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                Example movie = response.body();
                String title = movie.getTitle();
                titleinfoText.setText("Title- "+title);
                String language = movie.getOriginalLanguage();
                languageinfoText.setText("Language- "+language);
                String releaseDate = movie.getReleaseDate();
                releaseinfoText.setText("Release Date- "+releaseDate);
                String adult;
                Boolean a = movie.getAdult();
                if(a==true)
                {
                    adult = "18+";
                }
                else
                {
                    adult = "0+";
                }
                adultinfoText.setText("Adult- "+adult);
                String rating = String.valueOf(movie.getVoteAverage());
                ratinginfoText.setText("Rating- "+ rating);
                String overview = movie.getOverview();
                overviewinfoText.setText("Overview- "+ overview);
                posterpath = movie.getPosterPath();
                Log.i("poster path*****************", posterpath);
                Picasso.get().load("https://image.tmdb.org/t/p/w500"+posterpath).into(posterinfoView);
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("abhigyans error", t.getMessage());
            }
        });
     }
}
