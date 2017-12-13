package phototest2.com.phototest2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import phototest2.com.phototest2.model.Pictures;

/**
 * Created by Amanda on 08/12/2017.
 */

public class GridAdapter extends ArrayAdapter<Pictures> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Pictures> data = new ArrayList();

    public GridAdapter(Context context, int layoutResourceId, ArrayList<Pictures> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text_grid);
            holder.image = (ImageView) row.findViewById(R.id.image_grid);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Pictures pictures = data.get(position);
        holder.imageTitle.setText(pictures.toString());
        Bitmap bitmap = BitmapFactory.decodeFile(pictures.getPhotoPath());
        holder.image.setImageBitmap(bitmap);

        if (data.get(position).selected()) {
            holder.image.setBackgroundColor(Color.parseColor("#FF6347"));
        } else {
            holder.image.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }



}
