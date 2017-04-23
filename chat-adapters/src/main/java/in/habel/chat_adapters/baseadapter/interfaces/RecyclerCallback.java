package in.habel.chat_adapters.baseadapter.interfaces;

import android.databinding.ViewDataBinding;

public interface RecyclerCallback<VM extends ViewDataBinding, T> {
    void bindData(VM binder, T model);
}
