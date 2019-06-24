package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class Regency implements Parcelable {
    private long id;
    private long provinceId;
    private String name;

    public Regency() {
    }

    protected Regency(Parcel in) {
        id = in.readLong();
        provinceId = in.readLong();
        name = in.readString();
    }

    public static final Creator<Regency> CREATOR = new Creator<Regency>() {
        @Override
        public Regency createFromParcel(Parcel in) {
            return new Regency(in);
        }

        @Override
        public Regency[] newArray(int size) {
            return new Regency[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(long provinceId) {
        this.provinceId = provinceId;
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
        dest.writeLong(provinceId);
        dest.writeString(name);
    }
}
