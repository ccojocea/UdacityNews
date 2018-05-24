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
    private String byline;
    private String bodyText;

    News(String webTitle, String sectionName, String webPublicationDate, String webUrl, String thumbnailUrl, String byline, String bodyText) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.byline = byline;
        this.bodyText = bodyText;
    }

    String getWebTitle() {
        return webTitle;
    }

    String getSectionName() {
        return sectionName;
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

    String getByline() {
        return byline;
    }

    String getBodyText() {
        return bodyText;
    }
}
