package com.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;


import com.mow.cash.android.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActiveOrderPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Integer> pager;
    private final OnItemClickListener listener;

    public ActiveOrderPagerAdapter(Context context, ArrayList<Integer> pager, OnItemClickListener listener) {
        this.context = context;
        this.pager = pager;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return pager.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object o) {
        return view == o;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mow_active_order, container, false);
        TextView textViewReturnMyRental = (TextView) view.findViewById(R.id.textViewReturnMyRental);
        TextView textViewExtendMyRental = (TextView) view.findViewById(R.id.textViewExtendMyRental);
       // ImageView imageViewQuetionMark = (ImageView) view.findViewById(R.id.imageViewQuetionMark);
        textViewReturnMyRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v);
            }
        });

        textViewExtendMyRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v);
            }
        });
        /*imageViewQuetionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v);
            }
        });*/


       // imageView.setBackgroundResource(pager.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NotNull Object object) {
        return super.getItemPosition(object);
    }

    public interface OnItemClickListener {
        void onItemClick(View item);
    }
}



