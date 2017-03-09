package clery.kingnetconservator.app.kingnetconservator.PackageActivity.UIpattern;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackNewData;

/**
 * Created by clery on 2017/2/18.
 */

public class CheckTabletUi extends ScrollView{

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private Context context;

    private RelativeLayout scrollview_r;

    private ArrayList<LinearHTextCheckBox> linearHTextCheckArray = new ArrayList<>();

    private List<PackNewData> packNewDatalist;
    /**
     * 點選的用戶位置
     */
    private int position;

    public CheckTabletUi(Context context, List<PackNewData> packNewDatalist,int position) {
        super(context);

        this.context = context;
        this.packNewDatalist = packNewDatalist;
        this.position = position;
        initView();
        xmlArrange();

    }

    private void initView(){

        scrollview_r = new RelativeLayout(context);
        this.addView(scrollview_r);

    }

    private void xmlArrange(){

        RelativeLayout.LayoutParams linearHTextCheckBox_lay;

        List<String> postal_type = packNewDatalist.get(position).getPostal_type();
        List<String> P_name = packNewDatalist.get(position).getP_name();
        List<String> transport_code = packNewDatalist.get(position).getTransport_code();
        List<String> postal_logistics = packNewDatalist.get(position).getPostal_logistics();
        List<String> is_private = packNewDatalist.get(position).getIs_private();
        List<String> p_note = packNewDatalist.get(position).getP_note();

        for(int i=0,j=postal_type.size();i<j;i++){
            LinearHTextCheckBox linearHTextCheckBox = new LinearHTextCheckBox(context);
            linearHTextCheckBox.setTag(i);
            linearHTextCheckBox.setId(generateViewId());
            linearHTextCheckBox_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i >0) {
                linearHTextCheckBox_lay.addRule(RelativeLayout.BELOW, linearHTextCheckArray.get(i - 1).getId());
            }
            linearHTextCheckBox.setTextView("條碼："+transport_code.get(i)+"\n"+
                    "類型："+postal_type.get(i)+Checklogistics(postal_logistics.get(i))+"\n"+
                    "收件人："+P_name.get(i)+"\n" +
                    Checkp_note(p_note.get(i))) ;
            linearHTextCheckBox.setTextColor(is_private.get(i));
            linearHTextCheckArray.add(linearHTextCheckBox);
            linearHTextCheckBox.setLayoutParams(linearHTextCheckBox_lay);

            scrollview_r.addView(linearHTextCheckBox);
            scrollview_r.setBackgroundColor(Color.BLACK);
        }
    }
    //檢查包裹是否有物流公司
    private String  Checklogistics(String s){
        if(s.equals("無")){
            return "";
        }else return "-"+s;
    }
    //檢查備註是否有資料
    private String Checkp_note(String s){
        if(s.equals("")){
           return "";
        }else{
            return "備註："+s;
        }
    }
    public int[] CheckBoxSelected(){
        int[] checkBoxSelected = new int[linearHTextCheckArray.size()];

        for(int i=0,j=linearHTextCheckArray.size();i<j;i++){
            if(linearHTextCheckArray.get(i).CheckBoxSelected()){
                checkBoxSelected[i] = 1;
            }else{
                checkBoxSelected[i] = 0;
            }
        }
        return checkBoxSelected;
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
