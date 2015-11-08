package com.project.quiz.customclasses;

import android.database.Cursor;
import android.view.View;

/**
 * Created by Shitij on 12/10/15.
 */
public class DragObject {
    Cursor cursor;
    View view;
    int team;
    String selected;

    public DragObject(Cursor cursor, View view, int team, String selected){
        this.cursor = cursor;
        this.view = view;
        this.team = team;
        this.selected = selected;
    }

    public String getSelected() {
        return selected;
    }

    public int getTeam() {
        return team;
    }

    public View getView() {
        return view;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
