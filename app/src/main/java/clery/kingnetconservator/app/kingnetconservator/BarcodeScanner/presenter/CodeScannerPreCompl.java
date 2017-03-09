package clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.presenter;

import android.content.Context;

import clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.model.CodeScannerData;
import clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.model.CodeScannerModel;
import clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.view.CodeScannerView;

/**
 * Created by clery on 2017/2/16.
 */

public class CodeScannerPreCompl implements CodeScannerPre{

    Context context;
    CodeScannerView codeScannerView;
    CodeScannerData codeScannerData;

    public CodeScannerPreCompl(Context context, CodeScannerView codeScannerView){
        this.context=context;
        this.codeScannerView = codeScannerView;
        codeScannerData=new CodeScannerModel();
    }

    @Override
    public boolean CheckCodeScannerState() {
        return codeScannerData.isCodeScanner();
    }

    @Override
    public void setCodeScannerState(Boolean state) {
        codeScannerData.setCodeScanner(state);
    }

    @Override
    public boolean CheckEnmergenceState() {
        return codeScannerData.isEnmergence();
    }

    @Override
    public void setEnmergenceState() {
        codeScannerData.reverseEnmergence();
    }

    @Override
    public boolean CheckPreviewingState() {
        return codeScannerData.isPreviewing();
    }

    @Override
    public void setPreviewingState(Boolean state) {
        codeScannerData.setPreviewing(state);
    }
}
