package clery.kingnetconservator.app.kingnetconservator.TourActivity;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by clery on 2017/2/16.
 */

public class TourPagerAdapter extends PagerAdapter {

    private ArrayList<ViewGroup> pageList= new ArrayList<ViewGroup>();
    public int position;

    private int tempsize;

    @Override
    public int getCount() {
        tempsize = pageList.size();
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView((View) pageList.get(position));
        this.position=position;
        return pageList.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    public void addPagelist(ViewGroup pageView){
        pageList.add(pageView);

    }
    public int getPosition(){
        return this.position;
    }
}