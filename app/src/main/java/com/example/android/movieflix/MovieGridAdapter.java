package com.example.android.movieflix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shwetagk on 24/07/2017.
 */

public class MovieGridAdapter extends ArrayAdapter<MoviePoster> {

    private GridItemClickListener mgridItemClickListener;
    private int mnumberItems;
    private ArrayList<MoviePoster> mposterList;

    public interface GridItemClickListener{
        void onGridItemClick(int clickedItemIndex);
    }


    public MovieGridAdapter(Context context, List<MoviePoster> posters,int numberItems) {
        super(context, 0, posters);
        mgridItemClickListener=(GridItemClickListener)context;
        mnumberItems=numberItems;
        mposterList=(ArrayList<MoviePoster>)posters;



    }

    @Override
    public int getCount() {
        return mnumberItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup viewGroup) {
        MovieHolder mholder=null;
        Context context = viewGroup.getContext();
        if(convertView==null) {

            int layoutForGridItem = R.layout.movie_item_poster;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;
            convertView=inflater.inflate(layoutForGridItem,viewGroup,shouldAttachToParentImmediately);
            mholder=new MovieHolder(convertView,position);
            convertView.setTag(mholder);
        }
        else{
            mholder=(MovieHolder)convertView.getTag();
        }
            MoviePoster mposter=mposterList.get(position);
        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_poster);
           //Picasso.with(context).load(mposter.mimageUrl.toString()).into(mholder.mmovieposter);
        Picasso.with(context).load(mposter.mimageUrl).into(iconView);





        return convertView;
    }


     public class MovieHolder implements View.OnClickListener{
          //ImageView mmovieposter;
         private int mclickedPosition;
         public MovieHolder(View view,int position){
             //mmovieposter=(ImageView)view.findViewById(R.id.movie_poster);
             mclickedPosition=position;
             view.setOnClickListener(this);
         }

         @Override
         public void onClick(View v) {
            mgridItemClickListener.onGridItemClick(mclickedPosition);
         }
     }

}
