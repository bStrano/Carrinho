package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.stralom.compras.R;


public class Category implements Parcelable {
    private String tag;
    private String name;
    private boolean isDefault;
    private int iconFlag;

    public Category() {
    }

    public Category(String tag) {
        this.tag = tag;
        switch (this.tag) {
            case "Frutas":
                this.iconFlag = R.drawable.cherries;
                this.name = "Frutas";
                break;
            case "Meat":
                this.iconFlag = R.drawable.meat;
                this.name = "Carnes";
                break;
            case "Temporary":
                this.iconFlag = R.drawable.ic_help;
                this.name = "Produtos Temporários";
                break;
            case "Candies":
                this.iconFlag = R.drawable.ic_help;
                this.name = "Doces";
                break;
            default:
                this.iconFlag = R.drawable.ic_help;
        }
    }



    protected Category(Parcel in) {
        tag = in.readString();
        name = in.readString();
        isDefault = in.readByte() != 0;
        iconFlag = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(int aDefault) {
        if (aDefault == 1) {
            isDefault = true;
        }
        if (aDefault == 0) {
            isDefault = false;
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIconFlag() {
        return iconFlag;
    }

    public void setIconFlag(int iconFlag) {
        this.iconFlag = iconFlag;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", isDefault=" + isDefault +
                ", iconFlag=" + iconFlag +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(name);
        parcel.writeByte((byte) (isDefault ? 1 : 0));
        parcel.writeInt(iconFlag);
    }
}
