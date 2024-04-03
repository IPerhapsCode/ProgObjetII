package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.example.painttp2.MainActivity;
import com.example.painttp2.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


//Je pense que je check possiblement pas suffisement de point, faudrait checker les diagonales aussi pour certain niche cases
//Also live ça crash si aucune forme est présente parce qu'il y a trop de pixel à changer
//Potentiellement une solution serait de séparer nos groupes dans plusieurs hashmaps de roughly 100 000 ou 1 000 000 pixels (en soit la valeur la plus grande possible sans que ça crash)
//Je pense qu'une meilleure solution serait de ne pas copier un a un chaque valeur, simplement copier la hashmap dans une autre hashmap qui représenterait le groupe, pis on passerait à travers chacune des copies comme étant le groupe

//I just want to say that vectors are so slow holy shit
//Secondly hashmaps are better for single threads then hashtables, where hashtable are better for thread collections
public class Remplir extends Formes{
    private Bitmap bitmap;
    private int color;
    private int initialColor = 1;
    private int fillStyle;

    private Context context;
    public Remplir(Context context, int color, int fillStyle) {
        //N'a pas réellement besoin qu'on lui assigne la bonne taille de tracer
        super(context, color, 5);
        this.context = context;
        this.bitmap = MainActivity.getBitmap();
        this.color = color;
        this.fillStyle = fillStyle;
    }

    @Override
    public void draw(float x, float y)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            int defaultX = (int)x, defaultY = (int)y;
            this.initialColor = this.bitmap.getColor(defaultX, defaultY).toArgb();

