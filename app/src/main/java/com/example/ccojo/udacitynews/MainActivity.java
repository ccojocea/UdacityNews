package com.example.ccojo.udacitynews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private static final String NEWS_URL = "section=news";
    private static final String FILM_URL = "section=film";
    private static final String SPORT_URL = "section=sport";
    private static final String CULTURE_URL = "section=culture";
    private static final String LIFESTYLE_URL = "section=lifeandstyle";
    public static final String SECTION = "section";

    LinearLayout news;
    LinearLayout sport;
    LinearLayout film;
    LinearLayout culture;
    LinearLayout lifestyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news = findViewById(R.id.news_layout);
        sport = findViewById(R.id.sport_layout);
        film = findViewById(R.id.film_layout);
        culture = findViewById(R.id.culture_layout);
        lifestyle = findViewById(R.id.lifestyle_layout);

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra(SECTION, NEWS_URL);
                startActivity(intent);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra(SECTION, SPORT_URL);
                startActivity(intent);
            }
        });

        film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra(SECTION, FILM_URL);
                startActivity(intent);
            }
        });

        culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra(SECTION, CULTURE_URL);
                startActivity(intent);
            }
        });

        lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra(SECTION, LIFESTYLE_URL);
                startActivity(intent);
            }
        });
    }
}
