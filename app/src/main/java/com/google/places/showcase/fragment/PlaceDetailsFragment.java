package com.google.places.showcase.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.places.showcase.R;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.entity.PlacePhoto;
import com.google.places.showcase.event.ApiErrorEvent;
import com.google.places.showcase.event.PlaceDetailsLoadRequest;
import com.google.places.showcase.event.PlaceDetailsLoadResponse;
import com.google.places.showcase.utils.BusProvider;
import com.google.places.showcase.utils.CommonUtil;
import com.squareup.otto.Subscribe;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Fragment used to display detailed place information to user.
 * Shows block of information that is available in specific
 * {@link com.google.places.showcase.entity.Place} instance.
 * Also shows image pager with available photos.
 */
public class PlaceDetailsFragment extends Fragment implements Button.OnClickListener {

    private Place mPlace;

    /**
     * create a new instance of MyFragment that will be initialized with the given arguments.
     */
    public static PlaceDetailsFragment newInstance(Bundle arguments) {
        PlaceDetailsFragment newFragment = new PlaceDetailsFragment();
        newFragment.setArguments(arguments);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain fragment during configuration changes
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mPlace != null) {
            updateDetailsUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_details, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        BusProvider.getInstance().register(this);

        if (mPlace == null) {
            // need to load place information
            String placeId = getArguments().getString(CommonUtil.KEY_PLACE_ID);
            if (placeId != null) {
                BusProvider.getInstance().post(new PlaceDetailsLoadRequest(placeId));
            } else {
                throw new IllegalArgumentException("Place identifier not specified");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        BusProvider.getInstance().unregister(this);
        CommonUtil.hideProgressDialog(getFragmentManager());
    }

    @Subscribe
    public void onLoadPlaceDetails(PlaceDetailsLoadRequest request) {
        CommonUtil.showProgressDialog(getFragmentManager());
    }

    @Subscribe
    public void onPlaceDetailsLoaded(PlaceDetailsLoadResponse response) {
        if (!response.isBadResponse()) {
            mPlace = response.getPlace();
            updateDetailsUI();
        } else {
            CommonUtil.reportBadResponse(getActivity(), response);
        }
        CommonUtil.hideProgressDialog(getFragmentManager());
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        CommonUtil.hideProgressDialog(getFragmentManager());
    }

    private void updateDetailsUI() {
        // name
        if (!TextUtils.isEmpty(mPlace.getName())) {
            ((TextView) getView().findViewById(R.id.name_text_view)).setText(mPlace.getName());
            getView().findViewById(R.id.block_name).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.block_name).setVisibility(View.GONE);
        }

        // rating
        if (mPlace.getRating() > 0) {
            ((TextView) getView().findViewById(R.id.rating_text_view)).setText(String.valueOf(mPlace.getRating()));
            getView().findViewById(R.id.block_rating).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.block_rating).setVisibility(View.GONE);
        }

        // address
        if (!TextUtils.isEmpty(mPlace.getAddress())) {
            ((TextView) getView().findViewById(R.id.address_text_view)).setText(mPlace.getAddress());
            getView().findViewById(R.id.block_address).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.block_address).setVisibility(View.GONE);
        }

        // phone
        if (!TextUtils.isEmpty(mPlace.getPhone())) {
            ((TextView) getView().findViewById(R.id.phone_text_view)).setText(mPlace.getPhone());
            getView().findViewById(R.id.block_phone).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.block_phone).setVisibility(View.GONE);
        }

        // website
        if (!TextUtils.isEmpty(mPlace.getWebsite())) {
            ((TextView) getView().findViewById(R.id.website_text_view)).setText(mPlace.getWebsite());
            getView().findViewById(R.id.block_website).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.block_website).setVisibility(View.GONE);
        }

        // more
        if (!TextUtils.isEmpty(mPlace.getUrl())) {
            getView().findViewById(R.id.more_button).setOnClickListener(this);
            getView().findViewById(R.id.block_more).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.block_more).setVisibility(View.GONE);
        }

        // photos
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.photos_view_pager);
        CircleIndicator viewPagerIndicator = (CircleIndicator) getView()
                .findViewById(R.id.photos_pager_indicator);
        if (mPlace.getPhotos() != null && mPlace.getPhotos().size() > 0) {
            viewPager.setAdapter(new ImagePagerAdapter(getFragmentManager(), mPlace.getPhotos()));
            viewPagerIndicator.setViewPager(viewPager);
            viewPager.setVisibility(View.VISIBLE);
            viewPagerIndicator.setVisibility(View.VISIBLE);
        } else {
            viewPager.setVisibility(View.GONE);
            viewPagerIndicator.setVisibility(View.GONE);
        }

        getView().findViewById(R.id.details_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == getView().findViewById(R.id.more_button) && !TextUtils.isEmpty(mPlace.getUrl())) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getUrl()));
            startActivity(browserIntent);
        }
    }

    /**
     * Pager adapter to display images.
     * Extended from {@link android.support.v4.app.FragmentStatePagerAdapter} to handle lifecycle
     * automatically.
     */
    private static class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final List<PlacePhoto> mPhotos;

        public ImagePagerAdapter(FragmentManager fm, List<PlacePhoto> photos) {
            super(fm);
            mPhotos = photos;
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return ImageFragment.newInstance(mPhotos.get(position));
        }
    }

    public static class ImageFragment extends android.support.v4.app.Fragment {
        private static final String IMAGE_DATA_EXTRA = "resId";
        private String mPhotoReference;
        private ImageView mImageView;

        // empty constructor, required as per Fragment docs
        public ImageFragment() {
        }

        static ImageFragment newInstance(PlacePhoto photo) {
            final ImageFragment imageFragment = new ImageFragment();
            final Bundle args = new Bundle();
            args.putString(IMAGE_DATA_EXTRA, photo.getPhotoReference());
            imageFragment.setArguments(args);
            return imageFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPhotoReference = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // image_detail_fragment.xml contains just an ImageView
            final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
            mImageView = (ImageView) v.findViewById(R.id.image_view);
            // load image
            Glide.with(ImageFragment.this)
                    .load(CommonUtil.getImageUri(mPhotoReference))
                    .placeholder(android.R.drawable.ic_menu_camera)
                    .crossFade()
                    .into(mImageView);
            return v;
        }
    }
}
