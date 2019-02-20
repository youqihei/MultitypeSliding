package multitypesliding.privatecom.wwei.multitypesliding.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import multitypesliding.privatecom.wwei.multitypesliding.R;
import multitypesliding.privatecom.wwei.multitypesliding.view.SlideDeleteView;

/**
 * Created by Administrator on 2019/1/14.
 */

public class Chat_ViewGroupSwipeAdapter extends RecyclerView.Adapter<Chat_ViewGroupSwipeAdapter.ViewHolder> {
    private Context mContext;
    private List<String> data;
    private Chat_ViewGroupSwipeAdapter.DeleteAndStick callback;
    public Chat_ViewGroupSwipeAdapter(List<String> data, Context context, Chat_ViewGroupSwipeAdapter.DeleteAndStick callback) {
        this.data = data;
        mContext = context;
        this.callback = callback;
    }

    @Override
    public Chat_ViewGroupSwipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_vgroup_main,parent,false);
        return new Chat_ViewGroupSwipeAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(Chat_ViewGroupSwipeAdapter.ViewHolder viewHolder, final int position)
    {
      //  ConstraintLayout layout= (ConstraintLayout) viewHolder.deleteView.getChildAt(0);
        // TextView deleteView = (TextView) layout.getChildAt(0);
        //deleteView.setText(data.get(position));


    }
    @Override
    public int getItemCount()
    {
        return  data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        SlideDeleteView deleteView;
        public ViewHolder(View view)
        {
            super(view);
            deleteView = view.findViewById(R.id.slide_delete_view);
        }
    }
    public interface DeleteAndStick
    {
        public void Delete(int position);
    }
}

