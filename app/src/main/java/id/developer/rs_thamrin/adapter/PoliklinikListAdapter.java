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
import id.developer.rs_thamrin.model.MenuItemModel;
import id.developer.rs_thamrin.model.Poliklinik;

public class PoliklinikListAdapter extends RecyclerView.Adapter<PoliklinikListAdapter.ViewHolder> {
    private Context context;
    private List<Poliklinik> poliklinikList;
    private Listener listener;

    public PoliklinikListAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
        poliklinikList = new ArrayList<>();
    }

    public void setData(List<Poliklinik> poliklinikList) {
        this.poliklinikList = poliklinikList;
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
        viewHolder.title.setText(poliklinikList.get(i).getPoliklinikName());
        viewHolder.kuota.setText("Kuota : " + poliklinikList.get(i).getKuota());
    }

    @Override
    public int getItemCount() {
        return poliklinikList == null ? 0 : poliklinikList.size();
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
            listener.onClick(poliklinikList.get(getAdapterPosition()));
        }
    }

    public interface Listener{
        void onClick(Poliklinik dataPosition);
    }
}
