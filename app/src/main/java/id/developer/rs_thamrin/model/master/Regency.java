package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class Regency implements Parcelable {
    private String id;
    private String provinceId;
    private String name;

    public Regency() {
    }


    protected Regency(Parcel in) {
        id = in.readString();
        provinceId = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(provinceId);
        dest.writeString(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Creator<Regency> getCREATOR() {
        return CREATOR;
    }
}
