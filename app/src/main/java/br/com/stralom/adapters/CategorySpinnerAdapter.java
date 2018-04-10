package br.com.stralom.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Category;

public class CategorySpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Category> categories;
    LayoutInflater inflater;
    Resources res;

    public CategorySpinnerAdapter(Context context,ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
        inflater = LayoutInflater.from(context);
        res = context.getResources();
    }

    @Override
    public int getCount(){
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = inflater.inflate(R.layout.spinner_item_category,parent,false);
        ImageView icon = convertView.findViewById(R.id.category_spinner_icon);
        TextView name = convertView.findViewById(R.id.category_spinner_name);

        Category category = categories.get(position);
        icon.setImageDrawable(ResourcesCompat.getDrawable(res,category.getIconFlag(),null));
        name.setText(category.getName());
        return convertView;
    }
}
