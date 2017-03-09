package clery.kingnetconservator.app.kingnetconservator.OkPackActivity.View.UIpattern;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.Control.BestBitmap.BitmapUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;

/**
 * Created by clery on 2017/2/24.
 */

public class Okadapter_ThreeText extends RelativeLayout{

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    TextView transport_code;
    TextView pictureurl;
    TextView postal;
    TextView p_name;

    Context context;

    ImageView imageView;

    String pictureuri;

    public Okadapter_ThreeText(Context context) {
        super(context);

        this.context = context;

        initView();
        xmlArrange();
    }

    private void initView(){

        transport_code = new TextView(context);
        transport_code.setId(generateViewId());
        pictureurl = new TextView(context);
        postal = new TextView(context);
        postal.setId(generateViewId());
        p_name = new TextView(context);
        imageView = new ImageView(context);

    }

    private void xmlArrange(){

        RelativeLayout.LayoutParams transport_code_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        transport_code.setLayoutParams(transport_code_lay);
        transport_code.setTextColor(Color.BLACK);

        addView(transport_code);

        RelativeLayout.LayoutParams pictureurl_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pictureurl_lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pictureurl_lay.setMargins(0,0,ScreenWH.getUISpacingX(),0);
        pictureurl.setLayoutParams(pictureurl_lay);
        pictureurl.setText("簽名檔案連結");
        pictureurl.setTextColor(Color.BLUE);
        pictureurl.setOnClickListener(click);

        addView(pictureurl);

        RelativeLayout.LayoutParams postal_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        postal_lay.addRule(RelativeLayout.BELOW,transport_code.getId());
        postal.setLayoutParams(postal_lay);
        postal.setTextColor(Color.BLACK);

        addView(postal);

        RelativeLayout.LayoutParams p_name_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p_name_lay.addRule(RelativeLayout.BELOW,transport_code.getId());
        p_name_lay.addRule(RelativeLayout.RIGHT_OF,postal.getId());
        p_name_lay.setMargins(ScreenWH.getUISpacingX(),0,0,0);
        p_name.setLayoutParams(p_name_lay);
        p_name.setTextColor(Color.BLACK);

        addView(p_name);

        RelativeLayout.LayoutParams imageView_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(imageView_lay);
        imageView.setVisibility(GONE);
        imageView.setOnClickListener(imgclick);
        imageView.setBackgroundColor(Color.WHITE);

        addView(imageView);
    }

    public void setPictureuri(String pictureuri) {
        this.pictureuri = pictureuri;
    }

    public void setTransport_codeText(String s){
        transport_code.setText("條碼編號："+s);
    }

    public void setPostalText(String s,String s1){
        if(s1.equals("無")){
            postal.setText("包裹："+s);
        }else{
            postal.setText("包裹："+s+" - "+s1);
        }
    }

    public void setP_nameText(String s){
        p_name.setText("收件人："+s);
    }

    private View.OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View view) {

            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFd(pictureuri,ScreenWH.getScreenWidth(),ScreenWH.getNoStatus_bar_Height(getContext())/10*7);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(VISIBLE);

        }
    };
    private View.OnClickListener imgclick = new OnClickListener() {
        @Override
        public void onClick(View view) {

            imageView.setVisibility(GONE);
        }
    };

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
