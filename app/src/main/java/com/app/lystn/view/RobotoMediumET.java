package com.app.lystn.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class RobotoMediumET extends EditText {

    public RobotoMediumET(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoMediumET(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoMediumET(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_medium.ttf");
        setTypeface(tf );

    }
}