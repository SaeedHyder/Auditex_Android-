package com.ingic.auditix.fragments;

import android.os.Bundle;
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
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.DialogHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.helpers.Utils;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.PodcastEpisodeBinder;
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

/**
 * Created on 12/27/2017.
 */
public class PodcastDetailFragment extends BaseFragment {
    private static final String TAG = "PodcastDetailFragment";
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
    private String podcastName = "";
    private TitleBar titleBar;
    private PodcastDetailEnt podcastDetailEnt;
    private Integer trackID;
    private List<List<PodcastTrackEnt>> result;
    private ArrayList<PodcastTrackEnt> podcastTrackEnts;
    private PodcastTrackEnt patsyObject = new PodcastTrackEnt();
    private Integer totalPagesCount, currentPageNumber = 0;
    private DownloadListenerFragment fileDownloadListener = new DownloadListenerFragment() {
        @Override
        public void pending(final BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (podcastTrackEnts != null && rvEpisodes != null) {
                patsyObject.setId((Integer) task.getTag());
                if (podcastTrackEnts.contains(patsyObject)) {
                    int index = podcastTrackEnts.indexOf(patsyObject);
                    podcastTrackEnts.get(index).setStatusState(AppConstants.DownloadStates.PENDING);
                    rvEpisodes.notifyItemChanged(index);
                }

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
                patsyObject.setId((Integer) task.getTag());
                if (podcastTrackEnts.contains(patsyObject)) {
                    int index = podcastTrackEnts.indexOf(patsyObject);
                    podcastTrackEnts.get(index).setStatusState(AppConstants.DownloadStates.DOWNLOADING);
                    podcastTrackEnts.get(index).setDownloadProgress(progress);
                    rvEpisodes.notifyItemChanged(index);
                }

            }
        }

        @Override
        public void completed(final BaseDownloadTask task) {
            if (podcastTrackEnts != null && rvEpisodes != null) {
                patsyObject.setId((Integer) task.getTag());
                if (podcastTrackEnts.contains(patsyObject)) {
                    int index = podcastTrackEnts.indexOf(patsyObject);
                    podcastTrackEnts.get(index).setStatusState(AppConstants.DownloadStates.COMPLETE);
                    rvEpisodes.notifyItemChanged(index);
                }
            }

        }

        @Override
        public void error(final BaseDownloadTask task, Throwable e) {
            if (podcastTrackEnts != null && rvEpisodes != null) {
                patsyObject.setId((Integer) task.getTag());
                if (podcastTrackEnts.contains(patsyObject)) {
                    int index = podcastTrackEnts.indexOf(patsyObject);
                    podcastTrackEnts.get(index).setStatusState(AppConstants.DownloadStates.ERROR);
                    rvEpisodes.notifyItemChanged(index);
                   /* UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.download_error)
                            + podcastTrackEnts.get(index).getName());*/
                }

            }
        }

        @Override
        public void warn(BaseDownloadTask task) {
            task.getAutoRetryTimes();
        }
    };
    private RecyclerViewItemListener episodeItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            if (Ent == null) {
                //Case For Play Button Clicked
                openPlayer(position);
            } else {
                //Case For Download Button Clicked
                PodcastTrackEnt ent = (PodcastTrackEnt) Ent;
                if (podcastDetailEnt.isEpisodeAdded()) {
                    getDockActivity().addDownload(podcastDetailEnt.AudioUrl, ent.getFileUrl(), ent.getId(),ent.getName());
                } else {
                    getDockActivity().addDownload(ent.getFileUrl(), ent.getName(), ".mp3", ent.getId(),ent.getName());
                }
            }

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            PodcastTrackEnt ent = (PodcastTrackEnt) Ent;

        }
    };
    private DialogHelper helper;
    private LinearLayoutManager layoutManager;

    public static PodcastDetailFragment newInstance(Integer trackID) {
        Bundle args = new Bundle();

        PodcastDetailFragment fragment = new PodcastDetailFragment();
        fragment.setArguments(args);
        fragment.setTrackID(trackID);
        return fragment;
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
                podcastDetailEnt = (PodcastDetailEnt) result;
                bindDataToViews();
                break;
            case WebServiceConstants.RATE_PODCAST:
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.rating_submit_message));
                if (helper != null) {
                    rbRating.setScore(helper.getDialogRating());
                }
                break;
            case WebServiceConstants.ADD_FAVORITE:
                podcastDetailEnt.setFavorite(btnAddFavorite.isChecked());
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
            ImageLoader.getInstance().displayImage(podcastDetailEnt.getImage(), imgItemPic, options);
            txtTitle.setText(podcastDetailEnt.getTitle() + "");
            txtNarratorText.setText(podcastDetailEnt.getAuthor() + "");
            txtDurationText.setText("");
            txtGenreText.setText("");
            txtLocationText.setText("");
            if (podcastDetailEnt.getRating() == -1) {
                rbRating.setScore(0);
            } else {
                rbRating.setScore((float) podcastDetailEnt.getRating());
            }
            btnAddFavorite.setChecked(podcastDetailEnt.isFavorite());

            txtGenreText.setText(podcastDetailEnt.getGenre() + "");
            txtAboutText.setText(podcastDetailEnt.getDescription() + "");
            txtNarratorIntroText.setText("");
            podcastName = podcastDetailEnt.getTitle();
            if (titleBar != null) {
                titleBar.setSubHeading(podcastName);
                titleBar.invalidate();
            }
            bindEpisodeList();
            addListenersToViews();
        }
    }

    private void addListenersToViews() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
        });
        btnAddFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                serviceHelper.enqueueCall(webService.changeFavoriteStatus(trackID, isChecked, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
            }
        });
    }

    private void bindEpisodeList() {
        result = new ArrayList<>();
        podcastTrackEnts = new ArrayList<>();

        Thread t = new Thread() {
            public void run() {
                result.addAll(Utils.getPages(podcastDetailEnt.getTrackList(), 10));
                if (result.size() > currentPageNumber) {
                    podcastTrackEnts.addAll(result.get(currentPageNumber));
                }
            }
        };
        t.start();

        rvEpisodes.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvEpisodes.BindRecyclerView(new PodcastEpisodeBinder(episodeItemListener,getMainActivity().realm), podcastTrackEnts, layoutManager, new DefaultItemAnimator());
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setTrackID(Integer trackID) {
        this.trackID = trackID;
    }

    private void getDetails() {
        serviceHelper.enqueueCall(webService.getPodcastDetailByTrack(trackID, prefHelper.getUserToken()), WebServiceConstants.GET_PODCAST_DETAIL);
    }

    private void setupUIViews() {
        txtPlayerItemAlbum.setSelected(true);
        txtPlayerItemName.setSelected(true);
        txtTitle.setSelected(true);

    }

    @OnClick({R.id.btn_play, R.id.btn_download, R.id.btn_rate, R.id.btn_player_play, R.id.btn_player_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                openPlayer(0);
                break;
            case R.id.btn_download:
// TODO: 1/15/2018 Download Work
                break;
            case R.id.btn_add_favorite:

                break;
            case R.id.btn_rate:
                if (podcastDetailEnt != null) {
                    helper = new DialogHelper(getDockActivity());
                    helper.initRatingDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            serviceHelper.enqueueCall(webService.ratePodcast(trackID, helper.getDialogRating(),
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

    private void openPlayer(int startingIndex) {
        if (podcastDetailEnt != null && podcastDetailEnt.getTrackList() != null && podcastDetailEnt.getTrackList().size() > 0) {
            if (getMainActivity().filterFragment != null) {
                getMainActivity().filterFragment.clearFilters();
            }
            getDockActivity().replaceDockableFragment(PlayerFragment.newInstance(podcastDetailEnt, trackID, AppConstants.TAB_PODCAST, null, startingIndex), "PlayerFragment");
        }
    }
}