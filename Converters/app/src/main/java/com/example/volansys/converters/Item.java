package com.example.volansys.converters;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Date;

public class Item extends BaseObservable
{

    private double dose;
    private Date date;

    @Bindable
    public double getDose() {
        return dose;
    }

    public void setDose(double dose) {
        this.dose = dose;
        notifyPropertyChanged(BR.dose);
    }
    @Bindable
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        notifyPropertyChanged(BR.date);

    }
}