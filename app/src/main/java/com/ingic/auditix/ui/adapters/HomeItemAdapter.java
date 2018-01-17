package com.ingic.auditix.ui.adapters;

import android.graphics.drawable.Drawable;

import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 12/19/2017.
 */

public class HomeItemAdapter extends WheelArrayAdapter<Drawable> {
    private List<Drawable> arrayitems;

    public HomeItemAdapter(List<Drawable> items) {
        super(items);
        arrayitems = new ArrayList<>();
        arrayitems.addAll(items);
    }

    @Override
    public Drawable getDrawable(int position) {
        return arrayitems.get(position);
    }
}
