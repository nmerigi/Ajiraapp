package com.example.ajiraapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ExpertList_adapter extends RecyclerView.Adapter<ExpertList_adapter.MyViewHolder> {
    Context context;
    ArrayList<Expert> list;
    String clientPhoneNumber;
    public ExpertList_adapter(ArrayList<Expert> list, Context context,String clientPhoneNumber) {
        this.list = list;
        this.context = context;
        this.clientPhoneNumber = clientPhoneNumber;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Expert expert = list.get(position);
        holder.expert_name.setText(expert.getFullName());
        holder.service_charge.setText(expert.getServicecharge());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotifyExpert.class);
            intent.putExtra("EXPERT_NAME", expert.getFullName());
            intent.putExtra("EXPERT_CONTACT", expert.getPhonenumber());
            intent.putExtra("EXPERT_SERVICE", expert.getService());
            intent.putExtra("EXPERT_CHARGE", expert.getServicecharge());
            intent.putExtra("CLIENT_PHONE_NUMBER", clientPhoneNumber);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView expert_name, service_charge;

        public MyViewHolder(@NonNull View itemView){

            super(itemView);
            expert_name= itemView.findViewById(R.id.expert_name);
            service_charge= itemView.findViewById(R.id.service_charge);
        }

    }
}
