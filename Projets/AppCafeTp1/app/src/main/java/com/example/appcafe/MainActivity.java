package com.example.appcafe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cafe.ListeProduits;
import com.example.cafe.Produit;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    Hashtable<String, ImageView> images;
    Hashtable<String, TextView> text;
    Hashtable<String, Button> buttons;
    Hashtable<String, Chip> chips;

    LinearLayout parentLayout;
    LinearLayout orderLayout;
    ListeProduits listeProduits;
    Commande commande;
    Vector<Integer> nbChaque;
    Vector<TextView> nbChaqueText;
    boolean changerLayout = false;
    String nomProduit = "";
    String tailleProduit = "Petit";
    Produit produitSelectionner = null;
    DecimalFormat caloriesFormat = new DecimalFormat("## cal");
    DecimalFormat prixFormat = new DecimalFormat("0.00$");
    AlertDialog.Builder commandeEnvoye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();
        images = new Hashtable();
        text = new Hashtable();
        buttons = new Hashtable();
        chips = new Hashtable();

        parentLayout = findViewById(R.id.ParentLayout);
        orderLayout = findViewById(R.id.OrderLayout);
        this.findChildren(parentLayout);

        listeProduits = new ListeProduits();
        commande = new Commande();
        nbChaque = new Vector<>(4,1); //CafFiltre, Americano, CafGlace, Latte
        nbChaqueText = new Vector<>(4, 1);
        for(int i = 0; i < images.size(); ++i)
        {
            nbChaque.add(0);
        }
        commandeEnvoye = new AlertDialog.Builder(this);
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
                        images.put(getResources().getResourceEntryName(child.getId()), (ImageView) child);
                        child.setOnClickListener(ec);
                    }
                    else if(child instanceof Button)
                    {
                        buttons.put(getResources().getResourceEntryName(child.getId()), (Button) child);
                        child.setOnClickListener(ec);
                    }
                    else if(child instanceof TextView) //For some reason, this needs to be put after Button since Buttons are considered TextView? I assume Buttons are a children of TextView but that's hella stupid
                    {
                        if(!getResources().getResourceEntryName(child.getId()).toLowerCase().matches(".*textview.*"))
                        {
                            text.put(getResources().getResourceEntryName(child.getId()), (TextView) child);
                        }
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

                chips.put(getResources().getResourceEntryName(child.getId()), child);
                child.setOnClickListener(ec);
            }
        }
    }

    private ImageView findImgProduit()
    {
        ImageView produit = new ImageView(getApplicationContext());

        switch (produitSelectionner.getNom())
        {
            case "Café Filtre":
            {
                produit.setImageResource(R.drawable.cafe_filtre);
                nbChaque.set(0, nbChaque.get(0) + 1);
                if(nbChaqueText.size() > 0)
                {
                    nbChaqueText.get(0).setText(nbChaque.get(0).toString());
                }
                break;
            }
            case "Americano" :
            {
                produit.setImageResource(R.drawable.americano);
                nbChaque.set(1, nbChaque.get(1) + 1);
                if(nbChaqueText.size() > 0)
                {
                    nbChaqueText.get(1).setText(nbChaque.get(1).toString());
                }
                break;
            }
            case "Café Glacé" :
            {
                produit.setImageResource(R.drawable.cafe_glace);
                nbChaque.set(2, nbChaque.get(2) + 1);
                if(nbChaqueText.size() > 0)
                {
                    nbChaqueText.get(2).setText(nbChaque.get(2).toString());
                }
                break;
            }
            case "Latté" :
            {
                produit.setImageResource(R.drawable.latte);
                nbChaque.set(3, nbChaque.get(3) + 1);
                if(nbChaqueText.size() > 0)
                {
                    nbChaqueText.get(3).setText(nbChaque.get(3).toString());
                }
                break;
            }
        }
        return produit;
    }

    private class Ecouteur implements View.OnClickListener
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            boolean produitChange = false;
            if(images.contains(v))
            {
                //Changes the product based on the image clicked
                if (images.get("imgCafFiltre").equals(v))
                {
                    nomProduit = "Café filtre";
                }
                else if (images.get("imgAmericano").equals(v))
                {
                    nomProduit = "Americano";
                }
                else if (images.get("imgCafGlace").equals(v))
                {
                    nomProduit = "Café glacé";
                }
                else if (images.get("imgLatte").equals(v))
                {
                    nomProduit = "Latté";
                }

                //Enables the add button if it isnt already
                if(!buttons.get("addButton").isEnabled())
                {
                    buttons.get("addButton").setEnabled(true);
                }

                produitChange = true;
            }
            else if(chips.contains(v))
            {
                //Changes the size of the product
                tailleProduit = ((Chip) v).getText().toString();
                produitChange = true;
            }
            else if(buttons.contains(v))
            {
                if(buttons.get("addButton").equals(v))
                {
                    //Adds the product to the command
                    commande.ajouterProduit(produitSelectionner);
                    //Changes the visible order total
                    text.get("currentPriceText").setText(prixFormat.format(commande.getTotal()));

                    //Choose the appropriate icon
                    if(orderLayout.getChildCount() < 10 && !changerLayout)
                    {
                        ImageView produit = findImgProduit();

                        //Changes the size of the icon
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, RelativeLayout.LayoutParams.WRAP_CONTENT); //Values here are in pixel
                        produit.setLayoutParams(layoutParams);
                        //Adds the product icon to the appropriate layout
                        orderLayout.addView(produit);
                    }
                    else if(!changerLayout)//New layout if the size of the order is too big
                    {
                        changerLayout = true;
                        orderLayout.removeAllViewsInLayout();

                        for(int i = 0; i < images.size(); ++i)
                        {
                            ImageView produit = new ImageView(getApplicationContext());
                            nbChaqueText.add(new TextView(getApplicationContext()));
                            nbChaqueText.lastElement().setText(nbChaque.get(i).toString());

                            switch(i)
                            {
                                case 0:
                                {
                                    produit.setImageResource(R.drawable.cafe_filtre);
                                    break;
                                }
                                case 1:
                                {
                                    produit.setImageResource(R.drawable.americano);
                                    break;
                                }
                                case 2:
                                {
                                    produit.setImageResource(R.drawable.cafe_glace);
                                    break;
                                }
                                case 3:
                                {
                                    produit.setImageResource(R.drawable.latte);
                                    break;
                                }
                            }

                            //Changes the size of the icon
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.WRAP_CONTENT); //Values here are in pixel
                            layoutParams.weight = 0.15f;
                            produit.setLayoutParams(layoutParams);
                            //Changes the size, color, gravity, and margins of the text
                            layoutParams = new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 0, 20, 0);
                            layoutParams.weight = 0.1f;
                            nbChaqueText.lastElement().setLayoutParams(layoutParams);
                            nbChaqueText.lastElement().setTextSize(30);
                            nbChaqueText.lastElement().setTextColor(Color.BLACK);
                            nbChaqueText.lastElement().setGravity(Gravity.CENTER);
                            //Adds the product icon to the appropriate layout
                            orderLayout.addView(produit);
                            orderLayout.addView( nbChaqueText.lastElement());
                        }
                    }
                    else
                    {
                        findImgProduit();
                    }
                }
                else if(buttons.get("eraseButton").equals(v) && commande.getCommande().size() > 0)
                {
                    //Disables the add button
                    buttons.get("addButton").setEnabled(false);
                    //Reset the current selected product to null
                    produitSelectionner = null;
                    nomProduit = "";
                    //Removes all of the icon present in the orderLayout
                    orderLayout.removeAllViewsInLayout();
                    //Creates a new Commande
                    commande = new Commande();
                    //Resets the number of each item to 0
                    for(int i = 0; i < images.size(); ++i)
                    {
                        nbChaque.set(i, 0);
                    }
                    nbChaqueText = new Vector<>(4, 1);
                    //Resets the order layout to its default state
                    changerLayout = false;
                    //Resets the modifiable text fields
                    text.get("currentSelectionText").setText("");
                    text.get("currentPriceText").setText(prixFormat.format(commande.getTotal()));
                }
                else if(buttons.get("orderButton").equals(v))
                {
                    //Shows the transaction confirmation
                    commandeEnvoye.setMessage("Paiement de " + prixFormat.format(commande.getTotal()) + " en cours...").setTitle("Commande envoyée!");
                    AlertDialog dialog = commandeEnvoye.create();
                    dialog.show();
                }
            }

            //Changes the text showcasing the currently selected product
            if(nomProduit != "" && produitChange)
            {
                produitSelectionner = listeProduits.recupererProduit(nomProduit + " " + tailleProduit);
                text.get("currentSelectionText").setText(produitSelectionner.getNom() + " " + caloriesFormat.format(produitSelectionner.getCalories()) + " " + prixFormat.format(produitSelectionner.getPrix()));
            }
        }
    }
}