package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class District implements Parcelable {
    private long id;
    private long regencyId;
    private String name;

    public District() {
    }

    protected District(Parcel in) {
        id = in.readLong();
        regencyId = in.readLong();
        name = in.readString();
    }

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override
        public District createFromParcel(Parcel in) {
            return new District(in);
        }

        @Override
        public District[] newArray(int size) {
            return new District[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRegencyId() {
        return regencyId;
    }

    public void setRegencyId(long regencyId) {
        this.regencyId = regencyId;
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
        dest.writeLong(regencyId);
        dest.writeString(name);
    }
}
