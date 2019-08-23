package br.com.stralom.compras.helper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.com.stralom.compras.entities.Product;

public class DataViewModel extends ViewModel {
    private MutableLiveData<List<Product>> products;
    public LiveData<List<Product>> getProducts() {
        if (products == null) {
            products = new MutableLiveData<List<Product>>();
            loadUsers();
        }
        return products;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}