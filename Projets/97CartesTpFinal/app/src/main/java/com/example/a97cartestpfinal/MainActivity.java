package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.logique.Cartes;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    public static int[] marginsMain;
    public static int[] marginsPile;
    public static int[] marginsPileAlt;

    private EcouteurOnTouch ecot;
    private EcouteurOnDrag ecod;

    LinearLayout parent;
    private Vector<LinearLayout> piles;
    private Vector<LinearLayout> main;
    private Vector<View> buttons;
    private Vector<TextView> ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marginsMain = new int[]{(int)(22.5 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(22.5 * getResources().getDisplayMetrics().density),
                (int)(32 * getResources().getDisplayMetrics().density)};
        marginsPile = new int[]{(int)(20 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(10 * getResources().getDisplayMetrics().density),
                (int)(40 * getResources().getDisplayMetrics().density)};
        marginsPileAlt = new int[]{(int)(10 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(40 * getResources().getDisplayMetrics().density)};

        ecot = new EcouteurOnTouch();
        ecod = new EcouteurOnDrag();

        piles = new Vector<>(1, 1);
        main = new Vector<>(1, 1);
        buttons = new Vector<>(1, 1);
        ui = new Vector<>(1, 1);

        parent = findViewById(R.id.parent);

        this.findChildren(parent);

        //Appliquer un style à une nouvelle vu:
//        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.MyTextViewStyle);
//        TextView textView = new TextView(themedContext);

        //Changer la couleur du background et l'opacité de la vu
//        TextView testing = findViewById(R.id.textView8);
//        GradientDrawable bruh = (GradientDrawable) testing.getBackground();
//        bruh.setColor(Color.rgb(255, 0,0));
//        testing.setAlpha(0);
    }

    private void findChildren(LinearLayout parent)
    {
        for(int i = 0; i < parent.getChildCount(); ++i)
        {
            View child = parent.getChildAt(i);
            if(child instanceof LinearLayout)
            {
                try
                {
                    String tag = child.getTag().toString();

                    if(tag.matches("pile.*"))
                    {
                        piles.add((LinearLayout) child);
                        piles.lastElement().setOnDragListener(ecod);
                    }
                    else if(tag.matches("main"))
                    {
                        main.add((LinearLayout) child);
                        main.lastElement().setOnTouchListener(ecot);
                    }
                }
                catch(Exception e)
                {
                    this.findChildren((LinearLayout) child);
                }
            }
            else
            {
                try
                {
                    String tag = child.getTag().toString();

                    if(tag.matches("button.*"))
                    {
                        buttons.add(child);
                        buttons.lastElement().setOnTouchListener(ecot);
                    }
                    else if(tag.matches("ui.*"))
                    {
                        ui.add((TextView) child);
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Unecessary child");
                }
            }
        }
    }

    private class EcouteurOnTouch implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(main.contains(v) && event.getAction() == MotionEvent.ACTION_DOWN)
            {
                LinearLayout test = (LinearLayout)v;
                test.removeAllViewsInLayout();
                Cartes testing = new Cartes(45, 97, getApplicationContext());
                test.addView(testing.getCarte());
            }

            return true;
        }
    }

    private class EcouteurOnDrag implements View.OnDragListener
    {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            return true;
        }
    }
}