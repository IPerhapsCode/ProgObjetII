package com.example.painttp2;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;

public class DessinLibre extends Formes {
    private Path path;

    public DessinLibre(Context context, int color, int sizeTrace, Paint.Style style)
    {
        super(context, color, sizeTrace, style);
    }

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

    public Path getPath() {
        return path;
    }
}
