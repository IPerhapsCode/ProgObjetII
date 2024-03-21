package com.example.painttp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.painttp2.shapes.Cercle;
import com.example.painttp2.shapes.DessinLibre;
import com.example.painttp2.shapes.Efface;
import com.example.painttp2.shapes.Formes;
import com.example.painttp2.shapes.Rectangle;
import com.example.painttp2.shapes.Remplir;
import com.example.painttp2.shapes.Triangle;

import java.util.Hashtable;
import java.util.Vector;
//Il serait important de pouvoir maintenir notre doigt enfoncer sur l'image defface pour qu'on puisse alors changer la taille du trait de l'efface et
//peut être pouvoir changer la forme de l'efface genre un carré ou un cercle
//Peut être retoucher la taille du cercle de l'efface j'aime la manière dont fonctionne, peut être même que l'efface ne devrait pas utiliser un path :/
//Puisqu'on va coder l'outil remplir pour de vrai il nous faut un moyen de changer la couleur de fond,
//je propose de faire cela en maintenant le bouton enfoncer sur l'outil remplir ou avec l'outil pipette
//Créer une option qui permet de fermer une forme libre, on peut sélectionner l'option en enfoncant le crayon
//PS finalement on va faire un scoll view dans l'alert dialog de largeur du trait pour changer les dites options
//Il me faut un moyen de savoir que la couleur que la pipette me donne est équivalente à une couleur préfette
public class MainActivity extends AppCompatActivity {

    private EcouteurOnTouch ecot;
    private SurfaceDessin sd;
    private Vector<Formes> formes;
    private Vector<Formes> formesUndone;
    public static Hashtable<String, Integer> couleurs;
    private Hashtable<String, ImageView> outils;
    private LinearLayout zoneCouleurs, zoneDessin, zoneOutils;

    //Variables
    private int couleur = R.color.black;
    private int backgroundColor = R.color.white;
    private static int sizeTrace = 5;
    private Paint.Style style = Paint.Style.STROKE;

    private int currentEvent;
    private float x, y;
    private ImageView outil;
    private ImageView lastOutil;
    public static Bitmap bitmap = null;

    private boolean fermerForme = false;

    //Alert Dialogs
    private Dialog_Largeur_Trait dialogLargeurTrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mise en place de la zone de dessin et de notre écouteur
        ecot = new EcouteurOnTouch();
        sd = new SurfaceDessin(this);

        //Mise en place de nos vecteurs et de nos hashtables
        formes = new Vector<>(1, 1);
        formesUndone = new Vector<>(1, 1);
        couleurs = new Hashtable<>();
        outils = new Hashtable<>();

        //Mise en place de nos linear layouts
        zoneCouleurs = findViewById(R.id.zoneCouleurs);
        zoneDessin = findViewById(R.id.zoneDessin);
        zoneOutils = findViewById(R.id.zoneOutils);

        //Alert Dialogs
        dialogLargeurTrait = new Dialog_Largeur_Trait(this);

        //Obtention des enfants contenu dans nos linear layouts
            //Bouton des couleurs
        for(int i = 0; i < zoneCouleurs.getChildCount(); ++i)
        {
            int color = 0;
            Button child = (Button)zoneCouleurs.getChildAt(i);

            child.setOnTouchListener(ecot);

            //Assignation des couleurs à chacun des boutons
            //PS: Il n'est pas nécessaire de conserver les boutons dans un vecteur puisque ceux-ci ne sont utilisé que pour leur tag
            switch(child.getTag().toString())
            {
                case "darkorange": color = R.color.darkorange; break;
                case "yellow": color = R.color.yellow; break;
                case "white": color = R.color.white; break;
                case "black": color = R.color.black; break;
                case "pink": color = R.color.pink; break;
                case "red": color = R.color.red; break;
                case "turquoise": color = R.color.turquoise; break;
                case "lightgreen": color = R.color.lightgreen; break;
            }

            couleurs.put(child.getTag().toString(), color);
        }
            //Outils de dessin
        for(int i = 0; i < zoneOutils.getChildCount(); ++i)
        {
            ImageView child = (ImageView)zoneOutils.getChildAt(i);

            outils.put(child.getTag().toString(), child);
            outils.get(child.getTag().toString()).setOnTouchListener(ecot);
        }

