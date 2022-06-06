package NB.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import NB.main.R;


public class BoardItemAdapter extends RecyclerView.Adapter<BoardItemAdapter.ViewHolder> {

    private ArrayList<BoardItem> itemList = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public BoardItemAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardItemAdapter.ViewHolder holder, int position) {
        holder.onBind(itemList.get(position));
    }

    public void setItemList(ArrayList<BoardItem> list){
        this.itemList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView info;
        TextView profile;
        TextView writer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (TextView) itemView.findViewById(R.id.item_profile);
            info = (TextView) itemView.findViewById(R.id.item_info);
            writer = (TextView) itemView.findViewById(R.id.item_writer);

            //항목 클릭 이벤트
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION) {
                    BoardItem item = itemList.get(pos);
                    Toast.makeText(v.getContext(), item.boardName + " 글 클릭됨", Toast.LENGTH_SHORT).show();
                }
            });
        }

        void onBind(BoardItem item){
            profile.setText(item.boardName);
            info.setText(item.boardInfo);
            writer.setText(item.userName);
        }
    }
}
