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
//1. Parfois certain pixel de la même couleur sont considéré comme étant de couleur différentes?
//2. La condition d'arrêt de l'algorithme est clairement insuffisante
// Elle ne sert pas a rien, mais il doit clairement y avoir un second failsafe
public class Remplir extends Formes{
    private Bitmap bitmap;
    private int color;
    private int initialColor = 1;
    private boolean[][] check;

    private Context context;
    public Remplir(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);
        this.context = context;
        this.bitmap = MainActivity.bitmap;
        this.color = color;
        this.check = new boolean[this.bitmap.getHeight()][this.bitmap.getWidth()];

        for(int i = 0; i < this.bitmap.getHeight(); ++i)
        {
            for(int j = 0; j < this.bitmap.getWidth(); ++j)
            {
                this.check[i][j] = true;
            }
        }
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
        int count = 0;
        int side = 0;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            while(iteration < max)
            {
                //Determines the next pixel to be potentielly color changed
                if(count == 0 && side == 0)
                {
                    tempX = defaultX - perimeterThickness + 1;
                    tempY = defaultY + perimeterThickness;
                }
                else if(count < perimeter / 4 && side == 0)
                {
                    tempX += 1;
                }
                else if(count < perimeter / 4 && side == 1)
                {
                    tempY -= 1;
                }
                else if(count < perimeter / 4 && side == 2)
                {
                    tempX -= 1;
                }
                else if(count < perimeter / 4 && side == 3)
                {
                    tempY += 1;
                }
                ++count;

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
                    }
                    else
                    {
                        this.check[tempY][tempX] = false;
                    }

                    ++iteration;
                }
                else
                {
                    ++wrongCount;
                    if(wrongCount >= perimeter)
                    {
                        break;
                    }

                    if(this.bitmap.getWidth() > tempX  && tempX >= 0
                            && this.bitmap.getHeight() > tempY && tempY >= 0)
                        ++iteration; //nb iteration reach max too fast parce qu'on compte les point out
                }

                //Met en place les variables pour le prochain cycle
                if(count >= perimeter / 4 && side >= 3)
                {
                    count = 0;
                    side = 0;
                    wrongCount = 0;

                    ++perimeterThickness;

                    //Calculer le nombre de pixel composant le périmètre
                    perimeter = (int)(Math.pow(3 + 2 * (perimeterThickness - 1), 2)
                            - Math.pow(3 + 2 * (perimeterThickness - 2), 2));
                }

                //Met en place les variables pour le prochain côté
                if(count >= perimeter / 4)
                {
                    count = 0;
                    ++side;
                }
            }
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
