package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.example.painttp2.MainActivity;
import com.example.painttp2.R;


//So what are we taking away from this.
//1. The algorithm clearly doesnt work
//That's it you a dumbass get gud
public class Remplir extends Formes{
    private Bitmap bitmap;
    private int color;
    private int initialColor = 1;

    private Context context;
    public Remplir(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);
        this.context = context;
        this.bitmap = MainActivity.bitmap;
        this.color = color;
    }

    @Override
    public void draw(float x, float y) {
        int defaultX = (int)x, defaultY = (int)y,
                max = this.bitmap.getWidth() * this.bitmap.getHeight(),
                iteration = 0, wrongCount = 0;
        int perimeterThickness = 1;
        int perimeter = (int)(Math.pow(3 + 2 * (perimeterThickness - 1), 2)
                - Math.pow(3 + 2 * (perimeterThickness - 2), 2));
        int tempX = defaultX, tempY = defaultY;
        boolean[] corners = {false, false, false, false};
        int count = 0;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            while(iteration < max)
            {
                if(count < perimeter)
                {
                    if(count == 0)
                    {
                        tempX = defaultX - perimeterThickness + 1;
                        tempY = defaultY + perimeterThickness;
                    }
                    else if(!corners[0])
                    {
                        tempX += 1;
                    }
                    else if(!corners[1])
                    {
                        tempY += 1;
                    }
                    else if(!corners[2])
                    {
                        tempX -= 1;
                    }
                    else if(!corners[3])
                    {
                        tempY -= 1;
                    }

                    ++count;
                }

                //Change la couleur du pixel pour celle désiré
                if(this.initialColor == 1)
                {
                    this.initialColor = this.bitmap.getColor(tempX, tempY).toArgb();
                    if(MainActivity.couleurs.contains(this.color))
                    {
                        this.bitmap.setPixel(tempX, tempY, ContextCompat.getColor(this.context, this.color));
                    }
                    else
                    {
                        this.bitmap.setPixel(tempX, tempY, this.color);
                    }
                }
                else if(this.bitmap.getWidth() > tempX  && tempX >= 0
                        && this.bitmap.getHeight() > tempY && tempY >= 0)
                {
                    if(this.bitmap.getColor(tempX, tempY).toArgb() == this.initialColor)
                    {
                        wrongCount = 0;
                        if(MainActivity.couleurs.contains(this.color))
                        {
                            this.bitmap.setPixel(tempX, tempY, ContextCompat.getColor(this.context, this.color));
                        }
                        else
                        {
                            this.bitmap.setPixel(tempX, tempY, this.color);
                        }
                        ++iteration;
                    }
                }
                else
                {
                    ++wrongCount;
                    ++iteration;
                    if(wrongCount >= perimeter)
                    {
                        break;
                    }
                }

                if(tempX == defaultX + perimeterThickness && tempY == defaultY + perimeterThickness)
                {
                    corners[0] = true;
                }
                else if(tempX == defaultX + perimeterThickness && tempY == defaultY - perimeterThickness)
                {
                    corners[1] = true;
                }
                else if(tempX == defaultX - perimeterThickness && tempY == defaultY - perimeterThickness)
                {
                    corners[2] = true;
                }
                else if(tempX == defaultX - perimeterThickness && tempY == defaultY + perimeterThickness)
                {
                    corners[3] = true;
                }

                //Met en place les variables pour le prochain cycle
                if(count >= perimeter)
                {
                    count = 0;

                    ++perimeterThickness;

                    for(boolean i : corners)
                    {
                        i = false;
                    }

                    //Calculer le nombre de pixel composant le périmètre
                    perimeter = (int)(Math.pow(3 + 2 * (perimeterThickness - 1), 2)
                            - Math.pow(3 + 2 * (perimeterThickness - 2), 2));
                }
            }
        }

//        for(int i = 0; i < this.bitmap.getHeight(); ++i)
//        {
//            for(int j = 0; j < this.bitmap.getWidth(); ++j)
//            {
//                this.bitmap.setPixel(j, i, Color.RED);
//            }
//        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
