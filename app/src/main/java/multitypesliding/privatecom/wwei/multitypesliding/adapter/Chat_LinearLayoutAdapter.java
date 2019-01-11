package multitypesliding.privatecom.wwei.multitypesliding.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import multitypesliding.privatecom.wwei.multitypesliding.R;
import multitypesliding.privatecom.wwei.multitypesliding.view.SlideDeleteView;

/**
 * Created by Administrator on 2018/12/4.
 */

public class Chat_LinearLayoutAdapter extends RecyclerView.Adapter<Chat_LinearLayoutAdapter.ViewHolder> {
    private Context mContext;
    private List<String> data;
    private Chat_LinearLayoutAdapter.DeleteAndStick callback;
    public Chat_LinearLayoutAdapter(List<String> data, Context context, Chat_LinearLayoutAdapter.DeleteAndStick callback) {
        this.data = data;
        mContext = context;
        this.callback = callback;
    }

    @Override
    public Chat_LinearLayoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_ll_main,parent,false);
        return new Chat_LinearLayoutAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(Chat_LinearLayoutAdapter.ViewHolder viewHolder, final int position)
    {
        Log.e("dd222"+position,"ddd");
        ConstraintLayout layout= (ConstraintLayout) viewHolder.deleteView.getChildAt(0);
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
