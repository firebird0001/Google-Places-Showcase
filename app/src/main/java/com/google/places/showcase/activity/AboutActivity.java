package com.google.places.showcase.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.google.places.showcase.R;
import com.google.places.showcase.utils.CommonUtil;

/**
 * Simple about activity
 */
public class AboutActivity extends ActionBarActivity {

    private static final String ABOUT_ASSET_NAME = "about.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        setAboutText();
    }

    private void setAboutText() {
        String aboutString = CommonUtil.readAssetFileToString(this, ABOUT_ASSET_NAME);
        ((TextView) findViewById(R.id.about_text_view)).setText(aboutString);
    }
}
