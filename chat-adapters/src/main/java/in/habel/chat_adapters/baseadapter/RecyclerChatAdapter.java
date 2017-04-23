package in.habel.chat_adapters.baseadapter;

import android.content.Context;
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

import java.util.ArrayList;

import in.habel.chat_adapters.baseadapter.interfaces.RecyclerChatCallback;
import in.habel.chat_adapters.baseadapter.interfaces.chatInterface;

public class RecyclerChatAdapter<T extends chatInterface, VM extends ViewDataBinding, VN extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerChatAdapter.ViewHolder> {
    private final RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private final Context context;
    private ArrayList<T> items;
    private int incomingLayoutId, outgoingLayoutId;
    private int lastVisiblePosition;
    @Nullable
    private RecyclerChatCallback<VM, VN, T> bindingInterface;

    public RecyclerChatAdapter(RecyclerView recyclerView, ArrayList<T> items, int incomingLayoutId, int outgoingLayoutId, @Nullable RecyclerChatCallback<VM, VN, T> bindingInterface) {
        this.items = items;
        this.context = recyclerView.getContext();
        this.recyclerView = recyclerView;
        this.incomingLayoutId = incomingLayoutId;
        this.outgoingLayoutId = outgoingLayoutId;
        this.bindingInterface = bindingInterface;
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
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
    }


/*    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = items.get(position);
        holder.bindData(item);
    }*/

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
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

    public void insert(T data, int position) {
        lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
        items.add(position, data);
        scrollIfLast();
        notifyItemInserted(position);
    }

    private void scrollIfLast() {
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
