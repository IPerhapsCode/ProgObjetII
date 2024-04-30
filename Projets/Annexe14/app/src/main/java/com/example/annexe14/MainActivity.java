package com.example.annexe14;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Vector<LinearLayout> linearLayouts;
    Vector<ImageView> images;

    LinearLayout parent;

    EcouteurOnTouch ecot;
    EcouteurOnDrag ecod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ecot = new EcouteurOnTouch();
        ecod = new EcouteurOnDrag();

        parent = findViewById(R.id.parent);

        linearLayouts = new Vector<>(1, 1);
        images = new Vector<>(1, 1);

        for(int i = 0; i < parent.getChildCount(); ++i)
        {
            if(parent.getChildAt(i) instanceof LinearLayout)
            {
                linearLayouts.add((LinearLayout) parent.getChildAt(i));
                linearLayouts.lastElement().setOnDragListener(ecod);

                for(int j = 0; j < linearLayouts.lastElement().getChildCount(); ++j)
                {
                    if(linearLayouts.lastElement().getChildAt(j) instanceof ImageView)
                    {
                        images.add((ImageView) linearLayouts.lastElement().getChildAt(j));
                        images.lastElement().setOnTouchListener(ecot);
                    }
                }
            }
        }
    }

    private class EcouteurOnTouch implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                v.setVisibility(View.INVISIBLE);
                v.startDragAndDrop(null, new View.DragShadowBuilder(v), v, 0);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                v.setVisibility(View.VISIBLE);
            }
            return true;
        }


    }

    private class EcouteurOnDrag implements View.OnDragListener
    {
        LinearLayout parent;
        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch(event.getAction())
            {
                case DragEvent.ACTION_DRAG_ENTERED:{
                    v.setBackground(getResources().getDrawable(R.drawable.background_contenant_triangle_selectionne));
                    break;
                }

                case DragEvent.ACTION_DRAG_EXITED:{
                    v.setBackground(getResources().getDrawable(R.drawable.background_contenant_triangle));
                    break;
                }

                case DragEvent.ACTION_DRAG_STARTED:{
                    View jeton = (View)event.getLocalState();
                    LinearLayout temp = (LinearLayout)v;
                    for(int i = 0; i < temp.getChildCount(); ++i)
                    {
                        if(temp.getChildAt(i) == jeton)
                        {
                            parent = temp;
                            parent.removeView(jeton);
                            break;
                        }
                    }
                    break;
                }

                case DragEvent.ACTION_DROP:{
                    LinearLayout container = (LinearLayout)v;
                    View jeton = (View)event.getLocalState();
                    jeton.setVisibility(View.VISIBLE);
                    container.addView(jeton);
                    container.setBackground(getResources().getDrawable(R.drawable.background_contenant_triangle));
                    break;
                }

                case DragEvent.ACTION_DRAG_ENDED:{
                    if(!event.getResult() && parent != null)
                    {
                        View jeton = (View)event.getLocalState();
                        jeton.setVisibility(View.VISIBLE);
                        parent.addView(jeton);
                        parent = null;
                    }
                    break;
                }
            }

            return true;
        }
    }
}