package com.example.projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Book> bookArrayList;

    public MyAdapter(Context context, ArrayList<Book> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("create");
        View v =LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Book book=bookArrayList.get(position);

        System.out.println("bindviewholder");

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.pages.setText(String.valueOf(book.getNumberOfPages()));

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, author, pages;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            System.out.println("myviewholder");

            title=itemView.findViewById(R.id.textViewTitle);
            author=itemView.findViewById(R.id.textViewAuthor);
            pages=itemView.findViewById(R.id.textViewPages);
        }
    }


}
