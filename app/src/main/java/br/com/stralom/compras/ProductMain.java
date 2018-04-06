package br.com.stralom.compras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import br.com.stralom.adapters.ProductAdapter;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Product;
import br.com.stralom.helper.ProductForm;
import br.com.stralom.helper.SwipeToDeleteCallback;


public class ProductMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView productListView;
    private Button buttonView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProductMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductMain.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductMain newInstance(String param1, String param2) {
        ProductMain fragment = new ProductMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_produtos, container, false);

        buttonView = view.findViewById(R.id.btn_newProduct);
        productListView = view.findViewById(R.id.list_products);

        ProductDAO productDAO = new ProductDAO(getActivity());
        List<Product> productList;
        productList = productDAO.getAll();
        registerForContextMenu(productListView);

        // Recycler View
        ProductAdapter adapter = new ProductAdapter(productList,getActivity());
        productListView.setAdapter(adapter);
        productListView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListView.setHasFixedSize(true);
        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper itemTouch = new ItemTouchHelper(callback);
        itemTouch.attachToRecyclerView(productListView);


        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View newProductView = getLayoutInflater().inflate(R.layout.product_registration,null);
                final ProductForm productForm = new ProductForm(newProductView);


                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProductDAO productDAO = new ProductDAO(getActivity());
                        Product product = productForm.getProduct();
                        try{
                            productDAO.add(product);
                            getActivity().recreate();
                            Toast.makeText(getActivity(),"Produto salvo!",Toast.LENGTH_LONG).show();
                        }catch (android.database.sqlite.SQLiteConstraintException e){
                            Toast.makeText(getActivity(),"Produto já está cadastrado.",Toast.LENGTH_LONG).show();
                        }


                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Registro cancelado", Toast.LENGTH_LONG).show();
                    }
                });


                builder.setView(newProductView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.main_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_editId:
                Toast.makeText(getActivity(),"Produto editado!",Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_deleteId:
                Toast.makeText(getActivity(),"Produto deletado.", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
