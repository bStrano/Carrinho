package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class ItemCart extends Item  implements Parcelable {
    private Long convertedId;
    private boolean isRemoved;
    private boolean updateStock;

    protected ItemCart(Parcel in) {
        super(in);
        if (in.readByte() == 0) {
            convertedId = null;
        } else {
            convertedId = in.readLong();
        }
        isRemoved = in.readByte() != 0;
        updateStock = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if (convertedId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(convertedId);
        }
        dest.writeByte((byte) (isRemoved ? 1 : 0));
        dest.writeByte((byte) (updateStock ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemCart> CREATOR = new Creator<ItemCart>() {
        @Override
        public ItemCart createFromParcel(Parcel in) {
            return new ItemCart(in);
        }

        @Override
        public ItemCart[] newArray(int size) {
            return new ItemCart[size];
        }
    };

    public Map toHash(){
        Map<String, Object> map = new HashMap<>();
        map.put("convertedId",this.convertedId);
        map.put("isRemoved",this.isRemoved);
        map.put("updateStock",this.updateStock);
        return map;
    }

    public ItemCart(double amount) {
        super(amount);
        isRemoved = false;
        updateStock = false;
    }

    public ItemCart(Long id,double amount) {
        super(id, amount);
        isRemoved = false;
        updateStock = false;
    }


    public ItemCart() {
        this.amount = 0;
        isRemoved = false;
        updateStock = false;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public boolean isUpdateStock() {
        return updateStock;
    }

    public void setUpdateStock(int updateStock) {
        if(updateStock == 0) {
            this.updateStock = false;
        } else {
            this.updateStock = true;
        }
    }

    public void setUpdateStock(boolean updateStock){
        this.updateStock = updateStock;
    }

    public Long getConvertedId() {
        return convertedId;
    }

    public void setConvertedId(Long convertedId) {
        this.convertedId = convertedId;
    }


    /**
     *  This method convert a simpleItem into a ItemCart and returns the converted ItemCart
     *
     * @param simpleItem
     * @return
     */
    public static ItemCart convertToItemCart(SimpleItem simpleItem){
        Product product = new Product();
        product.setName(simpleItem.getName());
        ItemCart itemCart = new ItemCart(simpleItem.getAmount());
        itemCart.setConvertedId(simpleItem.getId());

        return itemCart;
    }


}
