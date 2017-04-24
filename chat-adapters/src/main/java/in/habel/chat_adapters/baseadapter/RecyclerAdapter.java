package in.habel.chat_adapters.baseadapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.habel.chat_adapters.interfaces.RecyclerCallback;

public class RecyclerAdapter<T, VM extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<T> items;
    private int layoutId;
    @Nullable
    private RecyclerCallback<VM, T> bindingInterface;
    private boolean animate;
    public RecyclerAdapter(Context context, ArrayList<T> items, int layoutId, @Nullable RecyclerCallback<VM, T> bindingInterface) {
        this.items = items;
        this.context = context;
        this.layoutId = layoutId;
        this.bindingInterface = bindingInterface;
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
        // if (animate) setAnimation(holder.itemView);
    }


/*    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = items.get(position);
        holder.bindData(item);
    }*/

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate) {
       /* Animation animation = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
        viewToAnimate.startAnimation(animation);*/
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

    public void insert(T data, int position) {
        animate = true;
        items.add(position, data);
        notifyItemInserted(position);
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
