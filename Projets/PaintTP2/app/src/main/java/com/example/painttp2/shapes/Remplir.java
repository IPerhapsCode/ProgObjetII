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
    private int currentColor = 1;
    private boolean[][] check;

    private Context context;
    public Remplir(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);
        this.context = context;
        this.bitmap = MainActivity.bitmap;
        this.color = color;
    }

//    @Override
//    public void draw(float x, float y)
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//        {
//            this.initialColor = this.bitmap.getColor((int)x, (int)y).toArgb();
//            fill((int)x, (int)y);
//        }
//    }

//    public void fill(int x, int y)
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//        {
//            LinkedList<Point> queue = new LinkedList<>();
//            queue.add(new Point(x, y));
//
//            while(!queue.isEmpty())
//            {
//                Point p = queue.remove();
//                int south = y;
//                int north = y + 1;
//
//                while(south >= 0 && this.bitmap.getColor(p.x, south).toArgb() == this.initialColor)
//                {
//                    if (MainActivity.couleurs.contains(this.color))
//                    {
//                        this.bitmap.setPixel(p.x, south, ContextCompat.getColor(this.context, this.color));
//                    }
//                    else
//                    {
//                        this.bitmap.setPixel(p.x, south, this.color);
//                    }
//                    --south;
//                }
//
//                while(north < this.bitmap.getHeight() && this.bitmap.getColor(p.x, north).toArgb() == this.initialColor)
//                {
//                    if (MainActivity.couleurs.contains(this.color))
//                    {
//                        this.bitmap.setPixel(p.x, north, ContextCompat.getColor(this.context, this.color));
//                    }
//                    else
//                    {
//                        this.bitmap.setPixel(p.x, north, this.color);
//                    }
//                    ++north;
//                }
//
//                for(int i = south + 1; i < north; ++i)
//                {
//                    if(p.x > 0 && this.bitmap.getColor(p.x - 1, i).toArgb() == this.initialColor)
//                    {
//                        queue.add(new Point(p.x - 1, i));
//                    }
//                    if(p.x < this.bitmap.getWidth() - 1 && this.bitmap.getColor(p.x + 1, i).toArgb() == this.initialColor)
//                    {
//                        queue.add(new Point(p.x + 1, i));
//                    }
//                }
//            }
//        }
//    }

    @Override
    public void draw(float x, float y) {
        int defaultX = (int)x, defaultY = (int)y;
        //Guides us through our hashmap group
        Integer nbGroup = 0;
        //Let's us know if we need to create a new hashmap in groups
        boolean newGroup = true;
        //Contains into potential groups at first every pixel that is the same color as initial color
        //HashMap<Point, int[]> pixels = new HashMap<>();
        HashMap<Integer, HashMap<Point, Point>> groups = new HashMap<>();
        HashMap<Integer, HashMap<Integer, HashMap<Point, Point>>> finalGroups = new HashMap<>();
        Point currentPoint;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            this.initialColor = this.bitmap.getColor(defaultX, defaultY).toArgb();

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
            System.out.println(groups.size());

            boolean breakTime = false;
            Point[] myPoints = new Point[8];
            Vector<Integer> groupsRemoved = new Vector<>();
            int initialGroupsSize = groups.size() - 1;
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
            System.out.println(finalGroups.size());
            //Removal of groups which have already been added to other groups (the fuck is this for?)
            for(Integer i : groupsRemoved)
            {
                groups.remove(i);
            }

            myPoints[0].set(defaultX, defaultY);
            breakTime = false;
            HashMap<Integer, HashMap<Integer, HashMap<Point, Point>>> myGroup = new HashMap<>();
            HashMap<Point, Point> pixels = new HashMap<>(10000, 10000);
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
            System.out.println(myGroup.size());

            //Adding stragglers back to the group where user clicked
            //Cette section pourrait être plus élégante mais elle fonctionne pour le moment
            //Dans le fond on fait tourner l'algorithme deux fois au cas où une section qui aurait été checké avant qu'une autre qui l'aurait relier au groupe n'est été ajouté à notre collection de pixel
            //Pourrait être optimisé je pense en copie collant la hashmap de point au lieu de passer une valeur après l'autre
            //Mais peut être aussi qu'il faudrait que la fonction soit rappelé à chaque fois qu'un nouveau groupe est retiré de final groupe (surtout pour des pattern de ligne complexe)
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

            System.out.println(myGroup.size());
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
    }

