package br.com.stralom.entities;

public class Category {
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
}