            //Surface de dessin
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                            ViewGroup.LayoutParams.MATCH_PARENT);
        sd.setLayoutParams(params);
        //Ps:Cette ligne empêche une null error
        sd.setTag("surface_dessin");
        sd.setBackgroundColor(ContextCompat.getColor(this, backgroundColor));
        sd.setOnTouchListener(ecot);
        zoneDessin.addView(sd);

        //Désignation de l'outil par défaut
        outil = outils.get("crayon");
    }

    private class EcouteurOnTouch implements View.OnTouchListener
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            currentEvent = event.getAction();
            //Si un bouton de couleur est cliqué
            if(couleurs.containsKey(v.getTag().toString())
                    && currentEvent == MotionEvent.ACTION_DOWN)
            {
                couleur = couleurs.get(v.getTag().toString());
            }
            //Si un outil est cliqué
            else if(outils.containsKey(v.getTag().toString())
                    && currentEvent == MotionEvent.ACTION_UP)
            {
                lastOutil = outil;
                outil = outils.get(v.getTag().toString());
                System.out.println(outil.getTag().toString());
                switch(outil.getTag().toString())
                {
                    case "largeur_trait":{
                        dialogLargeurTrait.show();
                        outil = lastOutil;
                        break;
                    }
                    case "undo":{
                        if(!formes.isEmpty())
                        {
                            formesUndone.add(formes.lastElement());
                            formes.remove(formes.lastElement());
                            sd.invalidate();
                        }
                        outil = lastOutil;
                        break;
                    }
                    case "redo":{
                        if(!formesUndone.isEmpty())
                        {
                            formes.add(formesUndone.lastElement());
                            formesUndone.remove(formesUndone.lastElement());
                            sd.invalidate();
                        }
                        outil = lastOutil;
                        break;
                    }
                }
            }
            //Si l'utilisateur dessine sur la surface de dessin
            else if(v instanceof SurfaceDessin)
            {
                x = event.getX();
                y = event.getY();

                switch(outil.getTag().toString())
                {
                    case "crayon":{
                        if(currentEvent == MotionEvent.ACTION_DOWN)
                        {
                            formes.add(new DessinLibre(MainActivity.this, couleur, sizeTrace, style));
                        }
                        else if(currentEvent == MotionEvent.ACTION_UP && fermerForme)
                        {
                            if(formes.lastElement() instanceof DessinLibre)
                            {
                                ((DessinLibre) formes.lastElement()).closePath();
                            }
                        }
                        break;
                    }
                    case "efface":{
                        if(currentEvent == MotionEvent.ACTION_DOWN)
                        {
                            formes.add(new Efface(MainActivity.this, backgroundColor,
                                    Efface.getSizeTraceEfface(), style));
                        }
                        break;
                    }
                    case "cercle":{
                        if(currentEvent == MotionEvent.ACTION_DOWN)
                        {
                            formes.add(new Cercle(MainActivity.this, couleur, sizeTrace, style));
                        }
                        break;
                    }
                    case "rectangle":{
                        if(currentEvent == MotionEvent.ACTION_DOWN)
                        {
                            formes.add(new Rectangle(MainActivity.this, couleur, sizeTrace, style));
                        }
                        break;
                    }
                    case "triangle":{
                        if(currentEvent == MotionEvent.ACTION_DOWN)
                        {
                            formes.add(new Triangle(MainActivity.this, couleur, sizeTrace, style));
                        }
                        if(formes.lastElement() instanceof Triangle)
                        {
                            ((Triangle) formes.lastElement()).setCurrentEvent(currentEvent);
                        }
                        break;
                    }
                    case "pipette":{
                        if(currentEvent == MotionEvent.ACTION_DOWN)
                        {
                            bitmap = sd.getBitmapImage();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                couleur = bitmap.getColor((int)x, (int)y).toArgb();
                            }
                        }
                        break;
                    }
                    case "remplir":{
                        if(currentEvent == MotionEvent.ACTION_DOWN)
                        {
                            bitmap = sd.getBitmapImage();
                            formes.add(new Remplir(MainActivity.this, couleur, 0, style));
                            formes.lastElement().draw(x, y);
                            sd.invalidate();
                        }
                    }
                }

                if(!outil.getTag().toString().equals("pipette")
                        && !outil.getTag().toString().equals("remplir"))
                {
                    formes.lastElement().draw(x, y);
                    sd.invalidate();
                }
            }
            return true;
        }
    }

    private class SurfaceDessin extends View
    {
        public SurfaceDessin(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            for(Formes i: formes)
            {
                if(i instanceof DessinLibre)
                {
                    canvas.drawPath(((DessinLibre) i).getPath(), i.getPaint());

                    if(i instanceof Efface && currentEvent != MotionEvent.ACTION_UP
                            && outil == outils.get("efface"))
                    {
                        canvas.drawCircle(x, y, Efface.getSizeTraceEfface() * 0.65f, ((Efface) i).getBorderPaint());
                    }
                }
                else if(i instanceof Cercle && !(i instanceof Rectangle))
                {
                    canvas.drawOval(((Cercle) i).getxDepart(), ((Cercle) i).getyDepart(),
                            ((Cercle) i).getX(), ((Cercle) i).getY(), i.getPaint());
                }
                else if(i instanceof Rectangle)
                {
                    canvas.drawRect(((Rectangle) i).getxDepart(), ((Rectangle) i).getyDepart(),
                            ((Rectangle) i).getX(), ((Rectangle) i).getY(), i.getPaint());
                }
                else if(i instanceof Triangle)
                {
                    canvas.drawLine(((Triangle) i).getX(), ((Triangle) i).getY(),
                            ((Triangle) i).getA(), ((Triangle) i).getB(), i.getPaint());
                    canvas.drawLine(((Triangle) i).getX(), ((Triangle) i).getY(),
                            ((Triangle) i).getC(), ((Triangle) i).getD(), i.getPaint());
                    canvas.drawLine(((Triangle) i).getA(), ((Triangle) i).getB(),
                            ((Triangle) i).getC(), ((Triangle) i).getD(), i.getPaint());
                }
                else if(i instanceof Remplir)
                {
                    canvas.drawBitmap(((Remplir) i).getBitmap(), 0, 0, i.getPaint());
                }
            }
        }

        public Bitmap getBitmapImage() {
            Bitmap bitmapImage;

            this.buildDrawingCache();
            bitmapImage = Bitmap.createBitmap(this.getDrawingCache());
            this.destroyDrawingCache();

            return bitmapImage;
        }

    }

    public static int getSizeTrace()
    {
        return sizeTrace;
    }

    public static void setSizeTrace(int newSizeTrace)
    {
        sizeTrace = newSizeTrace;
    }

    public static Bitmap getBitmap()
    {
        return bitmap;
    }
}