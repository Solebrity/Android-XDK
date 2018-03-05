package com.layer.xdk.ui.message.adapter2;

import android.databinding.Observable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.layer.xdk.ui.BR;
import com.layer.xdk.ui.R;
import com.layer.xdk.ui.avatar.AvatarViewModelImpl;
import com.layer.xdk.ui.databinding.XdkUiMessageItemDefaultBinding;
import com.layer.xdk.ui.message.container.MessageContainer;

public class MessageDefaultViewHolder extends MessageViewHolder<MessageDefaultViewHolderModel,XdkUiMessageItemDefaultBinding > {

    // Cache this so we know not to re-set the bias on the constraint layout
    private Boolean mCurrentlyMyMessage;

    public MessageDefaultViewHolder(ViewGroup parent, MessageDefaultViewHolderModel viewModel) {
        super(parent, R.layout.xdk_ui_message_item_default, viewModel);

        getBinding().avatar.init(new AvatarViewModelImpl(viewModel.getImageCacheWrapper()),
                viewModel.getIdentityFormatter());

        getBinding().currentUserAvatar.init(new AvatarViewModelImpl(viewModel.getImageCacheWrapper()),
                viewModel.getIdentityFormatter());

        getBinding().setViewHolderModel(viewModel);

        getBinding().getRoot().setClickable(true);
        getBinding().getRoot().setOnClickListener(viewModel.getOnClickListener());
        getBinding().getRoot().setOnLongClickListener(viewModel.getOnLongClickListener());
        getBinding().messageViewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, final View inflated) {
                getViewHolderModel().addOnPropertyChangedCallback(new AlphaAndBiasObserver(inflated));
            }
        });

        getBinding().executePendingBindings();
    }

    @Override
    void onBind() {
        MessageContainer messageContainer =
                (MessageContainer) getBinding().messageViewStub.getRoot();
        if (messageContainer != null) {
            messageContainer.setMessageModel(getItem());
        }

        getViewHolderModel().update();
    }

    public View inflateViewContainer(int containerLayoutId) {
        getBinding().messageViewStub.getViewStub().setLayoutResource(containerLayoutId);
        return getBinding().messageViewStub.getViewStub().inflate();
    }

    private class AlphaAndBiasObserver extends Observable.OnPropertyChangedCallback {
        private final View mInflated;

        public AlphaAndBiasObserver(View inflated) {
            mInflated = inflated;
        }

        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (propertyId == BR.messageCellAlpha || propertyId == BR._all) {
                mInflated.setAlpha(getViewHolderModel().getMessageCellAlpha());
            }
            if ((propertyId == BR._all || propertyId == BR.myMessage)
                    && (mCurrentlyMyMessage == null || getViewHolderModel().isMyMessage() != mCurrentlyMyMessage)) {
                mCurrentlyMyMessage = getViewHolderModel().isMyMessage();
                ConstraintSet set = new ConstraintSet();
                ConstraintLayout parent =
                        (ConstraintLayout) mInflated.getParent();
                set.clone(parent);
                set.setHorizontalBias(mInflated.getId(),
                        getViewHolderModel().isMyMessage() ? 1.0f : 0.0f);
                set.applyTo(parent);
            }
        }
    }
}
