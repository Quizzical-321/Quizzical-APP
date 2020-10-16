package com.cpen321.quizzical.MainMenuFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cpen321.quizzical.PageFunctions.QuizActivity;
import com.cpen321.quizzical.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class QuizFragment extends Fragment {

    boolean is_Instructor;
    int class_code;

    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    FloatingActionButton fab, edit_fab, note_fab;

    boolean clicked = false;
    Button quizStartButton;

    public QuizFragment(boolean is_Instructor) {
        this.is_Instructor = is_Instructor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    private void setupQuiz() {
        Intent quizIntent = new Intent(getActivity(), QuizActivity.class);
        quizIntent.putExtra(getString(R.string.Question_Num), 0);
        startActivity(quizIntent);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        class_code = getContext().getSharedPreferences(getString(R.string.curr_login_user), Context.MODE_PRIVATE).
                getInt(getString(R.string.class_code), 0);
        //if class code is 0, we need to prompt the user to
        // create a class if instructor
        // join a class if student

        if (is_Instructor) {
            return inflater.inflate(R.layout.fragment_quiz_teacher, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_quiz, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //may need to separate for teacher and students
        quizStartButton = view.findViewById(R.id.quiz_fragment_go_to_quiz_button);
        quizStartButton.setOnClickListener(v -> setupQuiz());

        if (is_Instructor) {
            onTeacherViewCreated();
        }
    }

    private void onTeacherViewCreated() {
        rotateOpen = AnimationUtils.loadAnimation(this.getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this.getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this.getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this.getContext(), R.anim.to_bottom_anim);

        fab = getView().findViewById(R.id.fab);
        edit_fab = getView().findViewById(R.id.edit_fab);
        note_fab = getView().findViewById(R.id.note_fab);

        fab.setOnClickListener(v -> onAddButtonClicked());


        edit_fab.hide();
        edit_fab.setOnClickListener(v -> Toast.makeText(this.getContext(), "Add quiz button clicked", Toast.LENGTH_SHORT).show());
        note_fab.hide();
        note_fab.setOnClickListener(v -> Toast.makeText(this.getContext(), "Note button clicked", Toast.LENGTH_SHORT).show());

    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            edit_fab.show();
            note_fab.show();
            edit_fab.setClickable(true);
            note_fab.setClickable(true);
        } else {
            edit_fab.hide();
            note_fab.hide();
            edit_fab.setClickable(false);
            note_fab.setClickable(false);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            edit_fab.startAnimation(fromBottom);
            note_fab.startAnimation(fromBottom);
            fab.startAnimation(rotateOpen);
        } else {
            edit_fab.startAnimation(toBottom);
            note_fab.startAnimation(toBottom);
            fab.startAnimation(rotateClose);
        }
    }
}
