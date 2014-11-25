package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frc3322_04.scoutingclient.*;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;

public class Scout extends Activity {

    ScrollView scrollView;
    ArrayList<FormPage> formPages;
    int currentPage;
    Button back;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        scrollView = (ScrollView)findViewById(R.id.scouting_scroll);
        back = (Button)findViewById(R.id.back_button);
        next = (Button)findViewById(R.id.next_button);
        formPages = new ArrayList<FormPage>();
        FormPage p1 = new FormPage(scrollView.getContext());
        p1.add(new TextBox(p1.getContext(),"test","Text1","default"));
        p1.add(new TextBox(p1.getContext(),"test","Text2",null));
        p1.add(new NumberBox(p1.getContext(),"Num","Num1",0,true,true,100, 0));
        p1.add(new MapCordinatePicker(p1.getContext(),"Map"));
        formPages.add(p1);
        FormPage p2 = new FormPage(scrollView.getContext());
        p2.add(new TextBox(p2.getContext(),"page 2","Text3","default"));
        p2.add(new NumberBox(p2.getContext(),"Num","Num2",0,true,true,100, 0));
        String[] opts = {"red","blue"};
        p2.add(new OptionPicker(p2.getContext(),"Choose","Optpick",opts));
        formPages.add(p2);
        if(formPages.size() > 0)
            scrollView.addView(formPages.get(0));
        currentPage = 0;
        fixLabels();
    }
    private void fixLabels() {
        if(currentPage == 0) {
            back.setVisibility(View.INVISIBLE);
        } else {
            back.setVisibility(View.VISIBLE);
        }
        if(currentPage >= formPages.size() - 1) {
            next.setText("Submit");
        } else {
            next.setText("Next");
        }
    }
    public void getValues() {
        formPages.get(currentPage).getValues();
    }
    public void next(View view) {
        if(currentPage < formPages.size() - 1) {
            scrollView.removeAllViews();
            currentPage++;
            scrollView.addView(formPages.get(currentPage));
        } else {
            ArrayList<Tuple<String, Serializable> > values = new ArrayList<Tuple<String, Serializable> >();
            boolean isFilled = true;
            for(FormPage page : formPages) {
                if(!page.isFilled()) {
                    isFilled = false;
                    break;
                }
                values.addAll(page.getValues());
            }
            if(isFilled) {
                Intent intent = new Intent(getBaseContext(), MatchSummary.class);
                intent.putExtra("FORM_CONTENTS", values);
                startActivity(intent);
            } else {
                Log.i("AOUT","NOT FILLED IN");
            }
        }
        fixLabels();
    }
    public void back(View view) {
        getValues();
        if(currentPage > 0) {
            scrollView.removeAllViews();
            currentPage--;
            scrollView.addView(formPages.get(currentPage));
            fixLabels();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
