package multitypesliding.privatecom.wwei.multitypesliding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import multitypesliding.privatecom.wwei.multitypesliding.R;
import multitypesliding.privatecom.wwei.multitypesliding.bean.Beauty;
import multitypesliding.privatecom.wwei.multitypesliding.utils.SwipeDeleteManager;

/**
 * Created by Administrator on 2018/10/29.
 */

public class Chat_FrameLayoutAdapter extends RecyclerView.Adapter<Chat_FrameLayoutAdapter.ViewHolder> {
    private Context mContext;
    private List<String> data;
    private Chat_FrameLayoutAdapter.DeleteAndStick callback;
    public Chat_FrameLayoutAdapter(List<String> data, Context context, Chat_FrameLayoutAdapter.DeleteAndStick callback) {
        this.data = data;
        mContext = context;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_main,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        viewHolder.textView.setText(data.get(position));
        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SwipeDeleteManager.getInstance().close();
//                SwipeDeleteManager.getInstance().clear();
               callback.Delete(position);
            }
        });

    }
    @Override
    public int getItemCount()
    {
        return  data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        TextView tv_delete;
        public ViewHolder(View view)
        {
            super(view);
            textView = view.findViewById(R.id.tv_context);
            tv_delete = view.findViewById(R.id.tv_delete);
        }
    }
    public interface DeleteAndStick
    {
        public void Delete(int position);
    }
}
