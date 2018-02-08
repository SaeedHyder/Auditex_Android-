package com.ingic.auditix.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.activities.MainActivity;

public class TitleBar extends RelativeLayout {

    private TextView txtTitle;
    private ImageView btnLeft;
    private ImageView btnRight2;
    private ImageView btnRight;
    private AnyTextView txtBadge;
    private CheckBox btnFavorite;
    private View.OnClickListener menuButtonListener;
    private OnClickListener backButtonListener;
    private OnClickListener notificationButtonListener;
    private Context context;
    private MainActivity mainActivity;

    public TitleBar(Context context) {
        super(context);
        this.context = context;
        initLayout(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }


    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void bindViews() {

        txtTitle = (TextView) this.findViewById(R.id.txt_subHead);
        btnRight = (ImageView) this.findViewById(R.id.btnRight);
        btnRight2 = (ImageView) this.findViewById(R.id.btnRight2);
        btnLeft = (ImageView) this.findViewById(R.id.btnLeft);
        txtBadge = (AnyTextView) findViewById(R.id.txtBadge);
        btnFavorite = (CheckBox) findViewById(R.id.btn_favorite);

    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_main, this);
        bindViews();
    }

    public void hideButtons() {
        txtTitle.setVisibility(View.INVISIBLE);
        btnLeft.setVisibility(View.INVISIBLE);
        btnRight.setVisibility(View.INVISIBLE);
        btnRight2.setVisibility(View.GONE);
        btnFavorite.setVisibility(View.GONE);
        txtBadge.setVisibility(View.GONE);

    }

    public void showBackButton() {
        btnLeft.setImageResource(R.drawable.back_btn);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(backButtonListener);

    }

    public void showFilterButton(OnClickListener listener) {
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setImageResource(R.drawable.filter_icon);
        btnRight2.setOnClickListener(listener);
    }
    public void showListingFragment(OnClickListener listener) {
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setImageResource(R.drawable.list);
        btnRight2.setOnClickListener(listener);
    }

    public void showFavoriteButton(boolean isChecked, CompoundButton.OnCheckedChangeListener listener) {
        btnFavorite.setVisibility(View.VISIBLE);
        btnFavorite.setChecked(isChecked);
        btnFavorite.setOnCheckedChangeListener(listener);
    }

    public void showMenuButton() {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(menuButtonListener);
        btnLeft.setImageResource(R.drawable.nav);
    }


    public void setSubHeading(String heading) {
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(heading);

    }

    public void showNotificationButton(int Count) {
        btnRight.setVisibility(View.INVISIBLE);
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setOnClickListener(notificationButtonListener);
        btnRight2.setImageResource(R.drawable.notification_icon_white);
        if (Count > 0) {
            txtBadge.setVisibility(View.VISIBLE);
            txtBadge.setText(Count + "");
        } else {
            txtBadge.setVisibility(View.GONE);
        }

    }

    public void setfilterbutton(OnClickListener listener) {
        btnRight2.setVisibility(VISIBLE);
        btnRight2.setOnClickListener(listener);
        btnRight2.setImageResource(R.drawable.filter_icon);
    }

    public void addBackground() {
        mainActivity.addMainframeBelowTitlebar();
        setBackgroundColor(getResources().getColor(R.color.app_title_orange));
    }

    public void setsettingbutton(OnClickListener listener) {
        btnRight2.setVisibility(VISIBLE);
        btnRight2.setOnClickListener(listener);
        btnRight2.setImageResource(R.drawable.filter);
    }

    public void showTitleBar() {
        this.setVisibility(View.VISIBLE);
    }

    public void hideTitleBar() {
        this.setVisibility(View.GONE);
    }

    public void setMenuButtonListener(View.OnClickListener listener) {
        menuButtonListener = listener;
    }

    public void setBackButtonListener(View.OnClickListener listener) {
        backButtonListener = listener;
    }

    public void setNotificationButtonListener(View.OnClickListener listener) {
        notificationButtonListener = listener;
    }


}
