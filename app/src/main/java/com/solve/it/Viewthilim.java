package com.solve.it;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Viewthilim extends Activity implements Constants {
    float textSize;
    ScrollView sc;
    TextView tv;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras().getBoolean(TEXT_BLACK))
            setContentView(R.layout.view_thilim_new_white);
        else
            setContentView(R.layout.view_thilim_new);
        tv = (TextView) findViewById(R.id.thilimtext);
        sc = (ScrollView) findViewById(R.id.scrollViewThilim);
        setSettings();
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    private void setSettings() {
        //change font
        String fontName = getIntent().getExtras().getString(FONT_TYPE);
        if (fontName.contains("ttf")) {
            Typeface hebFont = Typeface.createFromAsset(getAssets(), fontName);
            tv.setTypeface(hebFont);
        }
        //font size
        String fontSize = getIntent().getExtras().getString(FONT_SIZE);
        try {
            textSize = Float.parseFloat(fontSize);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_float, Toast.LENGTH_LONG).show();
        }
        text = getIntent().getExtras().getString(TEXT_KEY);
        boolean rightToLeft = getIntent().getExtras().getBoolean(RIGHT_TO_LEFT);
        if (rightToLeft)
            tv.setGravity(Gravity.RIGHT);
        else
            tv.setGravity(Gravity.LEFT);
        tv.setText(text, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    private void saveSettings() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        textSize = pixelsToSp(getApplicationContext(), tv.getTextSize());
        editor.putFloat(ACT_SAVE_FONT_KEY, textSize);
        editor.apply();
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        textSize = sharedPreferences.getFloat(ACT_SAVE_FONT_KEY, DEFAULT_FONT_SIZE_FLOTE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
    }

    @Override
    protected void onRestoreInstanceState(Bundle state){
        super.onRestoreInstanceState(state);
        final int pos_y = state.getInt("baseline_y", 0);
        final int pos_x = state.getInt("baseline_x", 0);
        sc.post(new Runnable() {
            @Override
            public void run() {
                sc.scrollTo(pos_x, pos_y);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putInt("baseline_y",sc.getScrollY());
        state.putInt("baseline_x",sc.getScrollX());
    }
    @Override
    protected void onRestart(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        loadSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.zoomIn:
                zoomIn();
                return true;
            case R.id.zoomOut:
                zoomOut();
                return true;
            case R.id.zoomReset:
                tv.setTextSize(DEFAULT_FONT_SIZE_FLOTE);
                saveSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void zoomOut() {
        if (textSize > 2)
            textSize -= 1;
        tv.setTextSize(textSize);
        saveSettings();
    }

    private void zoomIn() {
        textSize += 1;
        tv.setTextSize(textSize);
        saveSettings();
    }
}
