package id.developer.rs_thamrin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.model.MenuItemModel;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<MenuItemModel> menuItemModelList;
    private Listener listener;

    public HomeAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
        menuItemModelList = new ArrayList<>();
    }

    public void setData(ArrayList<MenuItemModel> menuItemModelList) {
        this.menuItemModelList = menuItemModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.title.setText(menuItemModelList.get(i).getTitle());
        viewHolder.icon.setImageResource(menuItemModelList.get(i).getImageSource());
    }

    @Override
    public int getItemCount() {
        return menuItemModelList == null ? 0 : menuItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.card_image);
            title = (TextView)itemView.findViewById(R.id.card_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(menuItemModelList.get(getAdapterPosition()));
        }
    }

    public interface Listener{
        void onClick(MenuItemModel dataPosition);
    }
}
