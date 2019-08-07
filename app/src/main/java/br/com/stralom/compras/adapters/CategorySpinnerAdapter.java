package br.com.stralom.compras.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.Category;

public class CategorySpinnerAdapter extends BaseAdapter implements Filterable{
    private Context context;
    private ArrayList<Category> categories;
    private ArrayList<Category> categoriesClone;
    private LayoutInflater inflater;
    private Resources res;

    public CategorySpinnerAdapter(Context context,ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.categoriesClone = (ArrayList<Category>) categories.clone();
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

    @Override
    public Filter getFilter() {
            return new Filter() {


                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    ArrayList<Category> filteredList = new ArrayList<>();
                    if (charSequence == null || charSequence.length() == 0) {
                        filteredList.addAll(categoriesClone);
                    } else {
                        String input = charSequence.toString().toLowerCase();
                        for (Category category : categoriesClone) {
                            if (category.getName().toLowerCase().startsWith(input)) {
                                filteredList.add(category);
                            }
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    categories.clear();
                    categories.addAll((Collection<? extends Category>) filterResults.values);
                    notifyDataSetChanged();
                }
            };


    }
}
