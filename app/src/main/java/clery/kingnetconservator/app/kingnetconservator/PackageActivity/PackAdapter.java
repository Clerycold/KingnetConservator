package clery.kingnetconservator.app.kingnetconservator.PackageActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackNewData;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/2/17.
 */

public class PackAdapter extends BaseAdapter{

    private Context context;
    List<PackNewData> packNewDatalist;
    private String[] tablet_note;
    private LayoutInflater infalInflater;
    private int selectedPos;
    private boolean isFirst = true;

    private Animation push_left_in,push_right_in;

    PackAdapter(Context context,String[] tablet_note,List<PackNewData> packNewDatalist){
        this.context=context;
        this.packNewDatalist = packNewDatalist;
        this.tablet_note=tablet_note;
        infalInflater=(LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        push_left_in= AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        push_right_in=AnimationUtils.loadAnimation(context, R.anim.slide_in_right);

    }

    @Override
    public int getCount(){
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
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            convertView=infalInflater.inflate(R.layout.packadapter_view,null);
            holder=new ViewHolder(convertView);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        RelativeLayout.LayoutParams tablet_note_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/2,
                ScreenWH.getNoStatus_bar_Height(context)/10);
        tablet_note_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tablet_note_lay.setMargins(0,ScreenWH.getUISpacingY(),ScreenWH.getUISpacingY(),ScreenWH.getUISpacingY());
        holder.tablet_note.setLayoutParams(tablet_note_lay);

        holder.tablet_note.setText(tablet_note[position]);

        if(selectedPos == position && !isFirst){
            holder.tablet_note.setSelected(true);
            holder.tablet_note.setTextColor(Color.WHITE);
        }else{
            holder.tablet_note.setSelected(false);
            holder.tablet_note.setTextColor(Color.BLACK);
        }

        RelativeLayout.LayoutParams text_count_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        text_count_lay.addRule(RelativeLayout.RIGHT_OF,holder.tablet_note.getId());
        text_count_lay.addRule(RelativeLayout.CENTER_VERTICAL);
        holder.text_count.setLayoutParams(text_count_lay);
        if(packNewDatalist != null && packNewDatalist.size() != 0){
            holder.text_count.setText("數量："+Integer.toString(packNewDatalist.get(position).getPostal_id().size()));
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tablet_note;
        TextView text_count;

        public ViewHolder(View view){
            tablet_note = (TextView) view.findViewById(R.id.tablet_noteText);
            text_count = (TextView) view.findViewById(R.id.text_count);
        }
    }

    public void setSelectedPosition(int pos) {
        selectedPos = pos;
        isFirst = false;
        // inform the view of this change
        notifyDataSetChanged();
    }
}
