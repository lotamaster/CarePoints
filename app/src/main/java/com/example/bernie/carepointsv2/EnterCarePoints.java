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

public class EnterCarePoints extends Activity {

    private LinearLayout optionsLayout;
    HashMap<String, EditText> currentCarePoints;
    HashMap<String, Integer> carePointsValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_care_points);
        optionsLayout = findViewById(R.id.optionsLayout);
        FloatingActionButton addPersonButton = findViewById(R.id.addPersonButton);
        FloatingActionButton finishButton = findViewById(R.id.finishButton);
        createLayout();

        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { nextScreen(EnterCarePoints.class, true);
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {nextScreen(Results.class, false);
            }
        });
    }

    private void createLayout() {
        Intent intent = getIntent();
        carePointsValues = (HashMap<String, Integer>)intent.getSerializableExtra("Options");
        ArrayList<String> options = new ArrayList<>(carePointsValues.keySet());
        currentCarePoints = new HashMap<>();

        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            CardView cardView = new CardView(EnterCarePoints.this);
            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)cardView.getLayoutParams();
            int pixels = getPixels(5);
            params.setMargins(pixels, pixels, pixels, pixels);

            LinearLayout firstCardLayout = new LinearLayout(EnterCarePoints.this);
            firstCardLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            params = (ViewGroup.MarginLayoutParams)firstCardLayout.getLayoutParams();
            pixels = getPixels(10);
            params.setMargins(pixels, pixels, pixels, pixels);
            firstCardLayout.setOrientation(LinearLayout.VERTICAL);

            final TextView optionText = new TextView(EnterCarePoints.this);
            optionText.setText("Option " + (i+1) + ": " + option);
            firstCardLayout.addView(optionText);

            final EditText newOptionText = new EditText(EnterCarePoints.this);
            newOptionText.setInputType(InputType.TYPE_CLASS_NUMBER);
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            newOptionText.setLayoutParams(linearParams);
            firstCardLayout.addView(newOptionText);

            cardView.addView(firstCardLayout);
            optionsLayout.addView(cardView);
            currentCarePoints.put(option, newOptionText);
        }
    }

    private void nextScreen(Class nextActivity, boolean anotherPerson) {
        Intent currentIntent = getIntent();
        Intent intent = new Intent(this, nextActivity);

        for (String option : currentCarePoints.keySet()) {
            int newValue = Integer.parseInt(currentCarePoints.get(option).getText().toString());
            int oldValue = carePointsValues.get(option);
            carePointsValues.remove(option);
            carePointsValues.put(option, (newValue + oldValue));
        }

        int numberOfPeople = currentIntent.getIntExtra("numberOfPeople", 1);

        if (anotherPerson) {
            numberOfPeople++;
        }

        intent.putExtra("numberOfPeople", numberOfPeople);
        intent.putExtra("Options", carePointsValues);
        startActivity(intent);
    }

    private int getPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
