package id.developer.rs_thamrin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.model.MenuItemModel;

public class HomeAdapter extends ArrayAdapter<MenuItemModel> {

    public HomeAdapter(Context context, ArrayList<MenuItemModel> menuItemModel) {
        super(context,0, menuItemModel);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItemModel menuItemModel = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, parent, false);
        }

        ImageView icon = (ImageView)convertView.findViewById(R.id.card_image);
        TextView title = (TextView)convertView.findViewById(R.id.card_title);

        Picasso.get()
                .load(menuItemModel.getImageSource())
                .into(icon);

        title.setText(menuItemModel.getTitle());

        return convertView;
    }
}
