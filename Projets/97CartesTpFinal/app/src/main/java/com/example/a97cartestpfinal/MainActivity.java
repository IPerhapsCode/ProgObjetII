package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.db.Database;
import com.example.a97cartestpfinal.logique.Cartes;
import com.example.a97cartestpfinal.logique.Partie;

import java.util.Hashtable;
import java.util.Vector;
//To do:
//Bug a demander au prof: Si on clique sur le bouton retour et qu'on retourne dans l'activité après, les piles prennent la couleur qu'avait la dernière carte en mémoire lors de la fermeture de l'activité?
//Game over activity
//Option de continue une partie si le joueur a précèdement save and quit
//Un menu de settings dans lequel le joueur peut : A.Turn on un bot qui montre les meilleurs coups B.Change la color pallete des cartes
//On pourrait rajouter de la musique genre du ai generated lofi, on pourrait alors changer le volume dans les settings
public class MainActivity extends AppCompatActivity {
    public static int[] marginsMain;
    public static int[] marginsPile;
    public static int[] marginsPileAlt;
    public static int backgroundCartesCouleur;
    private EcouteurOnTouch ecot;
    private EcouteurOnDrag ecod;
    private Database instance;

    private LinearLayout parent;
    private Vector<LinearLayout> piles;
    private Vector<LinearLayout> main;
    private Hashtable<String, View> buttons;
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
        //Solution temporaire, faudrait trouver une autre solution
        backgroundCartesCouleur = getResources().getColor(R.color.dark_grey);

        //Création des écouteurs
        this.ecot = new EcouteurOnTouch();
        this.ecod = new EcouteurOnDrag();

        //Obtient une référence à l'instance de la base de donnée
        this.instance = Database.getInstance(this);
        instance.ouvrirConnexion();

        //Zone de stockage des views manipulés lors de l'utilisation
        this.piles = new Vector<>(1, 1);
        this.main = new Vector<>(1, 1);
        this.buttons = new Hashtable<>(1, 1);
        this.ui = new Hashtable<>(1, 1);

        //Assignation des views à des écouteurs et mise en stockage
        this.parent = findViewById(R.id.parent);
        this.findChildren(parent);

        //Début de la partie
        this.partie = new Partie(this.piles, this);
        this.partie.gameStart(this.main, this.ecot);
        this.partie.setTurnStart(this.readTime());
        this.ui.get("ui_cartes").setText(String.valueOf(this.partie.getNbCartes()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.instance.fermerConnexion();
        this.finish();
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
                        piles.lastElement().setOnDragListener(this.ecod);
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
                        buttons.put(child.getTag().toString(), child);
                        child.setOnTouchListener(this.ecot);
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

    private int redo()
    {
        //Permet de savoir combien de cartes sont retournées dans la main du joueur
        int nbCartes = 0;
        //Add the views back to their respective layouts
        for(LinearLayout i : this.partie.getVoidCartes().keySet())
        {
            Cartes carte = this.partie.getVoidCartes().get(i);

            //Trouve l'index du layout où devra se situer la carte
            int index = this.partie.valeurIndex(i);

            //Vérifie que la vue n'a pas déjà un parent
            if(carte.getCarte().getParent() != null)
            {
                ((LinearLayout)carte.getCarte().getParent()).removeView(carte.getCarte());
            }

            //Si la carte retourne dans la main
            if(i.getTag().toString().contains("main"))
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.0f);
                params.setMargins(this.marginsMain[0],
                        this.marginsMain[1],
                        this.marginsMain[2],
                        this.marginsMain[3]);
                carte.getCarte().setLayoutParams(params);
                carte.getCarte().setOnTouchListener(this.ecot);
                this.partie.getMainCartes().put(carte.getCarte(), carte);

                ++nbCartes;
            }
            //Si la carte retourne dans une pile
            else
            {
                this.partie.getPiles().getPilesCartes().put(carte.getCarte(), carte);
            }

            //Adds the view back to their respective layouts and plays a little animation
            i.addView(carte.getCarte(), index);
            carte.getCarte().setAlpha(0);
            carte.spawnAnim(5);
        }

        return nbCartes;
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
                    else if(buttons.contains(v))
                    {
                        switch(v.getTag().toString())
                        {
                            case "button_param":{
                                System.out.println("param");
                                break;
                            }
                            case "button_menu":{
                                startActivity(new Intent(MainActivity.this, MainMenu.class));
                                break;
                            }
                            case "button_redo":{
                                if(partie.getVoidCartes().size() >= 2)
                                {
                                    //Replace les cartes dans leur layout respectif visuellement et en mémoire
                                    partie.setNbCartes(redo());

                                    //Reset les valeurs à leur état précédent
                                    ((ImageView) buttons.get("button_redo")).setImageResource(R.drawable.redo_grey);
                                    partie.getVoidCartes().clear();
                                    partie.setTurnStart(partie.getOldTurnStart());
                                    partie.resetScore();
                                    ui.get("ui_score").setText(String.valueOf(partie.getScore()));
                                    ui.get("ui_cartes").setText(String.valueOf(partie.getNbCartes()));
                                }
                                break;
                            }
                        }
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
                    LinearLayout pile = (LinearLayout) v;
                    TextView carte = (TextView) event.getLocalState();
                    if(partie.getPiles().addToPile(main.size(), pile, carte, partie))
                    {
                        int index = partie.valeurIndex(pile);

                        //Ajout de la pile pour le redo function si jamais il manque une seule carte dans la main du joueur
                        if(partie.getVoidCartes().size() < 2
                                || (partie.getNbCartes() < main.size() && !partie.getVoidCartes().keySet().contains(pile)))
                        {
                            partie.getVoidCartes().put(pile, partie.findCard(partie.getPiles().getPilesCartes(), pile.getChildAt(index)));
                            partie.saveOldTurnStart();
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

                        //Change la couleur du bouton redo s'il manque exactement une carte à la main du joueur
                        if(partie.getMainCartes().size() == main.size() - 1 || partie.getNbCartes() < main.size())
                        {
                            ((ImageView)buttons.get("button_redo")).setImageResource(R.drawable.redo_black);
                        }
                        //Fait piger le joueur s'il leur manque deux cartes
                        else if(partie.getMainCartes().size() == main.size() - 2 && partie.getNbCartes() >= main.size())
                        {
                            partie.drawCards(main, ecot);

                            //Reset le bouton redo pour le prochain cycle
                            partie.getVoidCartes().clear();
                            ((ImageView)buttons.get("button_redo")).setImageResource(R.drawable.redo_grey);
                        }

                        //Faudrait ouvrir un alert dialogue qui offre au joueur d'aller voir les highscores ou de retourner au menu et qui le félicite feel me dog
                        if(partie.gameOver(piles))
                        {
                            instance.ouvrirConnexion();
                            instance.saveHighscore(partie.getScore(), partie.getNbCartes(), ui.get("ui_time").getText().toString());
                            startActivity(new Intent(MainActivity.this, HighScoresActivity.class));
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