package clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.presenter;

/**
 * Created by clery on 2017/2/16.
 */

public interface CodeScannerPre {
    boolean CheckCodeScannerState();
    void setCodeScannerState(Boolean state);
    boolean CheckEnmergenceState();
    void setEnmergenceState();
    boolean CheckPreviewingState();
    void setPreviewingState(Boolean state);
}
