package NB.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MyUser implements Parcelable {

    public String userName;
    public String userMail;

    public MyUser(){};

    protected MyUser(Parcel in) {
        userName = in.readString();
        userMail = in.readString();
    }

    public MyUser(String userName, String userMail) {
        this.userMail = userMail;
        this.userName = userName;
    }

    public static final Creator<MyUser> CREATOR = new Creator<MyUser>() {
        @Override
        public MyUser createFromParcel(Parcel in) {
            return new MyUser(in);
        }

        @Override
        public MyUser[] newArray(int size) {
            return new MyUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userMail);
    }
}
