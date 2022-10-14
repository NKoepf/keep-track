package de.nkoepf.keeptrack.androidapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StorageOverviewAdapter extends RecyclerView.Adapter<StorageOverviewAdapter.StorageOverviewHolder> {

    public static class StorageOverviewHolder extends RecyclerView.ViewHolder{



        public StorageOverviewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @NonNull
    @Override
    public StorageOverviewAdapter.StorageOverviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StorageOverviewAdapter.StorageOverviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
