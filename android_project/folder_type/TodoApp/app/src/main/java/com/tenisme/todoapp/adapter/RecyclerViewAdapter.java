package com.tenisme.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tenisme.todoapp.MainActivity;
import com.tenisme.todoapp.R;
import com.tenisme.todoapp.model.Todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Context context;
    ArrayList<Todo> todoArrayList;

    Todo todo;

    public RecyclerViewAdapter(Context context, ArrayList<Todo> todoArrayList) {
        this.context = context;
        this.todoArrayList = todoArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        todo = todoArrayList.get(position);

        String title = todo.getTitle();
        String date = todo.getDate();
        int completed = todo.getCompleted();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC")); // 위의 시간을 UTC 로 맞춤

        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date d = df.parse(date);
            String formattedTime = output.format(d);
            holder.txt_date.setText(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (completed == 1) {
            holder.img_completed.setImageResource(android.R.drawable.checkbox_on_background);
        } else if (completed == 0) {
            holder.img_completed.setImageResource(android.R.drawable.checkbox_off_background);
        }

        holder.txt_title.setText(title);
    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;
        TextView txt_date;
        ImageView img_completed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            txt_date = itemView.findViewById(R.id.txt_date);
            img_completed = itemView.findViewById(R.id.img_completed);

            img_completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int completed = todoArrayList.get(position).getCompleted();

                    if (completed == 1) {
                        // 즐겨찾기 삭제
                        ((MainActivity) context).uncheckTodoRequest(position);
                    } else if(completed == 0) {
                        // 즐겨찾기 추가
                        ((MainActivity) context).checkTodoRequest(position);
                    }
                }
            });

        }
    }
}
