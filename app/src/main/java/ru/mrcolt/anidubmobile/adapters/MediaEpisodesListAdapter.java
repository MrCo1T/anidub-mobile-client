package ru.mrcolt.anidubmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.mrcolt.anidubmobile.R;
import ru.mrcolt.anidubmobile.models.MediaEpisodesListModel;

public class MediaEpisodesListAdapter extends BaseAdapter {

    ArrayList<MediaEpisodesListModel> listDataModel;

    LayoutInflater minflate;

    public MediaEpisodesListAdapter(Context context, ArrayList<MediaEpisodesListModel> mediaEpisodesListModels) {
        this.listDataModel = mediaEpisodesListModels;
        this.minflate = LayoutInflater.from(context);
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

        MediaEpisodesListModel objDataModel = (MediaEpisodesListModel) getItem(position);
        if (convertView == null) {
            convertView = minflate.inflate(R.layout.item_media_episodes_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.episode_title);
            viewHolder.services = convertView.findViewById(R.id.episode_services);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(objDataModel.getTitle());
        viewHolder.services.setText(objDataModel.getServices());
        return convertView;
    }

    public class ViewHolder {
        TextView title, services;
    }
}