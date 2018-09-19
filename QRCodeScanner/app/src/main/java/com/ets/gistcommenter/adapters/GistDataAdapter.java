package com.ets.gistcommenter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ets.gistcommenter.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class GistDataAdapter extends BaseAdapter{

    private Context context;
    private JSONArray gistdata;
    private LayoutInflater inflater=null;
    public GistDataAdapter(Context context, JSONArray gistdata) {
        this.context=context;
        this.gistdata=gistdata;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gistdata.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject obj = null;
        View vi=convertView;
        try {
            obj=gistdata.getJSONObject(position);
            if(convertView==null)
             vi = inflater.inflate(R.layout.gist_detail_adapter, null);

            TextView idfilename = (TextView)vi.findViewById(R.id.idfilename);
            TextView idcontaent = (TextView)vi.findViewById(R.id.idcontaent);
            idfilename.setText(""+obj.getString("filename"));
            idcontaent.setText(""+obj.getString("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vi;
    }
}
