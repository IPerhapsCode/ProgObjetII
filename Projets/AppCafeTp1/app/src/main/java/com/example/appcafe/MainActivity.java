package com.example.appcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    Vector<ImageView> images;
    Vector<TextView> text;
    Vector<Button> buttons;
    Vector<Chip> chips;

    LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();
        images = new Vector<>(1,1);
        text = new Vector<>(1,1);
        buttons = new Vector<>(1,1);
        chips = new Vector<>(1,1);

        parent = findViewById(R.id.ParentLayout);
        this.findChildren(parent);
    }

    //Let's us obtain the children of every parent container in the main activity
    private void findChildren(View parent)
    {
        if(parent instanceof LinearLayout)
        {
            LinearLayout castedParent = (LinearLayout) parent;
            View child;

            for(int i = 0; i < castedParent.getChildCount(); ++i)
            {
                child = castedParent.getChildAt(i);

                if(child instanceof LinearLayout) //If child is a parent container
                {
                    this.findChildren((LinearLayout) child);
                }
                else if(child instanceof ChipGroup)
                {
                    this.findChildren((ChipGroup) child);
                }
                else //If child is simply a child
                {
                    if(child instanceof ImageView)
                    {

                    }
                    else if(child instanceof Button)
                    {

                    }
                    else if(child instanceof TextView) //For some reason, this needs to be put after Button since Buttons are considered TextView? I assume Buttons are a children of TextView but that's hella stupid
                    {
                        System.out.println(getResources().getResourceName(child.getId()));
                    }
                }
            }
        }
        else if(parent instanceof ChipGroup)
        {
            ChipGroup castedParent = (ChipGroup) parent;
            Chip child;

            for(int i = 0; i < castedParent.getChildCount(); ++i)
            {
                child = (Chip) castedParent.getChildAt(i);

                chips.add(child);
                chips.lastElement().setOnClickListener(ec);
            }
        }
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            System.out.println("dabs on them haters");
        }
    }
}