package com.app.lystn.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class SFProRegularTV extends TextView {

    public SFProRegularTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SFProRegularTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFProRegularTV(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/sfpro_regular.ttf");
        setTypeface(tf );

    }
}