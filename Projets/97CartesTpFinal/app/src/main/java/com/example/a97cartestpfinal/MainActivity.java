package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.logique.Cartes;
import com.example.a97cartestpfinal.logique.Partie;

import java.util.Hashtable;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    public static int[] marginsMain;
    public static int[] marginsPile;
    public static int[] marginsPileAlt;

    private EcouteurOnTouch ecot;
    private EcouteurOnDrag ecod;

    private LinearLayout parent;
    private Vector<LinearLayout> piles;
    private Vector<LinearLayout> main;
    private Vector<View> buttons;
    private Hashtable<String, TextView> ui;

    //Game related variables
    private Partie partie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permet de créer et de déplacer les cartes sans que leur apparence ne change
        this.marginsMain = new int[]{(int)(22.5 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(22.5 * getResources().getDisplayMetrics().density),
                (int)(32 * getResources().getDisplayMetrics().density)};
        this.marginsPile = new int[]{(int)(20 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(10 * getResources().getDisplayMetrics().density),
                (int)(40 * getResources().getDisplayMetrics().density)};
        this.marginsPileAlt = new int[]{(int)(10 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(20 * getResources().getDisplayMetrics().density),
                (int)(40 * getResources().getDisplayMetrics().density)};

        //Création des écouteurs
        ecot = new EcouteurOnTouch();
        ecod = new EcouteurOnDrag();

        //Zone de stockage des views manipulés lors de l'utilisation
        this.piles = new Vector<>(1, 1);
        this.main = new Vector<>(1, 1);
        this.buttons = new Vector<>(1, 1);
        this.ui = new Hashtable<>(1, 1);

        //Assignation des views à des écouteurs et mise en stockage
        this.parent = findViewById(R.id.parent);
        this.findChildren(parent);

        //Début de la partie
        this.partie = new Partie(this.piles, this);
        this.partie.gameStart(this.main, ecot);
        this.partie.setTurnStart(this.readTime());
    }

    private void findChildren(LinearLayout parent)
    {
        for(int i = 0; i < parent.getChildCount(); ++i)
        {
            View child = parent.getChildAt(i);
            //Étape à suivre si la vu enfant est un linear layout
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
                    }
                }
                catch(Exception e)
                {
                    //Uniquement les linears layouts qui n'ont pas de tag peuvent avoir des enfants pertinent
                    this.findChildren((LinearLayout) child);
                }
            }
            else
            {
                //Ajout des enfants pertinent en stockage
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
                        ui.put(child.getTag().toString(), (TextView) child);
                        if(child.getTag().toString().contains("time"))
                        {
                            ((Chronometer)child).start();
                        }
                    }
                }
                catch(Exception e)
                {
                    //Rien à faire si l'enfant n'est pas nécessaire
                    System.out.println("Unecessary child");
                }
            }
        }
    }

    //Retourne la valeur du temps courant en secondes
    private int readTime()
    {
        String time = this.ui.get("ui_time").getText().toString();
        String h, m, s;

        if(time.matches("\\d{2}:\\d{2}"))
        {
            m = time.charAt(0) + String.valueOf(time.charAt(1));
            s = time.charAt(3) + String.valueOf(time.charAt(4));
            return Integer.parseInt(m) * 60 + Integer.parseInt(s);
        }
        else
        {
            h = time.charAt(0) + String.valueOf(time.charAt(1));
            m = time.charAt(3) + String.valueOf(time.charAt(4));
            s = time.charAt(6) + String.valueOf(time.charAt(7));
            return Integer.parseInt(h) * 60 * 60 + Integer.parseInt(m) * 60 + Integer.parseInt(s);
        }
    }

    private class EcouteurOnTouch implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            readTime();
            switch(event.getAction())
            {
                //Démare le drag and drop et rend la carte invisible
                case MotionEvent.ACTION_DOWN:{
                    if(partie.getMainCartes().containsKey(v))
                    {
                        v.startDragAndDrop(null, new View.DragShadowBuilder(v), v, 0);
                    }
                }
            }
            return true;
        }
    }

    private class EcouteurOnDrag implements View.OnDragListener
    {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            //Gère les drag event fait par le joueur
            switch(event.getAction())
            {
                //Rend la carte qu'on déplace invisible
                case DragEvent.ACTION_DRAG_STARTED:{
                    ((View)event.getLocalState()).setVisibility(View.INVISIBLE);
                    break;
                }
                //Modifie le background des piles quand le joueur survol une des zones
                case DragEvent.ACTION_DRAG_ENTERED:{
                    v.setBackground(getResources().getDrawable(R.drawable.background_piles_selected));
                    break;
                }
                //Remet la carte dans la main du joueur lorsqu'elle n'est pas drop dans une zone approprié
                case DragEvent.ACTION_DRAG_ENDED:{
                    if(!event.getResult())
                    {
                        View carte = (View)event.getLocalState();
                        carte.setVisibility(View.VISIBLE);
                    }
                    break;
                }
                //Ajoute à la pile la carte
                case DragEvent.ACTION_DROP:{
                    //Ajoute la carte à la pile, si possible change le ui en conséquence
                    if(partie.getPiles().addToPile((LinearLayout) v, (TextView) event.getLocalState(), partie))
                    {
                        LinearLayout pile = (LinearLayout) v;
                        TextView carte = (TextView) event.getLocalState();
                        int index = 0;
                        if(v.getTag().toString().contains("alt"))
                        {
                            index = 1;
                        }

                        //Affiche le nouveau score
                        partie.setTurnEnd(readTime());
                        ui.get("ui_score").setText(String.valueOf(
                                partie.calcNewScore(partie.findCard(partie.getMainCartes(), carte).getValue(),
                                partie.findCard(partie.getPiles().getPilesCartes(), pile.getChildAt(index)).getValue(),
                                pile.getTag().toString().contains("asc"))));
                        partie.setTurnStart(readTime());

                        //Affiche le nombre de cartes qu'il reste à jouer
                        partie.setNbCartes(-1);
                        ui.get("ui_cartes").setText(String.valueOf(partie.getNbCartes()));

                        //Mise à jour en mémoire et visuellement de l'emplacement des cartes
                        partie.getPiles().getPilesCartes().remove(pile.getChildAt(index));
                        partie.getPiles().getPilesCartes().put(carte,
                                partie.findCard(partie.getMainCartes(), carte));
                        partie.getMainCartes().remove(carte);
                        pile.removeView(pile.getChildAt(index));
                        pile.addView(carte, index);

                        //Fait piger le joueur s'il leur manque au moins deux cartes
                        if(partie.getMainCartes().size() <= main.size() - 2)
                        {
                            partie.drawCards(main, ecot);
                        }
                    }
                }
                //Modifie le background des piles quand le joueur quite la zone affectée
                case DragEvent.ACTION_DRAG_EXITED:{
                    v.setBackground(getResources().getDrawable(R.drawable.background_piles));
                    break;
                }
            }
            return true;
        }
    }
}