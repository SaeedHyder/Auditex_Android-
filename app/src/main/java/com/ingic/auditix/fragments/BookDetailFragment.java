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
import android.widget.ToggleButton;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.InternetHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.BookChapterBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRatingBar;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.RealmResults;

/**
 * Created on 2/1/2018.
 */
public class BookDetailFragment extends BaseFragment {
    public static final String TAG = "BookDetailFragment";
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txt_narrator_text)
    AnyTextView txtNarratorText;
    @BindView(R.id.txt_author_text)
    AnyTextView txtAuthorText;
    @BindView(R.id.rb_rating)
    CustomRatingBar rbRating;
    @BindView(R.id.txt_genre_text)
    AnyTextView txtGenreText;
    @BindView(R.id.txt_duration_text)
    AnyTextView txtDurationText;
    @BindView(R.id.txt_chapters_text)
    AnyTextView txtChaptersText;
    @BindView(R.id.txt_price)
    AnyTextView txtPrice;
    @BindView(R.id.txt_about_text)
    AnyTextView txtAboutText;
    @BindView(R.id.txt_narrator_intro_text)
    AnyTextView txtNarratorIntroText;
    @BindView(R.id.btn_listen)
    Button btnListen;
    @BindView(R.id.btn_add_cart)
    Button btnAddCart;
    @BindView(R.id.btn_add_favorite)
    ToggleButton btnAddFavorite;
    Unbinder unbinder;
    @BindView(R.id.parent_scroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.rv_chapters)
    CustomRecyclerView rvChapters;
    private int bookID;
    private BookDetailEnt detailEnt;
    private TitleBar titleBar;
    private BooksChapterItemEnt patsyObject = new BooksChapterItemEnt();
    private ArrayList<BooksChapterItemEnt> chapterCollections;
    private DownloadListenerFragment fileDownloadListener = new DownloadListenerFragment() {
        @Override
        public void pending(final BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((Integer) task.getTag());
                if (chapterCollections.contains(patsyObject)) {
                    int index = chapterCollections.indexOf(patsyObject);
                    chapterCollections.get(index).setStatusState(AppConstants.DownloadStates.PENDING);
                    rvChapters.notifyItemChanged(index);
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
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((Integer) task.getTag());
                if (chapterCollections.contains(patsyObject)) {
                    int index = chapterCollections.indexOf(patsyObject);
                    chapterCollections.get(index).setStatusState(AppConstants.DownloadStates.DOWNLOADING);
                    chapterCollections.get(index).setDownloadProgress(progress);
                    rvChapters.notifyItemChanged(index);
                }
            }
        }

        @Override
        public void completed(final BaseDownloadTask task) {
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((Integer) task.getTag());
                if (chapterCollections.contains(patsyObject)) {
                    int index = chapterCollections.indexOf(patsyObject);
                    chapterCollections.get(index).setStatusState(AppConstants.DownloadStates.COMPLETE);
                    rvChapters.notifyItemChanged(index);
                }
            }

        }

        @Override
        public void error(final BaseDownloadTask task, Throwable e) {
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((Integer) task.getTag());
                if (chapterCollections.contains(patsyObject)) {
                    int index = chapterCollections.indexOf(patsyObject);
                    chapterCollections.get(index).setStatusState(AppConstants.DownloadStates.ERROR);
                    rvChapters.notifyItemChanged(index);
                   /* UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.download_error) + " "
                            + getDockActivity().getResources().getString(R.string.chapters) + " " + chapterCollections.get(index).getChapterNumber());*/
                }

            }
        }

        @Override
        public void warn(BaseDownloadTask task) {
            task.getAutoRetryTimes();
        }
    };
    private RecyclerViewItemListener chapterItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            if (Ent == null) {
                //Case For Play Button Clicked
                openPlayer(position);
            } else {
                if (!prefHelper.isDownloadOnAll()) {
                    if (InternetHelper.isConnectedOnMobile(getDockActivity())) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.network_mobile_error));
                        return;
                    }
                }
                //Case For Download Button Clicked
                BooksChapterItemEnt ent = (BooksChapterItemEnt) Ent;
                getDockActivity().addDownload(detailEnt.getChapters().getAudioUrl(), ent.getAudioUrl(), ent.getChapterID(), getDownloadName(ent.getChapterNumber()));
            }
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {

        }
    };

    public static BookDetailFragment newInstance(int bookId) {
        Bundle args = new Bundle();

        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(args);
        fragment.setBookID(bookId);
        return fragment;
    }

    private String getDownloadName(Integer number) {
        return String.format(Locale.ENGLISH, "%s %s %d", detailEnt.getBookName(), getDockActivity().getResources().getString(R.string.chapter), number);
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
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
            case WebServiceConstants.GET_BOOK_DETAIL:
                detailEnt = (BookDetailEnt) result;
                bindDataToViews(detailEnt);
                break;
            case WebServiceConstants.ADD_FAVORITE:
                detailEnt.setIsFavorite(btnAddFavorite.isChecked());
                if (btnAddFavorite.isChecked()) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.book_item_added_favorite));
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.book_item_remove_favorite));
                }
                break;
            case WebServiceConstants.ADD_LIBRARY:
                btnAddCart.setVisibility(View.GONE);
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.book_item_added_Book));
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.addBackground();
        this.titleBar = titleBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceHelper.enqueueCall(webService.getBookDetails(this.bookID, prefHelper.getUserToken()), WebServiceConstants.GET_BOOK_DETAIL);
        getDockActivity().setFileDownloadListener(fileDownloadListener);
        if (detailEnt!=null) {
            if (detailEnt.getIsPurchased())
            try {
                RealmResults<BookDetailEnt> object = getMainActivity().realm
                        .where(BookDetailEnt.class)
                        .equalTo("bookID", detailEnt.getBookID()).findAll();
                if (object.size() > 0) {
                    btnAddCart.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        detailEnt = null;
    }

    public String getDurationText(Integer secound) {
        int minutes = (secound % 3600) / 60;
        int hours = secound / 3600;

        if (hours > 1) {
            return (hours) + "Hr" + (minutes) + "mins";
        } else if (minutes > 0) {
            return (minutes) + " mins";
        } else if (secound > 0) {
            return (secound) + " sec";
        } else {
            return "-";
        }

    }

    private void bindDataToViews(BookDetailEnt result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(result.getImageUrl(), imgItemPic, options);
        txtTitle.setText(result.getBookName() + "");
        txtNarratorText.setText(result.getNarratorName() + "");
        txtAuthorText.setText(result.getAuthorName() + "");
        txtGenreText.setText(result.getGenre() + "");
        txtDurationText.setText(getDurationText(result.getDuration()));
        txtAboutText.setText(result.getAboutTheBook() + "");
        txtNarratorIntroText.setText(result.getAboutTheNarrator() + "");
        if (result.getRating() == -1) {
            rbRating.setScore(0);
        } else {
            rbRating.setScore((float) result.getRating());
        }
        if (!result.getIsPaid()) {
            txtPrice.setText(R.string.free);
            btnAddCart.setText(R.string.addtolibrary);
        } else {
            txtPrice.setText("$ " + result.getPrice() + "");
            btnAddCart.setText(R.string.addtocart);
            try {
                RealmResults<BookDetailEnt> object = getMainActivity().realm
                        .where(BookDetailEnt.class)
                        .equalTo("bookID", detailEnt.getBookID()).findAll();
                if (object.size() > 0) {
                    btnAddCart.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (result.getIsPurchased()) {
            if (result.getTotalChapters() == 2) {
                txtChaptersText.setText(String.format(Locale.ENGLISH, "%d %s",
                        result.getTotalChapters() - 1, getDockActivity().getResources().getString(R.string.chapters)));

            } else {

                txtChaptersText.setText(String.format(Locale.ENGLISH, "%d %s", result.getTotalChapters() - 1, getDockActivity().getResources().getString(R.string.chapters_heading)));
            }
            btnListen.setText(R.string.listenbook);
            btnAddCart.setVisibility(View.GONE);
            if (detailEnt.getIsPurchased() && detailEnt.getChapters().getChapter().size() > 1) {
                detailEnt.getChapters().getChapter().remove(0);
            }
            chapterCollections = new ArrayList<>(detailEnt.getChapters().getChapter().subList(0, detailEnt.getChapters().getChapter().size()));
            rvChapters.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
            rvChapters.BindRecyclerView(new BookChapterBinder(chapterItemListener, getMainActivity().realm), chapterCollections, layoutManager, new DefaultItemAnimator());
        } else {
            if (result.getTotalChapters() == 2) {
                txtChaptersText.setText(String.format(Locale.ENGLISH, "%d %s", result.getTotalChapters() - 1, getDockActivity().getResources().getString(R.string.chapters)));

            } else {
                txtChaptersText.setText(String.format(Locale.ENGLISH, "%d %s", result.getTotalChapters() - 1, getDockActivity().getResources().getString(R.string.chapters_heading)));
            }
        }
        btnAddFavorite.setChecked(result.getIsFavorite());
        if (titleBar != null) {
            titleBar.setSubHeading(result.getBookName());
            titleBar.invalidate();
        }
        btnAddFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceHelper.enqueueCall(webService.AddBookToFavorite(bookID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                } else {
                    serviceHelper.enqueueCall(webService.RemoveBookFromFavorite(bookID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                }
            }
        });

    }

    @OnClick({R.id.btn_listen, R.id.btn_add_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_listen:
                openPlayer(0);
                break;
            case R.id.btn_add_cart:
                if (detailEnt != null) {
                    if (detailEnt.getIsPaid()) {
                        getMainActivity().realm.beginTransaction();
                        getMainActivity().realm.copyToRealm(detailEnt);
                        getMainActivity().realm.commitTransaction();
                        btnAddCart.setVisibility(View.GONE);
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.add_cart_toast));
                    } else {
                        serviceHelper.enqueueCall(webService.AddBookToLibrary(bookID + "", prefHelper.getUserToken()), WebServiceConstants.ADD_LIBRARY);
                    }
                }
                break;
        }
    }

    private void openPlayer(int startingIndex) {
        if (detailEnt != null && detailEnt.getChapters() != null && detailEnt.getChapters().getChapter().size() > 0) {
            if (getMainActivity().booksFilterFragment != null) {
                getMainActivity().booksFilterFragment.clearFilters();
            }
            getDockActivity().replaceDockableFragment(PlayerFragment.newInstance(null, bookID, AppConstants.TAB_BOOKS, detailEnt, startingIndex), "PlayerFragment");
        }
    }
}