package com.ingic.auditix.fragments.books;

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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.DialogHelper;
import com.ingic.auditix.helpers.InternetHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.interfaces.FavoriteCheckChangeListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.books.BookChapterBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRatingBar;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
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
    @BindView(R.id.txt_about_text)
    AnyTextView txtAboutText;
    @BindView(R.id.txt_narrator_intro_text)
    AnyTextView txtNarratorIntroText;
    @BindView(R.id.txt_chapter_header)
    AnyTextView chapterHeader;
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
    @BindView(R.id.btn_rate)
    Button btnRate;
    @BindView(R.id.btnDownloadAll)
    TextView btnDownloadAll;
    private int bookID;
    private BookDetailEnt detailEnt;
    private TitleBar titleBar;
    private BooksChapterItemEnt patsyObject = new BooksChapterItemEnt();
    private ArrayList<BooksChapterItemEnt> chapterCollections;
    private DownloadListenerFragment fileDownloadListener = new DownloadListenerFragment() {
        @Override
        public void pending(final BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((String) task.getTag());
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
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((String) task.getTag());
                findAndUpdateDownloadState(AppConstants.DownloadStates.DOWNLOADING, progress);
            }
        }

        @Override
        public void completed(final BaseDownloadTask task) {
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((String) task.getTag());
                findAndUpdateDownloadState(AppConstants.DownloadStates.COMPLETE, 0);
            }

        }

        @Override
        public void error(final BaseDownloadTask task, Throwable e) {
            if (chapterCollections != null && rvChapters != null) {
                patsyObject.setChapterID((String) task.getTag());
                findAndUpdateDownloadState(AppConstants.DownloadStates.ERROR, 0);

            }
        }

        @Override
        public void warn(BaseDownloadTask task) {
            task.getAutoRetryTimes();
        }
    };
    private DialogHelper helper;
    private CompoundButton.OnCheckedChangeListener favoriteCheckChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (prefHelper.isGuest()) {
                showGuestMessage();
                btnAddFavorite.setChecked(!b);
            } else {
                if (getMainActivity().getPlayerFragment() != null)
                    getMainActivity().getPlayerFragment().onFavoriteCheckChange(b, bookID);
                if (b) {
                    serviceHelper.enqueueCall(webService.AddBookToFavorite(bookID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                } else {
                    serviceHelper.enqueueCall(webService.RemoveBookFromFavorite(bookID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                }
            }
        }
    };
    private FavoriteCheckChangeListener favoritePlayerCheckChangeListener = new FavoriteCheckChangeListener() {
        @Override
        public void onFavoriteCheckChange(boolean check, int ID) {
            if (ID == bookID) {
                btnAddFavorite.setOnCheckedChangeListener(null);
                btnAddFavorite.setChecked(check);
                btnAddFavorite.setOnCheckedChangeListener(favoriteCheckChange);
            }
        }
    };
    private RecyclerViewItemListener chapterItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            if (Ent == null) {
                //Case For Play Button Clicked
                openPlayer(position);
            } else {
                if (prefHelper.isGuest()) {
                    showGuestMessage();
                    return;
                }
                if (!prefHelper.isDownloadOnAll()) {
                    if (InternetHelper.isConnectedOnMobile(getDockActivity())) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.network_mobile_error));
                        return;
                    }
                }
                //Case For Download Button Clicked
                BooksChapterItemEnt ent = (BooksChapterItemEnt) Ent;
                getDockActivity().addDownload(ent.getFilePath(), ent.getChapterID(), getDownloadName(ent.getChapterNumber()), detailEnt.getBookName(), detailEnt);
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

    private void findAndUpdateDownloadState(int State, int Progress) {
        if (chapterCollections.contains(patsyObject)) {
            int index = chapterCollections.indexOf(patsyObject);
            chapterCollections.get(index).setStatusState(State);
            chapterCollections.get(index).setDownloadProgress(Progress);
            rvChapters.notifyItemChanged(index);
        }
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
            case WebServiceConstants.RATE_BOOK:
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.rating_submit_message));
                if (helper != null) {
                    rbRating.setScore(helper.getDialogRating());
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
        if (detailEnt != null) {
            titleBar.setSubHeading(detailEnt.getBookName() + "");
        }
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
        if (getMainActivity().getPlayerFragment() != null)
            getMainActivity().getPlayerFragment().setCheckChangeListener(favoritePlayerCheckChangeListener);
        if (detailEnt != null) {
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
            return (hours) + " Hr " + (minutes) + " Mins";
        } else if (minutes > 0) {
            return (minutes) + " Mins";
        } else if (secound > 0) {
            return (secound) + " Sec";
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
        rbRating.setScore(result.getRating() == -1 ? 0 : (float) result.getRating());
//        btnRate.setVisibility(result.getRated()?View.GONE:View.VISIBLE);
        txtChaptersText.setText(String.format(Locale.ENGLISH, "%d %s",
                result.getTotalsChapters(), getResString(R.string.chapters)));
        btnListen.setText(R.string.listenbook);
        btnAddFavorite.setChecked(result.getIsFavorite());
        if (titleBar != null) {
            titleBar.setSubHeading(result.getBookName());
            titleBar.invalidate();
        }
        btnAddFavorite.setOnCheckedChangeListener(favoriteCheckChange);
        chapterCollections = new ArrayList<>(detailEnt.getChapters().getChapter().subList(0, detailEnt.getChapters().getChapter().size()));
        rvChapters.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvChapters.BindRecyclerView(new BookChapterBinder(chapterItemListener, getMainActivity().realm, detailEnt.getBookName()), chapterCollections, layoutManager, new DefaultItemAnimator());
        if (chapterCollections.size() <= 0) {
            chapterHeader.setVisibility(View.GONE);
        } else {
            chapterHeader.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.btn_rate, R.id.btn_listen, R.id.btn_add_cart, R.id.btnDownloadAll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_listen:
                openPlayer(0);
                break;
            case R.id.btn_rate:
                if (prefHelper.isGuest()) {
                    showGuestMessage();
                    return;
                }
                if (detailEnt != null) {
                    helper = new DialogHelper(getDockActivity());
                    helper.initRatingDialog(v -> {
                        serviceHelper.enqueueCall(webService.rateBook(bookID, helper.getDialogRating(),
                                prefHelper.getUserToken()), WebServiceConstants.RATE_BOOK);
                        helper.hideDialog();
                    }, v -> helper.hideDialog());
                    helper.setCancelable(false);
                    helper.showDialog();
                }
                break;
            case R.id.btn_add_cart:
                if (detailEnt != null) {
                    if (prefHelper.isGuest()) {
                        showGuestMessage();
                        return;
                    }
                    if (detailEnt.getIsPaid()) {

                        getMainActivity().realm.beginTransaction();
                        getMainActivity().realm.copyToRealmOrUpdate(detailEnt);
                        getMainActivity().realm.commitTransaction();
                        btnAddCart.setVisibility(View.GONE);
                        UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.add_cart_toast));
                    } else {
                        serviceHelper.enqueueCall(webService.AddBookToLibrary(bookID + "", prefHelper.getUserToken()), WebServiceConstants.ADD_LIBRARY);
                    }
                }
                break;
            case R.id.btnDownloadAll:
                for (BooksChapterItemEnt ent : detailEnt.getChapters().getChapter()
                        ) {
                    String path = AppConstants.DOWNLOAD_PATH + File.separator + detailEnt.getBookName() + File.separator + ent.getChapterID().replaceAll("\\s+", "");
                    if (!new File(path).exists()) {
                        getDockActivity().addDownload(ent.getFilePath(), ent.getChapterID(), getDownloadName(ent.getChapterNumber()), detailEnt.getBookName(), detailEnt);
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
            getMainActivity().showBottomPlayer(null, bookID, AppConstants.TAB_BOOKS, detailEnt,
                    null, startingIndex, favoritePlayerCheckChangeListener);
        }
    }
}