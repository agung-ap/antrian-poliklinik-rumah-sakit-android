package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class MariedStatus implements Parcelable {
    private String code;
    private String name;

    public MariedStatus() {
    }

    protected MariedStatus(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<MariedStatus> CREATOR = new Creator<MariedStatus>() {
        @Override
        public MariedStatus createFromParcel(Parcel in) {
            return new MariedStatus(in);
        }

        @Override
        public MariedStatus[] newArray(int size) {
            return new MariedStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Creator<MariedStatus> getCREATOR() {
        return CREATOR;
    }
}
