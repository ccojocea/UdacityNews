package com.example.ccojo.udacitynews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    // Strings used in the URL
    private static final String NEWS_URL = "world"; //NON-NLS
    private static final String FILM_URL = "film"; //NON-NLS
    private static final String SPORT_URL = "sport"; //NON-NLS
    private static final String CULTURE_URL = "culture"; //NON-NLS
    private static final String LIFESTYLE_URL = "lifeandstyle"; //NON-NLS
    public static final String SECTION = "section"; //NON-NLS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout news = findViewById(R.id.news_layout);
        LinearLayout sport = findViewById(R.id.sport_layout);
        LinearLayout film = findViewById(R.id.film_layout);
        LinearLayout culture = findViewById(R.id.culture_layout);
        LinearLayout lifestyle = findViewById(R.id.lifestyle_layout);

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
