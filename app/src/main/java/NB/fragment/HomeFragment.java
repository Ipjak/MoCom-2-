package NB.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import NB.data.BoardItem;
import NB.data.BoardItemAdapter;
import NB.data.REF;
import NB.main.R;


@IgnoreExtraProperties
public class HomeFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private DatabaseReference myRef;
    private BoardItemAdapter boardItemAdapter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myRef = FirebaseDatabase.getInstance().getReference(REF.LIST.name());
        boardItemAdapter = new BoardItemAdapter();

        myRef.addValueEventListener(new ValueEventListener() {
            //변경 되었을 시 할 행동
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<BoardItem> itemArrayList = new ArrayList<>();

                //리스트 새로 업데이트
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BoardItem item = dataSnapshot.getValue(BoardItem.class);
                    itemArrayList.add(item);
                }
                boardItemAdapter.setItemList(itemArrayList);
            }
            //실패
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.w("FireBase", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        refreshLayout = rootView.findViewById(R.id.home_refresh);
        recyclerView = rootView.findViewById(R.id.home_recycler);

        refreshLayout.setOnRefreshListener(() -> {
            boardItemAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        });

        recyclerView.setAdapter(boardItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }
}