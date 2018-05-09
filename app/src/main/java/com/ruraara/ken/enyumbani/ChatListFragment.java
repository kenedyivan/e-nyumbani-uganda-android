package com.ruraara.ken.enyumbani;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruraara.ken.enyumbani.models.Chat;
import com.ruraara.ken.enyumbani.models.ChatRooms;
import com.ruraara.ken.enyumbani.models.FeaturedProperty;
import com.ruraara.ken.enyumbani.sessions.SessionManager;
import com.ruraara.ken.enyumbani.utils.SharedDrawerNavigationUpHomeState;

import java.util.List;

public class ChatListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatListFragment newInstance(String param1, String param2) {
        ChatListFragment fragment = new ChatListFragment();
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
        ChatRooms chatRooms = new ChatRooms(getActivity());

        if (chatRooms.getChats().size() > 0) {
            Log.d("ChatRoomListSize",String.valueOf(chatRooms.getChats().size()));
            // Set the adapter
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        linearLayoutManager.getOrientation());
                recyclerView.addItemDecoration(mDividerItemDecoration);

                recyclerView.setAdapter(new ChatRoomListsRecyclerViewAdapter(chatRooms.getChats(), getActivity()));
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

    public class ChatRoomListsRecyclerViewAdapter
            extends RecyclerView.Adapter<ChatListFragment.ChatRoomListsRecyclerViewAdapter.ViewHolder> {

        private final List<Chat> chatArrayList;
        private final Context mContext;

        public ChatRoomListsRecyclerViewAdapter(List<Chat> items, Context c) {
            chatArrayList = items;
            mContext = c;
            Log.e("Adapter", "In adapter");
        }

        @Override
        public ChatListFragment.ChatRoomListsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_rooms_list_row, parent, false);

            return new ChatListFragment.ChatRoomListsRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChatListFragment.ChatRoomListsRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.mItem = chatArrayList.get(position);
            holder.mName.setText(chatArrayList.get(position).name);
            holder.mLastMessage.setText(chatArrayList.get(position).lastMessage);
            holder.mCreatedAt.setText(chatArrayList.get(position).at);

            final SharedDrawerNavigationUpHomeState sd = new SharedDrawerNavigationUpHomeState(mContext);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Context context = v.getContext();
                    sd.goneToChatRoomList(SharedDrawerNavigationUpHomeState.GONE);
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("propertyId",chatArrayList.get(position).propertyId);
                    intent.putExtra("propertyTitle", chatArrayList.get(position).name);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return chatArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mName;
            public final TextView mLastMessage;
            public final TextView mCreatedAt;
            public Chat mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mName = view.findViewById(R.id.name);
                mLastMessage = view.findViewById(R.id.message);
                mCreatedAt = view.findViewById(R.id.timestamp);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mName.getText() + "'";
            }
        }
    }

}
