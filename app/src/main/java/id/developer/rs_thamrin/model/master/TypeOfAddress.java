package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeOfAddress implements Parcelable {
    private String code;
    private String name;

    public TypeOfAddress() {
    }

    protected TypeOfAddress(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<TypeOfAddress> CREATOR = new Creator<TypeOfAddress>() {
        @Override
        public TypeOfAddress createFromParcel(Parcel in) {
            return new TypeOfAddress(in);
        }

        @Override
        public TypeOfAddress[] newArray(int size) {
            return new TypeOfAddress[size];
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

    public static Creator<TypeOfAddress> getCREATOR() {
        return CREATOR;
    }
}
