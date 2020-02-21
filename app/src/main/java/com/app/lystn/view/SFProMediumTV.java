package com.app.lystn.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class SFProMediumTV extends TextView {

    public SFProMediumTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SFProMediumTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFProMediumTV(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/sfpromedium.ttf");
        setTypeface(tf );

    }
}