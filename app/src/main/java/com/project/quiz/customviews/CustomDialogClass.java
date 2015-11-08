package com.project.quiz.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.NumberPicker;

import com.project.quiz.R;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.interfaces.DialogBoxListener;
import com.project.quiz.utils.CommonLibs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shitij on 22/07/15.
 */
public class CustomDialogClass extends Dialog{
    private final int value;
    public Dialog d;
    public TextViewRegularFont yes, no;
    private DialogBoxListener mListener;
    Context context;

    @Bind(R.id.numberPicker)
    NumberPicker numberPicker;
    @OnClick(R.id.button_next)
    public void onClick(){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences.edit();
        String teams = String.valueOf(numberPicker.getValue());
        editor2.putString(CommonLibs.SharedPrefsKeys.TEAM_NUMBER, teams).apply();
        mListener.onDialogPositivePressed();
    }
    @OnClick(R.id.button_no)
    public void onClickNo(){
        dismiss();
    }

    public CustomDialogClass(Context context,int value) {
        super(context);
        // TODO Auto-generated constructor stub
        mListener = (DialogBoxListener)context;
        this.context = context;
        this.value= value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        ButterKnife.bind(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(value);
        numberPicker.setValue(1);
    }

}