package com.example.bernie.carepointsv2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class EnterCarePoints extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_care_points);
        Intent intent = getIntent();
        ArrayList<String> options = intent.getStringArrayListExtra("Options");
        System.out.println("start");
        for (String s : options) {
            System.out.println(s);
        }
        System.out.println("end");
    }
}
