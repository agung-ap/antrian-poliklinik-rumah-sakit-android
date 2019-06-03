package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class Village implements Parcelable {
    private String id;
    private String districtId;
    private String name;

    public Village() {
    }


    protected Village(Parcel in) {
        id = in.readString();
        districtId = in.readString();
        name = in.readString();
    }

    public static final Creator<Village> CREATOR = new Creator<Village>() {
        @Override
        public Village createFromParcel(Parcel in) {
            return new Village(in);
        }

        @Override
        public Village[] newArray(int size) {
            return new Village[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(districtId);
        dest.writeString(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Creator<Village> getCREATOR() {
        return CREATOR;
    }
}
