package com.example.ccojo.udacitynews;

/**
 * Created by ccojo on 5/23/2018.
 */

class News {
    private String webTitle;
    private String sectionName;
    private String webPublicationDate;
    private String webUrl;
    private String thumbnailUrl;
    private String bodyText;
    private String byline;

    News(String webTitle, String sectionName, String webPublicationDate, String webUrl, String thumbnailUrl, String byline, String bodyText) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.bodyText = bodyText;
        this.byline = byline;
    }

    String getWebTitle() {
        return webTitle;
    }

    String getSectionName() {
        return sectionName;
    }

    public String getByline() {
        return byline;
    }

    String getWebPublicationDate() {
        return webPublicationDate;
    }

    String getWebUrl() {
        return webUrl;
    }

    String getThumbnailUrl() {
        return thumbnailUrl;
    }

    String getBodyText() {
        return bodyText;
    }
}
