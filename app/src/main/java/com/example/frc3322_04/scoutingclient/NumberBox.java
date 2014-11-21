package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.frc3322_04.scoutingclient.*;

class NumberBox extends FormWidget {
    //TODO make sure all the validation works
    EditText textBox;
    Button inc, decr;
    int max, min;
    final boolean hMax, hMin;
    NumberBox(Context context, String labelText, int initialValue, boolean hasMax, boolean hasMin, int maximum, int minimum) {
        super(context, labelText);
        hMax = hasMax;
        hMin = hasMin;
        max = maximum;
        min = minimum;
        if(hMax && hMin && max < min) {//dont bother validating min <= max if neither matter
            int temp = max;
            max = min;
            min = temp;
        }
        if(hMin && initialValue < min) {
            //log error
            initialValue = min;
        }else if(hMax && initialValue > max) {
            //log error
            initialValue = max;
        }
        textBox = new EditText(context);
        textBox.setText(String.valueOf(initialValue));
        DigitsKeyListener dkl = new DigitsKeyListener(min < 0, false);
        textBox.setKeyListener(dkl);
        textBox.addTextChangedListener(new TextWatcher() {//make sure max and min rules are enforced
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(hMax && getValue() > max) {
                    textBox.setText(String.valueOf(max));
                } else if(hMin && getValue() < min) {
                    textBox.setText(String.valueOf(min));
                }
            }
        });
        inc = new Button(context);
        inc.setText("+");
        inc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int old = getValue();
                if (!hMax || old < max) {
                    textBox.setText(String.valueOf(old+1));
                }
            }
        });
        decr = new Button(context);
        decr.setText("-");
        decr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int old = getValue();
                if (!hMin || old > min) {
                    textBox.setText(String.valueOf(old - 1));
                }
            }
        });
        this.addView(textBox);
        this.addView(inc);
        this.addView(decr);
    }
    public Integer getValue() {
        int ret = 0;
        try {
            ret = Integer.parseInt(textBox.getText().toString());
        } catch (NumberFormatException e){}
        return ret;
    }
}