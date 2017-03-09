package clery.kingnetconservator.app.kingnetconservator.Control.UIpattern;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;

/**
 * Created by clery on 2017/2/15.
 */

public class EditAnimation extends RelativeLayout implements View.OnFocusChangeListener{

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    TextView textView,  textViewEmpty;
    EditText editText;
    Context context;

    int Width;
    int Height;

    AnimationSet animationStart;
    AnimationSet animationBack;

    SaveData saveData;

    boolean isfirst;

    public EditAnimation(Context context) {
        super(context);

        this.context = context;
        initView();

    }

    public EditAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initView();
    }

    private void initView() {

        Width=ScreenWH.getScreenWidth();
        Height=ScreenWH.getNoStatus_bar_Height(context);

        SaveData.initManager(context);
        saveData = SaveData.getsaveData();

        isfirst = saveData.readDataBoolean("isCheckRemember");

        animationStart = setAnimationState(animationStart);

        TranslateAnimation am = new TranslateAnimation(0.0f, 0.0f, 0.0f, -(Height/18));
        ScaleAnimation scaleAnimationStart = new ScaleAnimation( 1.0f, 0.95f, 1.0f, 0.95f );
        AlphaAnimation alphaAnimationStart = new AlphaAnimation(0.5f,1.0f);
        animationStart.addAnimation(am);
        animationStart.addAnimation(scaleAnimationStart);
        animationStart.addAnimation(alphaAnimationStart);

        animationBack = setAnimationState(animationBack);

        //將動畫參數設定到圖片並開始執行動畫
        TranslateAnimation back = new TranslateAnimation(0.0f, 0.0f, -(Height/18), 0.0f);
        ScaleAnimation scaleAnimationEnd = new ScaleAnimation( 0.95f, 1.0f, 0.95f, 1.0f );
        AlphaAnimation alphaAnimationEnd = new AlphaAnimation(1.0f,0.5f);
        animationBack.addAnimation(back);
        animationBack.addAnimation(scaleAnimationEnd);
        animationBack.addAnimation(alphaAnimationEnd);

        editText = new EditText(context);
        editText.setId(generateViewId());
        RelativeLayout.LayoutParams editText_lay=new RelativeLayout.LayoutParams((ScreenWH.getScreenWidth()/2), Height/10);
        editText_lay.setMargins(0,Height/15,0,0);
        editText.setLayoutParams(editText_lay);
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(20);
        editText.setMaxLines(1);
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(textWatcher);

        this.addView(editText);

        textView = new TextView(context);
        textView.setId(generateViewId());

        RelativeLayout.LayoutParams textView_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView_lay.setMargins(0,Height/10,0,0);
        textView.setLayoutParams(textView_lay);

        textView.setText("測試領取人名稱");
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.parseColor("#00000000"));
        textView.setTextSize(18);
        textView.setAlpha(0.5f);

        this.addView(textView);

        textViewEmpty = new TextView(context);
        textViewEmpty.setId(generateViewId());

        RelativeLayout.LayoutParams textViewEmpty_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewEmpty_lay.addRule(RelativeLayout.BELOW,editText.getId());
        textViewEmpty.setLayoutParams(textViewEmpty_lay);

        textViewEmpty.setTextColor(Color.RED);
        textViewEmpty.setBackgroundColor(Color.parseColor("#00000000"));
        textViewEmpty.setTextSize(12);

        this.addView(textViewEmpty);

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
    //設定顯示文字
    public void setTextEx(String textString){
        textView.setText(textString);
    }

    private AnimationSet setAnimationState(AnimationSet animationSet){

        if(animationSet == null){
            animationSet = new AnimationSet(true);//这里的true表示共享一个Interpolator
        }

        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.setDuration(300);
        animationSet.setRepeatCount(0);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    public void startTextAnimation(){
        if(CheckEditString() || isfirst){
            textView.setAlpha(1.0f);
            textView.startAnimation(animationStart);
        }
        isfirst = false;
    }

    public void backTextAnimation(){
        if(CheckEditString()){
            textView.startAnimation(animationBack);
        }
    }

    public boolean CheckEditString(){
        if(editText.getText().toString().equals(""))return true;
        else return false;
    }

    public String getEditString(){
        return editText.getText().toString();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b){
            startTextAnimation();
        }else{
            backTextAnimation();
        }
    }
    //設定輸入文字是否隱藏
    public void setTransformationMethod(boolean isCheck){
        if(isCheck)editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        else editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    //輸入時檢查
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            editText.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            textViewEmpty.setGravity(View.GONE);
        }
        //正則判斷只輸入英文數字
        @Override
        public void afterTextChanged(Editable editable) {
            try {
                String temp = editable.toString();
                String tem = temp.substring(temp.length()-1, temp.length());
                char[] temC = tem.toCharArray();
                int mid = temC[0];
                if(mid>=48&&mid<=57){//数字
                    return;
                }
                if(mid>=65&&mid<=90){//大写字母
                    return;
                }
                if(mid>97&&mid<=122){//小写字母
                    return;
                }
                editable.delete(temp.length()-1, temp.length());
            } catch (Exception e) {
            }
        }
    };
    //開啟畫面時 如有儲存帳號密碼 設定文字前先清空 後再設定文字 啟動動畫
    public void setEditText(String string){
        if(!CheckEditString()){
            editText.setText("");
        }
        editText.setText(string);
        startTextAnimation();
    }
    //判斷為空值登入時 提醒用戶不能為空值
    public void setExitEmptyColor(String string){

        editText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        editText.requestFocus();
        textViewEmpty.setText(string);
        textViewEmpty.setGravity(View.VISIBLE);

    }

    public void setInputType(int type){
        editText.setInputType(type);
    }
}
