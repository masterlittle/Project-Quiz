package com.project.quiz.customClasses;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.NumberPicker;

import com.project.quiz.R;
import com.project.quiz.adapters.CustomStudentEditCursorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shitij on 11/10/15.
 */
public class CustomNumberPicker extends Dialog {

    private int score;
    private String identity;
    private CustomStudentEditCursorAdapter adapter;

    @Bind(R.id.numberPicker)
    NumberPicker numberPicker;

    @OnClick(R.id.button_yes)
    public void onClickYes() {
        score = numberPicker.getValue();
        adapter.setStudentScores(score, identity);
        dismiss();
    }

    @OnClick(R.id.button_no)
    public void onClickNo() {
        dismiss();
    }

    public CustomNumberPicker(Context context, int score, String identity, CustomStudentEditCursorAdapter adapter) {
        super(context);
        this.identity = identity;
        this.adapter = adapter;
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_number_picker_dialog);
        ButterKnife.bind(this);
        setValues();
    }

    void setValues() {
        numberPicker.setValue(score);
        numberPicker.setMinValue(score);
        numberPicker.setMaxValue(score + 30);
    }

}
