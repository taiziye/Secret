package shengshoubo.example.com.secret.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shengshoubo.example.com.secret.R;
import shengshoubo.example.com.secret.net.TimelineMessage;

/**
 * Created by shengshoubo on 2015/9/1.
 */
public class AtyTimelineMessageListAdapter extends BaseAdapter {

    private Context context=null;
    private List<TimelineMessage> data=new ArrayList<>();

    public AtyTimelineMessageListAdapter(Context context) {
        this.context=context;
    }

    public void addAll(List<TimelineMessage> timeline){
        data.addAll(timeline);
        notifyDataSetChanged();
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }
    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.aty_timeline_list_cell,null);
            //记录下列表项对象
            convertView.setTag(new ListCell((TextView) convertView.findViewById(R.id.tvCellLabel)));
        }

        ListCell lc= (ListCell) convertView.getTag();
        TimelineMessage msg= (TimelineMessage) getItem(position);
        lc.getTvCellLabel().setText(msg.getMsg());
        return convertView;
    }

    private static class ListCell{
        //这样做的目的是为了防止外界修改列表项对象中的数据
        public ListCell(TextView tvCellLabel){
            this.tvCellLabel=tvCellLabel;

        }
        private TextView tvCellLabel;

        public TextView getTvCellLabel() {
            return tvCellLabel;
        }
    }

}
