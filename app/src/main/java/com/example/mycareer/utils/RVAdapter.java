package com.example.mycareer.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycareer.R;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CourseViewHolder> implements View.OnClickListener {

    private static final String TAG = RVAdapter.class.getSimpleName();
    private List<Course> dataModel;
    Context mContext;

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
        courseViewHolder.courseDate.setText(("Not done yet".compareTo(c.getScore())==0) ? "" : dataStr);
        courseViewHolder.dateLayout.setVisibility(("Not done yet".compareTo(c.getScore())==0) ? View.GONE : View.VISIBLE);
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
        Object object= getItem(position);
        final Course c=(Course)object;

        switch (v.getId())
        {
            case R.id.edit_course:
                editCourse(c, v, position);
                break;
            case R.id.delete_course:
                deleteCourse(c, v);
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

    private void deleteCourse(final Course c, final View v){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Delete " + c.getName());
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Profile.getInstance().getFirebaseReference().child("courses").child(c.getName()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext,"Course " + c.getName() + " successfully deleted.", Toast.LENGTH_SHORT).show();
                                        dataModel.remove(c);
                                        Profile.getInstance().removeCourse(c);
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //
                                    }
                                });
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void editCourse(final Course course, View v, int pos) {
        // custom dialog
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.custom_dialog);

        // set the custom dialog components
        final TextView title = dialog.findViewById(R.id.dialog_title);
        final EditText courseName = dialog.findViewById(R.id.course_name);
        final EditText courseCredit = dialog.findViewById(R.id.course_credit);
        final CalendarView courseDate = dialog.findViewById(R.id.course_date);
        final TextView errorField = dialog.findViewById(R.id.error_field);
        final Calendar calendar = Calendar.getInstance();
        final Spinner courseGrade = dialog.findViewById(R.id.spinner_grade);

        courseGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int index = parentView.getSelectedItemPosition();
                if(index == 0)
                    courseDate.setVisibility(View.GONE);
                else
                    courseDate.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        title.setText(R.string.update_course);
        courseName.setText(course.getName());
        courseName.setEnabled(false);
        courseCredit.setText(course.getCredit());
        courseDate.setDate(course.getDate().getTime());
        courseGrade.setSelection(0);

        Button updateButton = dialog.findViewById(R.id.btn_save);
        Button cancelButton = dialog.findViewById(R.id.btn_cancel);
        updateButton.setText(R.string.btn_update);
        // if button is clicked, close the custom dialog
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(evaluateCourse(courseName.getText().toString(),courseCredit.getText().toString())) {
                    Log.d(TAG, "Update btn Pressed: "
                            + courseName.getText() + " - "
                            + courseCredit.getText() + " - "
                            + calendar.getTime().toString() + " - "
                            + courseGrade.getSelectedItem().toString());

                    final Course c = new Course(
                            courseName.getText().toString().trim(),
                            courseGrade.getSelectedItem().toString(),
                            courseCredit.getText().toString().trim(),
                            calendar.getTime());

                    Profile.getInstance().getFirebaseReference().child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange.");
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                if (data.getKey().equals(course.getName())) {
                                    Log.d(TAG, "Course updated in DB.");
                                    Profile.getInstance().getFirebaseReference().child("courses").child(c.getName()).setValue(c);
                                    Toast.makeText(v.getContext(), "Course " + course.getName() + " updated.", Toast.LENGTH_SHORT).show();
                                    dataModel.remove(pos);
                                    dataModel.add(pos,c);

                                    Profile.getInstance().updateCourse(c, pos);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                    return;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "DatabaseError.");
                            Toast.makeText(v.getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    errorField.setVisibility(View.VISIBLE);
                    errorField.setText(R.string.field_mandatory);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cancel btn Pressed: dismiss.");
                dialog.dismiss();
            }
        });

        courseDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                calendar.set(year, month, day, 0, 0);
            }
        });

        dialog.show();
    }
    private boolean evaluateCourse(String name, String credit){
        if(TextUtils.isEmpty(name.trim()) || TextUtils.isEmpty(credit.trim()))
            return false;
        return true;
    }
}
