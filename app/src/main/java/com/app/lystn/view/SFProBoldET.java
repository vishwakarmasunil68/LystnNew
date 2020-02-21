package com.app.lystn.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class SFProBoldET extends EditText {

    public SFProBoldET(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SFProBoldET(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SFProBoldET(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/sfprobold.ttf");
            setTypeface(tf);
        }
    }
}