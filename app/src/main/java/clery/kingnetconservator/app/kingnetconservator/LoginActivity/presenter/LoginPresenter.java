package clery.kingnetconservator.app.kingnetconservator.LoginActivity.presenter;

/**
 * Created by clery on 2017/2/21.
 */

public interface LoginPresenter {

    boolean firstCheckBox();
    boolean checkRememberBox();
    void ShowAccount();
    void setRemberBox(boolean b);
    void SaveAccount();
}
