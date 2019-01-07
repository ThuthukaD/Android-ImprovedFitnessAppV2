package com.example.desel.improvedfitnessappv2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter
        <HorizontalRecyclerViewAdapter.ViewHolder>
{
    // VARIABLES
    // Debugging
    private static final String TAG = "HorizontalRecyclerViewA";

    // Context - for glide
    Context mContextHorizontal;

    // Array Lists
    private ArrayList<String> mNamesHorizontal = new ArrayList();
    private ArrayList<String> mImageUrlsHorizontal = new ArrayList();

    // Constructor for variables
    public HorizontalRecyclerViewAdapter(Context mContextHorizontal,
                                         ArrayList<String> mNamesHorizontal,
                                         ArrayList<String> mImageUrlsHorizontal)
    {
        this.mContextHorizontal = mContextHorizontal;
        this.mNamesHorizontal = mNamesHorizontal;
        this.mImageUrlsHorizontal = mImageUrlsHorizontal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Log for debugging
        Log.d(TAG, "onCreateViewHolder: Inflated");

        // Inflates/Creates each individual View Item for the list
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_layout_list_item, parent,
                        false);
        return new ViewHolder(view);
    }

    // Puts the data/widgets into each inflated view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        // Log for debugging
        Log.d(TAG, "onBindViewHolder: Data called");

        // Getting and placing images in correct place
        Glide.with(mContextHorizontal)
                .asBitmap()
                .load(mImageUrlsHorizontal.get(position))
                .into(holder.civImageVertical);

        holder.tvNameHorizontal.setText(mNamesHorizontal.get(position));

        // When image is selected
        holder.civImageVertical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Makes Cardio open an intent instead
                if (position == 0)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Map Selected");

                    Intent intent = new Intent(mContextHorizontal,
                            MapsActivity.class);
                    mContextHorizontal.startActivity(intent);
                }
                else if (position == 1)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: BMI Selected");

                    // Change to BMI Activity
                    Intent intent = new Intent(mContextHorizontal,
                            DataActivity.class);
                    mContextHorizontal.startActivity(intent);
                }
                else if (position == 2)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Metric/Imperial Selected");

                    Intent intent = new Intent(mContextHorizontal,
                            MetricActivity.class);
                    mContextHorizontal.startActivity(intent);
                }
                else if (position == 3)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Diet Selected");

                    Intent intent = new Intent(mContextHorizontal,
                            DietActivity.class);
                    mContextHorizontal.startActivity(intent);
                }
                else if (position == 4)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Settings Selected");

                    Intent intent = new Intent(mContextHorizontal,
                            SettingsActivity.class);
                    mContextHorizontal.startActivity(intent);
                }
                else
                {
                    try
                    {
                        // Log for debugging
                        Log.d(TAG, "onClick: Clicked on image"
                                + mNamesHorizontal);
                        Toast.makeText(mContextHorizontal, "Selected "
                                        + mNamesHorizontal.get(position),
                                Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "onClick: Toast failed, check HRVA "
                                + "- Line 126");
                        Toast.makeText(mContextHorizontal, "ERROR: Check Logs",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Gets the number of slots required for the Recycler View
    @Override
    public int getItemCount()
    {
        // can use mNames or mImageUrls as they both give the same size
        return mNamesHorizontal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // VARIABLES
        CircleImageView civImageVertical;
        TextView tvNameHorizontal;

        // Constructor - Alt + Insert [select none]
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Linking variables to XML - does not need the same name. For continence
            civImageVertical = itemView.findViewById(R.id.civImageHorizontal);
            tvNameHorizontal = itemView.findViewById(R.id.tvNameHorizontal);

        }
    }
}