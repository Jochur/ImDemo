package com.zhihu.matisse.internal.ui.widget;

import com.zhihu.matisse.listener.ItemOnClickListener;

public class Item {
    public Item() {

    }
    public Item(String item_name, String text,ItemOnClickListener menuItemOnClickListener){
        this.item_name = item_name;
        this.text = text;

        this.menuItemOnClickListener = menuItemOnClickListener;
    }


    private String item_name;
    private String text;
    public String getItem_name() {
        return item_name;
    }

    public String getText() {
        return text;
    }



    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setText(String text) {
        this.text = text;
    }


    public ItemOnClickListener getMenuItemOnClickListener() {
        return menuItemOnClickListener;
    }

    public void setMenuItemOnClickListener(ItemOnClickListener menuItemOnClickListener) {
        this.menuItemOnClickListener = menuItemOnClickListener;
    }

    private ItemOnClickListener menuItemOnClickListener;
}
