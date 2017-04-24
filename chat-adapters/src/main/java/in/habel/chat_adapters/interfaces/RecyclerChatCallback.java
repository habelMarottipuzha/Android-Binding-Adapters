package in.habel.chat_adapters.interfaces;

import android.databinding.ViewDataBinding;

public interface RecyclerChatCallback<VM extends ViewDataBinding, VN extends ViewDataBinding, T> {
    void bindData(VM incomingBinder, VN outgoingBinder, T model);
}
