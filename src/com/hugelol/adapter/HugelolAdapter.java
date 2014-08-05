package com.hugelol.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugelol.R;
import com.hugelol.model.Hugelol;
import com.hugelol.utils.ImageUtils;

public class HugelolAdapter extends ArrayAdapter<Hugelol>{
    private final Context context;
    private final List<Hugelol> hugelols; 
    
    public HugelolAdapter(Context context, int resource, List<Hugelol> hugelols) {
        super(context, resource, hugelols);
        this.context = context;
        this.hugelols = hugelols;
    }

    
    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_layout, parent, false);
        Hugelol lolPost = hugelols.get(position);
        TextView title = (TextView)rowView.findViewById(R.id.title);
        if(lolPost.getTitle() != null){
            title.setText(lolPost.getTitle());
        }
        
        int type = lolPost.getType();
        ImageView image = (ImageView)rowView.findViewById(R.id.image);
        if(lolPost.getImage() != null){
            image.setBackground(ImageUtils.byteArrayToDrawable(context, lolPost.getImage()));
        }
        if(type == 2){
            ImageView gifIcon = (ImageView)rowView.findViewById(R.id.image_gif);
            gifIcon.setVisibility(View.VISIBLE);
        }
        
        TextView votes = (TextView)rowView.findViewById(R.id.votes);
        if(lolPost.getLikes() != null && lolPost.getDislikes() != null){
            String votesString = lolPost.getLikes() + " / " + lolPost.getDislikes();
            votes.setText(votesString);
        }
        
        TextView userName = (TextView)rowView.findViewById(R.id.user_name);
        if(lolPost.getUserName() != null){
            userName.setText(lolPost.getUserName());
        }
        
        
        return rowView;
    }
    
}
