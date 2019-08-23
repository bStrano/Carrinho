package br.com.stralom.compras.adapters;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.stralom.compras.activities.CartMain;
import br.com.stralom.compras.activities.ProductMain;
import br.com.stralom.compras.activities.RecipeMain;
import br.com.stralom.compras.activities.StockMain;
import br.com.stralom.compras.entities.Product;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
   private final String[] tabTitles;
   private HashMap data;

    public TabFragmentPagerAdapter(FragmentManager fm, String[] tabTitles, HashMap<String,ArrayList> data) {
        super(fm);
        this.tabTitles = tabTitles;
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("products", (ArrayList<Product>) data.get("products"));
        switch(position) {
            case 0:
                fragment =  new CartMain();
                 break;
            case 1:
                fragment =  new ProductMain();
                 break;
            case 2:
               fragment =  new RecipeMain();
               break;
            case 3:
                fragment =  new StockMain();
                break;
            default:
                return null;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles[position];
    }
}


