package com.filter.camerafilter.dialog;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.filter.camerafilter.ConstantFilters;
import com.dingmouren.camerafilter.R;

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */

public class DialogFilterListAdapter extends RecyclerView.Adapter<DialogFilterListAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    public static int currentIndex = 0;
    public DialogFilterListAdapter(Context context){}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_filter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.img.setImageResource(ConstantFilters.IMG_FILTERS[position+ getStartIndex()]);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) mOnItemClickListener.onItemClickListener(position + getStartIndex());
            }
        });
    }

    @Override
    public int getItemCount() {
        return getLength();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }

    private int getLength(){
        if(currentIndex == 0 || currentIndex == 1)return 30;
        if(currentIndex == -1) return ConstantFilters.IMG_FILTERS.length;
        return ConstantFilters.IMG_FILTERS.length - 60;
    }

    private int getStartIndex(){
        if(currentIndex == 0 || currentIndex == -1)return 0;
        if(currentIndex == 1) return 30;
        return 60;
    }
}
