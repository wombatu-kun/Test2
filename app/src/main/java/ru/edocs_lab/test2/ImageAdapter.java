package ru.edocs_lab.test2;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private WorldManager mWorldManager;
    private int mCellWidth;
    private int mCellHeight;

    public ImageAdapter(Context c, int width, int height) {
        mContext = c;
        mWorldManager = WorldManager.get();
        mCellWidth = width;
        mCellHeight = height;
    }

    public int getCount() {
        return WorldManager.COLUMNS * WorldManager.ROWS;
    }

    public Object getItem(int position) {
        return mWorldManager.getCellContent(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(mCellWidth, mCellHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(Color.BLUE);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        Cell item = (Cell)getItem(position);
        if (item.getType() != Type.EMPTY) {
            imageView.setImageResource(item.getPicture());
        } else {
            imageView.setImageBitmap(null);
        }
        return imageView;
    }
}
