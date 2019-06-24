package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Village implements Parcelable {
    private long id;
    private long districtId;
    private String name;

    public Village() {
    }

    protected Village(Parcel in) {
        id = in.readLong();
        districtId = in.readLong();
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(districtId);
        dest.writeString(name);
    }
}
