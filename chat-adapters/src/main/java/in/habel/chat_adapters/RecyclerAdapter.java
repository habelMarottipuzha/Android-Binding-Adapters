package in.habel.chat_adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import in.habel.animator.FadeInUpAnimator;
import in.habel.enums.Scroll;
import in.habel.interfaces.RecyclerCallback;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RecyclerAdapter<T, VM extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final RecyclerView recyclerView;
    private final int layoutId;
    private final LinearLayoutManager linearLayoutManager;
    @Nullable
    private final RecyclerCallback<VM, T> bindingInterface;
    private final List<T> items;
    private int maxSeenPosition;
    private Scroll scroll = Scroll.TOP;

    public RecyclerAdapter(RecyclerView view, List<T> items, int layoutId, @Nullable RecyclerCallback<VM, T> bindingInterface) {
        this.items = items;
        recyclerView = view;
        this.layoutId = layoutId;
        this.bindingInterface = bindingInterface;
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
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

    private void scroll() {
        new Handler().postDelayed(() -> {
            int scrollTo = 0;
            if (scroll == Scroll.BOTTOM) scrollTo = items.size() - 1;
            recyclerView.scrollToPosition(scrollTo);
        }, 2);
    }

    public void scroll(final Scroll scroll) {
        new Handler().post(() -> {
            int scrollTo = 0;
            if (scroll == Scroll.BOTTOM) scrollTo = items.size() - 1;
            recyclerView.scrollToPosition(scrollTo);
        });
    }

    public void setScroll(Scroll scroll) {
        this.scroll = scroll;
    }

    // Insert a new item to the RecyclerView on a predefined position

    /**
     * Appends new data T
     *
     * @param data item to be inserted
     */
    public void insert(T data) {
        int position = items.size();
        insert(data, position);
    }

    public void insert(List<T> data) {
        if (data.size() > getItemCount()) {
            for (int i = getItemCount(); i < data.size(); i++) {
                insert(data.get(i));
            }
        }
    }

    /**
     * Insert an item in a given position
     *
     * @param data     Item to be inserted
     * @param position position of the item
     */
    public void insert(T data, int position) {
        items.add(position, data);
        notifyItemInserted(position);
        broadcastUnread();
    }


    // Remove a RecyclerView item containing a specified Data object
    public void remove(T data) {
        int position = items.indexOf(data);
        remove(position);
    }  // Remove a RecyclerView item containing a specified Data object

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(int position, boolean scroll) {
        remove(position);
        if (scroll) scroll();
    }

    public void remove(T data, boolean scroll) {
        remove(data);
        if (scroll) scroll();
    }

    public synchronized void refresh(List<T> newData) {
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
            if (itemFoundAt == i) {
                if (model.equals(items.get(i)))
                    continue;
                else {
                    remove(i);
                    insert(model, i);
                }
            }
            if (itemFoundAt > i) {
                for (int j = i; j < itemFoundAt; j++) {
                    remove(i);
                }
            }
        }
        for (int i = newSize, itemSize = items.size(); i < itemSize; i++) remove(newSize);
        notifyDataSetChanged();
    }

    public synchronized void refresh(List<T> newData, Scroll scroll) {
        refresh(newData);
        setScroll(scroll);
        scroll();
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


    class ViewHolder extends RecyclerView.ViewHolder {

        final VM binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        void bindData(T model) {
            if (bindingInterface != null)
                bindingInterface.bindData(binding, model);
        }

    }
}
