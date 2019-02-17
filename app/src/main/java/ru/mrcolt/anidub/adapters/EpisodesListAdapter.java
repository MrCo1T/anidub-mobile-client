package ru.mrcolt.anidub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.mrcolt.anidub.R;
import ru.mrcolt.anidub.models.EpisodesListModel;

public class EpisodesListAdapter extends BaseAdapter {

    private ArrayList<EpisodesListModel> listDataModel;
    private LayoutInflater inflate;

    public EpisodesListAdapter(Context context, ArrayList<EpisodesListModel> mediaEpisodesListModels) {
        this.listDataModel = mediaEpisodesListModels;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listDataModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listDataModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        EpisodesListModel objDataModel = (EpisodesListModel) getItem(position);
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_episodes_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.episode_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(objDataModel.getTitle());
        return convertView;
    }

    public class ViewHolder {
        TextView title;
    }
}