package com.example.painttp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.painttp2.shapes.Cercle;
import com.example.painttp2.shapes.DessinLibre;
import com.example.painttp2.shapes.Efface;
import com.example.painttp2.shapes.Formes;
import com.example.painttp2.shapes.Rectangle;
import com.example.painttp2.shapes.Remplir;
import com.example.painttp2.shapes.Triangle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;

import yuku.ambilwarna.AmbilWarnaDialog;

//Basically, différentes options de triangle, empty canvas, fermer le trait libre, trois options de fill, utiliser des spinners tho
//Relire le code, peut être changer les constructeurs s'assurer qu'uniquement les paramètres nécessaires soit inclus

public class MainActivity extends AppCompatActivity {

    private EcouteurOnTouch ecot;
    private static SurfaceDessin sd;
    private static Vector<Formes> formes;
    private static Vector<Formes> formesUndone;
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
    private static Bitmap bitmap = null;

    private boolean fermerForme = false;
    private int nbImgSaved = 0;

    private static int styleDessin = 0, styleTriangle = 0, styleFill = 0;

    //Alert Dialogs
    private DialogSettings dialogSettings;

    //Toasts
    Toast toastSaved;
    Toast toastChangedColor;
    Toast toastFill;
//Couleurs, remplir speed, buttons edge, triangle
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
        dialogSettings = new DialogSettings(this);

        //Toasts
        toastSaved = Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT);
        toastChangedColor = Toast.makeText(this, "Color changed!", Toast.LENGTH_SHORT);
        toastFill = Toast.makeText(this, "Please wait, this operation is slow...", Toast.LENGTH_LONG);

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
                toastChangedColor.show();
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
                    case "settings":{
                        dialogSettings.show();
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
                    case "enregistrer":{
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "myDrawing" + nbImgSaved);
                        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                        Uri uri = MainActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                        if(uri != null)
                        {
                            try(OutputStream stream = MainActivity.this.getContentResolver().openOutputStream(uri)){
                                bitmap = sd.getBitmapImage();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                stream.close();
                                ++nbImgSaved;
                                toastSaved.show();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        outil = lastOutil;
                        break;
                    }
                    case "palette":{
                        AmbilWarnaDialog dialog = new AmbilWarnaDialog(MainActivity.this, couleur, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                            @Override
                            public void onCancel(AmbilWarnaDialog dialog) {

                            }

                            @Override
                            public void onOk(AmbilWarnaDialog dialog, int color) {
                                couleur = color;
                                toastChangedColor.show();
                            }
                        });
                        dialog.show();
                        outil = lastOutil;
                        break;
                    }
                }
                System.out.println(outil.getTag().toString());
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
                        else if(currentEvent == MotionEvent.ACTION_UP && styleDessin == 1)
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
                            formes.add(new Triangle(MainActivity.this, couleur, sizeTrace, style, styleTriangle));
                        }
                        break;
                    }
                    case "pipette":{
                        if(currentEvent == MotionEvent.ACTION_UP)
                        {
                            bitmap = sd.getBitmapImage();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                couleur = bitmap.getColor((int)x, (int)y).toArgb();
                                toastChangedColor.show();
                            }
                        }
                        break;
                    }
                    case "remplir":{
                        if(currentEvent == MotionEvent.ACTION_UP)
                        {
                            if(styleFill == 0)
                            {
                                toastFill.show();
                            }
                            bitmap = sd.getBitmapImage();
                            formes.add(new Remplir(MainActivity.this, couleur, 0, style, styleFill));
                            formes.lastElement().draw(x, y);
                            sd.invalidate();
                        }
                        break;
                    }
                }

                if(!outil.getTag().toString().equals("pipette")
                        && !outil.getTag().toString().equals("remplir"))
                {
                    formes.lastElement().draw(x, y);
                    sd.invalidate();
                }
                else if(outil.getTag().toString().equals("pipette") && event.getAction() == MotionEvent.ACTION_UP)
                {
                    outil = outils.get("crayon");
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
            //Affichages des formes
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
        //Obtention de la bitmap
        public Bitmap getBitmapImage() {
            Bitmap bitmapImage;

            this.buildDrawingCache();
            bitmapImage = Bitmap.createBitmap(this.getDrawingCache());
            this.destroyDrawingCache();

            return bitmapImage;
        }

    }

    //Retourne la taille du tracer
    public static int getSizeTrace()
    {
        return sizeTrace;
    }

    //Modifie la taille du tracer
    public static void setSizeTrace(int newSizeTrace)
    {
        sizeTrace = newSizeTrace;
    }

    //Retourne la bitmap dans d'autre classes
    public static Bitmap getBitmap()
    {
        return bitmap;
    }

    //Vide la surface de dessin
    public static void clearScreen(){
        formes = new Vector<>();
        formesUndone = new Vector<>();
        sd.invalidate();
    }

    //Change les paramètres de certaines formes
    public static void setStyleDessinLibre(int style)
    {
        styleDessin = style;
    }

    public static void setStyleTriangle(int style)
    {
        styleTriangle = style;
    }

    public static void setStyleFill(int style)
    {
        styleFill = style;
    }
}