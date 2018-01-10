package com.ruraara.ken.e_nyumbani;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruraara.ken.e_nyumbani.models.AgentProperty;

import java.util.ArrayList;
import java.util.List;


public class AgentSaleFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<AgentProperty.PropertyItem> propertyItemList = null;

    public AgentSaleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgentSaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgentSaleFragment newInstance(String param1, String param2) {
        AgentSaleFragment fragment = new AgentSaleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        Bundle bundle = this.getArguments();
        if (bundle != null) {


            //propertyItemList = (ArrayList<AgentProperty.PropertyItem>) bundle.getSerializable("sale");

            //Log.e("Saleff: ", "fs::"+bundle.getString("sale"));

            ArrayList<AgentProperty.PropertyItem> ap = (ArrayList<AgentProperty.PropertyItem>) bundle.getSerializable("sale");

            propertyItemList = ap;

            Log.e("Hello: ", "fs:::" + String.valueOf(ap.size()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = null;

        if (propertyItemList.size() < 1) {
            view = inflater.inflate(R.layout.empty_list_layout, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_item_list, container, false);
            // Set the adapter
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        linearLayoutManager.getOrientation());
                recyclerView.addItemDecoration(mDividerItemDecoration);

                recyclerView.setAdapter(new AgentPropertiesRecyclerViewAdapter(propertyItemList, getActivity()));
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
