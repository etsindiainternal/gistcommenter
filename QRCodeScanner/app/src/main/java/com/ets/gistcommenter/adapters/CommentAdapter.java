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

import util.DateTimeFormats;

public class CommentAdapter extends BaseAdapter{
    private Context context;
    private JSONArray commentaray;
    private LayoutInflater inflater=null;

    public CommentAdapter(Context context, JSONArray commentaray) {
        this.context=context;
        this.commentaray=commentaray;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return commentaray.length();
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
            obj=commentaray.getJSONObject(position);
        if(convertView==null)
        vi = inflater.inflate(R.layout.comment_adapter, null);

        TextView txtname = (TextView)vi.findViewById(R.id.cmtername);
        TextView txtcmton = (TextView)vi.findViewById(R.id.cmtondate);
        TextView txtcmtbody = (TextView)vi.findViewById(R.id.cmtbody);
        txtname.setText(""+obj.getJSONObject("user").getString("login").trim());
        txtcmtbody.setText(""+obj.getString("body"));
        txtcmton.setText("commented "+new DateTimeFormats().isCommentedAgo(obj.getString("updated_at")).trim());

        } catch (Exception e) {
            e.printStackTrace();
        }

         return vi;
    }
}
