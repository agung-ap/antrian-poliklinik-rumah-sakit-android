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
import id.developer.rs_thamrin.model.AdminData;

public class AdminListAdapter extends RecyclerView.Adapter<AdminListAdapter.ViewHolder> {
    private Context context;
    private List<AdminData> adminDataList;
    private Listener listener;
    public AdminListAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
        adminDataList = new ArrayList<>();
    }

    public void setData(List<AdminData> adminDataList) {
        this.adminDataList = adminDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.poliklinik_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.title.setText("Nama " + adminDataList.get(i).getName());
        viewHolder.kuota.setText("user Id : " + adminDataList.get(i).getUserId());
    }

    @Override
    public int getItemCount() {
        return adminDataList == null ? 0 : adminDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, kuota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            kuota = (TextView) itemView.findViewById(R.id.kuota);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(adminDataList.get(getAdapterPosition()));
//        }
        }
    }

    public interface Listener{
        void onClick(AdminData dataPositions);
    }

}
