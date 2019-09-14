package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.ObservableArrayList;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.activities.MainActivity;
import br.com.stralom.compras.activities.ProfileActivity;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.entities.Profile;
import br.com.stralom.compras.entities.Recipe;



public class ProfileItemAdapter extends RecyclerView.Adapter<ProfileItemAdapter.ProfileItemAdapterHolder> {

    private ArrayList<Profile> profileList;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    public ProfileItemAdapter(ArrayList<Profile> profileList, Activity activity) {
        super();
        this.profileList = profileList;
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("profiles", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ProfileItemAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_profiles,parent, false);
        return new ProfileItemAdapterHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ProfileItemAdapterHolder holder, int position) {
        Profile profile = profileList.get(position);
        holder.productText.setText(activity.getString(R.string.profile_itemList_productCount, profile.getProductsNumber()));
        holder.recipeText.setText(activity.getString(R.string.profile_itemList_recipeCount,profile.getRecipesNumber()));
        holder.cartText.setText(activity.getString(R.string.profile_itemList_cartCount,profile.getItemCartNumber()));
        holder.stockText.setText(activity.getString(R.string.profile_itemList_stockCount, profile.getItemStockNumber()));
        holder.shareCodeText.setText(profile.getShareCode());
        if(profile.isActive()){
            holder.activeText.setVisibility(View.VISIBLE);
        } else {
            holder.activeText.setVisibility(View.GONE);
        }

        holder.profileName.setText(profile.getName());
//        Recipe recipe = list.get(position);
//        holder.name.setText(String.format(res.getString(R.string.recipe_itemList_name),recipe.getName()));
//        holder.price.setText(String.format(res.getString(R.string.recipe_itemList_price), recipe.getTotal()));
//        holder.ingredientCount.setText(String.format(res.getString(R.string.recipe_itemList_ingredientCount),recipe.getIngredientCount()));
//        holder.foregroundView.setBackgroundColor(Color.parseColor("#FAFAFA"));


    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }




    public class ProfileItemAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView profileName;
        public TextView productText;
        public TextView recipeText;
        public TextView cartText;
        public TextView stockText;
        public TextView activeText;
        public TextView shareCodeText;
        public ProfileItemAdapterHolder(@NonNull View itemView) {
            super(itemView);
            this.productText = itemView.findViewById(R.id.item_profile_product);
            this.recipeText = itemView.findViewById(R.id.item_profile_recipe);
            this.cartText = itemView.findViewById(R.id.item_profile_cart);
            this.stockText = itemView.findViewById(R.id.item_profile_stock);
            this.profileName = itemView.findViewById(R.id.item_profile_name);
            this.activeText = itemView.findViewById(R.id.item_profile_active);
            this.shareCodeText = itemView.findViewById(R.id.item_profile_sharecode);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("Teste", "Teste");
            Profile profile = profileList.get(getAdapterPosition());
            Log.d("AA21", profile.toString());
            createConsumeDialog(profile);

        }
    }

    public void createConsumeDialog(final Profile profile) {
        Log.d("ATESTE", profile.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_profile_update, null);
       // final Spinner shareSpinner = dialogView.findViewById(R.id.profile_dialog_shareSpinner);
        final Switch activeSwitch = dialogView.findViewById(R.id.profile_dialog_activeSwitch);
        final Switch shareSwitch = dialogView.findViewById(R.id.profile_dialog_shareSwitch);
        TextView name = dialogView.findViewById(R.id.profile_dialog_name);
        TextView title = dialogView.findViewById(R.id.profile_dialog_title);
        String[] opcoesCompartilhamento = {"Todos", "Ninguem", "Seletivo"};
        title.setText("Atualizar Perfil");
       // shareSpinner.setAdapter(new ArrayAdapter<String>(this.activity, android.R.layout.simple_list_item_1, opcoesCompartilhamento));
        activeSwitch.setChecked(profile.isActive());
        name.setText(profile.getName());
        Log.d("Teste", profile.getName());
        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Dialog", profile.toString());
                        Log.e("ZZZZ", "xxx");
                        Log.d("Dialog", "xxx");
                        Log.d("Dialog", "xxx");
                        Log.d("Dialog", "xxx");

                    }

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profile.isActive() != activeSwitch.isChecked() ){
                    if(activeSwitch.isChecked()){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(activity.getString(R.string.sharedPreferences_selectedProfile), profile.getId());
                        editor.apply();
                        profile.setActive(true);
                        activeSwitch.setChecked(true);
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(activity.getString(R.string.sharedPreferences_selectedProfile));
                        editor.apply();
                        profile.setActive(false);
                        activeSwitch.setChecked(false);
                    }


                    for(Profile profileItem : profileList){
                        if(!profileItem.getId().equals(profile.getId())){
                            profileItem.setActive(false);
                        } else {
                            profileItem.setActive(true);
                        }
                    }

                    ArrayList<Profile> profilesClone = (ArrayList<Profile>) profileList.clone();
                    profileList.clear();
                    profileList.addAll(profilesClone);
                    notifyDataSetChanged();
                }
                dialog.dismiss();

            }

        });
    }





}
