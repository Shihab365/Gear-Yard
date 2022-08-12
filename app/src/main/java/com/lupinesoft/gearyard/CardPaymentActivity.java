package com.lupinesoft.gearyard;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import movile.com.creditcardguide.ActionOnPayListener;
import movile.com.creditcardguide.CreditCardFragment;
import movile.com.creditcardguide.model.CreditCardPaymentMethod;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PaymentMethod;
import movile.com.creditcardguide.model.PurchaseOption;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CardPaymentActivity extends AppCompatActivity implements ActionOnPayListener {
    String total, mycart, mycad, transID, trackID;
    private CreditCardFragment inputCardFragment;
    String sUID = DashboardActivity.strUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        total = getIntent().getStringExtra("total");
        mycart = getIntent().getStringExtra("mycart");
        mycad = getIntent().getStringExtra("mycad");

        char[] chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
        for (int i = 0; i < 5; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        transID = sb.toString();

        char[] charsT = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toCharArray();
        Random rndT = new Random();
        StringBuilder sbT = new StringBuilder((100000 + rndT.nextInt(900000)) + "");
        for (int i = 0; i < 5; i++)
            sbT.append(charsT[rndT.nextInt(charsT.length)]);
        trackID = sbT.toString();

        inputCardFragment = (CreditCardFragment) getFragmentManager().findFragmentById(R.id.frg_pay);

        inputCardFragment.setPagesOrder(CreditCardFragment.Step.FLAG, CreditCardFragment.Step.NUMBER,
                CreditCardFragment.Step.EXPIRE_DATE, CreditCardFragment.Step.CVV, CreditCardFragment.Step.NAME);

        inputCardFragment.setListPurchaseOptions(getList(), Double.valueOf(total));
    }

    private List<PurchaseOption> getList() {
        List<PurchaseOption> list = new ArrayList<>();
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.MASTERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.VISACREDITO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AMEX, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.PAYPAL, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.DINERS, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.NUBANK, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AURA, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.ELO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.HIPERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.OTHER, 6));
        return list;
    }

    @Override
    public void onChangedPage(CreditCardFragment.Step page) {

    }

    @Override
    public void onComplete(CreditCardPaymentMethod purchaseOption, boolean saveCard) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                submitUserHdy();
            }
        }).start();

        Intent intent = new Intent(CardPaymentActivity.this, EndActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(CardPaymentActivity.this, R.anim.enter_right_to_left,R.anim.exit_right_to_left);
        startActivity(intent, options.toBundle());
        if (saveCard) {
            purchaseOption.setSecurityCode(null);
        }
    }

    public void submitUserHdy() {
        String urlsHDY = "http://100.72.26.84/GYadminPanel/appDB/user_hdy/user_hdy_insert.php";
        String UID = sUID;
        String insTotal = total;
        String insMycart = mycart;
        String insMycad = mycad;
        String insMytid = transID;
        String insMytracking = trackID;

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("UID", UID)
                .add("Total", insTotal)
                .add("Cart", insMycart)
                .add("Cad", insMycad)
                .add("TID", insMytid)
                .add("Tracking", insMytracking)
                .build();

        Request request = new Request.Builder().url(urlsHDY)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
