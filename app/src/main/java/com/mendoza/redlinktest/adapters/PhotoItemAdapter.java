package com.mendoza.redlinktest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mendoza.redlinktest.R;
import com.mendoza.redlinktest.models.AlbumPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemAdapter.ViewHolder>//BaseAdapter
        implements Filterable {
    private List<AlbumPhoto> albumPhotos;
    private List<AlbumPhoto> originalPhotos;
    ItemClickListener onItemClick;

    public PhotoItemAdapter(List<AlbumPhoto> values, ItemClickListener onItemClick){
        albumPhotos = values;
        this.onItemClick = onItemClick;
    }

    public void setItems(List<AlbumPhoto> items)
    {
        albumPhotos = items;
        notifyDataSetChanged();
    }

//    @Override
//    public int getCount() {
//        return albumPhotos.size();
//    }

//    @Override
    public AlbumPhoto getItem(int position) {
        return albumPhotos.get(position);
    }

//    @Override
//    public long getItemId(int position) {
//        return albumPhotos.get(position).getId();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if(convertView==null)
//        {
//            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
//        }
//        ViewHolder holder = new ViewHolder(convertView);
//        holder.mItem = albumPhotos.get(position);
//        holder.mId.setText(albumPhotos.get(position).getId().toString());
//        holder.mTitle.setText(albumPhotos.get(position).getTitle().toUpperCase().trim());
//
////        holder.mImage.setImageURL(albumPhotos.get(position).getThumbnailUrl());
//        Picasso.get().load(albumPhotos.get(position).getUrl()).into(holder.mImage);
//
//        return convertView;
//    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                albumPhotos = (List<AlbumPhoto>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<AlbumPhoto> FilteredArrayNames = new ArrayList<AlbumPhoto>();

                if (originalPhotos == null) {
                    originalPhotos = new ArrayList<AlbumPhoto>(albumPhotos);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = originalPhotos.size();
                    results.values = originalPhotos;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalPhotos.size(); i++) {
                        AlbumPhoto dataNames = originalPhotos.get(i);
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

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = albumPhotos.get(position);
        holder.mId.setText(albumPhotos.get(position).getId().toString());
        holder.mTitle.setText(albumPhotos.get(position).getTitle().toUpperCase());
        Picasso.get().load(albumPhotos.get(position).getUrl()).into(holder.mImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
        public final TextView mId;
        public final TextView mTitle;
        public AlbumPhoto mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mId = (TextView) view.findViewById(R.id.tv_photo_id);
            mTitle = (TextView) view.findViewById(R.id.tv_photo_title);
            mImage = (ImageView) view.findViewById(R.id.iv_image);
        }
    }
}
