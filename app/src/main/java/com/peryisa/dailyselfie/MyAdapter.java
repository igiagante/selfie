package com.peryisa.dailyselfie;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import model.Item;

/**
 * Created by igiagante on 18/8/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    private Context context;
    private OnItemSelectedListener onItemSelectedListener;

    // inner class to hold a reference to each card_view of RecyclerView
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data card_view is just a string in this case
        public Long id;
        public CardView cardView;
        public ImageView imageView;
        public TextView txtTittle;
        public CheckBox checkBox;
        public ImageView deleteButton;

        public ItemViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            imageView = (ImageView) v.findViewById(R.id.thumbnail);
            txtTittle = (TextView) v.findViewById(R.id.title);
            checkBox = (CheckBox) v.findViewById(R.id.select_picture);
            deleteButton = (ImageView) v.findViewById(R.id.delete_image);
            deleteButton.setClickable(true);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = items.get(position);
                    File file = new File(item.getPath());
                    if (file.delete()) {
                        remove(position);
                    }
                    //onItemSelectedListener.deleteButtonPressed();
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item = items.get(getAdapterPosition());
                    onItemSelectedListener.itemSelected(item);
                }
            });

            if(checkBox != null && isPortrait()){

                checkBox.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Item item = items.get(getAdapterPosition());

                        if (checkBox.isChecked()) {
                            item.setSelected(true);
                            deleteButton.setClickable(false);
                            onItemSelectedListener.checkBoxSelected();
                        } else {
                            item.setSelected(false);
                            deleteButton.setClickable(true);
                            onItemSelectedListener.unCheckBoxSelected();
                        }
                    }
                });
            }
        }
    }

    public interface OnItemSelectedListener {
        void itemSelected(Item item);
        void checkBoxSelected();
        void unCheckBoxSelected();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.onItemSelectedListener = onItemSelectedListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, final int position) {

        itemViewHolder.id = items.get(position).getId();
        itemViewHolder.imageView.setImageBitmap(items.get(position).getImage());
        itemViewHolder.txtTittle.setText(items.get(position).getName());
        
        if(isPortrait()){
            itemViewHolder.checkBox.setChecked(items.get(position).isSelected());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void add(Item item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void removeAllItems(Set<Item> itemsId) {

        ArrayList<Item> tempList = new ArrayList<>(itemsId);

        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.contains(items.get(i))) {
                File file = new File(items.get(i).getPath());
                if (!file.delete()) {
                    Log.e("Delete File", "File " + file.getPath() + "could not be deleted");
                }
            }
        }

        items.removeAll(itemsId);
        notifyDataSetChanged();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    private boolean isPortrait(){
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
