package com.dreamguys.truelysell.adapters;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.ProvAvailData;
import com.dreamguys.truelysell.utils.AppUtils;

public class CheckAvailabilityAdapter extends RecyclerView.Adapter<CheckAvailabilityAdapter.ViewHolder> {

    private ArrayList<ProvAvailData> availList;
    private Context mContext;
    private Activity mActivity;
    private int[] minHourFrom, minMinsFrom, minHourTo, minMinsTo;
    private boolean[] flag;

    public CheckAvailabilityAdapter(Activity mActivity, ArrayList<ProvAvailData> availList, int viewType) {
        this.mActivity = mActivity;
        this.mContext = mActivity.getBaseContext();
        this.availList = availList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_provider_avail, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvAvailDay.setText(AppUtils.cleanString(mContext, availList.get(position).getDayText()));
        viewHolder.switchSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (availList.get(position).isEnabled()) {
                    try {
                        availList.get(position).setChecked(isChecked);
                        if (position == 0) {
                            for (int i = 1; i < availList.size(); i++) {
                                availList.get(i).setEnabled(!isChecked);
                                if (isChecked)
                                    availList.get(i).setChecked(!isChecked);
                                notifyItemChanged(i);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    viewHolder.switchSelect.setTag("tag_check");
                    viewHolder.switchSelect.setChecked(!isChecked);
                    viewHolder.switchSelect.setTag(null);
                }
            }
        });

        if (availList.get(position).isEnabled()) {
            viewHolder.switchSelect.setEnabled(true);
            viewHolder.tvFromTime.setEnabled(true);
            viewHolder.tvToTime.setEnabled(true);
        } else {
            viewHolder.switchSelect.setEnabled(false);
            viewHolder.tvFromTime.setEnabled(false);
            viewHolder.tvToTime.setEnabled(false);
        }

        viewHolder.switchSelect.setTag("tag_check");
        viewHolder.switchSelect.setChecked(availList.get(position).isChecked());
        viewHolder.switchSelect.setTag(null);
        viewHolder.tvFromTime.setText(availList.get(position).getFromTime());
        viewHolder.tvToTime.setText(availList.get(position).getToTime());

        viewHolder.tvFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availList.get(position).isEnabled() && availList.get(position).isChecked()) {
                    if (!flag[position]) {
                        try {
                            minHourFrom[position] = new SimpleDateFormat("HH:mm").parse(availList.get(position).getFromTime()).getHours();
                            minMinsFrom[position] = new SimpleDateFormat("HH:mm").parse(availList.get(position).getFromTime()).getMinutes();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String AM_PM;
                            if (selectedHour < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            selectedHour = selectedHour > 12 ? selectedHour - 12 : selectedHour;
                            String hr = selectedHour < 10 ? "0" + selectedHour : "" + selectedHour;
                            String min = selectedMinute < 10 ? "0" + selectedMinute : "" + selectedMinute;
                            String totTime = hr + ":" + min + " " + AM_PM;
                            viewHolder.tvFromTime.setText(totTime);
                            availList.get(position).setFromTime(totTime);
                            minHourFrom[position] = selectedHour;
                            minMinsFrom[position] = selectedMinute;
                            if (flag[position]) {
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                try {
                                    Date fromTime = sdf.parse(availList.get(position).getFromTime());
                                    Date toTime = sdf.parse(availList.get(position).getToTime());
                                    int dateDelta = fromTime.compareTo(toTime);

                                    if (fromTime.after(toTime)) {
                                        Toast.makeText(mContext, "FromTime is **Greater** than ToTime", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, minHourFrom[position], minMinsFrom[position], false).show();//Yes 24 hour time
                }
            }
        });

        viewHolder.tvToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availList.get(position).isEnabled() && availList.get(position).isChecked()) {
                    if (!flag[position]) {
                        try {
                            minHourTo[position] = new SimpleDateFormat("HH:mm").parse(availList.get(position).getToTime()).getHours();
                            minMinsTo[position] = new SimpleDateFormat("HH:mm").parse(availList.get(position).getToTime()).getMinutes();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            flag[position] = true;
                            String AM_PM;
                            if (selectedHour < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            minHourTo[position] = selectedHour;
                            minMinsTo[position] = selectedMinute;
                            selectedHour = selectedHour > 12 ? selectedHour - 12 : selectedHour;
                            String hr = selectedHour < 10 ? "0" + selectedHour : "" + selectedHour;
                            String min = selectedMinute < 10 ? "0" + selectedMinute : "" + selectedMinute;
                            String totTime = hr + ":" + min + " " + AM_PM;
                            viewHolder.tvToTime.setText(totTime);
                            availList.get(position).setToTime(totTime);

                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                            try {
                                Date fromTime = sdf.parse(availList.get(position).getFromTime());
                                Date toTime = sdf.parse(availList.get(position).getToTime());
                                int dateDelta = fromTime.compareTo(toTime);


                                if (toTime.before(fromTime)) {
                                    Toast.makeText(mContext, "ToTime is **Less** than fromTime", Toast.LENGTH_SHORT).show();
                                }


//                                switch (dateDelta) {
//                                    case 0:
//                                        Toast.makeText(mContext, "startTime and endTime not **Equal**", Toast.LENGTH_SHORT).show();
//                                        break;
//                                    case 1:
////                                        Toast.makeText(mContext, "endTime is **Greater** than startTime", Toast.LENGTH_SHORT).show();
//                                        break;
//                                    case -1:
//                                        Toast.makeText(mContext, "startTime is **Greater** than endTime", Toast.LENGTH_SHORT).show();
//                                        break;
//                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }, minHourFrom[position], minMinsFrom[position], false).show();//Yes 24 hour time
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        minHourFrom = new int[availList.size()];
        minMinsFrom = new int[availList.size()];
        minHourTo = new int[availList.size()];
        minMinsTo = new int[availList.size()];
        for (int i = 0; i < availList.size(); i++) {
            minHourFrom[i] = 9;
            minMinsFrom[i] = 0;
            minHourTo[i] = 18;
            minMinsTo[i] = 0;
        }
        flag = new boolean[availList.size()];
        return availList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.switch_select)
        Switch switchSelect;
        @BindView(R.id.tv_avail_day)
        TextView tvAvailDay;
        @BindView(R.id.tv_from_time)
        TextView tvFromTime;
        @BindView(R.id.tv_to_time)
        TextView tvToTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
