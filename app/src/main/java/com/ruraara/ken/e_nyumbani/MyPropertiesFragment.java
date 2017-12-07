package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.classes.MyProperty;
import com.ruraara.ken.e_nyumbani.sessions.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPropertiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPropertiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPropertiesFragment extends Fragment {

    String TAG = "Item fragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyPropertiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPropertiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPropertiesFragment newInstance(String param1, String param2) {
        MyPropertiesFragment fragment = new MyPropertiesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        SessionManager sessionManager = new SessionManager(getActivity());

        RequestParams params = new RequestParams();
        params.put("id", sessionManager.getUserID());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.getMyProperties(), params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "Started request");
                //progressBar.setVisibility(View.VISIBLE);
                mProgressDialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d(TAG, "Status: " + statusCode);
                String resp = new String(response);
                Log.d(TAG, "S: " + resp);

                Log.e(TAG, String.valueOf(MyProperty.ITEMS.size()));

                if (MyProperty.ITEMS.size() > 0) {
                    MyProperty.ITEMS.clear();
                }

                try {
                    JSONArray jsonArray = new JSONArray(resp);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        int rating = jsonObject.getInt("rating");
                        String address = jsonObject.getString("address");
                        String price = jsonObject.getString("price");
                        String currency = jsonObject.getString("currency");
                        String image = jsonObject.getString("image");

                        MyProperty.addPropertyItem(MyProperty.createPropertyItem(String.valueOf(id),
                                title, rating, address, price, currency, image));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Set the adapter
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                            linearLayoutManager.getOrientation());
                    recyclerView.addItemDecoration(mDividerItemDecoration);

                    recyclerView.setAdapter(new MyPropertyRecyclerViewAdapter(MyProperty.ITEMS, getActivity()));
                }


                mProgressDialog.dismiss();

                //End work from here

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "failed " + statusCode);
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "retryNO: " + retryNo);
                Toast.makeText(getActivity(), "Taking too long", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
