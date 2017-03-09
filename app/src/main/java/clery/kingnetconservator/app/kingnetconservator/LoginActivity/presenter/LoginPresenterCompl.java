package clery.kingnetconservator.app.kingnetconservator.LoginActivity.presenter;

import android.content.Context;
import android.util.Log;

import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;
import clery.kingnetconservator.app.kingnetconservator.LoginActivity.model.AccountDataModel;
import clery.kingnetconservator.app.kingnetconservator.LoginActivity.view.LoginView;

/**
 * Created by clery on 2017/2/21.
 */

public class LoginPresenterCompl implements LoginPresenter{

    private static String isCheckRemember = "isCheckRemember";
    private static String editaccount ="editaccount";
    private static String editpassword = "editpassword";
    private static String OPENCODE = "OPENCODE";

    Context context;
    LoginView loginView;
    AccountDataModel accountDataModel;

    SaveData saveData;

    public LoginPresenterCompl(Context context, LoginView loginView){

        this.context =context;
        this.loginView = loginView;
        accountDataModel = new AccountDataModel();

        SaveData.initManager(context);
        saveData = SaveData.getsaveData();
    }

    @Override
    public boolean firstCheckBox() {
        if(saveData.readDataBoolean(isCheckRemember)){
            accountDataModel.setCheckRemember(true);
        }
        return saveData.readDataBoolean(isCheckRemember);
    }

    @Override
    public boolean checkRememberBox() {
        return accountDataModel.isCheckRemember();
    }

    @Override
    public void ShowAccount() {
        if(firstCheckBox()){
            loginView.ShowAccount();

        }
        loginView.ShowCheckBox(firstCheckBox());
    }

    @Override
    public void setRemberBox(boolean b) {
        accountDataModel.setCheckRemember(b);
        saveData.saveBooleanData(isCheckRemember,b);
    }

    @Override
    public void SaveAccount() {
        if(accountDataModel.isCheckRemember()){
            saveData.saveStringData(editaccount,loginView.geteditaccount());
            saveData.saveStringData(editpassword,loginView.geteditpassword());
            saveData.saveStringData(OPENCODE,loginView.geteditCodes());
        }
    }
}
