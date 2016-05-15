package daniel.daniel_pset4;

/*
*  Student name: Daniel Oliemans
*  Student number: 11188669
*  Universiteit van Amsterdam
*/

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<ToDoItem> {

    Context context;
    int layoutResourceId;
    ArrayList<ToDoItem> data;

    public ItemAdapter(Context context, int layoutResourceId, ArrayList<ToDoItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ToDoHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ToDoHolder();
            holder.txtTitle = (TextView)row.findViewById(android.R.id.text1);

            row.setTag(holder);
        }
        else {
            holder = (ToDoHolder)row.getTag();
        }

        ToDoItem item = data.get(position);
        holder.txtTitle.setText(item.getTitle());
        if (item.isCompleted()) {
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.txtTitle.setPaintFlags(0);
        }

        return row;
    }

    static class ToDoHolder {
        TextView txtTitle;
    }
}
