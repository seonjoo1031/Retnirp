package com.example.chaeyoung.retnirp;

/**
 * Created by Chaeyoung on 2016-12-07.
 */
public class HistoryItem {
    private String image ;
    private String safetyStr ;
    private String dateStr;
    private String textColor;

    public void setImage(String icon) {
        image = icon ;
    }
    public void setSafety(String title) {
        safetyStr = title ;
    }
    public void setDate(String desc) {
        dateStr = desc ;
    }
    public void setTextColor(String color){
        textColor = color;
    }

    public String getImage() {
        return this.image ;
    }
    public String getSafety() {
        return this.safetyStr ;
    }
    public String getDate() {
        return this.dateStr ;
    }
    public String getTextColor(){
        return this.textColor;
    }
}

