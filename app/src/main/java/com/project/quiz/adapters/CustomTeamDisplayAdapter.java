package com.project.quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.project.quiz.R;
import com.project.quiz.customviews.TextViewBoldFont;
import com.project.quiz.customviews.TextViewRegularFont;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shitij on 05/10/15.
 */
public class CustomTeamDisplayAdapter extends BaseAdapter {
    private int layout;
    private Context context;
    private HashMap<Integer, ArrayList<String>> teams;

    public CustomTeamDisplayAdapter(Context context, int layout, HashMap<Integer, ArrayList<String>> teams) {
        this.context = context;
        this.layout = layout;
        this.teams = teams;
    }

    @Override
    public int getCount() {
        return teams.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(layout, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
            String m = "";
            viewHolder = (ViewHolder)view.getTag();
            viewHolder.teamNumber.setText(String.valueOf(position+1));
            ArrayList<String> members = teams.get(position+1);
            while(members.size()>0){
                m = m + members.get(0) + "\n";
                members.remove(0);
            }
            viewHolder.teamMembers.setText(m);
        return view;
    }

    public void changeData(HashMap<Integer, ArrayList<String>> data){
        teams = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder{
        @Bind(R.id.team_number)
        TextViewBoldFont teamNumber;
        @Bind(R.id.team_members)
        TextViewRegularFont teamMembers;

        public ViewHolder(View v){
            ButterKnife.bind(this, v);
        }
    }
}
