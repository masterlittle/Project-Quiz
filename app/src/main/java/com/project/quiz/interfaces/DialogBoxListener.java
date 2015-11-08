package com.project.quiz.interfaces;

/**
 * Created by Shitij on 09/09/15.
 */
public interface DialogBoxListener {
    void showEditDialog(int position, String info);
    void onDialogPositivePressed();
    void onDialogCancelPressed();
}