//    @Override
//    public void draw(float x, float y) {
//        int defaultX = (int)x, defaultY = (int)y;
//        //Guides us through our hashmap group
//        Integer nbGroup = 0;
//        //Let's us know if we need to create a new hashmap in groups
//        boolean newGroup = true;
//        //Contains into potential groups at first every pixel that is the same color as initial color
//        //HashMap<Point, int[]> pixels = new HashMap<>();
//        HashMap<Integer, HashMap<Point, Point>> groups = new HashMap<>();
//        HashMap<Integer, HashMap<Integer, HashMap<Point, Point>>> finalGroups = new HashMap<>();
//        Point currentPoint;
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//        {
//            this.initialColor = this.bitmap.getColor(defaultX, defaultY).toArgb();
//
//            //Making of initial groups
//            for(int i = 0; i < this.bitmap.getHeight(); ++i)
//            {
//                for(int j = 0; j < this.bitmap.getWidth(); ++j)
//                {
//                    if(this.bitmap.getColor(j, i).toArgb() == this.initialColor)
//                    {
//                        if(!groups.containsKey(nbGroup))
//                        {
//                            groups.put(nbGroup, new HashMap<>());
//                            newGroup = false;
//                        }
//
//                        groups.get(nbGroup).put(new Point(j, i), new Point(j, i));
//                    }
//                    else
//                    {
//                        if(!newGroup)
//                        {
//                            ++nbGroup;
//                            newGroup = true;
//                        }
//                    }
//                }
//            }
//            System.out.println(groups.size());
//
//            boolean breakTime = false;
//            Point[] myPoints = new Point[8];
//            Vector<Integer> groupsRemoved = new Vector<>();
//            int initialGroupsSize = groups.size() - 1;
//            for(int i = 0; i < myPoints.length; ++i)
//            {
//                myPoints[i] = new Point();
//            }
//
//            //Making aglomeration of most conjointed groups
//            for(int i = 0; i < groups.size(); ++i)
//            {
//                breakTime = false;
//
//                for(Point j : groups.get(i).values())
//                {
//                    myPoints[0].set(j.x + 1, j.y);
//                    myPoints[1].set(j.x - 1, j.y);
//                    myPoints[2].set(j.x, j.y + 1);
//                    myPoints[3].set(j.x, j.y - 1);
//                    myPoints[4].set(j.x + 1, j.y + 1);
//                    myPoints[5].set(j.x - 1, j.y - 1);
//                    myPoints[6].set(j.x - 1, j.y + 1);
//                    myPoints[7].set(j.x + 1, j.y - 1);
//
//                    for(HashMap<Integer, HashMap<Point, Point>> k : finalGroups.values())
//                    {
//                        for(HashMap<Point, Point> l : k.values())
//                        {
//                            for(Point m : myPoints)
//                            {
//                                if(l.containsKey(m))
//                                {
//                                    k.put(k.size(), groups.get(i));
//                                    groupsRemoved.add(i);
//                                    breakTime = true;
//                                }
//
//                                if(breakTime)
//                                {
//                                    break;
//                                }
//                            }
//
//                            if(breakTime)
//                            {
//                                break;
//                            }
//                        }
//                    }
//
//                    if(breakTime)
//                    {
//                        break;
//                    }
//                }
//
//                if(!breakTime)
//                {
//                    HashMap<Integer, HashMap<Point, Point>> temp = new HashMap<>();
//                    temp.put(0, groups.get(i));
//                    finalGroups.put(finalGroups.size(), temp);
//                }
//            }
//            System.out.println(finalGroups.size());
//            //Removal of groups which have already been added to other groups (the fuck is this for?)
//            for(Integer i : groupsRemoved)
//            {
//                groups.remove(i);
//            }
//
//            myPoints[0].set(defaultX, defaultY);
//            breakTime = false;
//            HashMap<Integer, HashMap<Integer, HashMap<Point, Point>>> myGroup = new HashMap<>();
//            HashMap<Point, Integer[]> pixels = new HashMap<>();
//            //Finding the group containing the pixel clicked by the user
//            for(HashMap<Integer, HashMap<Point, Point>> i : finalGroups.values())
//            {
//                for(HashMap<Point, Point> j : i.values())
//                {
//                    if(j.containsKey(myPoints[0]))
//                    {
//                        myGroup.put(myGroup.size(), i);
////                        for(Point k : j.values())
////                        {
////                            pixels.put(k, new Integer[2]);
////                            pixels.get(k)[0] = i.keySet()[0];
////                        }
//                        breakTime = true;
//                    }
//
//                    if(breakTime)
//                    {
//                        break;
//                    }
//                }
//
//                if(breakTime)
//                {
//                    break;
//                }
//            }
//            System.out.println(myGroup.size());
//
//            //Adding stragglers back to the group where user clicked (So this works, it is horribly slow tho) :(
//            //Potential optimization would be to reformat the way myGroup is made
//            //So that we can just ctrl+c ctrl+v the group that needs to be added added instead of adding each value one by one
//            //Could also potentially reduce the number of points checked back to 4?
//            for(HashMap<Integer, HashMap<Point, Point>> i : finalGroups.values())
//            {
//                breakTime = false;
//                if(i != myGroup.get(0))
//                {
//                    for(HashMap<Point, Point> j : i.values())
//                    {
//                        for(Point k : j.values())
//                        {
//                            myPoints[0].set(k.x + 1, k.y);
//                            myPoints[1].set(k.x - 1, k.y);
//                            myPoints[2].set(k.x, k.y + 1);
//                            myPoints[3].set(k.x, k.y - 1);
//                            myPoints[4].set(k.x + 1, k.y + 1);
//                            myPoints[5].set(k.x - 1, k.y - 1);
//                            myPoints[6].set(k.x - 1, k.y + 1);
//                            myPoints[7].set(k.x + 1, k.y - 1);
//
//                            for(HashMap<Point, Point> l : myGroup.get(0).values())
//                            {
//                                for(Point m : myPoints)
//                                {
//                                    if(l.containsKey(m))
//                                    {
//                                        myGroup.put(myGroup.size(), i);
//                                        breakTime = true;
//                                        break;
//                                    }
//                                }
//
//                                if(breakTime)
//                                {
//                                    break;
//                                }
//                            }
//
//                            if(breakTime)
//                            {
//                                break;
//                            }
//                        }
//
//                        if(breakTime)
//                        {
//                            break;
//                        }
//                    }
//                }
//            }
//            System.out.println(myGroup.size());
//            //Changing the color of the group the user clicked
//            for(HashMap<Integer, HashMap<Point, Point>> i : myGroup.values())
//            {
//                for(HashMap<Point, Point> j : i.values())
//                {
//                    for(Point k : j.values())
//                    {
//                        if(MainActivity.couleurs.contains(this.color))
//                        {
//                            this.bitmap.setPixel(k.x, k.y, ContextCompat.getColor(this.context, this.color));
//                        }
//                        else
//                        {
//                            this.bitmap.setPixel(k.x, k.y, this.color);
//                        }
//                    }
//                }
//            }
//        }
//    }


