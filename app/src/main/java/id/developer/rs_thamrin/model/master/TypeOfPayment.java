package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeOfPayment implements Parcelable {
    private String code;
    private String name;

    public TypeOfPayment() {
    }

    protected TypeOfPayment(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<TypeOfPayment> CREATOR = new Creator<TypeOfPayment>() {
        @Override
        public TypeOfPayment createFromParcel(Parcel in) {
            return new TypeOfPayment(in);
        }

        @Override
        public TypeOfPayment[] newArray(int size) {
            return new TypeOfPayment[size];
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

    public static Creator<TypeOfPayment> getCREATOR() {
        return CREATOR;
    }
}
