package com.example.bernie.carepointsv2;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private LinearLayout optionsLayout;
    private TextView welcomeText;
    private Map<FloatingActionButton, LinearLayout> allOptions;
    private FloatingActionButton addOptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.layout);
        optionsLayout = (LinearLayout) findViewById(R.id.optionsLayout);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        allOptions = new HashMap<FloatingActionButton, LinearLayout>();

        final FloatingActionButton continueButton = new FloatingActionButton(MainActivity.this);
        continueButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ((RelativeLayout.LayoutParams)continueButton.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ((RelativeLayout.LayoutParams)continueButton.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)continueButton.getLayoutParams();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24, getResources().getDisplayMetrics());
        params.setMargins(px, px, px, px);
        continueButton.setFabText("Enter Care Points");
        continueButton.setFabIcon(getResources().getDrawable(R.drawable.ic_right_arrow));
        continueButton.setFabColor(Color.parseColor("#006EDC"));
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                List<String> options = new ArrayList<String>();

                for (FloatingActionButton button : allOptions.keySet()) {
                    LinearLayout layout = allOptions.get(button);
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        if (layout.getChildAt(i) instanceof EditText) {
                            options.add(((EditText)layout.getChildAt(i)).getText().toString());
                        }
                    }
                }

                intent.putExtra("Options", options.toArray());
            }
        });

        addOptionButton = (FloatingActionButton) findViewById(R.id.addOptionButton);
        ((RelativeLayout.LayoutParams)continueButton.getLayoutParams()).addRule(RelativeLayout.LEFT_OF, addOptionButton.getId());
        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allOptions.size() > 0) {
                    welcomeText.setText("When you are done adding options, continue to start assigning Care Points!");
                }

                LinearLayout newOptionLayout = new LinearLayout(MainActivity.this);
                newOptionLayout.setOrientation(LinearLayout.HORIZONTAL);
                newOptionLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                EditText newOption = new EditText(MainActivity.this);
                newOption.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1));

                final FloatingActionButton removeButton = new FloatingActionButton(MainActivity.this);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionsLayout.removeView(allOptions.get(removeButton));
                        allOptions.remove(removeButton);

                        if (allOptions.size() <= 1) {
                            welcomeText.setText("Click the button below to add an option!");
                            layout.removeView(continueButton);
                        }
                    }
                });
                removeButton.setFabText("Remove");
                removeButton.setFabSize(FloatingActionButton.FAB_SIZE_MINI);
                removeButton.setFabColor(Color.parseColor("#F65050"));
                removeButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                newOptionLayout.addView(newOption);
                newOptionLayout.addView(removeButton);
                optionsLayout.addView(newOptionLayout);
                allOptions.put(removeButton, newOptionLayout);

                if (!continueButton.isShown() && allOptions.size() > 1) {
                    layout.addView(continueButton);
                }
            }
        });
    }
}
