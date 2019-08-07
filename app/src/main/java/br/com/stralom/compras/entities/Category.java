package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private String name;
    private String nameInternacional;
    private boolean isDefault;
    private int iconFlag;

    public Category() {
    }

    public Category(String name, String nameInternacional, int iconFlag) {
        this.name = name;
        this.nameInternacional = nameInternacional;
        this.iconFlag = iconFlag;
    }

    protected Category(Parcel in) {
        name = in.readString();
        nameInternacional = in.readString();
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
        if(aDefault == 1) {
            isDefault = true;
        }
        if(aDefault == 0){
            isDefault = false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconFlag() {
        return iconFlag;
    }

    public void setIconFlag(int iconFlag) {
        this.iconFlag = iconFlag;
    }

    public String getNameInternacional() {
        return nameInternacional;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", nameInternacional='" + nameInternacional + '\'' +
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
        parcel.writeString(name);
        parcel.writeString(nameInternacional);
        parcel.writeByte((byte) (isDefault ? 1 : 0));
        parcel.writeInt(iconFlag);
    }
}
