package multitypesliding.privatecom.wwei.multitypesliding.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import multitypesliding.privatecom.wwei.multitypesliding.R;
import multitypesliding.privatecom.wwei.multitypesliding.activity.FrameLayoutSwipeActivity;
import multitypesliding.privatecom.wwei.multitypesliding.activity.LinearLayoutSwipeActivity;
import multitypesliding.privatecom.wwei.multitypesliding.bean.Beauty;

/**
 * Created by Administrator on 2018/10/24.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private Context mContext;
    private List<Beauty> data;

    public MainAdapter(List<Beauty> data, Context context) {
        this.data = data;
        mContext = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup viewparent,int viewType)
    {
        View view = LayoutInflater.from(viewparent.getContext()).inflate(R.layout.beauty_item,viewparent,false);
        return  new MainViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MainViewHolder viewHolder , final int position)
    {
        Beauty beauty = data.get(position);
        int img = beauty.getImageId();
//        Glide.with(mContext).load(img).asBitmap().fitCenter()
//            .diskCacheStrategy(DiskCacheStrategy.SOURCE)//磁盘缓存
//            .skipMemoryCache(false).placeholder(R.mipmap.ic_launcher).into(viewHolder.beautyIamge);//内存缓存
        viewHolder.beautyIamge.setImageResource(beauty.getImageId());
        viewHolder.beautyIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(mContext, FrameLayoutSwipeActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(mContext, LinearLayoutSwipeActivity.class);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
        viewHolder.nameTv.setText(beauty.getName());
    }
    @Override
    public  int getItemCount()
    {
        return data.size();
    }
    static class MainViewHolder extends RecyclerView.ViewHolder
    {
        ImageView beautyIamge;
        TextView nameTv;
        public MainViewHolder(View itemView)
        {
            super(itemView);
            beautyIamge = itemView.findViewById(R.id.image_item);
            nameTv = itemView.findViewById(R.id.name_item);
        }
    }
}
