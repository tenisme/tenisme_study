package com.tenisme.employee2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.tenisme.employee2.R;
import com.tenisme.employee2.model.Employee;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    Context context;
    ArrayList<Employee> employeeArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Employee> employeeArrayList) {
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_row,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Employee employee = employeeArrayList.get(position);

        String name = employee.getName();
        int age = employee.getAge();
        int salary = employee.getSalary();

        holder.txtName.setText(name);
        holder.txtAge.setText(age+"세");
        holder.txtSalary.setText("$"+salary);
    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size(); // ★size() 기억!!
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName;
        public TextView txtAge;
        public TextView txtSalary;
        public ImageView imgThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtSalary = itemView.findViewById(R.id.txtSalary);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);

        }
    }
}
