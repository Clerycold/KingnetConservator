package clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.model;

/**
 * Created by clery on 2017/2/16.
 */

public class CodeScannerModel implements CodeScannerData{

    /**
     * barcodeScanned 是否偵測到QR code
     * enmergence 掃描時發生縮屏事件
     * previewing 是否掃描中
     */
    private boolean barcodeScanned = false;
    private boolean enmergence = false;
    private boolean previewing = true;

    @Override
    public Boolean isCodeScanner() {
        if(barcodeScanned){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void setCodeScanner(Boolean state) {
        barcodeScanned = state;
    }

    @Override
    public Boolean isEnmergence() {
        if(enmergence){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void reverseEnmergence() {
        if(enmergence){
            enmergence = false;
        }else{
            enmergence = true;
        }
    }

    @Override
    public Boolean isPreviewing() {
        if(previewing){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void setPreviewing(Boolean state) {
        previewing = state;
    }
}
