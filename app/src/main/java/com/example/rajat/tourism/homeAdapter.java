package com.example.rajat.tourism;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;


public class homeAdapter extends RecyclerView.Adapter<homeAdapter.ViewHolder>{

    ArrayList<HashMap<String, Object>> al_details;
   home fragment;
    Context mContext;

    public static boolean boolean_id;
    boolean setnews = false;

    ImageView iv_image;
    TextView tv_title;

    String URLTOIMAGE="urlToImage";
    String DESCRIPTIONz="description";
    String AUTHOR="author";
    String TITLE="title";
    String URL_WEB="url";
    String PUBLISHEDAT="publishedAt";

    Snackbar snackbar;

    DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;
    ProgressDialog mProgressDialog;
    private Firebase mRoofRef;
    public Uri mImgUri= null;
    FirebaseAuth mAuth;

    public String  atv, des;
    public static String time;
    FirebaseAuth firebaseauth;
    String userid , timea;




    public homeAdapter(ArrayList<HashMap<String,Object>> al_details, home fragment, Context context) {

        this.al_details = al_details;
        this.fragment=fragment;
        this.mContext=context;
    }


    @Override
    public homeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);

        return viewHolder1;
    }



    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image ;
        public ImageView share ,bookmark;
        TextView tv_title,vicinity;
        RelativeLayout rl_click;
        Button button;



        public ViewHolder(View v) {

            super(v);

            iv_image = v.findViewById(R.id.imageview);
            tv_title = v.findViewById(R.id.tv1);
            rl_click = v.findViewById(R.id.relmarket);
            vicinity = v.findViewById(R.id.vicinity);


        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder vholder, final int position) {



        try {
            Glide.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+al_details.get(position).get("photo_reference")+"&key=AIzaSyBTYpsEx0f8FyKSRScuIFOL5_MOfQrKPRQ")
                    .thumbnail( Glide.with(mContext).load(R.drawable.loading_icon))
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .listener(new RequestListener<Object, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.d("headline glide issue ","issue on position"+al_details.get(position).get("name"));
                            vholder.iv_image.setImageResource(R.drawable.noimage);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("headline glide success","Success on position"+al_details.get(position).get("name"));
                            vholder.iv_image.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(vholder.iv_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        vholder.tv_title.setText(al_details.get(position).get("name").toString());
        vholder.vicinity.setText(al_details.get(position).get("vicinity").toString());

        vholder.rl_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fragment.click(position);
                fragmentJump( al_details.get(position).get("place_id").toString());
                Toast.makeText(mContext, al_details.get(position).get("place_id").toString(), Toast.LENGTH_SHORT).show();


            }
        });

    }



    private void fragmentJump(String mItemSelected) {
       place_detail mFragment = new place_detail();
       Bundle mBundle =   new Bundle();
        mBundle.putString("placeid",mItemSelected);
        mFragment.setArguments(mBundle);
        switchContent(R.id.content, mFragment);
    }


    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }


    @Override
    public int getItemCount() {

        return al_details.size();
    }



}

