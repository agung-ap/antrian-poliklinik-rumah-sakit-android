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
import id.developer.rs_thamrin.model.response.QueueListResponse;

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.ViewHolder> {
    private Context context;
    private List<QueueListResponse> queueList;
    private Listener listener;

    public QueueListAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
        queueList = new ArrayList<>();
    }

    public void setData(List<QueueListResponse> queueList) {
        this.queueList = queueList;
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
        viewHolder.title.setText("Pasien " + queueList.get(i).getUsername());
        viewHolder.kuota.setText("Poliklinik " + queueList.get(i).getPoliklinik());
    }

    @Override
    public int getItemCount() {
        return queueList == null ? 0 : queueList.size();
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
            listener.onClick(queueList.get(getAdapterPosition()));
        }
    }

    public interface Listener{
        void onClick(QueueListResponse dataPosition);
    }
}
