package id.developer.rs_thamrin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Poliklinik implements Parcelable {
       private int id;
       private int scheduleId;
       private int poliklinikNameId;
       private int doctorId;
       private String code;
       private String schedule;
       private String time;
       private String doctorName;
       private String poliklinikName;
       private int kuota;

       public Poliklinik() {

       }

       public int getId() {
              return id;
       }

       public void setId(int id) {
              this.id = id;
       }

       public int getScheduleId() {
              return scheduleId;
       }

       public void setScheduleId(int scheduleId) {
              this.scheduleId = scheduleId;
       }

       public int getPoliklinikNameId() {
              return poliklinikNameId;
       }

       public void setPoliklinikNameId(int poliklinikNameId) {
              this.poliklinikNameId = poliklinikNameId;
       }

       public int getDoctorId() {
              return doctorId;
       }

       public void setDoctorId(int doctorId) {
              this.doctorId = doctorId;
       }

       public String getCode() {
              return code;
       }

       public void setCode(String code) {
              this.code = code;
       }

       public String getSchedule() {
              return schedule;
       }

       public void setSchedule(String schedule) {
              this.schedule = schedule;
       }

       public String getTime() {
              return time;
       }

       public void setTime(String time) {
              this.time = time;
       }

       public String getDoctorName() {
              return doctorName;
       }

       public void setDoctorName(String doctorName) {
              this.doctorName = doctorName;
       }

       public String getPoliklinikName() {
              return poliklinikName;
       }

       public void setPoliklinikName(String poliklinikName) {
              this.poliklinikName = poliklinikName;
       }

       public int getKuota() {
              return kuota;
       }

       public void setKuota(int kuota) {
              this.kuota = kuota;
       }

       public static Creator<Poliklinik> getCREATOR() {
              return CREATOR;
       }

       protected Poliklinik(Parcel in) {
              id = in.readInt();
              scheduleId = in.readInt();
              poliklinikNameId = in.readInt();
              doctorId = in.readInt();
              code = in.readString();
              schedule = in.readString();
              time = in.readString();
              doctorName = in.readString();
              poliklinikName = in.readString();
              kuota = in.readInt();
       }

       public static final Creator<Poliklinik> CREATOR = new Creator<Poliklinik>() {
              @Override
              public Poliklinik createFromParcel(Parcel in) {
                     return new Poliklinik(in);
              }

              @Override
              public Poliklinik[] newArray(int size) {
                     return new Poliklinik[size];
              }
       };

       @Override
       public int describeContents() {
              return 0;
       }

       @Override
       public void writeToParcel(Parcel dest, int flags) {
              dest.writeInt(id);
              dest.writeInt(scheduleId);
              dest.writeInt(poliklinikNameId);
              dest.writeInt(doctorId);
              dest.writeString(code);
              dest.writeString(schedule);
              dest.writeString(time);
              dest.writeString(doctorName);
              dest.writeString(poliklinikName);
              dest.writeInt(kuota);
       }
}
