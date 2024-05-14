package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.alertDialogsActivity.GameOver;
import com.example.a97cartestpfinal.alertDialogsActivity.Parametres;
import com.example.a97cartestpfinal.db.Database;
import com.example.a97cartestpfinal.exceptions.ExceptionDB;
import com.example.a97cartestpfinal.logique.Cartes;
import com.example.a97cartestpfinal.logique.Partie;

import java.util.Hashtable;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    public static int[] marginsMain;
    public static int[] marginsPile;
    public static int[] marginsPileAlt;
    public static boolean savedGame = false;
    private EcouteurOnTouch ecot;
    private EcouteurOnDrag ecod;
    private Database instance;
    private boolean dbState = false;

    private LinearLayout parent;
    private Vector<Cartes> helperSelectedCards;
    private Vector<LinearLayout> piles;
    private Vector<LinearLayout> main;
    private Hashtable<String, View> buttons;
    private Hashtable<String, TextView> ui;

    //Game related variables
    private Partie partie;
    private GameOver gameOver;
    private Parametres parametres;
    private boolean helper = false;
    private int couleurChoisi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permet de créer et de déplacer les cartes sans que leur apparence ne change
        float densite = getResources().getDisplayMetrics().density;
        this.marginsMain = new int[]{(int)(22.5 * densite),
                (int)(20 * densite),
                (int)(22.5 * densite),
                (int)(32 * densite)};
        this.marginsPile = new int[]{(int)(20 * densite),
                (int)(20 * densite),
                (int)(10 * densite),
                (int)(40 * densite)};
        this.marginsPileAlt = new int[]{(int)(10 * densite),
                (int)(20 * densite),
                (int)(20 * densite),
                (int)(40 * densite)};

        //Création des écouteurs
        this.ecot = new EcouteurOnTouch();
        this.ecod = new EcouteurOnDrag();

        //Obtient une référence à l'instance de la base de donnée
        this.instance = Database.getInstance(this);

        //Zone de stockage des views manipulés lors de l'utilisation
        this.helperSelectedCards = new Vector<>(1 ,1);
        this.piles = new Vector<>(1, 1);
        this.main = new Vector<>(1, 1);
        this.buttons = new Hashtable<>(1, 1);
        this.ui = new Hashtable<>(1, 1);

        //Assignation des views à des écouteurs et mise en stockage
        this.parent = findViewById(R.id.parent);
        this.findChildren(this.parent);

        //Début de la partie
        this.dbState = this.instance.ouvrirConnexion();
        try
        {
            this.obtainPreferences(this.instance.loadPreferences());
        }
        catch (ExceptionDB e)
        {
            System.out.println(e.getMessage());
        }
        this.partie = new Partie(this.piles, this, savedGame, this.couleurChoisi);
        try
        {
            this.dbState = this.instance.fermerConnexion();
        }
        catch (ExceptionDB e)
        {
            System.out.println(e.getMessage());
        }
        this.partie.gameStart(this.main, this.ecot);
        this.partie.setTurnStart(this.readTime(this.ui.get("ui_time").getText().toString()));
        this.ui.get("ui_cartes").setText(String.valueOf(this.partie.getNbCartes()));
        ((Chronometer)this.ui.get("ui_time")).setBase(SystemClock.elapsedRealtime() - this.readTime(this.partie.getBaseTime()) * 1000);
        this.ui.get("ui_score").setText(String.valueOf(this.partie.getScore()));

        //Turns on the helper if necessary
        if(this.helper)
        {
            this.helperAi();
        }

        //Alert dialog creation
        this.gameOver = new GameOver(this);
        this.parametres = new Parametres(this, this.helper, this.couleurChoisi);
    }

    //Assure que la base de donnée est fermée si l'activité est fermée
    @Override
    protected void onStop() {
        super.onStop();
        if(this.dbState)
        {
            try
            {
                this.instance.fermerConnexion();
            }
            catch (ExceptionDB e) {
                System.out.println(e.getMessage());
            }

        }
        this.finish();
    }

    //Trouve tous les enfants que l'on aura besoin de manipulé au courant de l'activité
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
                        this.piles.add((LinearLayout) child);
                        this.piles.lastElement().setOnDragListener(this.ecod);
                    }
                    else if(tag.matches("main.*"))
                    {
                        this.main.add((LinearLayout) child);
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
                        this.buttons.put(child.getTag().toString(), child);
                        child.setOnTouchListener(this.ecot);
                    }
                    else if(tag.matches("ui.*"))
                    {
                        this.ui.put(child.getTag().toString(), (TextView) child);
                        if(child.getTag().toString().contains("time"))
                        {
                            ((Chronometer)child).start();
                        }
                    }
                }
                catch(Exception e)
                {
                    //Rien à faire si l'enfant n'est pas nécessaire
                    continue;
                }
            }
        }
    }

    //Retourne la valeur du temps courant en secondes
    private int readTime(String time)
    {
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

    //Donne l'ocasion au joueur d'annuler son dernier coup
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.0f);
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
            carte.spawnAnim(1);
        }

        //Rappele l'ia si jamais cela est nécessaire
        if(this.helper)
        {
            this.helperAi();
        }

        return nbCartes;
    }

    //Trouve le meilleur coup à jouer selon l'intervalle entre les cartes et le score que le coup peu potentiellement apporter
    public void helperAi()
    {
        Cartes main = null, pile = null, tempPile = null;
        Vector<Cartes> oldCartes = new Vector<>(1, 1);
        Vector<Cartes[]> pairs = new Vector<>(1, 1);
        int index = 0, score = 0, tempScore = 0, intervalle = 100, tempIntervalle = 100;
        boolean direction = true, jump = false, jumpTemp = false;

        for(Cartes i : this.partie.getMainCartes().values())
        {
            //Vérifie si un bon de dix est présent dans la main du joueur pour plus tard déterminer l'ordre de jeu approprié
            for(Cartes k : oldCartes)
            {
                if(Math.abs(i.getValue() - k.getValue()) == 10)
                {
                    pairs.add(new Cartes[2]);
                    pairs.lastElement()[0] = i;
                    pairs.lastElement()[1] = k;
                }
            }
            oldCartes.add(i);

            //Trouve le meilleur coup selon l'intervalle et le nombre de point que peut potentiellement apporté un coup
            for(LinearLayout j : this.piles)
            {
                index = this.partie.valeurIndex(j);
                tempPile = this.partie.findCard(this.partie.getPiles().getPilesCartes(), j.getChildAt(index));
                direction = j.getTag().toString().contains("asc");

                if((direction && tempPile.getValue() - i.getValue() == 10)
                        || (!direction && i.getValue() - tempPile.getValue() == 10))
                {
                    tempScore = this.partie.calcLastScoreAddition(i.getValue(), tempPile.getValue(), true, false);
                    tempIntervalle = Math.abs(i.getValue() - tempPile.getValue());
                    jumpTemp = true;
                }
                else if(direction && tempPile.getValue() < i.getValue()
                        || !direction && tempPile.getValue() > i.getValue())
                {
                    tempScore = this.partie.calcLastScoreAddition(i.getValue(), tempPile.getValue(), false, false);
                    tempIntervalle = Math.abs(i.getValue() - tempPile.getValue());
                    jumpTemp = false;
                }

                if((score < tempScore) || (score == tempScore && tempIntervalle < intervalle))
                {
                    score = tempScore;
                    intervalle = tempIntervalle;
                    jump = jumpTemp;
                    main = i;
                    pile = tempPile;
                }
            }
        }

        //Détermine l'ordre de jeu approprié si un bon de dix est présent dans la main du joueur et si une des cartes formant le bon est la prochaine carte à jouer
        for(Cartes[] i: pairs)
        {
            index = (main == i[0] ? 1 : (main == i[1] ? 0 : -1));

            if(index != -1)
            {
                main = (pile.getValue() > main.getValue() && main.getValue() > i[index].getValue() && !jump
                        ? i[index]
                        : (pile.getValue() < main.getValue() && main.getValue() < i[index].getValue() && !jump
                        ? i[index]
                        : main));
            }
        }

        //Si le meilleur coup à jouer a été trouvé, le montrer au joueur
        if(main != null && pile != null)
        {
            this.stopHelperAi();

            this.helperSelectedCards.add(main);
            this.helperSelectedCards.add(pile);

            for(Cartes i : this.helperSelectedCards)
            {
                i.setHelperSelected(true);
                if(this.partie.getMainCartes().values().contains(i))
                {
                    i.helperAnim(5, 0, new int[]{0, 0, 0});
                }
                else
                {
                    i.helperAnim(5, 1, new int[]{255, 255, 255});
                }
            }
        }
    }

    //Permet d'arrêter l'animation faite par le helper
    public void stopHelperAi()
    {
        if(!this.helperSelectedCards.isEmpty())
        {
            for(Cartes i : helperSelectedCards)
            {
                i.setHelperSelected(false);
            }
            helperSelectedCards.clear();
        }
    }

    //Indique à notre activité si le helper est actif
    public void setHelper(boolean helper) {
        this.helper = helper;
    }

    //Nous permet de changer la couleur des cartes selon la préférence de l'utilisateur
    public void changeCardColor(int couleur)
    {
        this.partie.setColor(couleur);
        for(Cartes i : this.partie.getMainCartes().values())
        {
            i.setCouleur(couleur);
        }

        for(Cartes i : this.partie.getVoidCartes().values())
        {
            if(i.getValue() != 0 && i.getValue() != 100)
            {
                i.setCouleur(couleur);
            }
        }

        for(Cartes i : this.partie.getPiles().getPilesCartes().values())
        {
            if(i.getValue() != 0 && i.getValue() != 100)
            {
                i.setCouleur(couleur);
            }
        }
    }

    //Nous permet d'obtenir les préférences de l'utilisateur
    public void obtainPreferences(Cursor preferences)
    {
        while(preferences.moveToNext())
        {
            switch(preferences.getInt(0))
            {
                case 0:
                {
                    this.helper = preferences.getInt(1) == 0 ? false : true;
                }
                case 1:
                {
                    this.couleurChoisi = preferences.getInt(1);
                }
            }
        }
    }

    private class EcouteurOnTouch implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:{
                    //Démare le drag and drop
                    if(partie.getMainCartes().containsKey(v))
                    {
                        v.startDragAndDrop(null, new View.DragShadowBuilder(v), v, 0);
                    }
                    else if(buttons.contains(v))
                    {
                        switch(v.getTag().toString())
                        {
                            //Affiche les paramètres
                            case "button_param":{
                                parametres.show();
                                break;
                            }
                            //Sauvegarde la partie dans son état courant et retourne l'utilisateur au menu principal
                            case "button_menu":{
                                dbState = instance.ouvrirConnexion();
                                try
                                {
                                    instance.saveGame(partie.getScore(), partie.getNbCartes(),
                                            ui.get("ui_time").getText().toString(),
                                            partie.getCarteValues(),
                                            partie.getMainCartes().values(),
                                            partie.getPiles().getPilesCartes().values());
                                }
                                catch (ExceptionDB e)
                                {
                                    System.out.println(e.getMessage());
                                }

                                try
                                {
                                    dbState = instance.fermerConnexion();
                                }
                                catch (ExceptionDB e)
                                {
                                    System.out.println(e.getMessage());
                                }

                                startActivity(new Intent(MainActivity.this, MainMenu.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                break;
                            }
                            //Permet à l'utilisateur d'annuler son dernier coup
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
                                    partie.resetVoidScore();
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
                        partie.setTurnEnd(readTime(ui.get("ui_time").getText().toString()));
                        ui.get("ui_score").setText(String.valueOf(
                                partie.calcNewScore(partie.findCard(partie.getMainCartes(), carte).getValue(),
                                partie.findCard(partie.getPiles().getPilesCartes(), pile.getChildAt(index)).getValue(),
                                pile.getTag().toString().contains("asc"))));
                        partie.setTurnStart(readTime(ui.get("ui_time").getText().toString()));

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


                        //Fait piger le joueur s'il leur manque deux cartes
                        if((partie.getMainCartes().size() == main.size() - 2 && partie.getNbCartes() >= main.size()) || partie.getNbCartes() == main.size())
                        {
                            partie.drawCards(main, ecot);

                            //Reset le bouton redo pour le prochain cycle
                            partie.getVoidCartes().clear();
                            partie.resetVoidScore();
                            ((ImageView)buttons.get("button_redo")).setImageResource(R.drawable.redo_grey);
                        }
                        //Change la couleur du bouton redo s'il manque exactement une carte à la main du joueur
                        else if (partie.getMainCartes().size() == main.size() - 1 || partie.getNbCartes() < main.size())
                        {
                            ((ImageView)buttons.get("button_redo")).setImageResource(R.drawable.redo_black);
                        }

                        //Ouvre un alert dialog qui félicite le joueur et lui offre d'aller voir les highscore ou de retourner au menu
                        if(partie.gameOver(piles))
                        {
                            dbState = instance.ouvrirConnexion();
                            try
                            {
                                instance.saveHighscore(partie.getScore(), partie.getNbCartes(), ui.get("ui_time").getText().toString(), partie.isSavedGame());
                            }
                            catch (ExceptionDB e)
                            {
                                System.out.println(e.getMessage());
                            }

                            try
                            {
                                dbState = instance.fermerConnexion();
                            }
                            catch (ExceptionDB e)
                            {
                                System.out.println(e.getMessage());
                            }
                            gameOver.setWinLose(partie.getMainCartes().size() == 0);
                            gameOver.show();
                            ((Chronometer)ui.get("ui_time")).stop();
                            break;
                        }
                        //Rappele le helper pour le prochain coup si il est toujours actif
                        else if(helper)
                        {
                            helperAi();
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