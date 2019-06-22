package id.developer.rs_thamrin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.model.master.DoctorData;

public class DoctorListAdapter  extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {
    private Context context;
    private List<DoctorData> doctorDataList;
    private Listener listener;

    public DoctorListAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
        doctorDataList = new ArrayList<>();
    }

    public void setData(List<DoctorData> doctorDataList) {
        this.doctorDataList = doctorDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.poliklinik_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.title.setText("Dokter " + doctorDataList.get(i).getName());
        viewHolder.kuota.setText("Spesialisasi " + doctorDataList.get(i).getSpecialization());
    }

    @Override
    public int getItemCount() {
        return doctorDataList == null ? 0 : doctorDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, kuota;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            kuota = (TextView)itemView.findViewById(R.id.kuota);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(doctorDataList.get(getAdapterPosition()));
        }
    }

    public interface Listener{
        void onClick(DoctorData dataPosition);
    }
}