            if(this.fillStyle == 0) //Ma version de la fonction bien que lente
            {
                //Guides us through our hashmap group
                Integer nbGroup = 0;
                //Let's us know if we need to create a new hashmap in groups
                boolean newGroup = true;
                //Contains potential groups at first, composed of every pixel that is the same color as initial color
                HashMap<Integer, HashMap<Point, Point>> groups = new HashMap<>();
                HashMap<Integer, HashMap<Integer, HashMap<Point, Point>>> finalGroups = new HashMap<>();

                //Making of initial groups
                for(int i = 0; i < this.bitmap.getHeight(); ++i)
                {
                    for(int j = 0; j < this.bitmap.getWidth(); ++j)
                    {
                        if(this.bitmap.getColor(j, i).toArgb() == this.initialColor)
                        {
                            if(!groups.containsKey(nbGroup))
                            {
                                groups.put(nbGroup, new HashMap<>());
                                newGroup = false;
                            }

                            groups.get(nbGroup).put(new Point(j, i), new Point(j, i));
                        }
                        else
                        {
                            if(!newGroup)
                            {
                                ++nbGroup;
                                newGroup = true;
                            }
                        }
                    }
                }

                boolean breakTime = false;
                Point[] myPoints = new Point[8];
                Vector<Integer> groupsRemoved = new Vector<>();
                for(int i = 0; i < myPoints.length; ++i)
                {
                    myPoints[i] = new Point();
                }

                //Making aglomeration of most conjointed groups
                for(int i = 0; i < groups.size(); ++i)
                {
                    breakTime = false;

                    for(Point j : groups.get(i).values())
                    {
                        myPoints[0].set(j.x + 1, j.y);
                        myPoints[1].set(j.x - 1, j.y);
                        myPoints[2].set(j.x, j.y + 1);
                        myPoints[3].set(j.x, j.y - 1);
                        myPoints[4].set(j.x + 1, j.y + 1);
                        myPoints[5].set(j.x - 1, j.y - 1);
                        myPoints[6].set(j.x - 1, j.y + 1);
                        myPoints[7].set(j.x + 1, j.y - 1);

                        for(HashMap<Integer, HashMap<Point, Point>> k : finalGroups.values())
                        {
                            for(HashMap<Point, Point> l : k.values())
                            {
                                for(Point m : myPoints)
                                {
                                    if(l.containsKey(m))
                                    {
                                        k.put(k.size(), groups.get(i));
                                        groupsRemoved.add(i);
                                        breakTime = true;
                                    }

                                    if(breakTime)
                                    {
                                        break;
                                    }
                                }

                                if(breakTime)
                                {
                                    break;
                                }
                            }
                        }

                        if(breakTime)
                        {
                            break;
                        }
                    }

                    if(!breakTime)
                    {
                        HashMap<Integer, HashMap<Point, Point>> temp = new HashMap<>();
                        temp.put(0, groups.get(i));
                        finalGroups.put(finalGroups.size(), temp);
                    }
                }

                myPoints[0].set(defaultX, defaultY);
                breakTime = false;
                HashMap<Integer, HashMap<Integer, HashMap<Point, Point>>> myGroup = new HashMap<>();
                HashMap<Point, Point> pixels = new HashMap<>(100000, 100000);
                //Finding the group containing the pixel clicked by the user
                for(Integer i : finalGroups.keySet())
                {
                    for(HashMap<Point, Point> j : finalGroups.get(i).values())
                    {
                        if(j.containsKey(myPoints[0]))
                        {
                            myGroup.put(myGroup.size(), finalGroups.get(i));
                            for(HashMap<Point, Point> k : finalGroups.get(i).values())
                                for(Point l : k.keySet())
                                {
                                    pixels.put(l, l);
                                }
                            breakTime = true;
                        }

                        if(breakTime)
                        {
                            break;
                        }
                    }

                    if(breakTime)
                    {
                        break;
                    }
                }

                //Adding stragglers back to the group where user clicked
                //Cette section pourrait être plus élégante mais elle fonctionne pour le moment
                //Dans le fond on fait tourner l'algorithme deux fois au cas où une section qui aurait été checké avant qu'une autre qui l'aurait relier au groupe n'est été ajouté à notre collection de pixel
                //Pourrait être optimisé je pense en copie collant la hashmap de point au lieu de passer une valeur après l'autre
                for(HashMap<Integer, HashMap<Point, Point>> i : finalGroups.values())
                {
                    breakTime = false;
                    if(i != myGroup.get(0))
                    {
                        for(HashMap<Point, Point> j : i.values())
                        {
                            for(Point k : j.values())
                            {
                                myPoints[0].set(k.x + 1, k.y);
                                myPoints[1].set(k.x - 1, k.y);
                                myPoints[2].set(k.x, k.y + 1);
                                myPoints[3].set(k.x, k.y - 1);
                                myPoints[4].set(k.x + 1, k.y + 1);
                                myPoints[5].set(k.x - 1, k.y - 1);
                                myPoints[6].set(k.x - 1, k.y + 1);
                                myPoints[7].set(k.x + 1, k.y - 1);

                                for(Point m : myPoints)
                                {
                                    if(pixels.containsKey(m))
                                    {
                                        myGroup.put(myGroup.size(), i);

                                        for(HashMap<Point, Point> l : i.values())
                                        {
                                            for(Point n : l.keySet())
                                            {
                                                pixels.put(n, n);
                                            }
                                        }

                                        finalGroups.remove(i);

                                        breakTime = true;
                                        break;
                                    }
                                }

                                if(breakTime)
                                {
                                    break;
                                }
                            }

                            if(breakTime)
                            {
                                break;
                            }
                        }
                    }
                }

                for(HashMap<Integer, HashMap<Point, Point>> i : finalGroups.values())
                {
                    breakTime = false;
                    if(i != myGroup.get(0))
                    {
                        for(HashMap<Point, Point> j : i.values())
                        {
                            for(Point k : j.values())
                            {
                                myPoints[0].set(k.x + 1, k.y);
                                myPoints[1].set(k.x - 1, k.y);
                                myPoints[2].set(k.x, k.y + 1);
                                myPoints[3].set(k.x, k.y - 1);
                                myPoints[4].set(k.x + 1, k.y + 1);
                                myPoints[5].set(k.x - 1, k.y - 1);
                                myPoints[6].set(k.x - 1, k.y + 1);
                                myPoints[7].set(k.x + 1, k.y - 1);

                                for(Point m : myPoints)
                                {
                                    if(pixels.containsKey(m))
                                    {
                                        myGroup.put(myGroup.size(), i);

                                        for(HashMap<Point, Point> l : i.values())
                                        {
                                            for(Point n : l.keySet())
                                            {
                                                pixels.put(n, n);
                                            }
                                        }

                                        finalGroups.remove(i);

                                        breakTime = true;
                                        break;
                                    }
                                }

                                if(breakTime)
                                {
                                    break;
                                }
                            }

                            if(breakTime)
                            {
                                break;
                            }
                        }
                    }
                }

                //Changing the color of the group the user clicked
                for(HashMap<Integer, HashMap<Point, Point>> i : myGroup.values())
                {
                    for(HashMap<Point, Point> j : i.values())
                    {
                        for(Point k : j.values())
                        {
                            if(MainActivity.couleurs.contains(this.color))
                            {
                                this.bitmap.setPixel(k.x, k.y, ContextCompat.getColor(this.context, this.color));
                            }
                            else
                            {
                                this.bitmap.setPixel(k.x, k.y, this.color);
                            }
                        }
                    }
                }
            }
            else if(this.fillStyle == 1) //Fonction version line scanning
            {
                LinkedList<Point> queue = new LinkedList<>();
                queue.add(new Point(defaultX, defaultY));

                while(!queue.isEmpty())
                {
                    //Retire le point qui est sur le point d'être itéré
                    Point p = queue.remove();
                    int north = p.y;
                    int south = p.y + 1;

                    //Change les couleurs des pixels dans une ligne verticale vers le haut de l'écran jusqu'à atteindre l'extérieur de l'écran ou un pixel d'une couleur différente que celle de départ
                    while(north >= 0 && this.bitmap.getColor(p.x, north).toArgb() == this.initialColor)
                    {
                        if (MainActivity.couleurs.contains(this.color))
                        {
                            this.bitmap.setPixel(p.x, north, ContextCompat.getColor(this.context, this.color));
                        }
                        else
                        {
                            this.bitmap.setPixel(p.x, north, this.color);
                        }
                        --north;
                    }

                    //Change les couleurs des pixels dans une ligne verticale vers le bas de l'écran jusqu'à atteindre l'extérieur de l'écran ou un pixel d'une couleur différente que celle de départ
                    while(south < this.bitmap.getHeight() && this.bitmap.getColor(p.x, south).toArgb() == this.initialColor)
                    {
                        if (MainActivity.couleurs.contains(this.color))
                        {
                            this.bitmap.setPixel(p.x, south, ContextCompat.getColor(this.context, this.color));
                        }
                        else
                        {
                            this.bitmap.setPixel(p.x, south, this.color);
                        }
                        ++south;
                    }

                    //Ajoute à la queue tous les points parralèles à la droite et la gauche de la dernière ligne dessinée, et cela pour tout les pixels de la même couleur que la couleur initiale
                    for(int i = north + 1; i < south; ++i)
                    {
                        if(p.x > 0 && this.bitmap.getColor(p.x - 1, i).toArgb() == this.initialColor)
                        {
                            queue.add(new Point(p.x - 1, i));
                        }
                        if(p.x < this.bitmap.getWidth() - 1 && this.bitmap.getColor(p.x + 1, i).toArgb() == this.initialColor)
                        {
                            queue.add(new Point(p.x + 1, i));
                        }
                    }
                }
            }
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
