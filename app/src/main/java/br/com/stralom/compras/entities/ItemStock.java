package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bruno Strano on 24/01/2018.
 */


public class ItemStock extends Item implements Parcelable {
    private Stock stock;
    private double stockPercentage;
    private Status status;
    private double actualAmount;


    public ItemStock() {
        this.actualAmount = 0;
        this.amount = 0;
    }

    public enum Status{
        EMPTY,BAD,NEUTRAL,GOOD,FULL
    }

    public ItemStock(Long id, double amount, double total, int stockPercentage, Status status, double actualAmount, Stock stock) {
        super(amount, total);
        this.stockPercentage = stockPercentage;
        this.status = status;
        this.actualAmount = actualAmount;
        this.stock = stock;
    }

    public ItemStock(double amount, double actualAmount) {
        super(amount);
        this.actualAmount = actualAmount;
        setStockPercentage(amount, actualAmount);
        setStatus();
    }


    protected ItemStock(Parcel in) {
        super(in);
        stockPercentage = in.readDouble();
        actualAmount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(stockPercentage);
        dest.writeDouble(actualAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemStock> CREATOR = new Creator<ItemStock>() {
        @Override
        public ItemStock createFromParcel(Parcel in) {
            return new ItemStock(in);
        }

        @Override
        public ItemStock[] newArray(int size) {
            return new ItemStock[size];
        }
    };

    /**
     * This method cheks the value of the stockPercentage attribute and updates the status with the correspondent value
     */
    public void setStatus(){
        if(stockPercentage == 0){
            this.status = Status.EMPTY;
        } else if (stockPercentage < 30) {
            this.status = Status.BAD;
        } else if (stockPercentage < 65){
            this.status = Status.NEUTRAL;
        } else if(stockPercentage <= 99) {
            this.status = Status.GOOD;
        } else if(stockPercentage == 100){
            this.status = Status.FULL;
        }

    }

    private void setStockPercentage(double amount, double actualAmount) {
        this.stockPercentage = ((100 * actualAmount)/amount);
    }

    public void setAmounts( double actualAmount, double maxAmount){
        this.actualAmount = actualAmount;
        this.amount = maxAmount;
        setStockPercentage((int) this.amount,this.actualAmount);
        setStatus();
    }

    public String getFormattedActualAmount(){
        return super.formatAmount(this.actualAmount);
    }


    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public double getStockPercentage() {
        return stockPercentage;
    }

    public void setStockPercentage(int stockPercentage) {
        this.stockPercentage = stockPercentage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ItemStock{" +
                "stock=" + stock +
                ", stockPercentage=" + stockPercentage +
                ", status=" + status +
                ", atualAmount=" + actualAmount +
                '}';
    }
}
