package com.ruraara.ken.e_nyumbani;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.ruraara.ken.e_nyumbani.models.DummyContent.DummyItem;
import com.ruraara.ken.e_nyumbani.models.MyFavorite;
import com.ruraara.ken.e_nyumbani.sessions.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyFavoritesFragment extends Fragment {

    String TAG = "Item fragment";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    OnDataPass dataPasser;

    OnEmptyList emptyDataPasser;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyFavoritesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyFavoritesFragment newInstance(int columnCount) {
        MyFavoritesFragment fragment = new MyFavoritesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        //final ProgressBar progressBar= getView().findViewById(R.id.indeterminateBar);

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        SessionManager sessionManager = new SessionManager(getActivity());
        String agentId = sessionManager.getUserID();

        RequestParams params = new RequestParams();
        params.put("agent_id", agentId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AppData.getFavorites(), params, new AsyncHttpResponseHandler() {

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

                Log.e(TAG, String.valueOf(MyFavorite.ITEMS.size()));

                if (MyFavorite.ITEMS.size() > 0) {
                    MyFavorite.ITEMS.clear();
                }

                try {
                    JSONArray jsonArray = new JSONArray(resp);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        int rating = jsonObject.getInt("rating");
                        String address = jsonObject.getString("address");
                        String agentId = jsonObject.getString("agent_id");
                        String agent = jsonObject.getString("agent");
                        String price = jsonObject.getString("price");
                        String currency = jsonObject.getString("currency");
                        String image = jsonObject.getString("image");
                        MyFavorite.addPropertyItem(MyFavorite.createPropertyItem(String.valueOf(id),
                                title, rating, address, agentId, agent, price, currency, image));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Do the working from here

                if (MyFavorite.ITEMS.size() < 1) {
                    listEmpty(true);
                } else {

                    // Set the adapter
                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view;
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                linearLayoutManager.getOrientation());
                        recyclerView.addItemDecoration(mDividerItemDecoration);

                        recyclerView.setAdapter(new MyFavoriteRecyclerViewAdapter(MyFavorite.ITEMS, getActivity()));
                    }
                }

                //setupRecyclerView((RecyclerView) recyclerView);

                //progressBar.setVisibility(View.INVISIBLE);

                mProgressDialog.dismiss();

                passData(true);

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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        if (context instanceof OnDataPass) {
            dataPasser = (OnDataPass) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDataPass");
        }

        if (context instanceof OnEmptyList) {
            emptyDataPasser = (OnEmptyList) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEmptyList");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }


    public interface OnDataPass {
        public void onDataPass(boolean data);
    }

    public void passData(boolean data) {
        dataPasser.onDataPass(data);
    }

    public interface OnEmptyList {
        public void listEmpty(boolean data);
    }

    public void listEmpty(boolean data) {
        emptyDataPasser.listEmpty(data);
    }

}
