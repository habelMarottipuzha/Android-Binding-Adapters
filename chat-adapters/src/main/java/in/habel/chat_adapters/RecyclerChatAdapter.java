package in.habel.chat_adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import in.habel.animator.FadeInUpAnimator;
import in.habel.interfaces.RecyclerChatCallback;
import in.habel.interfaces.chatInterface;

/**
 * @param <T>  Chat data model
 * @param <VM> DataBinding class of incoming messages
 * @param <VN> DataBinding class of outgoing messages
 */
@SuppressWarnings("unused")
public class RecyclerChatAdapter<T extends chatInterface, VM extends ViewDataBinding, VN extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerChatAdapter.ViewHolder> {
    private final RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<T> items;
    private int incomingLayoutId, outgoingLayoutId;
    private int maxSeenPosition;
    private boolean scrollToLast;

    @Nullable
    private RecyclerChatCallback<VM, VN, T> bindingInterface;

    /**
     * Creates a recycler chat adapter.
     *
     * @param recyclerView     RecyclerView to which this adapter binds
     * @param items            dataset of <T>
     * @param incomingLayoutId view resource id for incoming messages
     * @param outgoingLayoutId view resource id for outgoing messages
     * @param bindingInterface Callback for binding adapter
     */
    public RecyclerChatAdapter(RecyclerView recyclerView, ArrayList<T> items, int incomingLayoutId, int outgoingLayoutId, @Nullable RecyclerChatCallback<VM, VN, T> bindingInterface) throws Exception {
        this.items = items;
        if (recyclerView == null)
            throw new Exception("Recycler view for RecyclerChatAdapter cannot be null");
        this.recyclerView = recyclerView;
        this.incomingLayoutId = incomingLayoutId;
        this.outgoingLayoutId = outgoingLayoutId;
        this.bindingInterface = bindingInterface;
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(this);
        FadeInUpAnimator animator = new FadeInUpAnimator(new OvershootInterpolator(.2f));
        recyclerView.setItemAnimator(animator);
        //  maxSeenPosition = items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        boolean out = items.get(viewType).isOutgoing();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(out ? outgoingLayoutId : incomingLayoutId, parent, false);
        return new ViewHolder(v, !out);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerChatAdapter.ViewHolder holder, int position) {
        T item = items.get(position);
        holder.bindData(item);
        maxSeenPosition = Math.max(maxSeenPosition, linearLayoutManager.findLastVisibleItemPosition());
    }


    /**
     * @return size of dataset
     */
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(T data) {
        int position = items.size();
        insert(data, position);
    }

    public void insert(ArrayList<T> data) {
        if (data.size() > getItemCount()) {
            for (int i = getItemCount(); i < data.size(); i++) {
                insert(data.get(i));
            }
        }
    }

    public void insert(T data, int position) {
        items.add(position, data);
        scrollIfLast();
        notifyItemInserted(position);
        broadcastUnread();
    }

    private void broadcastUnread() {
        try {
            maxSeenPosition = Math.max(maxSeenPosition, linearLayoutManager.findLastVisibleItemPosition());
            if (bindingInterface != null) {
                //   bindingInterface.onUnreadMessageFound(getItemCount(), items.size() - 2 - maxSeenPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScrollToBottom(boolean scrollToBottom) {
        scrollToLast = scrollToBottom;
    }
    private void scrollIfLast() {
        if (!scrollToLast) return;
        int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
        Log.w(getClass().getSimpleName(), "item size : " + items.size() + "   lvp : " + lastVisiblePosition);
        if (items.size() - 1 <= lastVisiblePosition + 1) {
            new Handler().post(() -> recyclerView.scrollToPosition(items.size() - 1));
        }
    }


    // Remove a RecyclerView item containing a specified Data object
    public void remove(T data) {
        int position = items.indexOf(data);
        remove(position);
    }  // Remove a RecyclerView item containing a specified Data object

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        scrollIfLast();
    }


    @Deprecated
    public void refresh() {
        notifyDataSetChanged();
        scrollIfLast();
    }


    @SuppressWarnings("unchecked")
    public synchronized void refresh(ArrayList<T> newData) {
        if (newData == null) newData = new ArrayList<>();
        int newSize = newData.size();
        for (int i = 0; i < newSize; i++) {
            T model = newData.get(i);
            if (i == items.size()) {
                insert(model, i);
                continue;
            }
            int itemFoundAt = items.indexOf(model);
            if (itemFoundAt == -1) {
                insert(model, i);
                continue;
            }
            if (itemFoundAt == i) continue;
            if (itemFoundAt > i) {
                for (int j = i; j < itemFoundAt; j++) {
                    remove(i);
                }
            }
        }
        for (int i = newSize, itemSize = items.size(); i < itemSize; i++) remove(newSize);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        VM incomingBinding;
        @Nullable
        VN outgoingBinding;

        public ViewHolder(View view, boolean incoming) {
            super(view);
            if (incoming)
                incomingBinding = DataBindingUtil.bind(view);
            else
                outgoingBinding = DataBindingUtil.bind(view);
        }

        void bindData(T model) {
            if (bindingInterface != null)
                bindingInterface.bindData(incomingBinding, outgoingBinding, model);
        }

    }
}
