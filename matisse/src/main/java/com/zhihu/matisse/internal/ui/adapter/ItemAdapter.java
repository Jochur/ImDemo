package com.zhihu.matisse.internal.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhihu.matisse.R;
import com.zhihu.matisse.listener.ItemOnClickListener;
import com.zhihu.matisse.internal.ui.widget.Item;

import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater listContainer;
    private List<Item> menuItems;

    public ItemAdapter(Context _context, List<Item> _menuItems){
        this.context = _context;
        this.listContainer = LayoutInflater.from(_context);
        this.menuItems = _menuItems;
    }
    @Override
    public int getCount() {
        return this.menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        if(position >= menuItems.size() || position < 0) {
            return null;
        } else {
            return menuItems.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(convertView == null) {
            view = listContainer.inflate(R.layout.r_layout_bottom_comment, null);
        }

        Item menuItem = menuItems.get(position);

        TextView textView = (TextView) view.findViewById(R.id.tv_item_decr);
        textView.setText(menuItem.getText());
        if(menuItems.size() == 1) {
            textView.setBackgroundResource(R.drawable.r_bottom_btn_selector);
        } else if(position == 0) {
            textView.setBackgroundResource(R.drawable.r_bottom_top_btn_selector);
        } else if(position < menuItems.size() - 1) {
            textView.setBackgroundResource(R.drawable.r_bottom_mid_btn_selector);
        } else {
            textView.setBackgroundResource(R.drawable.r_bottom_bottom_btn_selector);
        }
        textView.setTextColor(ContextCompat.getColor(context, R.color.r_bsd_black));
        ItemOnClickListener _menuItemOnClickListener =menuItem.getMenuItemOnClickListener();
        if(_menuItemOnClickListener != null) {
            textView.setOnClickListener(_menuItemOnClickListener);
        }
        return view;
    }
}
