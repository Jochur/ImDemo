package com.zhihu.matisse.listener;

import android.app.DialogFragment;
import android.view.View;

import com.zhihu.matisse.internal.ui.widget.Item;

public abstract class ItemOnClickListener implements View.OnClickListener{

    public ItemOnClickListener(DialogFragment _bottomMenuFragment, Item _menuItem) {
        this.bottomMenuFragment = _bottomMenuFragment;
        this.menuItem = _menuItem;
    }
    public DialogFragment getBottomMenuFragment() {
        return bottomMenuFragment;
    }

    public void setBottomMenuFragment(DialogFragment bottomMenuFragment) {
        this.bottomMenuFragment = bottomMenuFragment;
    }

    public Item getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(Item menuItem) {
        this.menuItem = menuItem;
    }

    private DialogFragment bottomMenuFragment;
    private Item menuItem;

    @Override
    public void onClick(View v){
        this.onClickMenuItem(v, this.menuItem);
    }
    public abstract void onClickMenuItem(View v, Item menuItem);
}
