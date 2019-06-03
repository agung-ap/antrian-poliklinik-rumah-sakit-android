package id.developer.rs_thamrin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.model.master.TypeOfIdentityCard;


public class TypeOfIdentityCardAdapter extends RecyclerView.Adapter<TypeOfIdentityCardAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<TypeOfIdentityCard> typeOfIdentityCardList;
    private DataListener listener;

    public TypeOfIdentityCardAdapter(Context context, DataListener listener) {
        this.context = context;
        this.listener = listener;
        typeOfIdentityCardList = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.master_data_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.name.setText(typeOfIdentityCardList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return typeOfIdentityCardList == null ? 0 : typeOfIdentityCardList.size();
    }

    public void setData(ArrayList<TypeOfIdentityCard> typeOfIdentityCardList) {
        this.typeOfIdentityCardList = typeOfIdentityCardList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(typeOfIdentityCardList.get(getAdapterPosition()));
        }
    }

    public interface DataListener{
        void onClick(TypeOfIdentityCard dataPosition);
    }
}
