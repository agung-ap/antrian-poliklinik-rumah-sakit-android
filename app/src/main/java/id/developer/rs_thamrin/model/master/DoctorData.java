package id.developer.rs_thamrin.model.master;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorData implements Parcelable {
    private int id;
    private String name;
    private String specialization;

    public DoctorData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public static Creator<DoctorData> getCREATOR() {
        return CREATOR;
    }

    protected DoctorData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        specialization = in.readString();
    }

    public static final Creator<DoctorData> CREATOR = new Creator<DoctorData>() {
        @Override
        public DoctorData createFromParcel(Parcel in) {
            return new DoctorData(in);
        }

        @Override
        public DoctorData[] newArray(int size) {
            return new DoctorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(specialization);
    }
}
