package com.layer.xdk.ui.message;

import android.view.ViewGroup;

import com.layer.xdk.ui.R;
import com.layer.xdk.ui.avatar.AvatarViewModelImpl;
import com.layer.xdk.ui.databinding.XdkUiMessageItemCardBinding;
import com.layer.xdk.ui.message.model.MessageModelManager;

public class MessageItemCardViewHolder extends MessageItemViewHolder<MessageItemCardViewModel, XdkUiMessageItemCardBinding> {
    public MessageItemCardViewHolder(ViewGroup parent, MessageItemCardViewModel viewModel, MessageModelManager modelRegistry) {
        super(parent, R.layout.xdk_ui_message_item_card, viewModel);

        getBinding().avatar.init(new AvatarViewModelImpl(viewModel.getImageCacheWrapper()),
                viewModel.getIdentityFormatter());

        getBinding().currentUserAvatar.init(new AvatarViewModelImpl(viewModel.getImageCacheWrapper()),
                viewModel.getIdentityFormatter());

        getBinding().setViewModel(viewModel);
        getBinding().messageViewer.setMessageModelManager(modelRegistry);
        getBinding().messageViewer.setOnClickListener(viewModel.getOnClickListener());
        getBinding().messageViewer.setOnLongClickListener(viewModel.getOnLongClickListener());

        getBinding().getRoot().setClickable(true);
        getBinding().getRoot().setOnClickListener(viewModel.getOnClickListener());
        getBinding().getRoot().setOnLongClickListener(viewModel.getOnLongClickListener());
    }

    public void bind(MessageCluster messageCluster, int position, int recipientStatusPosition, int parentWidth) {
        getViewModel().update(messageCluster, position, recipientStatusPosition);
    }
}