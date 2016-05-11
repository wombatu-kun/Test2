package ru.edocs_lab.test2;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private WorldManager worldManager;
    private int cellWidth;
    private int cellHeight;

    public ImageAdapter(Context c, int width, int height) {
        context = c;
        worldManager = WorldManager.get();
        cellWidth = width;
        cellHeight = height;
    }

    public int getCount() {
        return WorldManager.COLUMNS * WorldManager.ROWS;
    }

    public Object getItem(int position) {
        return worldManager.getCellContent(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(cellWidth, cellHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(Color.BLUE);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        CellContent item = (CellContent)getItem(position);
        if (item.getType() != Type.EMPTY) {
            imageView.setImageResource(item.getPicture());
        } else {
            imageView.setImageBitmap(null);
        }
        return imageView;
    }
}
