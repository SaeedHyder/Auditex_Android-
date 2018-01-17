package com.ingic.auditix.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.ui.views.AnyEditTextView;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRatingBar;


/**
 * Created on 5/24/2017.
 */

public class DialogHelper {
    private Dialog dialog;
    private Context context;

    public DialogHelper(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }


    public void setTextViewText(int ID, String Text) {
        AnyTextView textView = (AnyTextView) dialog.findViewById(ID);
        textView.setText(Text);
    }


    public Dialog initlogout(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_No);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog initRatingDialog( View.OnClickListener onokclicklistener, View.OnClickListener closeListener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialog_rating);
        TextView okbutton = (TextView) dialog.findViewById(R.id.btn_ok);
        okbutton.setOnClickListener(onokclicklistener);
        ImageView closebutton = (ImageView) dialog.findViewById(R.id.btn_close);
        closebutton.setOnClickListener(closeListener);
        return this.dialog;
    }

    public Integer getDialogRating() {
        CustomRatingBar rbAddRating = (CustomRatingBar) dialog.findViewById(R.id.rbAddRating);
        return Math.round(rbAddRating.getScore());
    }


    public float getRating(int ratingBarID) {
        CustomRatingBar ratingBar = (CustomRatingBar) dialog.findViewById(ratingBarID);
        return ratingBar.getScore();
    }


    public String getEditText(int editTextID) {
        AnyEditTextView editTextView = (AnyEditTextView) dialog.findViewById(editTextID);
        UIHelper.hideSoftKeyboard(dialog.getContext(), editTextView);
        return editTextView.getText().toString();
    }

    public void showDialog() {

        dialog.show();
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
