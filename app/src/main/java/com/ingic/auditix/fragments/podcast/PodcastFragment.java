package com.ingic.auditix.fragments.podcast;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastRecommendedEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.podcast.PodcastRecommendedBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/23/2017.
 */
public class PodcastFragment extends BaseFragment implements RecyclerViewItemListener {
    @BindView(R.id.btn_subscription_seeall)
    AnyTextView btnSubscriptionSeeall;
    @BindView(R.id.si1_image)
    ImageView si1Image;
    @BindView(R.id.si1_text)
    AnyTextView si1Text;
    @BindView(R.id.si1_btn)
    Button si1Btn;
    @BindView(R.id.si2_image)
    ImageView si2Image;
    @BindView(R.id.si2text)
    AnyTextView si2text;
    @BindView(R.id.si2_btn)
    Button si2Btn;
    @BindView(R.id.rv_recommended)
    CustomRecyclerView rvRecommended;
    @BindView(R.id.btn_best_seeall)
    AnyTextView btnBestSeeall;
    @BindView(R.id.bi1_image)
    ImageView bi1Image;
    @BindView(R.id.bi1_title)
    AnyTextView bi1Title;
    @BindView(R.id.bi1_narrator_text)
    AnyTextView bi1NarratorText;
    @BindView(R.id.bi2_image)
    ImageView bi2Image;
    @BindView(R.id.bi2_title)
    AnyTextView bi2Title;
    @BindView(R.id.bi2_narrator_text)
    AnyTextView bi2NarratorText;
    Unbinder unbinder;
    private ArrayList<PodcastRecommendedEnt> podcastCollections;

    public static PodcastFragment newInstance() {
        Bundle args = new Bundle();

        PodcastFragment fragment = new PodcastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    public void setTitleBar(TitleBar titleBar) {
//        getMainActivity().settingFilterMenu();
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.podcast));
        titleBar.showBackButton();
        titleBar.showFilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().isNavigationGravityRight = true;
                getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
        setData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setData() {
        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        podcastCollections = new ArrayList<>();
        podcastCollections.add(new PodcastRecommendedEnt(R.drawable.dummyalbum, "Ed Sheeran"));
        podcastCollections.add(new PodcastRecommendedEnt(R.drawable.dummyalbum, "Ed Sheeran"));
        podcastCollections.add(new PodcastRecommendedEnt(R.drawable.dummyalbum, "Ed Sheeran"));
        podcastCollections.add(new PodcastRecommendedEnt(R.drawable.dummyalbum, "Ed Sheeran"));
        rvRecommended.BindRecyclerView(new PodcastRecommendedBinder(options, this), podcastCollections,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false),
                new DefaultItemAnimator());
        loader.displayImage(AppConstants.DRAWABLE_PATH + R.drawable.dummyalbum, bi1Image, options);
        loader.displayImage(AppConstants.DRAWABLE_PATH + R.drawable.dummyalbum, bi2Image, options);
        loader.displayImage(AppConstants.DRAWABLE_PATH + R.drawable.dummyalbum, si1Image, options);
        loader.displayImage(AppConstants.DRAWABLE_PATH + R.drawable.dummyalbum, si2Image, options);
        si1Text.setText("Ed Sheeran");
        si2text.setText("Ed Sheeran");
        bi1Title.setText("Ed Sheeran");
        bi2Title.setText("Ed Sheeran");
        bi1NarratorText.setText("Ed Sheeran");
        bi2NarratorText.setText("Ed Sheeran");

    }

    @OnClick({R.id.btn_subscription_seeall, R.id.si1_btn, R.id.si2_btn, R.id.bi1_btn, R.id.bi2_btn, R.id.btn_best_seeall, R.id.bi1_image, R.id.bi2_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_subscription_seeall:
                openSubscriptionFragment(getString(R.string.my_subscriptions));
                break;
            case R.id.si1_btn:
                willbeimplementedinfuture();
                break;
            case R.id.si2_btn:
                willbeimplementedinfuture();
                break;
            case R.id.bi1_btn:
                willbeimplementedinfuture();
                break;
            case R.id.bi2_btn:
                willbeimplementedinfuture();
                break;
            case R.id.btn_best_seeall:
                openSubscriptionFragment(getString(R.string.best_of_best));
                break;
            case R.id.bi1_image:
                openPodcastDetail();
                break;
            case R.id.bi2_image:
                openPodcastDetail();
                break;
        }
    }

    private void openSubscriptionFragment(String titleHeading) {
        SubscriptionsFragment fragment = new SubscriptionsFragment();
        fragment.setTitleHeading(titleHeading);
        getDockActivity().replaceDockableFragment(fragment, "SubscriptionsFragment");
    }

    private void openPodcastDetail() {
        //getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(256945396), "PodcastDetailFragment");
    }

    @Override
    public void onRecyclerItemButtonClicked(Object Ent, int position) {

    }

    @Override
    public void onRecyclerItemClicked(Object Ent, int position) {
        openPodcastDetail();
    }
}