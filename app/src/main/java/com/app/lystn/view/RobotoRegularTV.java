package com.app.lystn.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class RobotoRegularTV extends TextView {

    public RobotoRegularTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoRegularTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoRegularTV(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf");
        setTypeface(tf );

    }
}