package com.example.projekt.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.Book;
import com.example.projekt.EditorActivity;
import com.example.projekt.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecyclerViewAdapter> {

    Context context;
    ArrayList<Book> bookArrayList;
    private final ItemClickListener itemClickListener;

    public MainAdapter(Context context, ArrayList<Book> bookArrayList, ItemClickListener itemClickListener) {
        this.context = context;
        this.bookArrayList = bookArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new RecyclerViewAdapter(v,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {

        Book book=bookArrayList.get(position);

        holder.tv_title.setText(book.getTitle());
        holder.tv_author.setText(book.getAuthor());
        holder.tv_pages.setText(book.getNumberOfPages());

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }


    public class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView tv_title, tv_author, tv_pages;
        CardView card_item;
        ItemClickListener itemClickListener;

        RecyclerViewAdapter(View itemView,ItemClickListener itemClickListener){
            super(itemView);

            tv_title=itemView.findViewById(R.id.title);
            tv_author=itemView.findViewById(R.id.author);
            tv_pages=itemView.findViewById(R.id.page);
            card_item=itemView.findViewById(R.id.cardView);

            this.itemClickListener=itemClickListener;
            card_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
            Book book=bookArrayList.get(getAdapterPosition());
            Intent intent=new Intent(context, EditorActivity.class);
            intent.putExtra("book",book);
            context.startActivity(intent);



        }

    }
    public interface ItemClickListener {

        void onItemClick(View view, int position);
    }


}
