package NB.data;

import android.os.Parcel;
import android.os.Parcelable;


public class BoardItem implements Parcelable {
    //public int id;
    public String boardName;
    public String boardInfo;
    public String userName;


    public BoardItem(){}

    public BoardItem(String name, String info, MyUser userData) {
        this.boardInfo = info;
        this.boardName = name;
        this.userName = userData.userName;
    }

    protected BoardItem(Parcel in) {
        boardName = in.readString();
        boardInfo = in.readString();
        userName = in.readString();
    }

    public static final Creator<BoardItem> CREATOR = new Creator<BoardItem>() {
        @Override
        public BoardItem createFromParcel(Parcel in) {
            return new BoardItem(in);
        }

        @Override
        public BoardItem[] newArray(int size) {
            return new BoardItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boardName);
        dest.writeString(boardInfo);
        dest.writeString(userName);
    }
}
