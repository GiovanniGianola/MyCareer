package com.example.mycareer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.PredictionEntry;
import com.example.mycareer.view.PredictionFragmentView;
import com.example.mycareer.view.fragment.CoursesFragment;
import com.example.mycareer.view.fragment.PredictionFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PredictionsAdapter extends RecyclerView.Adapter<PredictionsAdapter.PredictionsViewHolder>{
    private Context mContext;
    private PredictionFragmentView predictionFragmentView;
    private List<PredictionEntry> dataModel;

    public static class PredictionsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView_gradeEntry;
        TextView textView_newAverageEntry;
        TextView textView_variationEntry;

        PredictionsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            textView_gradeEntry = itemView.findViewById(R.id.textView_gradeEntry);
            textView_newAverageEntry = itemView.findViewById(R.id.textView_newAverageEntry);
            textView_variationEntry = itemView.findViewById(R.id.textView_variationEntry);
        }
    }

    public PredictionsAdapter(Context context, List<PredictionEntry> predictionEntryList) {
        this.mContext=context;
        this.dataModel = predictionEntryList;
    }

    public PredictionsAdapter(Context context) {
        this.mContext=context;
    }

    public void attachView(PredictionFragment view) {
        predictionFragmentView = view;
    }

    public void detachView() {
        predictionFragmentView = null;
    }

    public void setDataModel(List<PredictionEntry> dataModel){
        this.dataModel = dataModel;
    }



    @NonNull
    @Override
    public PredictionsAdapter.PredictionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prediction_card, viewGroup, false);
        PredictionsAdapter.PredictionsViewHolder cvh = new PredictionsAdapter.PredictionsViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionsAdapter.PredictionsViewHolder holder, int position) {
        PredictionEntry predictionEntry = dataModel.get(position);

        holder.textView_gradeEntry.setText(predictionEntry.getGrade());
        holder.textView_newAverageEntry.setText(String.format("%.2f", predictionEntry.getNewAvg()));

        String var = String.format("%.2f", predictionEntry.getVariation());
        holder.textView_variationEntry.setTextColor(Color.GRAY);
        if(predictionEntry.getVariation() > 0) {
            var = "+" + var;
            holder.textView_variationEntry.setTextColor(Color.GREEN);
        }else if(predictionEntry.getVariation() < 0) {
            //var = "- " + var;
            holder.textView_variationEntry.setTextColor(Color.RED);
        }
        holder.textView_variationEntry.setText(var);
    }

    @Override
    public int getItemCount() {
        if(dataModel != null)
            return dataModel.size();
        return 0;
    }

    public void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
