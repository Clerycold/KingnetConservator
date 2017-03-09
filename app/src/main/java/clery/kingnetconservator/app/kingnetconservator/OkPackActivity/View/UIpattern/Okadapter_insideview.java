package clery.kingnetconservator.app.kingnetconservator.OkPackActivity.View.UIpattern;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackNewData;

/**
 * Created by clery on 2017/2/24.
 */

public class Okadapter_insideview extends RelativeLayout{

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    List<PackNewData> packNewDataList;
    int position;
    private ArrayList<Okadapter_ThreeText> Okadapter_ThreeTextArray = new ArrayList<>();
    Context context;

    public Okadapter_insideview(Context context, List<PackNewData> packNewDataList,int position) {
        super(context);

        this.context = context;
        this.packNewDataList = packNewDataList;
        this.position = position;
        xmlArrange();
    }

    private void xmlArrange(){

        RelativeLayout.LayoutParams okadapter_three_lay ;
        Okadapter_ThreeText okadapter_threeText;
        for(int i=0,j=packNewDataList.get(position).getPostal_id().size();i<j;i++){
            okadapter_threeText = new Okadapter_ThreeText(context);
            okadapter_threeText.setTag(i);
            okadapter_threeText.setId(generateViewId());

            okadapter_three_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i>0){
                okadapter_three_lay.addRule(RelativeLayout.BELOW,Okadapter_ThreeTextArray.get(i-1).getId());
            }
            okadapter_three_lay.setMargins(ScreenWH.getUISpacingX(),0,ScreenWH.getUISpacingX(),0);
            okadapter_threeText.setLayoutParams(okadapter_three_lay);
            okadapter_threeText.setTransport_codeText(packNewDataList.get(position).getTransport_code().get(i));
            okadapter_threeText.setPostalText(packNewDataList.get(position).getPostal_type().get(i),packNewDataList.get(position).getPostal_logistics().get(i));
            okadapter_threeText.setP_nameText(packNewDataList.get(position).getP_name().get(i));
            Okadapter_ThreeTextArray.add(okadapter_threeText);

            okadapter_threeText.setPictureuri(packNewDataList.get(position).getPictureUrl().get(i));

            addView(okadapter_threeText);
        }
    }

    //得到ViewId
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

}
