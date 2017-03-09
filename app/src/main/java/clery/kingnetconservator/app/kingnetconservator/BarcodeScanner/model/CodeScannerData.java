package clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.model;

/**
 * Created by clery on 2017/2/16.
 */

public interface CodeScannerData {

    Boolean isCodeScanner();
    void setCodeScanner(Boolean state);
    Boolean isEnmergence();
    void reverseEnmergence();
    Boolean isPreviewing();
    void setPreviewing(Boolean state);
}
