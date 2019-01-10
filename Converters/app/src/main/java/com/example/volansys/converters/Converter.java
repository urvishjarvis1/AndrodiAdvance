package com.example.volansys.converters;

import android.content.res.Resources;
import android.databinding.BindingConversion;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseMethod;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converter
{
    @InverseMethod("toDate")
    @BindingConversion
    public static String fromDate(Date date) {
      return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
    @InverseBindingAdapter(attribute = "android:text")
    public static Date toDate(String value){
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }


}