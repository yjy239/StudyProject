package yjy.com.baselibrary.CommonRecycleAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yjy on 2017/7/16.
 */
public abstract class RecyclerCommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private int mLayoutId;
    private List<T> mData;
    private LayoutInflater mInflater;
    private MulitpleTypeSupport mTypeSupport;

    public RecyclerCommonAdapter(Context context, List<T> data, int layoutid){
        mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mLayoutId = layoutid;
    }

    public RecyclerCommonAdapter(Context context, List<T> data,MulitpleTypeSupport typeSupport){
        this(context, data, -1);
        this.mTypeSupport = typeSupport;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建view
        if(mTypeSupport != null){
            mLayoutId = viewType;
        }
        View itemView = mInflater.inflate(mLayoutId,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        if(mTypeSupport !=null){
            return mTypeSupport.getLayoutId(mData.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        convert(holder,mData.get(position),position);
        if(mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }

        if(mItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    mItemLongClickListener.ItemLongClick(position);
                    return true;
                }
            });
        }
    }

    //将必要的参数全部传出去
    /****
     * 复写的方法
     *@param holder
     * @param data
     * @param position **/
    protected abstract void convert(ViewHolder holder,T data, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private ItemClickListener mItemClickListener;
    public void setOnItemClickListener(ItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    private ItemLongClickListener mItemLongClickListener;
    public void setOnItemLongClickListener(ItemLongClickListener mItemLongClickListener){
        this.mItemLongClickListener = mItemLongClickListener;
    }
}
