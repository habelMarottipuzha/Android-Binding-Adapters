package in.habel.interfaces;

import android.databinding.ViewDataBinding;
import android.support.annotation.Keep;

@Keep
public interface RecyclerCallback<VM extends ViewDataBinding, T> {
    void bindData(VM binder, T model);

    void onUnreadMessageFound(int totalItemCount, int unreadItemCount);
}
