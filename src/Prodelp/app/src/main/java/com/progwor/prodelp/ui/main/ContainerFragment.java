package com.progwor.prodelp.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdgoal;
import com.progwor.prodelp.core.Pdprogress;
import com.progwor.prodelp.core.Pdreview;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.data.ProdelpLoader;

import java.util.Iterator;
import java.util.Set;

// A simple Fragment subclass. Activities that contain this fragment must
// implement the ContainerFragment.OnFragmentInteractionListener interface to handle
// interaction events. Use the ContainerFragment#newInstance
// factory method to create an instance of this fragment.

public class ContainerFragment extends Fragment {

    //The fragment initialization parameters
    private static final String ARG_SELECTED_ITEM = "selected_item";

    //Selected item in drawer
    private int mSelectedItem;

    //Allows fragment to communicate with Activity
    private OnFragmentInteractionListener mListener;

    //CursorLoader for loading data
    private ProdelpLoader mProdelpLoader;

    Pdprogress pdprogress;

    public ContainerFragment() {
        // Required empty public constructor
    }

    // Returns new instance of ContainerFragment
    public static ContainerFragment newInstance(int selectedItem) {
        ContainerFragment fragment = new ContainerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SELECTED_ITEM, selectedItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectedItem = getArguments().getInt(ARG_SELECTED_ITEM);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Delete all rows
        // getActivity().getContentResolver().delete(ProdelpContract.PdroutineEntry.CONTENT_URI,null,null);


        // Inflate the layout for this fragment
        switch (mSelectedItem) {
            case Prodelp.PDROUTINE:
                Pdroutine pdroutine = new Pdroutine();
                mProdelpLoader = pdroutine.getLoaderCallbacks();
                return pdroutine.load(getActivity(), inflater, container, mSelectedItem);
            case Prodelp.PDGOAL:
                Pdgoal pdgoal = new Pdgoal();
                mProdelpLoader = pdgoal.getLoaderCallbacks();
                return pdgoal.load(getActivity(), inflater, container);
            case Prodelp.PDREVIEW:
                Pdreview pdreview = new Pdreview();
                mProdelpLoader = pdreview.getLoaderCallbacks();
                return pdreview.load(getActivity(),this, inflater, container, mSelectedItem);
            case Prodelp.PDPROGRESS:
                pdprogress = new Pdprogress();
                return pdprogress.load(getActivity(),inflater,container);
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(savedInstanceState.getString("title"));
        }


        switch (mSelectedItem) {
            case Prodelp.PDROUTINE:
                getLoaderManager().initLoader(Prodelp.LOADER, null, mProdelpLoader);
                break;
            case Prodelp.PDGOAL:
                getLoaderManager().initLoader(Prodelp.LOADER, null, mProdelpLoader);
                break;
            case Prodelp.PDREVIEW:
                Bundle bundle = new Bundle();
                bundle.putLong(Pdroutine.ID_STRING, 1);
               // getLoaderManager().initLoader(Prodelp.LOADER, bundle, mProdelpLoader);
                break;
            case Prodelp.PDPROGRESS:
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

                Set falseValuesSet = sharedPref.getStringSet("reviewFalseValues",null);

                if(falseValuesSet!=null)
                {
                    Iterator iterator = falseValuesSet.iterator();
                    RelativeLayout relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.pdprogress_fragment);
                    FetchTask fetchTask = new FetchTask(getActivity(),relativeLayout,relativeLayout.findViewById(R.id.pdprogress_pdgoal_listview).getId());
                    fetchTask.execute("how to "+iterator.next().toString());
                    sharedPref.edit().clear().apply();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("title",((ActionBarActivity) getActivity()).getSupportActionBar().getTitle().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
