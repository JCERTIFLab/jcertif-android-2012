package com.jcertif.android.app;

import com.jcertif.android.mobile.R;

import android.app.Activity;
import android.os.Bundle;

public class JCertifMobileActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}