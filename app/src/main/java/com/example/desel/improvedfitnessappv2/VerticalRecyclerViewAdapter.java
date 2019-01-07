package com.example.desel.improvedfitnessappv2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter
        .ViewHolder>
{
    // For debugging
    private static final String TAG = "VerticalRecyclerViewAda";

    // Variables
    private Context mContextVertical;
    private ArrayList<String> mNamesVertical = new ArrayList<>();
    private ArrayList<String> mImageUrlsVertical = new ArrayList<>();

    public VerticalRecyclerViewAdapter(Context mContextVertical, ArrayList<String> mNamesVertical,
                                       ArrayList<String> mImageUrlsVertical)
    {
        this.mNamesVertical = mNamesVertical;
        this.mImageUrlsVertical = mImageUrlsVertical;
        this.mContextVertical = mContextVertical;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        // Check here for issues!!!!!!!  (ViewGroup/parent)

        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.vertical_layout_list_item, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContextVertical)
                .asBitmap()
                .load(mImageUrlsVertical.get(position))
                .into(holder.civImageVertical);

        holder.tvImageNameVertical.setText(mNamesVertical.get(position));
        holder.parent_layout_vertical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d(TAG, "onClick: clicked on: " + mNamesVertical.get(position));

                if (position == 0)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Walk Outdoor Selected");

                    Intent intent = new Intent(mContextVertical,
                            RunMapActivity.class);
                    mContextVertical.startActivity(intent);
                }
                else if (position == 2)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Jog Outdoor Selected");

                    Intent intent = new Intent(mContextVertical,
                            RunMapActivity.class);
                    mContextVertical.startActivity(intent);
                }
                else if (position == 4)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Run Outdoor Selected");

                    Intent intent = new Intent(mContextVertical,
                            RunMapActivity.class);
                    mContextVertical.startActivity(intent);
                }
                else if (position == 6)
                {
                    // Log for debugging
                    Log.d(TAG, "onClick: Marathon Outdoor Selected");

                    Intent intent = new Intent(mContextVertical,
                            RunMapActivity.class);
                    mContextVertical.startActivity(intent);
                }
                else
                {
                    // For changing activities
                    try
                    {
                        Intent intent = new Intent(mContextVertical, GymCounterActivity.class);
                        mContextVertical.startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        // Determines how many items show up
        return mNamesVertical.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView civImageVertical;
        TextView tvImageNameVertical;
        RelativeLayout parent_layout_vertical;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            civImageVertical = itemView.findViewById(R.id.civImageHorizontal);
            tvImageNameVertical = itemView.findViewById(R.id.tvImageNameVertical);
            parent_layout_vertical = itemView.findViewById(R.id.parent_layout_vertical);
        }
    }
}
