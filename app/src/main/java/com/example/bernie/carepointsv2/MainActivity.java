package com.example.bernie.carepointsv2;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private RelativeLayout layout;
    private LinearLayout optionsLayout;
    private TextView welcomeText;
    private Map<ImageButton, CardView> allCardViews;
    private Map<CardView, EditText> allOptions;
    private FloatingActionButton addOptionButton;
    private FloatingActionButton continueButton;
    private Map<LinearLayout, ImageButton> removeButtons;
    private List<TextView> optionsTexts;
    private Boolean showRemoveButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.layout);
        optionsLayout = (LinearLayout) findViewById(R.id.optionsLayout);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        allCardViews = new HashMap<ImageButton, CardView>();
        allOptions = new HashMap<CardView, EditText>();
        removeButtons = new HashMap<LinearLayout, ImageButton>();
        optionsTexts = new ArrayList<TextView>();
        showRemoveButtons = false;

        continueButton = (FloatingActionButton) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueButtonClick();
            }
        });

        addOptionButton = (FloatingActionButton) findViewById(R.id.addOptionButton);
        ((RelativeLayout.LayoutParams)continueButton.getLayoutParams()).addRule(RelativeLayout.LEFT_OF, addOptionButton.getId());
        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addButtonClick();
            }
        });

        addButtonClick();
    }

    private void continueButtonClick() {
        Intent intent = new Intent(this, EnterCarePoints.class);
        ArrayList<String> options = new ArrayList<String>();

        for (EditText editText : allOptions.values()) {
            options.add(editText.getText().toString());
        }

        intent.putExtra("Options", options);
        startActivity(intent);
    }

    private void addButtonClick() {
        if (allCardViews.size() > 0) {
            welcomeText.setText("When you are done adding options, continue to start assigning Care Points!");
        }

        CardView cardView = new CardView(MainActivity.this);
        cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)cardView.getLayoutParams();
        int pixels = getPixels(5);
        params.setMargins(pixels, pixels, pixels, pixels);

        LinearLayout firstCardLayout = new LinearLayout(MainActivity.this);
        firstCardLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params = (ViewGroup.MarginLayoutParams)firstCardLayout.getLayoutParams();
        pixels = getPixels(10);
        params.setMargins(pixels, pixels, pixels, pixels);
        firstCardLayout.setOrientation(LinearLayout.VERTICAL);

        final TextView optionText = new TextView(MainActivity.this);
        int numOption = allOptions.size() + 1;
        optionText.setText("Option " + numOption);
        firstCardLayout.addView(optionText);
        optionsTexts.add(optionText);


        final LinearLayout cardLayout = new LinearLayout(MainActivity.this);
        cardLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        params = (ViewGroup.MarginLayoutParams)cardLayout.getLayoutParams();
        pixels = getPixels(10);
        params.setMargins(pixels, pixels, pixels, pixels);
        cardLayout.setOrientation(LinearLayout.HORIZONTAL);
        cardLayout.setWeightSum(10);

        final EditText newOptionText = new EditText(MainActivity.this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        newOptionText.setLayoutParams(linearParams);
        newOptionText.requestFocus();

        final ImageButton removeButton = new ImageButton(MainActivity.this);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeButtonClick(removeButton);
                optionsTexts.remove(optionText);

                for (int x = 0; x < optionsTexts.size(); x++) {
                    optionsTexts.get(x).setText("Option " + (x+1));
                }
            }
        });
        removeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_x));
        linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 9);
        removeButton.setLayoutParams(linearParams);
        removeButtons.put(cardLayout, removeButton);
        cardLayout.addView(newOptionText);
        if (showRemoveButtons) {
            cardLayout.addView(removeButton);
        }

        newOptionText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for (int i = 0; i < cardLayout.getChildCount(); i++) {
                    if (cardLayout.getChildAt(i) instanceof ImageButton) {
                        for (LinearLayout ll : removeButtons.keySet()) {
                            ll.removeView(removeButtons.get(ll));
                        }
                        showRemoveButtons = false;

                        return true;
                    }
                }

                for (LinearLayout ll : removeButtons.keySet()) {
                    ll.addView(removeButtons.get(ll));
                    showRemoveButtons = true;
                }

                newOptionText.requestFocus();
                return true;
            }
        });

//        newOptionText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus && showRemoveButtons) {
//                    for (LinearLayout ll : removeButtons.keySet()) {
//                        ll.removeView(removeButtons.get(ll));
//                    }
//                }
//            }
//        });

        firstCardLayout.addView(cardLayout);
        cardView.addView(firstCardLayout);
        optionsLayout.addView(cardView);
        allCardViews.put(removeButton, cardView);
        allOptions.put(cardView, newOptionText);

        if (!continueButton.isShown() && allCardViews.size() > 1) {
            layout.addView(continueButton);
        }
    }

    public void removeButtonClick(ImageButton button) {
        allOptions.remove(allCardViews.get(button));
        optionsLayout.removeView(allCardViews.get(button));
        allCardViews.remove(button);

        if (allCardViews.size() <= 1) {
            welcomeText.setText("Click the button below to add an option!");
            layout.removeView(continueButton);
        }
    }

    private int getPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
