package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;

public class DessinLibre extends Formes {
    private Path path;

    public DessinLibre(Context context, int color, int sizeTrace)
    {
        super(context, color, sizeTrace);
        this.path = new Path();
    }

    //Dessine la forme libre
    @Override
    public void draw(float x, float y)
    {
        if(this.path.isEmpty())
        {
            this.path.moveTo(x, y);
        }
        else
        {
            this.path.lineTo(x, y);
        }
    }

    //Permet d'afficher la forme
    public Path getPath() {
        return path;
    }

    //Permet de fermer une forme libre
    public void closePath()
    {
        this.path.close();
    }
}
