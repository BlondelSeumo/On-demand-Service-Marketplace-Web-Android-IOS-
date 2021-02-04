package com.dreamguys.truelysell.wallet;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.WalletTransactionHistoryAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerViewTransactionHistory;
    WalletTransactionHistoryAdapter walletTransactionHistoryAdapter;
    @BindView(R.id.title_wallet)
    TextView titleWallet;
    @BindView(R.id.label_transaction_history)
    TextView labelTransactionHistory;
    @BindView(R.id.action_filter)
    TextView actionFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        ButterKnife.bind(this);

        recyclerViewTransactionHistory = findViewById(R.id.rcv_transaction_history);
        recyclerViewTransactionHistory.setHasFixedSize(true);
        recyclerViewTransactionHistory.setLayoutManager(new LinearLayoutManager(this));

    }
}
