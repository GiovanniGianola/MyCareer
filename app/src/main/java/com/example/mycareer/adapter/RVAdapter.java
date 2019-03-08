package com.example.mycareer.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycareer.R;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.Profile;
import com.example.mycareer.utils.Costants;
import com.example.mycareer.utils.UtilsConversions;
import com.example.mycareer.view.CourseFragmentView;
import com.example.mycareer.view.fragment.CoursesFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CourseViewHolder> implements View.OnClickListener {

    private static final String TAG = RVAdapter.class.getSimpleName();
    private List<Course> dataModel;
    Context mContext;
    private CourseFragmentView courseFragmentView;

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView courseName;
        TextView courseDate;
        TextView courseCredit;
        TextView courseGrade;
        ImageView edit;
        ImageView delete;
        ImageView icCredit;
        ConstraintLayout dateLayout;

        CourseViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            courseName = itemView.findViewById(R.id.c_name);
            courseDate = itemView.findViewById(R.id.c_date);
            courseCredit = itemView.findViewById(R.id.c_credit);
            courseGrade = itemView.findViewById(R.id.c_grade);
            edit = itemView.findViewById(R.id.edit_course);
            delete = itemView.findViewById(R.id.delete_course);
            dateLayout = itemView.findViewById(R.id.date_layout);
            icCredit = itemView.findViewById(R.id.ic_credit);
        }
    }

    public RVAdapter(List<Course> data, Context context) {
        this.dataModel = data;
        this.mContext=context;
    }

    public void attachView(CoursesFragment view) {
        courseFragmentView = view;
    }

    public void detachView() {
        courseFragmentView = null;
    }

    public RVAdapter(Context context){
        this.mContext=context;
    }

    public void setDataModel(List<Course> dataModel){
        this.dataModel = dataModel;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_card, viewGroup, false);
        CourseViewHolder cvh = new CourseViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        Course c = dataModel.get(i);

        Locale.setDefault(Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String dataStr = sdf.format(c.getDate());

        courseViewHolder.courseName.setText(c.getName());
        courseViewHolder.courseDate.setText(((Costants.Strings.NOT_DONE_YET).compareTo(c.getScore())==0) ? "" : dataStr);
        courseViewHolder.dateLayout.setVisibility(((Costants.Strings.NOT_DONE_YET).compareTo(c.getScore())==0) ? View.GONE : View.VISIBLE);
        courseViewHolder.courseCredit.setText(c.getCredit());
        courseViewHolder.courseGrade.setText(c.getScore());
        courseViewHolder.edit.setOnClickListener(this);
        courseViewHolder.edit.setTag(i);
        courseViewHolder.delete.setOnClickListener(this);
        courseViewHolder.delete.setTag(i);
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(final View v) {

        int position=(Integer) v.getTag();
        Course object= getItem(position);
        final Course c = object;

        switch (v.getId())
        {
            case R.id.edit_course:
                editCourse(c);
                break;
            case R.id.delete_course:
                deleteCourse(c);
                break;
        }
    }

    public Course getItem(int position) {
        return dataModel.get(position);
    }

    public void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    //----------------------------------------------------------------------------------------------

    private void deleteCourse(final Course course){
        courseFragmentView.initAlerDialogOnDeleteCourse(course);
    }

    private void editCourse(Course course) {
        courseFragmentView.initCustomDialog(mContext.getResources().getString(R.string.update_course), course);
    }
}