//    @Override
//    public void draw(float x, float y) {
//        int defaultX = (int)x, defaultY = (int)y;
//        //Guides us through our hashmap group
//        Integer nbGroup = 0;
//        //Let's us know if we need to create a new hashmap in groups
//        boolean newGroup = true;
//        //Contains into potential groups at first every pixel that is the same color as initial color
//        HashMap<Integer, HashMap<Point, Point>> groups = new HashMap<>();
//        HashMap<Integer, HashMap<Integer, HashMap<Point, Point>>> finalGroups = new HashMap<>();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//        {
//            this.initialColor = this.bitmap.getColor(defaultX, defaultY).toArgb();
//
//            //Making of initial groups
//            for(int i = 0; i < this.bitmap.getHeight(); ++i)
//            {
//                for(int j = 0; j < this.bitmap.getWidth(); ++j)
//                {
//                    if(this.bitmap.getColor(j, i).toArgb() == this.initialColor)
//                    {
//                        if(!groups.containsKey(nbGroup))
//                        {
//                            groups.put(nbGroup, new HashMap<>());
//                            newGroup = false;
//                        }
//
//                        groups.get(nbGroup).put(new Point(j, i), new Point(j, i));
//                    }
//                    else
//                    {
//                        if(!newGroup)
//                        {
//                            ++nbGroup;
//                            newGroup = true;
//                        }
//                    }
//                }
//            }
//            System.out.println(groups.size());
//            boolean breakTime = false;
//            Point[] myPoints = new Point[8];
//            Vector<Integer> groupsRemoved = new Vector<>();
//            int initialGroupsSize = groups.size() - 1;
//            for(int i = 0; i < myPoints.length; ++i)
//            {
//                myPoints[i] = new Point();
//            }
//
//            //Making aglomeration of most conjointed groups
//            for(int i = 0; i < groups.size(); ++i)
//            {
//                breakTime = false;
//
//                for(Point j : groups.get(i).values())
//                {
//                    myPoints[0].set(j.x + 1, j.y);
//                    myPoints[1].set(j.x - 1, j.y);
//                    myPoints[2].set(j.x, j.y + 1);
//                    myPoints[3].set(j.x, j.y - 1);
//                    myPoints[4].set(j.x + 1, j.y + 1);
//                    myPoints[5].set(j.x - 1, j.y - 1);
//                    myPoints[6].set(j.x - 1, j.y + 1);
//                    myPoints[7].set(j.x + 1, j.y - 1);
//
//                    for(HashMap<Integer, HashMap<Point, Point>> k : finalGroups.values())
//                    {
//                        for(HashMap<Point, Point> l : k.values())
//                        {
//                            for(Point m : myPoints)
//                            {
//                                if(l.containsKey(m))
//                                {
//                                    k.put(k.size(), groups.get(i));
//                                    groupsRemoved.add(i);
//                                    breakTime = true;
//                                }
//
//                                if(breakTime)
//                                {
//                                    break;
//                                }
//                            }
//
//                            if(breakTime)
//                            {
//                                break;
//                            }
//                        }
//                    }
//
//                    if(breakTime)
//                    {
//                        break;
//                    }
//                }
//
//                if(!breakTime)
//                {
//                    HashMap<Integer, HashMap<Point, Point>> temp = new HashMap<>();
//                    temp.put(0, groups.get(i));
//                    finalGroups.put(finalGroups.size(), temp);
//                }
//            }
//            System.out.println(finalGroups.size());
//            //Removal of groups which have already been added to other groups
//            for(Integer i : groupsRemoved)
//            {
//                groups.remove(i);
//            }
//
//            myPoints[0].set(defaultX, defaultY);
//            breakTime = false;
//            HashMap<Integer, HashMap<Point, Point>> myGroup = new HashMap<>();
//
//            //Finding the group containing the pixel clicked by the user
//            for(HashMap<Integer, HashMap<Point, Point>> i : finalGroups.values())
//            {
//                for(HashMap<Point, Point> j : i.values())
//                {
//                    if(j.containsKey(myPoints[0]))
//                    {
//                        myGroup = i;
//                        breakTime = true;
//                    }
//
//                    if(breakTime)
//                    {
//                        break;
//                    }
//                }
//
//                if(breakTime)
//                {
//                    break;
//                }
//            }
//            System.out.println(myGroup.size());
//
//            //Adding stragglers back to the group where user clicked (So this works, it is horribly slow tho) :(
//            //Potential optimization would be to reformat the way myGroup is made
//            //So that we can just ctrl+c ctrl+v the group that needs to be added added instead of adding each value one by one
//            //Could also potentially reduce the number of points checked back to 4?
//            for(HashMap<Integer, HashMap<Point, Point>> i : finalGroups.values())
//            {
//                breakTime = false;
//                if(i != myGroup)
//                {
//                    for(HashMap<Point, Point> j : i.values())
//                    {
//                        for(Point k : j.values())
//                        {
//                            myPoints[0].set(k.x + 1, k.y);
//                            myPoints[1].set(k.x - 1, k.y);
//                            myPoints[2].set(k.x, k.y + 1);
//                            myPoints[3].set(k.x, k.y - 1);
//                            myPoints[4].set(k.x + 1, k.y + 1);
//                            myPoints[5].set(k.x - 1, k.y - 1);
//                            myPoints[6].set(k.x - 1, k.y + 1);
//                            myPoints[7].set(k.x + 1, k.y - 1);
//
//                            for(HashMap<Point, Point> l : myGroup.values())
//                            {
//                                for(Point m : myPoints)
//                                {
//                                    if(l.containsKey(m))
//                                    {
//                                        for(HashMap<Point, Point> n : i.values())
//                                        {
//                                            myGroup.put(-myGroup.size(), n); //Needs to be negative to avoid overwritting
//                                        }
//                                        breakTime = true;
//                                        break;
//                                    }
//                                }
//
//                                if(breakTime)
//                                {
//                                    break;
//                                }
//                            }
//
//                            if(breakTime)
//                            {
//                                break;
//                            }
//                        }
//
//                        if(breakTime)
//                        {
//                            break;
//                        }
//                    }
//                }
//            }
//            System.out.println(myGroup.size());
//            //Changing the color of the group the user clicked
//            for(HashMap<Point, Point> i : myGroup.values())
//            {
//                for(Point j : i.values())
//                {
//                    if(MainActivity.couleurs.contains(this.color))
//                    {
//                        this.bitmap.setPixel(j.x, j.y, ContextCompat.getColor(this.context, this.color));
//                    }
//                    else
//                    {
//                        this.bitmap.setPixel(j.x, j.y, this.color);
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public void draw(float x, float y) {
//        int defaultX = (int)x, defaultY = (int)y;
//        //Guides us through our hashmap group
//        Integer nbGroup = 0;
//        //Let's us know if we need to create a new hashmap in groups
//        boolean newGroup = true;
//        //Contains into potential groups at first every pixel that is the same color as initial color
//        HashMap<Integer, HashMap<Point, Point>> groups = new HashMap<>();
//        HashMap<Integer, HashMap<Point, Point>> finalGroups = new HashMap<>();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//        {
//            this.initialColor = this.bitmap.getColor(defaultX, defaultY).toArgb();
//
//            for(int i = 0; i < this.bitmap.getHeight(); ++i)
//            {
//                for(int j = 0; j < this.bitmap.getWidth(); ++j)
//                {
//                    if(this.bitmap.getColor(j, i).toArgb() == this.initialColor)
//                    {
//                        if(!groups.containsKey(nbGroup))
//                        {
//                            groups.put(nbGroup, new HashMap<>());
//                            newGroup = false;
//                        }
//
//                        groups.get(nbGroup).put(new Point(j, i), new Point(j, i));
//                    }
//                    else
//                    {
//                        if(!newGroup)
//                        {
//                            ++nbGroup;
//                            newGroup = true;
//                        }
//                    }
//                }
//            }
//            System.out.println(groups.size());
//
//            boolean breakTime = false;
//            Point[] myPoints = new Point[4];
//            for(int i = 0; i < myPoints.length; ++i)
//            {
//                myPoints[i] = new Point();
//            }
//
//            for(int i = 0; i < groups.size(); ++i)
//            {
//                breakTime = false;
//
//                for(Point j : groups.get(i).values())
//                {
//                    myPoints[0].set(j.x + 1, j.y);
//                    myPoints[1].set(j.x - 1, j.y);
//                    myPoints[2].set(j.x, j.y + 1);
//                    myPoints[3].set(j.x + 1, j.y - 1);
//
//                    for(HashMap<Point, Point> k : finalGroups.values())
//                    {
//                        if(k.containsKey(myPoints[0])
//                                || k.containsKey(myPoints[1])
//                                || k.containsKey(myPoints[2])
//                                || k.containsKey(myPoints[3]))
//                        {
//                            for(Point m : groups.get(i).values())
//                            {
//                                k.put(m, m);
//                            }
//
//                            breakTime = true;
//                        }
//
//                        if(breakTime)
//                        {
//                            break;
//                        }
//                    }
//
//                    if(breakTime)
//                    {
//                        break;
//                    }
//                }
//
//                if(!breakTime)
//                {
//                    finalGroups.put(finalGroups.size(), groups.get(i));
//                }
//            }
//
//            System.out.println(finalGroups.size());
//
//            myPoints[0].set(defaultX, defaultY);
//            HashMap<Point, Point> myGroup = new HashMap<>();
//            for(HashMap<Point, Point> i : finalGroups.values())
//            {
//                if(i.containsKey(myPoints[0]))
//                {
//                    myGroup = i;
//                    break;
//                }
//            }
//
//            System.out.println(myGroup.size());
//
//            for(Point i : myGroup.values())
//            {
//                if(MainActivity.couleurs.contains(this.color))
//                {
//                    this.bitmap.setPixel(i.x, i.y, ContextCompat.getColor(this.context, this.color));
//                }
//                else
//                {
//                    this.bitmap.setPixel(i.x, i.y, this.color);
//                }
//            }
//        }
//    }

//    @Override
//    public void draw(float x, float y) {
//        int defaultX = (int)x, defaultY = (int)y,
//                max = this.bitmap.getWidth() * this.bitmap.getHeight(),
//                iteration = 0, wrongCount = 0;
//        int perimeterThickness = 1;
//        int perimeter = (int)(Math.pow(3 + 2 * (perimeterThickness - 1), 2)
//                - Math.pow(3 + 2 * (perimeterThickness - 2), 2));
//        int tempX = defaultX, tempY = defaultY;
//        int count = 0;
//        int side = 0;
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//        {
//            while(iteration < max)
//            {
//                //Determines the next pixel to be potentielly color changed
//                if(count == 0 && side == 0)
//                {
//                    tempX = defaultX - perimeterThickness + 1;
//                    tempY = defaultY + perimeterThickness;
//                }
//                else if(count < perimeter / 4 && side == 0)
//                {
//                    tempX += 1;
//                }
//                else if(count < perimeter / 4 && side == 1)
//                {
//                    tempY -= 1;
//                }
//                else if(count < perimeter / 4 && side == 2)
//                {
//                    tempX -= 1;
//                }
//                else if(count < perimeter / 4 && side == 3)
//                {
//                    tempY += 1;
//                }
//                ++count;
//
//                //Change la couleur du pixel pour celle désiré
//                if(this.initialColor == 1)
//                {
//                    this.initialColor = this.bitmap.getColor(tempX, tempY).toArgb();
//
//                    if(MainActivity.couleurs.contains(this.color))
//                    {
//                        this.bitmap.setPixel(tempX, tempY, ContextCompat.getColor(this.context, this.color));
//                    }
//                    else
//                    {
//                        this.bitmap.setPixel(tempX, tempY, this.color);
//                    }
//                }
//                else if(this.bitmap.getWidth() > tempX  && tempX >= 0
//                        && this.bitmap.getHeight() > tempY && tempY >= 0)
//                {
//                    if(this.bitmap.getColor(tempX, tempY).toArgb() == this.initialColor)
//                    {
//                        wrongCount = 0;
//                        if(MainActivity.couleurs.contains(this.color))
//                        {
//                            this.bitmap.setPixel(tempX, tempY, ContextCompat.getColor(this.context, this.color));
//                        }
//                        else
//                        {
//                            this.bitmap.setPixel(tempX, tempY, this.color);
//                        }
//                    }
//                    else
//                    {
//                        this.check[tempY][tempX] = false;
//                    }
//
//                    ++iteration;
//                }
//                else
//                {
//                    ++wrongCount;
//                    if(wrongCount >= perimeter)
//                    {
//                        break;
//                    }
//
//                    if(this.bitmap.getWidth() > tempX  && tempX >= 0
//                            && this.bitmap.getHeight() > tempY && tempY >= 0)
//                        ++iteration; //nb iteration reach max too fast parce qu'on compte les point out
//                }
//
//                //Met en place les variables pour le prochain cycle
//                if(count >= perimeter / 4 && side >= 3)
//                {
//                    count = 0;
//                    side = 0;
//                    wrongCount = 0;
//
//                    ++perimeterThickness;
//
//                    //Calculer le nombre de pixel composant le périmètre
//                    perimeter = (int)(Math.pow(3 + 2 * (perimeterThickness - 1), 2)
//                            - Math.pow(3 + 2 * (perimeterThickness - 2), 2));
//                }
//
//                //Met en place les variables pour le prochain côté
//                if(count >= perimeter / 4)
//                {
//                    count = 0;
//                    ++side;
//                }
//            }
//        }
//    }



    public Bitmap getBitmap() {
        return bitmap;
    }
}
