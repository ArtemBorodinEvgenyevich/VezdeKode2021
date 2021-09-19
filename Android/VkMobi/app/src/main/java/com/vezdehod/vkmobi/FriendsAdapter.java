package com.vezdehod.vkmobi;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.vezdehod.vkmobi.models.User;

import java.util.ArrayList;

public class FriendsAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private  Context context;
    private  LayoutInflater inflater;

    public FriendsAdapter(Context context, ArrayList<User> users){
        super(context, R.layout.friends_layout, users);
        this.users = users;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.friends_layout, parent, false);

        ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
        TextView nameView = (TextView) view.findViewById(R.id.full_name);

        User user = users.get(position);

        try {
            Picasso.with(context)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.warning)
                    .error(R.drawable.placeholder)
                    .into(avatarView);
        }
        catch (Exception e){}
        nameView.setText(user.getFirstName() + " " + user.getLastName());

        return view;
    }
}

