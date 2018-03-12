package com.example.bernie.carepointsv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class Results extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        LinearLayout optionsLayout = findViewById(R.id.optionsLayout);
        TextView winnerText = findViewById(R.id.winnerText);
        TextView numberOfPeopleText = findViewById(R.id.numberOfPeopleText);
        Intent intent = getIntent();
        HashMap<String, Integer> carePointsValues = (HashMap<String, Integer>)intent.getSerializableExtra("Options");
        ArrayList<String> options = new ArrayList<>(carePointsValues.keySet());
        int maxValue = 0;
        String winningOption = "";

        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);

            if (carePointsValues.get(option) > maxValue) {
                maxValue = carePointsValues.get(option);
                winningOption = option;
            }

            CardView cardView = new CardView(Results.this);
            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)cardView.getLayoutParams();
            int pixels = getPixels(5);
            params.setMargins(pixels, pixels, pixels, pixels);

            LinearLayout cardLayout = new LinearLayout(Results.this);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            params = (ViewGroup.MarginLayoutParams)cardLayout.getLayoutParams();
            pixels = getPixels(10);
            params.setMargins(pixels, pixels, pixels, pixels);
            cardLayout.setOrientation(LinearLayout.VERTICAL);

            TextView optionText = new TextView(Results.this);
            optionText.setText("Option " + (i+1) + ": " + option);
            cardLayout.addView(optionText);

            TextView optionValue = new TextView(Results.this);
            optionValue.setText(carePointsValues.get(option) + " points");
            cardLayout.addView(optionValue);

            cardView.addView(cardLayout);
            optionsLayout.addView(cardView);
        }

        winnerText.setText(winningOption + " with " + maxValue + " points!");
        numberOfPeopleText.setText(intent.getIntExtra("numberOfPeople", 0) + " people voted");

        FloatingActionButton restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartButtonClick();
            }
        });
    }

    private void restartButtonClick() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private int getPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
