package com.zhihu.matisse.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.zhihu.matisse.GifSizeFilter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.R;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.ui.adapter.ItemAdapter;
import com.zhihu.matisse.internal.ui.widget.Item;
import com.zhihu.matisse.internal.utils.MediaStoreCompat;
import com.zhihu.matisse.listener.ItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.zhihu.matisse.ui.MatisseActivity.REQUEST_CODE_CAPTURE;


public class PickPicFragment extends DialogFragment {
    private static PickPicFragment mPickPicFragment;
    public MediaStoreCompat mMMediaStoreCompat;
    public static final int PICK_PHOTO = 100;
    public PickPicFragment() {
    }

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static PickPicFragment getInstance() {
        if (mPickPicFragment == null) {
            mPickPicFragment = new PickPicFragment();
        }
        return mPickPicFragment;
    }

    private Activity context;

    public void creatPickPicDialog(Activity context) {
        this.context = context;
        final PickPicFragment bottomFragment = new PickPicFragment();
        List<Item> menuItemList = new ArrayList<Item>();
        Item item1 = new Item();
        item1.setText("拍照");
        Item item2 = new Item();
        item2.setText("从相册中选取");
        item1.setMenuItemOnClickListener(new ItemOnClickListener(bottomFragment, item1) {
            @Override
            public void onClickMenuItem(View v, Item menuItem) {
                //拍照选择
                chooseFromCamera();
                bottomFragment.dismiss();
            }
        });
        item2.setMenuItemOnClickListener(new ItemOnClickListener(bottomFragment, item2) {
            @Override
            public void onClickMenuItem(View v, Item menuItem) {
                //从相册选取
                chooseFromGallery();
                bottomFragment.dismiss();
            }
        });

        menuItemList.add(item1);
        menuItemList.add(item2);
        bottomFragment.setItems(menuItemList);
        bottomFragment.show(context.getFragmentManager(), "BottomFragment");
    }

    private void chooseFromGallery() {
        Matisse.from(context)
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Dracula)//选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .countable(false)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .maxSelectable(1)
                .originalEnable(true)
                .maxOriginalSize(10)
                .imageEngine(new GlideEngine())
                .forResult(PICK_PHOTO);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == PICK_PHOTO) {
//                List<Uri> uris = Matisse.obtainResult(data);
//                List<String> list = Matisse.obtainPathResult(data);
//            } else if (requestCode == REQUEST_CODE_CAPTURE) {
//                Uri contentUri = mMMediaStoreCompat.getCurrentPhotoUri();
//                String path = mMMediaStoreCompat.getCurrentPhotoPath();
//                ArrayList<Uri> selected = new ArrayList<>();
//                selected.add(contentUri);
//                ArrayList<String> selectedPath = new ArrayList<>();
//                selectedPath.add(path);
//                Intent result = new Intent();
//                result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, selected);
//                result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, selectedPath);
//                List<Uri> uris = Matisse.obtainResult(result);//content://
//                List<String> list = Matisse.obtainPathResult(result);// /data/data
//            }
//        }
//    }

    private void chooseFromCamera() {
        mMMediaStoreCompat = new MediaStoreCompat(context);
        mMMediaStoreCompat.setCaptureStrategy(new CaptureStrategy(true, "com.basestonedata.instalment.mastisse.fileprovider"));
        mMMediaStoreCompat.dispatchCaptureIntent(context, REQUEST_CODE_CAPTURE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        getDialog().getWindow().setWindowAnimations(R.style.r_bottom_anim);//添加一组进出动画
        View view = inflater.inflate(R.layout.r_fragment_bottom_layout, container, false);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickPicFragment.this.dismiss();
            }
        });
        ListView lv_menu = (ListView) view.findViewById(R.id.lv_item);
        ItemAdapter menuItemAdapter = new ItemAdapter(getActivity().getBaseContext(), this.items);
        lv_menu.setAdapter(menuItemAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置弹出框宽屏显示，适应屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        //移动弹出菜单到底部
        WindowManager.LayoutParams wlp = getDialog().getWindow().getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(wlp);
    }

    @Override
    public void onStop() {
        this.getView().setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.r_anim_disappear));
        super.onStop();
    }

    //////////////////////////////////////////增加删除按钮事件/////////////////////////////////////////////////

    public void creatPickPicDialog(Activity context, final ViewGroup viewGroup) {
        this.context = context;
        final PickPicFragment bottomFragment = new PickPicFragment();
        List<Item> menuItemList = new ArrayList<Item>();
        Item item1 = new Item();
        item1.setText("拍照");
        Item item2 = new Item();
        item2.setText("从相册中选取");

        item1.setMenuItemOnClickListener(new ItemOnClickListener(bottomFragment, item1) {
            @Override
            public void onClickMenuItem(View v, Item menuItem) {
                //拍照选择
                chooseFromCamera();
                bottomFragment.dismiss();
            }
        });
        item2.setMenuItemOnClickListener(new ItemOnClickListener(bottomFragment, item2) {
            @Override
            public void onClickMenuItem(View v, Item menuItem) {
                //从相册选取
                chooseFromGallery();
                bottomFragment.dismiss();
            }
        });

        menuItemList.add(item1);
        menuItemList.add(item2);
        // 动态添加删除事件
        if (viewGroup.getTag() != null) {
            if (!TextUtils.isEmpty((String) viewGroup.getTag())) {
                Item item3 = new Item();
                item3.setText("删除");
                item3.setMenuItemOnClickListener(new ItemOnClickListener(bottomFragment, item2) {
                    @Override
                    public void onClickMenuItem(View v, Item menuItem) {
                        //从相册选取
                        if (mOnDeletPic != null) {
                            mOnDeletPic.deletPic(viewGroup);
                        }
                        bottomFragment.dismiss();
                    }
                });
                menuItemList.add(item3);
            }
        }
        bottomFragment.setItems(menuItemList);
        bottomFragment.show(context.getFragmentManager(), "BottomFragment");
    }

    public interface OnDeletPic {
        void deletPic(ViewGroup view);
    }

    private OnDeletPic mOnDeletPic;

    public OnDeletPic getOnDeletPic() {
        return mOnDeletPic;
    }

    public void setOnDeletPic(OnDeletPic onDeletPic) {
        mOnDeletPic = onDeletPic;
    }
}
