package com.dreamguys.truelysell.wallet;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.BaseActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddWalletActivity extends BaseActivity implements View.OnClickListener {

    Toolbar tbWallet;
    EditText editTextAmount;
    TextView textViewCurrentBalance, action1000, action2000, action3000;
    Button actionAddCash;
    String walletAmount = "", currency = "", currencycode = "";
    @BindView(R.id.title_wallet)
    TextView titleWallet;
    @BindView(R.id.tv_gigs_title)
    TextView tvGigsTitle;
    @BindView(R.id.label_current_balance)
    TextView labelCurrentBalance;
    @BindView(R.id.txt_add_cash)
    TextView txtAddCash;
    @BindView(R.id.txt_minimum_amount_of_50)
    TextView txtMinimumAmountOf50;
    @BindView(R.id.label_add_card)
    TextView labelAddCard;
    @BindView(R.id.rb_debit_credit_cards)
    RadioButton rbDebitCreditCards;
    @BindView(R.id.text_card_expiry)
    TextView textCardExpiry;
    @BindView(R.id.text_cvv)
    TextView textCvv;
    @BindView(R.id.txt_card_secure)
    TextView txtCardSecure;
    @BindView(R.id.rg_card_details)
    RadioGroup rgCardDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        ButterKnife.bind(this);
        tbWallet = findViewById(R.id.tb_wallet);
//        setSupportActionBar(tbWallet);
        setToolBarTitle("Add Cash");
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);

        walletAmount = getIntent().getStringExtra(AppConstants.WALLETAMOUNT);
        currency = getIntent().getStringExtra(AppConstants.CURRENCY);
        currencycode = getIntent().getStringExtra(AppConstants.CURRENCYCODE);

        textViewCurrentBalance = findViewById(R.id.text_current_balance);
        editTextAmount = findViewById(R.id.edit_amount);
        action1000 = findViewById(R.id.action_1000);
        action2000 = findViewById(R.id.action_2000);
        action3000 = findViewById(R.id.action_3000);
        actionAddCash = findViewById(R.id.action_add_cash);

        action1000.setOnClickListener(this);
        action2000.setOnClickListener(this);
        action3000.setOnClickListener(this);
        actionAddCash.setOnClickListener(this);

        textViewCurrentBalance.setText(Html.fromHtml(currency) + walletAmount);
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_1000:
                editTextAmount.setText("1000");
                break;
            case R.id.action_2000:
                editTextAmount.setText("2000");
                break;
            case R.id.action_3000:
                editTextAmount.setText("3000");
                break;
            case R.id.action_add_cash:
                if (!editTextAmount.getText().toString().isEmpty()) {
                    Intent intentBacktoWalletDashBoard = new Intent(this, CardListActivity.class);
                    intentBacktoWalletDashBoard.putExtra(AppConstants.CASH_AMOUNT, editTextAmount.getText().toString());
                    intentBacktoWalletDashBoard.putExtra(AppConstants.WALLETAMOUNT, walletAmount);
                    intentBacktoWalletDashBoard.putExtra(AppConstants.CURRENCY, currency);
                    intentBacktoWalletDashBoard.putExtra(AppConstants.CURRENCYCODE, currencycode);
                    startActivity(intentBacktoWalletDashBoard);
                    finish();
                } else {
                    Toast.makeText(this, "Please enter the amount to proceed...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
