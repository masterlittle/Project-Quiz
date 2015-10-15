package com.project.quiz.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.project.quiz.R;
import com.project.quiz.adapters.CustomDisplaySimpleCursorAdapter;
import com.project.quiz.contentprovider.DataContentProvider;
import com.project.quiz.database.StudentRecords;

import java.util.Formatter;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 **/
public class FragmentDisplayScore extends Fragment implements AbsListView.OnItemClickListener, android.app.LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Cursor mCursor;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CustomDisplaySimpleCursorAdapter mAdapter;
    private ShareActionProvider mShareActionProvider;

    // TODO: Rename and change types of parameters
    public static FragmentDisplayScore newInstance(String param1, String param2) {
        FragmentDisplayScore fragment = new FragmentDisplayScore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDisplayScore() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share){
            makeScoreSheet();
        }
        return true;
    }

    private void makeScoreSheet() {
        String sheet = "League standings: \n\n";
        int length;
        Formatter formatter;
        mCursor.moveToPosition(-1);
        while(mCursor.moveToNext()){
            String position = String.valueOf(mCursor.getPosition() + 1);
            String name = mCursor.getString(mCursor.getColumnIndex(StudentRecords.STUDENT_NAME)).trim();
//            length = name.length();
//            while(length <25){
//                name+= " ";
//                length++;
//            }
            String score = mCursor.getString(mCursor.getColumnIndex(StudentRecords.STUDENT_SCORE)).trim();
            formatter = new Formatter();
            String format = "%1$-5s%2$-25s%3$-5s\n";
            sheet+=formatter.format(format, position, name, score);
            sheet+="\n";
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sheet);
        sendIntent.setType("text/plain");
        try {
            PackageInfo info=getActivity().getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
        sendIntent.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("League Standings");

        String[] from = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_SCORE};
        int[] to = new int[]{R.id.student_name_position, R.id.student_name_field, R.id.student_score_field};

        // TODO: Change Adapter to display your content
        mAdapter = new CustomDisplaySimpleCursorAdapter(getActivity(),
                R.layout.custom_fragment_display_score,null, from, to, 0);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = new String[]{StudentRecords.COLUMN_ID, StudentRecords.STUDENT_NAME, StudentRecords.STUDENT_SCORE};
        return new CursorLoader(getActivity(), DataContentProvider.CONTENT_STORE_STUDENTS_URI , projection, null, null, StudentRecords.STUDENT_SCORE+" desc");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
