package com.ingic.auditix.fragments.podcast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.DialogHelper;
import com.ingic.auditix.helpers.InternetHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.interfaces.FavoriteCheckChangeListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.podcast.NewPodcastEpisodeBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRatingBar;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created on 12/27/2017.
 */
public class PodcastDetailFragment extends BaseFragment {
    public static final String TAG = "PodcastDetailFragment";
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.btn_play)
    ImageView btnPlay;
    @BindView(R.id.img_player_cover)
    ImageView imgPlayerCover;
    @BindView(R.id.txt_narrator_text)
    AnyTextView txtNarratorText;
    @BindView(R.id.rb_rating)
    CustomRatingBar rbRating;
    @BindView(R.id.txt_genre_text)
    AnyTextView txtGenreText;
    @BindView(R.id.txt_duration_text)
    AnyTextView txtDurationText;
    @BindView(R.id.txt_location_text)
    AnyTextView txtLocationText;
    @BindView(R.id.btn_download)
    ImageView btnDownload;
    @BindView(R.id.txt_about_text)
    AnyTextView txtAboutText;
    @BindView(R.id.txt_narrator_intro_text)
    AnyTextView txtNarratorIntroText;
    @BindView(R.id.txt_podcast_header)
    AnyTextView txtPodcastHeader;
    @BindView(R.id.btn_add_favorite)
    ToggleButton btnAddFavorite;
    @BindView(R.id.btn_rate)
    Button btnRate;
    @BindView(R.id.container_buttons)
    LinearLayout containerButtons;
    @BindView(R.id.txt_player_item_name)
    AnyTextView txtPlayerItemName;
    @BindView(R.id.txt_player_item_album)
    AnyTextView txtPlayerItemAlbum;
    @BindView(R.id.btn_player_play)
    ImageView btnPlayerPlay;
    @BindView(R.id.btn_player_forward)
    ImageView btnPlayerForward;
    @BindView(R.id.container_player)
    LinearLayout containerPlayer;
    Unbinder unbinder;
    @BindView(R.id.rv_episodes)
    CustomRecyclerView rvEpisodes;
    @BindView(R.id.parent_scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.btn_subscribe)
    ToggleButton btnSubscribe;
    ArrayList<PodcastEpisodeEnt> podcastEpisodeCollection;
    private List<List<PodcastTrackEnt>> result;
    private String podcastName = "";
    private TitleBar titleBar;
    private PodcastDetailHomeEnt podcastDetailEnt;
    private ArrayList<PodcastTrackEnt> podcastTrackEnts;
    private PodcastTrackEnt patsyObject = new PodcastTrackEnt();
    private Integer totalPagesCount, currentPageNumber = 0;
    private DownloadListenerFragment fileDownloadListener = new DownloadListenerFragment() {
        @Override
        public void pending(final BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (podcastTrackEnts != null && rvEpisodes != null) {
                patsyObject.setId((String) task.getTag());
                findAndUpdateDownloadState(AppConstants.DownloadStates.PENDING, 0);
            }
        }

        @Override
        public void started(BaseDownloadTask task) {

        }

        @Override
        public void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        }

        @Override
        public void progress(final BaseDownloadTask task, int progress) {
            if (podcastTrackEnts != null && rvEpisodes != null) {
                patsyObject.setId((String) task.getTag());
                findAndUpdateDownloadState(AppConstants.DownloadStates.DOWNLOADING, progress);
            }
        }

        @Override
        public void completed(final BaseDownloadTask task) {
            if (podcastTrackEnts != null && rvEpisodes != null) {
                patsyObject.setId((String) task.getTag());
            }

        }

        @Override
        public void error(final BaseDownloadTask task, Throwable e) {
            if (podcastTrackEnts != null && rvEpisodes != null) {
                patsyObject.setId((String) task.getTag());
                findAndUpdateDownloadState(AppConstants.DownloadStates.ERROR, 0);
            }
        }

        @Override
        public void warn(BaseDownloadTask task) {
            task.getAutoRetryTimes();
        }
    };
    private DialogHelper helper;
    private LinearLayoutManager layoutManager;
    private CompoundButton.OnCheckedChangeListener favoriteCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (prefHelper.isGuest()) {
                showGuestMessage();
                btnAddFavorite.setChecked(!isChecked);
            } else {
                if (getMainActivity().getPlayerFragment() != null)
                    getMainActivity().getPlayerFragment().onFavoriteCheckChange(isChecked, podcastDetailEnt.getTrackId());
                serviceHelper.enqueueCall(webService.changeFavoriteStatus(podcastDetailEnt.getTrackId(), isChecked, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
            }
        }
    };
    private FavoriteCheckChangeListener favoritePlayerCheckChangeListener = new FavoriteCheckChangeListener() {
        @Override
        public void onFavoriteCheckChange(boolean check, int ID) {
            if (ID == podcastDetailEnt.getTrackId()) {
                btnAddFavorite.setOnCheckedChangeListener(null);
                btnAddFavorite.setChecked(check);
                btnAddFavorite.setOnCheckedChangeListener(favoriteCheckListener);
            }
        }
    };
    private RecyclerViewItemListener episodeItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            if (Ent == null) {
                //Case For Play Button Clicked
                //openPlayer(position);
            } else {
                if (prefHelper.isGuest()) {
                    showGuestMessage();
                    return;
                }
                //Case For Download Button Clicked
                if (!prefHelper.isDownloadOnAll()) {
                    if (InternetHelper.isConnectedOnMobile(getDockActivity())) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.network_mobile_error));
                        return;
                    }
                }
              /*  PodcastTrackEnt ent = (PodcastTrackEnt) Ent;
                if (podcastDetailEnt.getEpisodesAdded()) {
                    getDockActivity().addDownload(podcastDetailEnt.AudioUrl, ent.getFileUrl(), ent.getId(), ent.getName(), podcastDetailEnt.getTitle(), podcastDetailEnt);
                } else {
                    getDockActivity().addDownload(ent.getFileUrl(), ent.getName(), ".mp3", ent.getId(), ent.getName(), podcastDetailEnt.getTitle(), podcastDetailEnt);
                }*/
            }

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            if (podcastDetailEnt != null) {
                PodcastEpisodeEnt ent = (PodcastEpisodeEnt) Ent;
                ent.setPodcast(podcastDetailEnt);
                getDockActivity().replaceDockableFragment(PodcastEpisodeDetailFragment.newInstance(ent, podcastEpisodeCollection), PodcastEpisodeDetailFragment.TAG);
            }

        }
    };

    public static PodcastDetailFragment newInstance(PodcastDetailHomeEnt detailEnt) {
        Bundle args = new Bundle();

        PodcastDetailFragment fragment = new PodcastDetailFragment();
        fragment.setArguments(args);
        fragment.setPodcastDetailEnt(detailEnt);
        return fragment;
    }


    private void findAndUpdateDownloadState(final int State, final int Progress) {
        if (podcastTrackEnts.contains(patsyObject)) {
            final int index = podcastTrackEnts.indexOf(patsyObject);
            getMainActivity().realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    podcastTrackEnts.get(index).setStatusState(State);
                    podcastTrackEnts.get(index).setDownloadProgress(Progress);
                }
            });
            rvEpisodes.notifyItemChanged(index);
        }
    }

    public void setPodcastDetailEnt(PodcastDetailHomeEnt podcastDetailEnt) {
        this.podcastDetailEnt = podcastDetailEnt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_PODCAST_DETAIL:
                //  podcastDetailEnt = (PodcastDetailEnt) result;
                bindDataToViews();
                break;
            case WebServiceConstants.GET_PODCAST_EPISODE_DETAIL:
                bindEpisodeList((ArrayList<PodcastEpisodeEnt>) result);
                break;
            case WebServiceConstants.RATE_PODCAST:
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.rating_submit_message));
                if (helper != null) {
                    rbRating.setScore(helper.getDialogRating());
                }
                break;
            case WebServiceConstants.ADD_FAVORITE:
                getMainActivity().realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        podcastDetailEnt.setFavorite(btnAddFavorite.isChecked());
                    }
                });

                if (btnAddFavorite.isChecked()) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.item_added_favorite));
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.item_remove_favorite));
                }
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.addBackground();
        titleBar.setSubHeading(podcastName);
        this.titleBar = titleBar;
    }

    private void bindDataToViews() {
        if (podcastDetailEnt != null) {
            DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
            ImageLoader.getInstance().displayImage(podcastDetailEnt.getImageUrl(), imgItemPic, options);
            txtTitle.setText(podcastDetailEnt.getName() + "");
            txtNarratorText.setText(podcastDetailEnt.getArtistName() + "");
            txtDurationText.setText("");
            txtGenreText.setText("");
            txtLocationText.setText("");
            if (podcastDetailEnt.getRated()) {
                btnRate.setVisibility(View.GONE);
            }
            if (podcastDetailEnt.getRating() == -1) {
                rbRating.setScore(0);
            } else {
                rbRating.setScore((float) podcastDetailEnt.getRating());
            }
            btnAddFavorite.setChecked(podcastDetailEnt.getFavorite());
            btnSubscribe.setChecked(podcastDetailEnt.isSubscribed());
            btnSubscribe.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    serviceHelper.enqueueCall(webService.subscribePodcast(podcastDetailEnt.getTrackId(), podcastDetailEnt.getCategoryId(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_PODCAST);
                } else {
                    serviceHelper.enqueueCall(webService.unSubscribePodcast(podcastDetailEnt.getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
                }
            });

            txtGenreText.setText(podcastDetailEnt.getGenre() + "");
            txtAboutText.setText(podcastDetailEnt.getDescription() + "");
            txtNarratorIntroText.setText("");
            podcastName = podcastDetailEnt.getName();
            if (titleBar != null) {
                titleBar.setSubHeading(podcastName);
                titleBar.invalidate();
            }
            //bindEpisodeList();
            addListenersToViews();
        }
    }

    private void addListenersToViews() {
      /*  nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() - 500)) &&
                            scrollY > oldScrollY) {
                        if (result.size() > 0) {
                            currentPageNumber = currentPageNumber + 1;
                            getNextPageItems(currentPageNumber);
                        }
                        //code to fetch more data for endless scrolling
                    }
                }
            }
        });*/

        btnAddFavorite.setOnCheckedChangeListener(favoriteCheckListener);
    }

    private void bindEpisodeList(ArrayList<PodcastEpisodeEnt> episodeCollection) {
        podcastEpisodeCollection = new ArrayList<>(episodeCollection);
        rvEpisodes.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvEpisodes.BindRecyclerView(new NewPodcastEpisodeBinder(episodeItemListener), podcastEpisodeCollection, layoutManager, new DefaultItemAnimator());
        if (podcastEpisodeCollection.size() <= 0) {
            txtPodcastHeader.setVisibility(View.GONE);
        } else {
            txtPodcastHeader.setVisibility(View.VISIBLE);
        }
    }

    private void bindEpisodeList() {
        result = new ArrayList<>();
        podcastTrackEnts = new ArrayList<>();
        //  result.addAll(Utils.getPages(podcastDetailEnt.getTrackList(), 10));
        if (result.size() > currentPageNumber) {
            podcastTrackEnts.addAll(result.get(currentPageNumber));
        }
        Thread t = new Thread() {
            public void run() {

            }
        };
        t.start();

        rvEpisodes.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        //rvEpisodes.BindRecyclerView(new PodcastEpisodeBinder(episodeItemListener, getMainActivity().realm, podcastDetailEnt.getTitle()), podcastTrackEnts, layoutManager, new DefaultItemAnimator());
        if (podcastTrackEnts.size() <= 0) {
            txtPodcastHeader.setVisibility(View.GONE);
        } else {
            txtPodcastHeader.setVisibility(View.VISIBLE);
        }
    }

    private void getNextPageItems(final Integer pageNumber) {
        if (result != null && podcastTrackEnts != null && rvEpisodes != null && layoutManager != null) {
            if (pageNumber < result.size()) {
                Thread t = new Thread() {
                    public void run() {
                        podcastTrackEnts.addAll(result.get(pageNumber));
                    }
                };
                t.start();
                rvEpisodes.notifyItemRangeChanged(layoutManager.findLastVisibleItemPosition(), result.get(pageNumber).size());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDockActivity().setFileDownloadListener(fileDownloadListener);
        setupUIViews();
        getDetails();
        if (getMainActivity().getPlayerFragment() != null)
            getMainActivity().getPlayerFragment().setCheckChangeListener(favoritePlayerCheckChangeListener);
    }


    private void getDetails() {
        if (podcastDetailEnt != null) {
            bindDataToViews();
            serviceHelper.enqueueCall(webService.getPodcastEpisodesByPodcastID(podcastDetailEnt.getPodcastId(), prefHelper.getUserToken()), WebServiceConstants.GET_PODCAST_EPISODE_DETAIL);
        } else {
            // serviceHelper.enqueueCall(webService.getPodcastDetailByTrack(trackID, categoryID, prefHelper.getUserToken()), WebServiceConstants.GET_PODCAST_DETAIL);
            //  serviceHelper.enqueueCall(webService.getPodcastEpisodesByPodcastID(podcastID, prefHelper.getUserToken()), WebServiceConstants.GET_PODCAST_EPISODE_DETAIL);
        }
    }

    private void setupUIViews() {
        txtPlayerItemAlbum.setSelected(true);
        txtPlayerItemName.setSelected(true);
        txtTitle.setSelected(true);

    }

    @OnClick({R.id.btn_play, R.id.btn_download, R.id.btn_rate, R.id.btn_player_play, R.id.btn_player_forward})
    public void ReReredsfdsfsdfdsfonViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                // openPlayer(0);
                break;
            case R.id.btn_download:
// TODO: 1/15/2018 Download Work
                break;
            case R.id.btn_add_favorite:

                break;
            case R.id.btn_rate:
                if (prefHelper.isGuest()) {
                    showGuestMessage();
                    return;
                }
                if (podcastDetailEnt != null) {
                    helper = new DialogHelper(getDockActivity());
                    helper.initRatingDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            serviceHelper.enqueueCall(webService.ratePodcast(podcastDetailEnt.getTrackId(), helper.getDialogRating(),
                                    prefHelper.getUserToken()), WebServiceConstants.RATE_PODCAST);
                            helper.hideDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            helper.hideDialog();
                        }
                    });
                    helper.setCancelable(false);
                    helper.showDialog();
                }
                break;
            case R.id.btn_player_play:
                break;
            case R.id.btn_player_forward:
                break;
        }
    }

   /* private void openPlayer(int startingIndex) {
        if (podcastDetailEnt != null && podcastDetailEnt.getTrackList() != null && podcastDetailEnt.getTrackList().size() > 0) {
            if (getMainActivity().filterFragment != null) {
                getMainActivity().filterFragment.clearFilters();
            }
            getMainActivity().showBottomPlayer(podcastDetailEnt, podcastDetailEnt.getTrackId(), AppConstants.TAB_PODCAST, null, null,
                    startingIndex, favoritePlayerCheckChangeListener);
          *//*  getDockActivity().replaceDockableFragment(PlayerFragment.newInstance(podcastDetailEnt, trackID, AppConstants.TAB_PODCAST, null,null,
                    startingIndex), PlayerFragment.TAG);*//*
        }
    }
*/

}