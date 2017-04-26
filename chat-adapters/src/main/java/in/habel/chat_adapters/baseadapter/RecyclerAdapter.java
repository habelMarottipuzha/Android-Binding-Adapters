package in.habel.chat_adapters.baseadapter;

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

import in.habel.chat_adapters.animator.FadeInUpAnimator;
import in.habel.chat_adapters.interfaces.RecyclerCallback;

public class RecyclerAdapter<T, VM extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private RecyclerView recyclerView;
    private int layoutId;
    private int maxSeenPosition;
    private LinearLayoutManager linearLayoutManager;
    @Nullable
    private RecyclerCallback<VM, T> bindingInterface;
    private ArrayList<T> items;
    private boolean scrollToLast;

    public RecyclerAdapter(RecyclerView view, ArrayList<T> items, int layoutId, @Nullable RecyclerCallback<VM, T> bindingInterface) {
        this.items = items;
        recyclerView = view;
        this.layoutId = layoutId;
        this.bindingInterface = bindingInterface;
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this);
        FadeInUpAnimator animator = new FadeInUpAnimator(new OvershootInterpolator(.2f));
        recyclerView.setItemAnimator(animator);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = items.get(position);
        holder.bindData(item);
        maxSeenPosition = Math.max(maxSeenPosition, linearLayoutManager.findLastVisibleItemPosition());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
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
                bindingInterface.onUnreadMessageFound(getItemCount(), items.size() - 2 - maxSeenPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrollIfLast() {
        if (!scrollToLast) return;
        int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
        Log.w(getClass().getSimpleName(), "item size : " + items.size() + "   lvp : " + lastVisiblePosition);
        if (items.size() - 1 <= lastVisiblePosition + 1) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(items.size() - 1);
                }
            });
        }
    }

    public void setScrollToBottom(boolean scrollToBottom) {
        scrollToLast = scrollToBottom;
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

    public void refresh() {
        notifyDataSetChanged();
        scrollIfLast();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VM binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        void bindData(T model) {
            if (bindingInterface != null)
                bindingInterface.bindData(binding, model);
        }

    }
}
