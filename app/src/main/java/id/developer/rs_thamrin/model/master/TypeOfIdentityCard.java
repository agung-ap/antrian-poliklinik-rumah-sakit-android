package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeOfIdentityCard implements Parcelable {
    private String code;
    private String name;

    public TypeOfIdentityCard() {
    }

    protected TypeOfIdentityCard(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    public static final Creator<TypeOfIdentityCard> CREATOR = new Creator<TypeOfIdentityCard>() {
        @Override
        public TypeOfIdentityCard createFromParcel(Parcel in) {
            return new TypeOfIdentityCard(in);
        }

        @Override
        public TypeOfIdentityCard[] newArray(int size) {
            return new TypeOfIdentityCard[size];
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

    public static Creator<TypeOfIdentityCard> getCREATOR() {
        return CREATOR;
    }
}
