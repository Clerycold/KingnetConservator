package clery.kingnetconservator.app.kingnetconservator.PackageActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Backbutton;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Titledesign;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.UIpattern.CheckTabletUi;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.UIpattern.ListEmptyView;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.View.PackListShow;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackData;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackDataArrange;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackNewData;
import clery.kingnetconservator.app.kingnetconservator.R;
import clery.kingnetconservator.app.kingnetconservator.OkPackActivity.SQLite.OkPackSQData;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.SQLite.PackFieldName;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.SQLite.PackSQData;
import clery.kingnetconservator.app.kingnetconservator.SignFeatures.SignActivity;

/**
 * Created by clery on 2017/2/17.
 */

public class PackActivity extends Activity implements PackListShow {

    private static String TAG = "PackActivity";
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static final int ACTION_FLAG = 1;

    private Titledesign packagetitle;
    private Backbutton backbutton;

    /**
     * ListView物件 ListEmptyView空值時顯示的元件
     */
    private ListView packlist;
    private PackAdapter packAdapter;
    private ListEmptyView listEmptyView;
    /**
     * Alert元件
     */
    private android.app.AlertDialog dialog;

    /**
     * 點選的用戶資料位置
     */
    private int table_note_i;
    /**
     * 整理後的資料
     */
    private String[] TabletNote;
    private List<PackNewData> packNewDatalist;
    /**
     * checktableui 自定義的CheckBox
     */
    private CheckTabletUi checkTabletUi;
    private int[] checkBoxSelected;
    /**
     * packSQData 未送達資料 okPackSQData已送達資料
     */
    private PackSQData packSQData;
    private OkPackSQData okPackSQData;

