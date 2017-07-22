package com.example.starhood.newsapp;

/**
 * Created by Starhood on 7/3/17.
 */

public final class News {

    private String aTitle;
    private String aURL;
    private String aType;
    private String aDate;
    private String aTime;
    private String aSection;


    public News(String Title, String Type, String Time, String Date, String Section, String URL) {
        aDate = Date;
        aTitle = Title;
        aType = Type;
        aTime = Time;
        aSection = Section;
        aURL = URL;
    }


    public String getaDate() {
        return aDate;
    }

    public String getaTitle() {
        return aTitle;
    }

    public String getaURL() {
        return aURL;
    }

    public String getaType() {
        return aType;
    }

    public String getaTime() {
        return aTime;
    }

    public String getaSection() {
        return aSection;
    }
}
