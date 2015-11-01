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
import shengshoubo.example.com.secret.net.Comments;

/**
 * Created by shengshoubo on 2015/9/1.
 */
public class AtyMessageCommentListAdapter extends BaseAdapter {

    private List<Comments> comments=new ArrayList<Comments>();
    private Context context;

    public AtyMessageCommentListAdapter(Context context) {
        this.context=context;
    }

    public Context getContext() {
        return context;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void addAll(List<Comments> data){
        comments.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        comments.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comments getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.aty_comments_list_cell,null);
            convertView.setTag(new ListCell((TextView) convertView.findViewById(R.id.tvCellLabel)));
        }
        ListCell lc= (ListCell) convertView.getTag();

        Comments comments=getItem(position);
        lc.getTvCellLabel().setText(comments.getContent());
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
