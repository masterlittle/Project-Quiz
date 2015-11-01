package com.project.quiz.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.project.quiz.R;
import com.project.quiz.customviews.TextViewBoldFont;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.interfaces.DialogBoxListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shitij on 10/10/15.
 */
public class CustomDialogTextClass extends Dialog {
    private DialogBoxListener listener;
    private String heading;
    private String message;

    @Bind(R.id.dialog_heading)
    TextViewBoldFont headingText;
    @Bind(R.id.dialog_message)
    TextViewRegularFont messageText;
    @OnClick(R.id.button_yes)
    public void OnClick(){
        listener.onDialogPositivePressed();
        dismiss();
    }
    @OnClick(R.id.button_no)
    public void onClickNo(){
        dismiss();
    }

    public CustomDialogTextClass(Activity activity, String heading, String message) {
        super(activity);
        listener = (DialogBoxListener)activity;
        this.heading = heading;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_text);
        ButterKnife.bind(this);
        headingText.setText(heading);
        messageText.setText(message);
    }
}
