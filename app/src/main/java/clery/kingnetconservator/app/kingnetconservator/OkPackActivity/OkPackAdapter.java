package clery.kingnetconservator.app.kingnetconservator.OkPackActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.OkPackActivity.View.UIpattern.Okadapter_insideview;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackNewData;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/2/23.
 */

public class OkPackAdapter extends BaseAdapter {

    private LayoutInflater infalInflater;

    private List<PackNewData> packNewDataList;
    private String[] tablet_note;
    private Context context;

    OkPackAdapter(Context context,String[] tablet_note,List<PackNewData> packNewDatalist){

        this.context = context;
        this.packNewDataList = packNewDatalist;
        this.tablet_note = tablet_note;

        infalInflater=(LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return (tablet_note!=null ? tablet_note.length:0);
    }

    @Override
    public Object getItem(int position) {
        return tablet_note[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final OkPackAdapter.ViewHolder holder;

        if (convertView == null) {

            convertView=infalInflater.inflate(R.layout.okpackadapter_view,null);
            holder=new OkPackAdapter.ViewHolder();

            holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.relative);
            holder.oktable_note = (TextView)convertView.findViewById(R.id.oktable_note);
            holder.okadapterInsideview = new Okadapter_insideview(context,packNewDataList,position);

            convertView.setTag(holder);
        }else{
            holder=(OkPackAdapter.ViewHolder)convertView.getTag();
        }

        RelativeLayout.LayoutParams oktable_note_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenWH.getNoStatus_bar_Height(context)/15);
        oktable_note_lay.setMargins(ScreenWH.getUISpacingX(),0,ScreenWH.getUISpacingX(),0);
        holder.oktable_note.setLayoutParams(oktable_note_lay);
        holder.oktable_note.setText(tablet_note[position]);

        RelativeLayout.LayoutParams okadapterinsideview_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        okadapterinsideview_lay.addRule(RelativeLayout.BELOW,holder.oktable_note.getId());
        holder.okadapterInsideview.setLayoutParams(okadapterinsideview_lay);

        if(holder.okadapterInsideview.getParent()!=null){
            ((ViewGroup)holder.okadapterInsideview.getParent()).removeView(holder.okadapterInsideview);
        }
        holder.relativeLayout.addView(holder.okadapterInsideview);

        return convertView;
    }

    static class ViewHolder{
        RelativeLayout relativeLayout;
        TextView oktable_note;
        Okadapter_insideview okadapterInsideview;
    }
}
