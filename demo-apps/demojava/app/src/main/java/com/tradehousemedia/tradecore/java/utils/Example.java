package com.tradehousemedia.tradecore.java.utils;

import android.app.Activity;
import android.view.View;


public class Example {

    public String title;
    public Class classActivity;

    public Example(String title, Class activityClass) {
        this.title = title;
        this.classActivity = activityClass;
    }

    @Override
    public String toString() {
        return title;
    }
}