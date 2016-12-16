package com.example.chaeyoung.retnirp;

/**
 * Created by Chaeyoung on 2016-12-07.
 */
import android.graphics.drawable.Drawable;

public class HistoryItem {
    private Drawable iconDrawable ;
    private String safetyStr ;
    private String dateStr;
    private String textColor;

    public void setImage(Drawable icon) {
        iconDrawable = icon ;
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

    public Drawable getImage() {
        return this.iconDrawable ;
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

