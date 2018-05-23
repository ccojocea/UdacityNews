package com.example.ccojo.udacitynews;

/**
 * Created by ccojo on 5/23/2018.
 */

public class News {
    private String webTitle;
    private String sectionName;
    private String webPublicationDate;
    private String webUrl;
    private String thumbnailUrl;
    private String byline;
    private String bodyText;

    public News(String webTitle, String sectionName, String webPublicationDate, String webUrl, String thumbnailUrl, String byline, String bodyText) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.byline = byline;
        this.bodyText = bodyText;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getByline() {
        return byline;
    }

    public String getBodyText() {
        return bodyText;
    }
}
