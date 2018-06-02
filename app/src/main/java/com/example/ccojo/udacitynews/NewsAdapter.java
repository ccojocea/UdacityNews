package com.example.ccojo.udacitynews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

class NewsAdapter extends ArrayAdapter<News> {
    //View lookup cache
    private static class ViewHolder {
        TextView dateTV;
        TextView timeTV;
        TextView newsTitle;
        TextView newsBody;
        TextView byline;
        TextView section;
        ImageView thumbnailView;
    }

    private static final String TAG = NewsAdapter.class.getSimpleName() + "DEBUG";
    private static final String E_DD_MMM_YYYY = "E dd, MMM, yyyy"; //NON-NLS
    private static final String HH_MM_SS_A = "HH:mm:ss a"; //NON-NLS
    private static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"; //NON-NLS

    NewsAdapter(@NonNull Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        News currentNews = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.news_list_item, parent, false);

            viewHolder.dateTV = convertView.findViewById(R.id.date);
            viewHolder.timeTV = convertView.findViewById(R.id.time);
            viewHolder.newsTitle = convertView.findViewById(R.id.webtitle);
            viewHolder.newsBody = convertView.findViewById(R.id.body);
            viewHolder.byline = convertView.findViewById(R.id.byline);
            viewHolder.section = convertView.findViewById(R.id.section);
            viewHolder.thumbnailView = convertView.findViewById(R.id.thumbnail);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object into the template view.

        if (currentNews != null) {
            //set the news article title
            viewHolder.newsTitle.setText(!currentNews.getWebTitle().equals("") ? currentNews.getWebTitle() : getContext().getString(R.string.no_title));

            //set the news article body
            viewHolder.newsBody.setText(!currentNews.getBodyText().equals("") ? currentNews.getBodyText() : getContext().getString(R.string.no_body));

            //set the section name
            viewHolder.section.setText(!currentNews.getSectionName().equals("") ? currentNews.getSectionName() : getContext().getString(R.string.no_section));

            //set the author name
            if (!currentNews.getByline().equals("")) {
                viewHolder.byline.setText(currentNews.getByline());
            } else {
                viewHolder.byline.setVisibility(View.GONE);
            }

            //set date and time
            String dateTimeString = !currentNews.getWebPublicationDate().equals("") ? currentNews.getWebPublicationDate() : null;
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_Z, Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTimeString);
                setTime(viewHolder.timeTV, date);
                setDate(viewHolder.dateTV, date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (currentNews.getThumbnailUrl() != null) {
                viewHolder.thumbnailView.setVisibility(View.VISIBLE);
                Ion.with(viewHolder.thumbnailView)
                        .placeholder(null)
                        .error(null)
                        .animateLoad(null)
                        .animateIn(R.anim.fade_anim)
                        .load(currentNews.getThumbnailUrl());
            } else {
                viewHolder.thumbnailView.setVisibility(View.GONE);
            }
        }

        // return the completed view to render on screen
        return convertView;
    }

    private void setDate(TextView view, Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat(E_DD_MMM_YYYY, Locale.getDefault());
        String date = formatter.format(dt);
        view.setText(date);
    }

    private void setTime(TextView view, Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat(HH_MM_SS_A, Locale.getDefault());
        String time = formatter.format(dt);
        view.setText(time);
    }
}