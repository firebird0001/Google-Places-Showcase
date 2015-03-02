package com.google.places.showcase.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.places.showcase.R;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.provider.LocationProvider;

import java.util.List;

/**
 * Adapter for main places grid
 */
public class PlacesGridAdapter extends BaseAdapter {

    private final Fragment mFragment;
    private List<Place> mPlaces;
    private final LayoutInflater mInflater;

    /**
     * Fragment is used in constructor instead of usual context to tie image loading to it's
     * lifecycle. See {@link Glide#with(android.app.Fragment)} for more information.
     */
    public PlacesGridAdapter(Fragment fragment, List<Place> places) {
        mFragment = fragment;
        mPlaces = places;
        mInflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Update adapter's data and cause reload
     */
    public void updateData(List<Place> places) {
        mPlaces = places;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPlaces != null ? mPlaces.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mPlaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = (View) mInflater.inflate(R.layout.grid_item, null);

            viewHolder = new ViewHolder();
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.grid_item_icon);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.grid_item_name);
            viewHolder.addressTextView = (TextView) convertView.findViewById(R.id.place_address);
            viewHolder.distanceTextView = (TextView) convertView.findViewById(R.id.place_distance);

            convertView.setTag(viewHolder);
            convertView.setLayoutParams(new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    mFragment.getResources().getDimensionPixelSize(R.dimen.grid_item_height)));
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Place place = mPlaces.get(position);

        // fill text fields
        viewHolder.nameTextView.setText(place.getName());
        if (!TextUtils.isEmpty(place.getAddress())) {
            viewHolder.addressTextView.setText(place.getAddress());
            viewHolder.addressTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.addressTextView.setVisibility(View.GONE);
        }
        viewHolder.distanceTextView.setText(LocationProvider.getInstance().distanceToPlace(place));

        // load image
        Glide.with(mFragment)
                .load(place.getIcon())
                .crossFade()
                .into(viewHolder.iconImageView);

        return convertView;
    }

    /**
     * Basic view holder
     */
    private static class ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;
        TextView addressTextView;
        TextView distanceTextView;
    }
}
