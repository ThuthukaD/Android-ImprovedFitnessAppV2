package com.example.desel.improvedfitnessappv2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter
        <StaggeredRecyclerViewAdapter.ViewHolder>
{
    // VARIABLES
    // Debugging
    private static final String TAG = "StaggeredRecyclerViewAd";

    // Other
    private Context mContext;

    // Arrays
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    public StaggeredRecyclerViewAdapter(Context mContext, ArrayList<String> mNames,
                                        ArrayList<String> mImageUrls)
    {
        this.mContext = mContext;
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Log for debugging
        Log.d(TAG, "onCreateViewHolder: Inflated");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_grid_item, parent, false);

        // Inflates/Creates each individual View Item for the list
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    // Puts the data/widgets into each inflated view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        // Log for debugging
        Log.d(TAG, "onBindViewHolder: Called");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        // Getting and placing images in correct place
        Glide.with(mContext)
                .load(mImageUrls.get(position))
                .apply(requestOptions)
                .into(holder.image);

        holder.name.setText(mNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Log for debugging
                Log.d(TAG, "onClick: Clicked on: " + mNames.get(position));

                // Makes Cardio open an intent instead
                if (position == 2)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Cardio Selected");

                    Intent intent = new Intent(mContext, CardioMainActivity.class);
                    // Code for potential future use to transfer image and text to new
                    // Activity
//                    intent.putExtra("image_url", mImageUrls.get(position));
//                    intent.putExtra("image_name", mNames.get(position));
                    mContext.startActivity(intent);
                }
                else
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Other than Cardio selected");
                    Toast.makeText(mContext, mNames.get(position) + " Locked",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Gets the number of slots required for the Recycler View
    @Override
    public int getItemCount()
    {
        // can use mNames or mImageUrls as they both give the same size
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // VARIABLES
        ImageView image;
        TextView name;

        // Constructor - Alt + Insert [select none]
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Linking variables to XML - does not need the same name. For continence
            this.image = itemView.findViewById(R.id.imgViewWidget);
            this.name = itemView.findViewById(R.id.nameWidget);
        }
    }
}
