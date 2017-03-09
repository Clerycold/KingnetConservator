package clery.kingnetconservator.app.kingnetconservator.PatrolActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.Control.BestBitmap.BitmapUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.FileUtils.FileUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/3/2.
 */

public class PatrolViewInside extends ScrollView{

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    RelativeLayout relativeLayout;
    Activity activity;
    Context context;

    private ArrayList<PatrolMapView> patrolMapViewArray = new ArrayList<>();

    String[] patrolString;

    FileUtils fileUtils;
    SaveData saveData;

    int[] patrolFinish;
    int tag;

    private AlertDialog build;

    public PatrolViewInside(Activity activity,Context context,String[] patrolString) {
        super(context);

        this.activity = activity;
        this.context = context;
        this.patrolString = patrolString;
        initView();
        xmlArrange();
    }

    private void initView(){

        FileUtils.initManager(context);
        fileUtils = FileUtils.getManager();

        SaveData.initManager(context);
        saveData = SaveData.getsaveData();

        relativeLayout = new RelativeLayout(context);
        this.addView(relativeLayout);
    }

    private void xmlArrange(){

        RelativeLayout.LayoutParams patrolMapView_lay;
        for(int i=0,j=patrolString.length;i<j;i++){
            PatrolMapView patrolMapView = new PatrolMapView(context);
            patrolMapView.setId(generateViewId());
            patrolMapView.setTag(i);
            patrolMapView_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/2 , ScreenWH.getScreenWidth() / 4);

            if(i == 0){
                patrolMapView.setPaintrectUp();
            }else{
                patrolMapView_lay.addRule(RelativeLayout.BELOW,patrolMapViewArray.get(i-1).getId());
                if(i+1 == j){
                    patrolMapView.setPaintrectDown();
                }
            }

            patrolMapView_lay.setMargins(ScreenWH.getScreenWidth()/3,0,0,0);
            patrolMapView.setLayoutParams(patrolMapView_lay);

            patrolMapView.setLocationText(patrolString[i]);
            patrolMapView.setOnClickListener(click);
            patrolMapViewArray.add(patrolMapView);
            relativeLayout.addView(patrolMapView);
        }
    }

    private View.OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View view) {
            tag = (int)view.getTag();

            patrolFinish = saveData.getIntArray(PatrolActivity.PATROLINTARRAY);

            if(patrolFinish[tag] == 0){
                StartPatrolActivity();
            }else{
                showOneDialog(tag);
            }

        }
    };
    public void ShowFinishTime(int position,String timeString){
        patrolMapViewArray.get(position).ShowFinishData(timeString);
    }
    public int getClickTag(){
        return tag;
    }

    public void FinishPatrol(int position){
        patrolMapViewArray.get(position).FinishPatrol();
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

    /***
     * 自訂AlertDialog畫面
     * @param position
     */
    private void showOneDialog(final int position) {
        build = new AlertDialog.Builder(activity).create();
        //自定义布局
        View view = activity.getLayoutInflater().inflate(R.layout.splash_dialog, null);
        //把自定义的布局设置到dialog中，注意，布局设置一定要在show之前。从第二个参数分别填充内容与边框之间左、上、右、下、的像素
        build.setView(view, 0, 0, 0, 0);
        //一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了
        build.show();
        //得到当前显示设备的宽度，单位是像素
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        //得到这个dialog界面的参数对象
        WindowManager.LayoutParams params = build.getWindow().getAttributes();
        //设置dialog的界面宽度
        params.width = width - (width / 6);
        //设置dialog高度为包裹内容
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的重心
        params.gravity = Gravity.CENTER;
        //dialog.getWindow().setLayout(width-(width/6), LayoutParams.WRAP_CONTENT);
        //用这个方法设置dialog大小也可以，但是这个方法不能设置重心之类的参数，推荐用Attributes设置
        //最后把这个参数对象设置进去，即与dialog绑定
        build.getWindow().setAttributes(params);
        build.setCancelable(false);

        RelativeLayout dialogbg = (RelativeLayout) view.findViewById(R.id.dialogbg);

        TextView dialogtitel = (TextView) view.findViewById(R.id.dialogtitel);
        Button dialogbtnright = (Button) view.findViewById(R.id.dialogbtnright);
        Button dialogbgbtnleft = (Button) view.findViewById(R.id.dialogbgbtnleft);

        RelativeLayout.LayoutParams dialogtitel_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtitel_lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        dialogtitel_lay.setMargins(ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX());
        dialogtitel.setLayoutParams(dialogtitel_lay);

        String[] stringLocation = saveData.getStringArryData(PatrolActivity.PARTROLLOCATION);

        dialogtitel.setText(stringLocation[position]);

        ImageView imageView = new ImageView(context);
        imageView.setId(generateViewId());
        imageView.setBackgroundColor(Color.BLACK);

        RelativeLayout.LayoutParams imageView_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth(),ScreenWH.getNoStatus_bar_Height(context)/10*7);
        imageView_lay.addRule(RelativeLayout.BELOW,dialogtitel.getId());
        imageView.setLayoutParams(imageView_lay);

        String[] stringPathArray = saveData.getStringArryData(PatrolActivity.PARTROLPICTUREPATH);
        final String stringPath = stringPathArray[position];

        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFd(stringPathArray[position],ScreenWH.getScreenWidth(),ScreenWH.getNoStatus_bar_Height(context));
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        dialogbg.addView(imageView);

        RelativeLayout.LayoutParams dialogbtnright_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogbtnright_lay.addRule(RelativeLayout.BELOW,imageView.getId());
        dialogbtnright_lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        dialogbtnright.setLayoutParams(dialogbtnright_lay);

        RelativeLayout.LayoutParams dialogbgbtnleft_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogbgbtnleft_lay.addRule(RelativeLayout.BELOW,imageView.getId());
        dialogbgbtnleft_lay.addRule(RelativeLayout.LEFT_OF,dialogbtnright.getId());
        dialogbgbtnleft.setLayoutParams(dialogbgbtnleft_lay);
        dialogbgbtnleft.setText("修改");

        View.OnClickListener dialogClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.dialogbtnright:

                        build.dismiss();

                        break;
                    case R.id.dialogbgbtnleft:

                        fileUtils.selectDeleteFiled(stringPath);
                        patrolMapViewArray.get(position).ClearFinishPatrol();
                        patrolMapViewArray.get(position).ClearFinishData();

                        StartPatrolActivity();

                        build.dismiss();
                        break;
                }
            }
        };

        dialogbtnright.setOnClickListener(dialogClick);
        dialogbgbtnleft.setOnClickListener(dialogClick);

    }

    private void StartPatrolActivity(){
        Intent intent = new Intent();
        intent.setClass(context, PatrolPictureActivity.class);
        activity.startActivityForResult(intent, PatrolActivity.ACTION_FLAG);
    }
}
