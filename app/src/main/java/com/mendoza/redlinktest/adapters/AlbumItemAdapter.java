package com.mendoza.redlinktest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.mendoza.redlinktest.R;
import com.mendoza.redlinktest.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumItemAdapter extends RecyclerView.Adapter<AlbumItemAdapter.ViewHolder>//BaseAdapter
        implements Filterable {

    private List<Album> albums;
    private List<Album> originalAlbums;
    ItemClickListener onItemClick;

    public AlbumItemAdapter(List<Album> items, ItemClickListener onItemClick) {
        albums = items;
        this.onItemClick = onItemClick;
    }

    public void setItems(List<Album> items)
    {
        albums = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = albums.get(position);
        holder.mId.setText(albums.get(position).getId().toString());
        holder.mTitle.setText(albums.get(position).getTitle().toUpperCase());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                albums = (List<Album>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Album> FilteredArrayNames = new ArrayList<Album>();

                if (originalAlbums == null) {
                    originalAlbums = new ArrayList<Album>(albums);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = originalAlbums.size();
                    results.values = originalAlbums;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalAlbums.size(); i++) {
                        Album dataNames = originalAlbums.get(i);
                        if (String.valueOf(dataNames.getTitle()).toLowerCase()
                                .contains(constraint)) {
                            FilteredArrayNames.add(dataNames);
                        }
                    }

                    results.count = FilteredArrayNames.size();

                    results.values = FilteredArrayNames;

                }

                return results;
            }
        };

        return filter;
    }

//    @Override
//    public int getCount() {
//        return albums.size();
//    }
//
//    @Override
    public Album getItem(int position) {
        return albums.get(position);
    }
//
//    @Override
//    public long getItemId(int position) {
//        return albums.get(position).getId();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView==null)
//        {
//            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
//        }
//        ViewHolder holder = new ViewHolder(convertView);
//        holder.mItem = albums.get(position);
//        holder.mId.setText(albums.get(position).getId().toString());
//        holder.mTitle.setText(albums.get(position).getTitle().toUpperCase());
//        return convertView;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mId;
        public final TextView mTitle;
        public Album mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mId = (TextView) view.findViewById(R.id.tv_album_id);
            mTitle = (TextView) view.findViewById(R.id.tv_album_title);


        }
    }
}