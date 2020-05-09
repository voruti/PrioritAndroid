package voruti.prioritandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import voruti.priorit.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.OwnViewHolder> {

    private static MainActivity mainActivity;
    private List<Item> items;
    private IOnItemClickListener clickListener;

    public ItemAdapter(List<Item> items, IOnItemClickListener clickListener, MainActivity mainActivity) {
        this.items = items;
        this.clickListener = clickListener;
        ItemAdapter.mainActivity = mainActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OwnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.own_list_item, parent, false);

        OwnViewHolder viewHolder = new OwnViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(OwnViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.bind(items.get(position), clickListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class OwnViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public OwnViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(final Item item, final IOnItemClickListener clickListener) {
            String text = String.format("%s%.20s (%s)",
                    item.isDone() ? mainActivity.getString(R.string.lblDone) + ": " : "",
                    item.getTitle(),
                    item.getuName());
            textView.setText(text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(item);
                }
            });
        }
    }
}

