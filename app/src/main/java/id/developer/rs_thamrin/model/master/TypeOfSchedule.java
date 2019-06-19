package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeOfSchedule implements Parcelable {
    private String name;
    private String time;

    public TypeOfSchedule() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static Creator<TypeOfSchedule> getCREATOR() {
        return CREATOR;
    }

    protected TypeOfSchedule(Parcel in) {
        name = in.readString();
        time = in.readString();
    }

    public static final Creator<TypeOfSchedule> CREATOR = new Creator<TypeOfSchedule>() {
        @Override
        public TypeOfSchedule createFromParcel(Parcel in) {
            return new TypeOfSchedule(in);
        }

        @Override
        public TypeOfSchedule[] newArray(int size) {
            return new TypeOfSchedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(time);
    }
}