    private AlertDialog build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        initView();
        xmlArrange();
        setData();
        setViewListener();
    }

    private void initView() {

        packagetitle = (Titledesign) findViewById(R.id.packagetitle);
        backbutton = (Backbutton) findViewById(R.id.backbutton);

        packlist = (ListView) findViewById(R.id.packlist);
        listEmptyView = new ListEmptyView(getApplicationContext(), this);
        listEmptyView.setVisibility(View.GONE);
        ((ViewGroup) packlist.getParent()).addView(listEmptyView);
        packlist.setEmptyView(listEmptyView);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);   //得到一个LayoutAnimationController对象；
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);   //设置控件显示间隔时间；
        controller.setDelay(0.3f);   //为ListView设置LayoutAnimationController属性；
        packlist.setLayoutAnimation(controller);
        packlist.startLayoutAnimation();

    }

    private void xmlArrange() {

        RelativeLayout.LayoutParams backbutton_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth() / 10, ScreenWH.getScreenWidth() / 10);
        backbutton_lay.setMargins(ScreenWH.getUISpacingY(), (int) (ScreenWH.getUISpacingY() * 1.3), 0, 0);
        backbutton.setLayoutParams(backbutton_lay);

        RelativeLayout.LayoutParams listEmptyView_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listEmptyView_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        listEmptyView_lay.addRule(RelativeLayout.CENTER_VERTICAL);
        listEmptyView.setLayoutParams(listEmptyView_lay);

    }

    private void setData() {

        PackSQData.initManager(getApplicationContext());
        packSQData = PackSQData.getManager();

        OkPackSQData.initManager(getApplicationContext());
        okPackSQData = OkPackSQData.getManager();

        packNewDatalist = PackDataArrange.getPackNewDataList();
        TabletNote = PackDataArrange.getTabletNoteOk();

        packagetitle.setTextView("選取住戶");

        if (TabletNote != null && packNewDatalist != null) {
            packAdapter = new PackAdapter(getApplicationContext(), TabletNote, packNewDatalist);
            packlist.setAdapter(packAdapter);
        }
    }

    private void setViewListener() {

        backbutton.setOnTouchListener(Touch);
        packlist.setOnItemClickListener(onItemClickListener);

    }

    private View.OnTouchListener Touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.backbutton:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            backbutton.setPaintColor(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            backbutton.setPaintColor(false);
                            finish();
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            packAdapter.setSelectedPosition(i);

            table_note_i = i;

            checkTabletUi = new CheckTabletUi(getApplicationContext(), packNewDatalist, i);
            checkTabletUi.setId(generateViewId());

            if (packNewDatalist.get(i).getPostal_id().size() != 0) {
                showOneDialog("包裹用戶：" + PackDataArrange.getTableNotePosition(i),packNewDatalist.get(i).getPostal_id().size());
            } else {
                RemindToast.showText(getApplicationContext(), "尚無須送包裹");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (packAdapter != null) {
            packAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void packListShow() {
        if (PackDataArrange.getTabletNoteOk() != null && PackDataArrange.getTabletNoteOk() != null) {
            packAdapter = new PackAdapter(getApplicationContext(), PackDataArrange.getTabletNoteOk(), PackDataArrange.getPackNewDataList());
            packlist.setAdapter(packAdapter);
            packAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doNext(requestCode, data);
    }

    //刪除確認包裹的資料
    private void doNext(int requestCode, Intent data) {
        if (ACTION_FLAG == requestCode && data != null) {
            List<String> tempTransport = new ArrayList<>();
            PackData packData;
            boolean result = data.getExtras().getBoolean("SING_FINSH");
            String picturel = data.getExtras().getString("PICTURE");
            if (result) {
                int x = 0;
                for (int i = 0, j = checkBoxSelected.length; i < j; i++) {
                    if (checkBoxSelected[i] == 1) {
                        tempTransport.add(packNewDatalist.get(table_note_i).getTransport_code().get(i - x));

                        packData = new PackData();
                        packData.setP_note(packNewDatalist.get(table_note_i).getP_note().get(i - x));
                        packData.setCreate_date(packNewDatalist.get(table_note_i).getCreate_date().get(i - x));
                        packData.setIs_private(packNewDatalist.get(table_note_i).getIs_private().get(i - x));
                        packData.setTransport_code(packNewDatalist.get(table_note_i).getTransport_code().get(i - x));
                        packData.setPostal_type(packNewDatalist.get(table_note_i).getPostal_type().get(i - x));
                        packData.setPostal_logistics(packNewDatalist.get(table_note_i).getPostal_logistics().get(i - x));
                        packData.setP_name(packNewDatalist.get(table_note_i).getP_name().get(i - x));
                        packData.setPostal_id(packNewDatalist.get(table_note_i).getPostal_id().get(i - x));
                        packData.setTablet_note(TabletNote[table_note_i]);
                        packData.setPicture_Url(picturel);

                        packNewDatalist.get(table_note_i).getP_note().remove(i - x);
                        packNewDatalist.get(table_note_i).getCreate_date().remove(i - x);
                        packNewDatalist.get(table_note_i).getIs_private().remove(i - x);
                        packNewDatalist.get(table_note_i).getTransport_code().remove(i - x);
                        packNewDatalist.get(table_note_i).getPostal_type().remove(i - x);
                        packNewDatalist.get(table_note_i).getPostal_logistics().remove(i - x);
                        packNewDatalist.get(table_note_i).getP_name().remove(i - x);
                        packNewDatalist.get(table_note_i).getPostal_id().remove(i - x);

                        okPackSQData.insert(packData);
                        x++;
                    }
                }
                packSQData.delete(PackFieldName.TRANSPORT_CODE, tempTransport);
            }
        }
    }

    /***
     * 自訂AlertDialog畫面
     * @param title
     * @param count
     */
    private void showOneDialog(String title,int count) {
        build = new AlertDialog.Builder(this).create();
        //自定义布局
        View view = getLayoutInflater().inflate(R.layout.splash_dialog, null);
        //把自定义的布局设置到dialog中，注意，布局设置一定要在show之前。从第二个参数分别填充内容与边框之间左、上、右、下、的像素
        build.setView(view, 0, 0, 0, 0);
        //一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了
        build.show();
        //得到当前显示设备的宽度，单位是像素
        int width = getWindowManager().getDefaultDisplay().getWidth();
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
        dialogbg.addView(checkTabletUi);

        TextView dialogtitel = (TextView) view.findViewById(R.id.dialogtitel);
        Button dialogbtnright = (Button) view.findViewById(R.id.dialogbtnright);
        Button dialogbgbtnleft = (Button) view.findViewById(R.id.dialogbgbtnleft);

        RelativeLayout.LayoutParams dialogtitel_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtitel_lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        dialogtitel_lay.setMargins(ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX());
        dialogtitel.setLayoutParams(dialogtitel_lay);

        dialogtitel.setText(title);

        RelativeLayout.LayoutParams checkTabletUi_lay;
        if(count>5){
            checkTabletUi_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10*7);
        }else{
            checkTabletUi_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        checkTabletUi_lay.addRule(RelativeLayout.BELOW,dialogtitel.getId());
        checkTabletUi.setLayoutParams(checkTabletUi_lay);

        RelativeLayout.LayoutParams dialogbtnright_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogbtnright_lay.addRule(RelativeLayout.BELOW,checkTabletUi.getId());
        dialogbtnright_lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        dialogbtnright.setLayoutParams(dialogbtnright_lay);

        RelativeLayout.LayoutParams dialogbgbtnleft_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogbgbtnleft_lay.addRule(RelativeLayout.BELOW,checkTabletUi.getId());
        dialogbgbtnleft_lay.addRule(RelativeLayout.LEFT_OF,dialogbtnright.getId());
        dialogbgbtnleft.setLayoutParams(dialogbgbtnleft_lay);

        dialogbtnright.setOnClickListener(dialogClick);
        dialogbgbtnleft.setOnClickListener(dialogClick);

    }
    private View.OnClickListener dialogClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.dialogbtnright:

                    build.dismiss();

                    StringBuffer makeSureTable = new StringBuffer(TabletNote[table_note_i] + "\n");
                    checkBoxSelected = checkTabletUi.CheckBoxSelected();
                    for (int x = 0, y = checkBoxSelected.length; x < y; x++) {
                        if (checkBoxSelected[x] == 1) {
                            makeSureTable.append(packNewDatalist.get(table_note_i).getTransport_code().get(x) + "\n");
                        }
                    }

                    Intent intent = new Intent();
                    intent.setClass(PackActivity.this, SignActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("makeSureTable", makeSureTable.toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, ACTION_FLAG);

                    break;
                case R.id.dialogbgbtnleft:
                    build.dismiss();
                    RemindToast.showText(getApplicationContext(), "簽收包裹取消");
                    break;
            }
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
