package com.example.bakalarkaupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>{

    private Context context;
    private List<Collection> collectionList;
    private OnItemClickListener listener;

    public CollectionAdapter(Context context, List<Collection> collectionList) {
        this.context = context;
        this.collectionList = collectionList;
    }


    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.collection_item, parent, false);
        return new CollectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.CollectionViewHolder holder, int position) {
        Collection collection = collectionList.get(position);
        holder.tvCollectionName.setText(collection.getName());
        holder.tvCollectionDescription.setText(collection.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(collection);
            }
        });
    }

    public static class CollectionViewHolder extends RecyclerView.ViewHolder {
        TextView tvCollectionName, tvCollectionDescription;
        ImageView ivCollectionImage;
        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCollectionName = itemView.findViewById(R.id.tvCollectionItemName);
            tvCollectionDescription = itemView.findViewById(R.id.tvCollectionItemDescription);
            ivCollectionImage = itemView.findViewById(R.id.ivCollectionItemImage);

        }
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Collection collection);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
