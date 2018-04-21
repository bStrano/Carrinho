package br.com.stralom.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.stralom.compras.CartMain;
import br.com.stralom.compras.ProductMain;
import br.com.stralom.compras.RecipeMain;
import br.com.stralom.compras.StockMain;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
   private final String[] tabTitles;

    public TabFragmentPagerAdapter(FragmentManager fm,  String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new CartMain();
            case 1:
                return new ProductMain();
            case 2:
               return new RecipeMain();
            case 3:
                return new StockMain();
            default:
                return null;
        }
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


