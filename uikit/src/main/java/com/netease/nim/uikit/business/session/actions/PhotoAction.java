package com.netease.nim.uikit.business.session.actions;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.activity.PreviewImageFromCameraActivity;
import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nim.uikit.common.util.storage.StorageType;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

public class PhotoAction extends BaseAction {
    private static final int PORTRAIT_IMAGE_WIDTH = 720;
    public static final String JPG = ".jpg";
    public static final String MIME_JPEG = "image/jpeg";
    private static final int PICK_IMAGE_COUNT = 9;
    private boolean crop = false;
    /**
     * 构造函数
     */
    public PhotoAction() {
        super(R.drawable.nim_message_plus_photo_selector, R.string.input_panel_photo);
    }

    @Override
    public void onClick() {
        int requestCode = makeRequestCode(RequestCode.PICK_IMAGE);
        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
        option.titleResId = R.string.input_panel_photo;
        option.multiSelect = true;
        option.multiSelectMaxCount = PICK_IMAGE_COUNT;
        option.crop = crop;
        option.cropOutputImageWidth = PORTRAIT_IMAGE_WIDTH;
        option.cropOutputImageHeight = PORTRAIT_IMAGE_WIDTH;
        option.outputPath = tempFile();

//        PickImageHelper.pickImage(getActivity(), requestCode, option);
        int from = PickImageActivity.FROM_LOCAL;
        if (!option.crop) {
            PickImageActivity.start(getActivity(), requestCode, from, option.outputPath, option.multiSelect, 1,
                    true, false, 0, 0);
        } else {
            PickImageActivity.start(getActivity(), requestCode, from, option.outputPath, false, 1,
                    false, true, option.cropOutputImageWidth, option.cropOutputImageHeight);
        }
    }
    private String tempFile() {
        String filename = StringUtil.get32UUID() + JPG;
        return StorageUtil.getWritePath(filename, StorageType.TYPE_TEMP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.PICK_IMAGE:
                onPickImageActivityResult(requestCode, data);
                break;
            case RequestCode.PREVIEW_IMAGE_FROM_CAMERA:
                onPreviewImageActivityResult(requestCode, data);
                break;
        }
    }
    /**
     * 图片选取回调
     */
    private void onPickImageActivityResult(int requestCode, Intent data) {
        if (data == null) {
            Toast.makeText(getActivity(), R.string.picker_image_error, Toast.LENGTH_LONG).show();
            return;
        }
        boolean local = data.getBooleanExtra(Extras.EXTRA_FROM_LOCAL, false);
        if (local){
            // 本地相册
            sendImageAfterSelfImagePicker(data);
        }
    }
    /**
     * 发送图片
     */
    private void sendImageAfterSelfImagePicker(final Intent data) {
        SendImageHelper.sendImageAfterSelfImagePicker(getActivity(), data, new SendImageHelper.Callback() {

            @Override
            public void sendImage(File file, boolean isOrig) {
                onPicked(file);

            }
        });
    }
    /**
     * 拍摄回调
     */
    private void onPreviewImageActivityResult(int requestCode, Intent data) {
        if (data.getBooleanExtra(PreviewImageFromCameraActivity.RESULT_SEND, false)) {
            sendImageAfterPreviewPhotoActivityResult(data);
        } else if (data.getBooleanExtra(PreviewImageFromCameraActivity.RESULT_RETAKE, false)) {
            String filename = StringUtil.get32UUID() + JPG;
            String path = StorageUtil.getWritePath(filename, StorageType.TYPE_TEMP);

            if (requestCode == RequestCode.PREVIEW_IMAGE_FROM_CAMERA) {
                PickImageActivity.start(getActivity(), makeRequestCode(RequestCode.PICK_IMAGE), PickImageActivity.FROM_CAMERA, path);
            }
        }
    }
    /**
     * 从预览界面点击发送图片
     */
    private void sendImageAfterPreviewPhotoActivityResult(Intent data) {
        SendImageHelper.sendImageAfterPreviewPhotoActivityResult(data, new SendImageHelper.Callback() {

            @Override
            public void sendImage(File file, boolean isOrig) {
                onPicked(file);
            }
        });
    }
    protected void onPicked(File file){
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomImageMessage(getAccount(), file, file.getName());
        } else {
            message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        }
        sendMessage(message);
    }
}
