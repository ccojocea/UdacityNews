package com.example.ccojo.udacitynews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.ion.Ion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ccojo on 5/23/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String E_DD_MMM_YYYY = "E dd, MMM, yyyy";
    private static final String HH_MM_SS_A = "HH:mm:ss a";
    private static final String OF = " of ";

    private static final String TAG = NewsAdapter.class.getSimpleName();

    NewsAdapter(@NonNull Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        News currentNews = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        TextView dateTV = convertView.findViewById(R.id.date);
        TextView timeTV = convertView.findViewById(R.id.time);
        TextView newsTitle = convertView.findViewById(R.id.webtitle);
        TextView newsBody = convertView.findViewById(R.id.body);
        TextView byline = convertView.findViewById(R.id.byline);
        TextView section = convertView.findViewById(R.id.section);
        ImageView thumbnailView = convertView.findViewById(R.id.thumbnail);

        //set the news article title
        newsTitle.setText(currentNews.getWebTitle());

        //set the news article body
        newsBody.setText(currentNews.getBodyText());

        //set the byline
        byline.setText(currentNews.getByline());

        //set the section name
        section.setText(currentNews.getSectionName());

        //set date and time
        String currentNewsDateTime = currentNews.getWebPublicationDate();
        Date dt = new Date();
        setTime(timeTV, dt);
        setDate(dateTV, dt);

        String dateTimeString = "2018-05-22T21:45:45Z";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date date;
        try {
            date = sdf.parse(dateTimeString);
            String newDateString = sdf.format(date);
            Log.d(TAG, "SIMPLE DATE FORMAT: " + newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(currentNews.getThumbnailUrl() != null){
            Ion.with(thumbnailView)
                    .placeholder(null)
                    .error(null)
                    .animateLoad(R.anim.spin_anim)
                    .animateIn(R.anim.fade_anim)
                    .load(currentNews.getThumbnailUrl());
        } else {
            thumbnailView.setImageResource(R.drawable.image_placeholder);
        }

        return convertView;
    }

    private void setDate(TextView view, Date dt){
        SimpleDateFormat formatter = new SimpleDateFormat(E_DD_MMM_YYYY, Locale.getDefault());
        String date = formatter.format(dt);
        view.setText(date);
    }

    private void setTime(TextView view, Date dt){
        SimpleDateFormat formatter = new SimpleDateFormat(HH_MM_SS_A, Locale.getDefault());
        String time = formatter.format(dt);
        view.setText(time);
    }
}