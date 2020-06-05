package com.android.wellenssq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Model.Doctor;

public class DoctorAdaptor  extends RecyclerView.Adapter<DoctorAdaptor.MyViewHolder> {
    private List<Doctor> mDataset;
    private Context context;
    private String pName;
    private String timeSlot;
    private String patPhone;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DoctorAdaptor( List<Doctor> myDataset,String pName,String timeSlot,String patPhone) {
        mDataset = myDataset;
        this.pName=pName;
        this.timeSlot=timeSlot;
        this.patPhone=patPhone;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {

        // create a new view
        View v=  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_view, parent, false);


        MyViewHolder vh = new MyViewHolder(v);



        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset.get(position).getName());

       // holder.queueno.setText("No of persons in queue :"+mDataset.get(position).getQueueToken());
        holder.btnQueueIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context=view.getContext();

                Toast.makeText(
                        view.getContext(),
                        "clicked"+mDataset.get(position).getName(),
                        Toast.LENGTH_LONG).show();

               Intent intent = new Intent(context, PatientDashboardActivity.class);

                intent.putExtra("dname",  mDataset.get(position).getName());
                intent.putExtra("docPhone", mDataset.get(position).getPhoneNumber());
                intent.putExtra("category", mDataset.get(position).getCategory());
                intent.putExtra("latitude",  mDataset.get(position).getLattitude());
                intent.putExtra("longitude", mDataset.get(position).getLongitude());
                intent.putExtra("timeslot", timeSlot);
                intent.putExtra("pname", pName);
                intent.putExtra("patPhone", patPhone);

                context.startActivity(intent);
            }
        });
        // holder.img.setImageResource(@mipmap/ic_launcher_round);
        //holder.textView.setText("mDataset.get(position)");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView queueno;
        public ImageView img;
        public Button btnQueueIn;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.textView2);
            queueno = v.findViewById(R.id.queuesize);
            img= v.findViewById(R.id.imageView);
            btnQueueIn = v.findViewById(R.id.queueIn);

        }


    }


}