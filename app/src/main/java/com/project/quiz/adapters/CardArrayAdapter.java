package com.project.quiz.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.quiz.activities.ActivityUpdateScore;
import com.project.quiz.R;
import com.project.quiz.customClasses.CardScore;
import com.project.quiz.customClasses.CursorRecyclerAdapter;
import com.project.quiz.customviews.TextViewBoldFont;
import com.project.quiz.customviews.TextViewRegularFont;
import com.project.quiz.database.StorePointsTable;
import com.project.quiz.interfaces.UpdateScoreCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 23/09/15.
 */
public class CardArrayAdapter extends CursorRecyclerAdapter {
    private List<CardScore> cardList = new ArrayList<CardScore>();
    public UpdateScoreCallback listener;
//    private Cursor mCursor;



    public CardArrayAdapter(Activity activity, Cursor cursor) {
        super(cursor);
        listener = (ActivityUpdateScore)activity;
    }

//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        CardViewHolder card;
//        if(row == null) {
//            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = inflater.inflate(R.layout.activity_update_score, parent, false);
//            card = new CardViewHolder(row);
//            row.setTag(card);
//        }else {
//            card = (CardViewHolder) row.getTag();
//        }
//            CardScore c = cardList.get(position);
//            card.teamNum.setText(String.valueOf(c.getTeamNum()));
//            card.currentScore.setText(String.valueOf(c.getCurrentScore()));
//            card.increaseScore.setText(String.valueOf(c.getIncreaseScore()));
//        return row;
//    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_update_score, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(view);
        return viewHolder;
//        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        row = inflater.inflate(R.layout.activity_update_score, parent, false);
//        card = new CardViewHolder(row);
//        row.setTag(card);
//        return null;
    }

//    @Override
//    public void onBindViewHolder(CardViewHolder holder, final int position) {
//        if(mCursor != null) {
//            mCursor.moveToPosition(position);
//            holder.cardLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.expandToolbar(position);
//                }
//            });
////        CardScore c = cardList.get(position);
//            holder.teamNum.setText(mCursor.getColumnIndex(StorePointsTable.TEAM_NUMBER));
//            holder.currentScore.setText(mCursor.getColumnIndex(StorePointsTable.CURRENT_SCORE));
//            holder.increaseScore.setText(mCursor.getColumnIndex(StorePointsTable.CHANGED_SCORE));
////        holder.teamNum.setText(String.valueOf(c.getTeamNum()));
////        holder.currentScore.setText(String.valueOf(c.getCurrentScore()));
////        holder.increaseScore.setText(String.valueOf(c.getIncreaseScore()));
//        }
//    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (cursor != null) {
            final int cursorPosition = cursor.getPosition();
            final CardViewHolder cardViewHolder = (CardViewHolder)holder;
            cardViewHolder.cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.expandToolbar(cursorPosition);
                    ActivityUpdateScore.currentPoints =0;
                }
            });
//        CardScore c = cardList.get(position);
            cardViewHolder.teamNum.setText(cursor.getString(cursor.getColumnIndex(StorePointsTable.TEAM_NUMBER)));
            cardViewHolder.currentScore.setText(cursor.getString(cursor.getColumnIndex(StorePointsTable.CURRENT_SCORE)));
            cardViewHolder.increaseScore.setText(cursor.getString(cursor.getColumnIndex(StorePointsTable.CHANGED_SCORE)));
        }
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card_view) LinearLayout cardLayout;
        @Bind(R.id.team_number_text)TextViewRegularFont teamNum;
        @Bind(R.id.current_score_number)TextViewBoldFont currentScore;
        @Bind(R.id.inc_score_number)TextViewRegularFont increaseScore;

        public CardViewHolder(View rowView) {
            super(rowView);
            ButterKnife.bind(this, rowView);
        }
    }
}
