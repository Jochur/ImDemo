package com.grechur.imdemo.utils.chat.viewholder;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.grechur.imdemo.R;
import com.grechur.imdemo.utils.chat.attachment.StickerAttachment;
import com.netease.nim.uikit.business.session.emoji.StickerManager;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

public class MsgViewHolderSticker extends MsgViewHolderBase {

    private ImageView baseView;

    public MsgViewHolderSticker(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_sticker;
    }

    @Override
    protected void inflateContentView() {
        baseView = findViewById(R.id.message_item_sticker_image);
        baseView.setMaxWidth(MsgViewHolderThumbBase.getImageMaxEdge());
    }

    @Override
    protected void bindContentView() {
        StickerAttachment attachment = (StickerAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }

        Glide.with(context)
                .load(StickerManager.getInstance().getStickerUri(attachment.getCatalog(), attachment.getChartlet()))
                .error(R.drawable.nim_default_img_failed)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(baseView);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
