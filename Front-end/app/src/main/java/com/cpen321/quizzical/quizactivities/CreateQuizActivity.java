package com.cpen321.quizzical.quizactivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cpen321.quizzical.R;
import com.cpen321.quizzical.TestPage;
import com.cpen321.quizzical.data.CourseCategory;
import com.cpen321.quizzical.utils.ChoicePair;
import com.cpen321.quizzical.utils.QuestionPackage;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cpen321.quizzical.utils.OtherUtils.getBitmapFromUrl;

public class CreateQuizActivity extends AppCompatActivity {

    private LinearLayout questionCreateLayout;

    private Spinner categoryList;

    private EditText questionInput;

    private CheckBox questionHasPic;
    private ImageButton takePictureButton;

    private List<String> answerPics;
    private LinearLayout answersLayout;
    private List<LinearLayout> answerRows;

    private Button answerInputButton;

    private ImageButton addQuestionButton;

    private List<String> questionList;
    private QuestionPackage questionPackage;

    private CourseCategory currCategory;
    private String currQuestion;
    private Boolean currHasPic;
    private String currPicSrc;
    private List<ChoicePair> currChoices;
    private int currCorrectAnsNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        questionCreateLayout = findViewById(R.id.question_create_layout);

        categoryList = findViewById(R.id.category_list);
        String[] categories = getResources().getStringArray(R.array.course_category_array);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categoryList.setAdapter(categoryAdapter);
        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int selectedPosition = categoryList.getSelectedItemPosition();
                switch (selectedPosition) {
                    case 0:
                        currCategory = CourseCategory.Math;
                        break;
                    case 1:
                        currCategory = CourseCategory.English;
                        break;
                    case 2:
                        currCategory = CourseCategory.QuantumPhysic;
                        break;
                    default:
                        currCategory = CourseCategory.Misc;
                        break;
                }
                Toast.makeText(getBaseContext(), currCategory.toString(), Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        questionInput = findViewById(R.id.question_input);
        questionInput.setOnClickListener(v -> currQuestion = questionInput.getText().toString());

        questionHasPic = findViewById(R.id.question_has_pic);
        questionHasPic.setOnClickListener(v -> currHasPic = questionHasPic.isChecked());

        takePictureButton = findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(this, TestPage.class);
            startActivity(takePictureIntent);
        });

        answerPics = new ArrayList<>();
        answersLayout = findViewById(R.id.answers_layout);
        answerRows = new ArrayList<>();

        answerInputButton = findViewById(R.id.answer_input_button);
        answerInputButton.setOnClickListener(v -> addNewAnswer());

        addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(v -> addQuestionToList());

        currCategory = CourseCategory.Misc;
        currQuestion = "";
        currHasPic = false;
        currPicSrc = "";
        currChoices = new ArrayList<>();
        currCorrectAnsNum = 0;
    }

    private void addNewAnswer() {
        final String[] answer = {""};
        String pic = ""; // some default picture
        final boolean[] isPic = {false};
        final boolean[] isAnsValid = {true};

        final LinearLayout.LayoutParams[] layoutParams = {new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)};
        layoutParams[0].setMargins(10, 5, 10, 5);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView answerText = new TextView(this);
        answerText.setText("Enter answer");
        answerText.setLayoutParams(layoutParams[0]);

        EditText answerInput = new EditText(this);
        answerInput.setLayoutParams(layoutParams[0]);
        answerInput.setHint("What is 1+1");
        answerInput.setInputType(InputType.TYPE_CLASS_TEXT);
        answerInput.setText(answer[0]);
        answerInput.setMaxLines(1);

        ImageButton answerPic = new ImageButton(this);
        answerPic.setImageResource(android.R.drawable.ic_menu_camera);
        answerPic.setVisibility(View.GONE);
        answerPic.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(this, TestPage.class);
            startActivity(takePictureIntent);
        });

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(false);
        checkBox.setText("Picture ");
        checkBox.setOnClickListener(v -> {
            isPic[0] = checkBox.isChecked();
            answerInput.setVisibility(checkBox.isChecked() ? View.GONE : View.VISIBLE);
            answerPic.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
        });

        layout.addView(answerText);
        layout.addView(checkBox);
        layout.addView(answerInput);
        layout.addView(answerPic);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this).setTitle("Answer Input")
                .setView(layout)
                .setPositiveButton(R.string.UI_submit, ((dialogInterface, i) -> {}));

        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            answer[0] = answerInput.getText().toString();
            isAnsValid[0] = !answer[0].equals("");
            if (isAnsValid[0]) {
                for (int i = 0; i < answersLayout.getChildCount(); i++) {
                    View child = answersLayout.getChildAt(i);
                    for (int j = 0; j < ((LinearLayout) child).getChildCount(); j++) {
                        View rowChild = ((LinearLayout) child).getChildAt(j);
                        if (rowChild instanceof TextView && !(rowChild instanceof Button)) {
                            if (answer[0].equals(((TextView) rowChild).getText().toString())) {
                                isAnsValid[0] = false;
                                Log.d("hi_op","notvalid "+ ((TextView) rowChild).getText().toString());
                                break;
                            }
                        }
                    }
                    if (!isAnsValid[0]) {
                        break;
                    }
                }
            }
            Log.d("hi_op","valid? "+ isAnsValid[0]);

            if (isAnsValid[0]) {
                ChoicePair newAnswer = new ChoicePair(isPic[0], isPic[0] ? pic : answer[0]);

                Log.d("Hi_op", "" + newAnswer.isPic() + " " + answer[0]);
                currChoices.add(newAnswer);

                layoutParams[0] = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(layoutParams[0]);
                // I want a (index num textview?), checkbox for correct, textview or imageview, edit button, delete
                //edit opens dialog box, everything filled in
                CheckBox isCorrect = new CheckBox(this);
                isCorrect.setLayoutParams(layoutParams[0]);
                isCorrect.setChecked(currCorrectAnsNum == answersLayout.getChildCount());
                isCorrect.setEnabled(currCorrectAnsNum != answersLayout.getChildCount());
                // if checked, uncheck all other checkbox, else do nothing.
                isCorrect.setOnClickListener(u -> {
                    Log.d("Hi_op", "checked");
                    if (isCorrect.isChecked()) {
                        for (int i = 0; i < answersLayout.getChildCount(); i++) {
                            View child = answersLayout.getChildAt(i);
                            for (int j = 0; j < ((LinearLayout) child).getChildCount(); j++) {
                                View rowChild = ((LinearLayout) child).getChildAt(j);
                                if (rowChild instanceof CheckBox) {
                                    Log.d("Hi_op", "   yay2");
                                    ((CheckBox) rowChild).setChecked(rowChild.equals(isCorrect));
                                    rowChild.setEnabled(!rowChild.equals(isCorrect));
                                    if (rowChild.equals(isCorrect)) {
                                        currCorrectAnsNum = i;
                                    }
                                }
                            }
                        }
                    }
                });

                TextView answerContent = new TextView(this);
                answerContent.setText(answer[0]);
                answerContent.setLayoutParams(layoutParams[0]);

                ImageView answerImage = new ImageView(this);
                try {
                    if (!pic.equals("")) {
                        answerImage.setImageBitmap(getBitmapFromUrl(pic));
                    } else {
                        answerImage.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } catch (Exception e) {
                    answerImage.setImageResource(android.R.drawable.ic_menu_gallery);
                }
                answerImage.setMaxHeight(200);
                answerImage.setLayoutParams(layoutParams[0]);
                answerImage.setOnClickListener(x -> {
                    final LinearLayout.LayoutParams[] ansImgLayoutParams = {new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)};

                    LinearLayout ansImgLayout = new LinearLayout(this);
                    ansImgLayout.setOrientation(LinearLayout.VERTICAL);

                    ImageView ansImg = new ImageView(this);
                    ansImg.setMaxWidth(350);
                    ansImg.setMaxHeight(750);
                    ansImg.setLayoutParams(ansImgLayoutParams[0]);
                    try {
                        if (!pic.equals("")) {
                            ansImg.setImageBitmap(getBitmapFromUrl(pic));
                        } else {
                            ansImg.setImageResource(android.R.drawable.ic_menu_gallery);
                        }
                    } catch (Exception e) {
                        ansImg.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                    ansImgLayout.addView(ansImg);

                    AlertDialog.Builder ansImgAlertBuilder = new AlertDialog.Builder(this).setTitle("Preview")
                            .setView(ansImgLayout)
                            .setNeutralButton("Close", ((dialogInterface, i) -> dialogInterface.dismiss()));

                    final AlertDialog ansImgAlertDialog = ansImgAlertBuilder.create();
                    ansImgAlertDialog.show();
                });

                Button editButton = new Button(this);
                editButton.setLayoutParams(layoutParams[0]);
                editButton.setText("Edit");
                editButton.setOnClickListener(w -> {
                    Log.d("Hi_op", "click");
                    editExistingQuestion(answersLayout.getChildCount()-1);
                });

                ImageButton deleteButton = new ImageButton(this);
                deleteButton.setLayoutParams(layoutParams[0]);
                deleteButton.setImageResource(android.R.drawable.ic_delete);
                deleteButton.setOnClickListener(w -> {
                    final LinearLayout.LayoutParams[] delLayoutParams = {new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)};
                    delLayoutParams[0].setMargins(10, 5, 10, 5);

                    LinearLayout delLayout = new LinearLayout(this);
                    delLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView sureText = new TextView(this);
                    sureText.setText("Are you sure you want to delete?");
                    sureText.setLayoutParams(delLayoutParams[0]);

                    delLayout.addView(sureText);

                    AlertDialog.Builder delAlertBuilder = new AlertDialog.Builder(this).setTitle("Delete Answer Choice?")
                            .setView(delLayout)
                            .setPositiveButton("Delete", ((dialogInterface, i) -> {}))
                            .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()));

                    final AlertDialog delAlertDialog = delAlertBuilder.create();
                    delAlertDialog.show();

                    delAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(y -> {
                        Log.d("Hi_op", "curans " + currCorrectAnsNum);
                        for (int j = 0; j < answersLayout.getChildCount(); j++) {
                            View child = answersLayout.getChildAt(j);
                            if (child.equals(row)) {
                                answersLayout.removeView(row);
                                currChoices.remove(j);

                                if (j == answersLayout.getChildCount()) {
                                    currCorrectAnsNum = 0;
                                } else if (j < currCorrectAnsNum) {
                                    currCorrectAnsNum--;
                                }
                                break;
                            }
                        }
                        for (int j = 0; j < answersLayout.getChildCount(); j++) {
                            View child = answersLayout.getChildAt(j);
                            Log.d("hi_op", "schiesse " + child + " " + row);
                            for (int k = 0; k < ((LinearLayout) child).getChildCount(); k++) {
                                View rowChild = ((LinearLayout) child).getChildAt(k);
                                if (rowChild instanceof CheckBox) {
                                    Log.d("Hi_op", " " + currCorrectAnsNum + " " + j);
                                    ((CheckBox) rowChild).setChecked(currCorrectAnsNum == j);
                                    rowChild.setEnabled(!(currCorrectAnsNum == j));
                                }
                            }
                        }
                        Log.d("hi_op","dismoo");
                        alertDialog.dismiss();
                        Log.d("hi_op","dismoo");
                    });
                });

                if (isPic[0]) {
                    answerImage.setVisibility(View.VISIBLE);
                    answerText.setVisibility(View.GONE);
                } else {
                    answerImage.setVisibility(View.GONE);
                    answerText.setVisibility(View.VISIBLE);
                }
                answerPics.add(pic);

                row.addView(isCorrect);
                row.addView(answerImage);
                row.addView(answerContent);
                row.addView(editButton);
                row.addView(deleteButton);
                answerRows.add(row);

                answersLayout.addView(row);
                Log.d("hi_op","dismoo");
                alertDialog.dismiss();
                Log.d("hi_op","dismoo");
            }
        });
    }

    private void editExistingQuestion(int index) {

        final String[] answer = {""};
        String pic = ""; // some default picture
        final boolean[] isPic = {false};

        LinearLayout realRow = null;


        isPic[0] = currChoices.get(index).isPic();
        if (isPic[0]) {
            pic = answerPics.get(index);
        }


        if (!isPic[0]) {
            View child = answersLayout.getChildAt(index);
            if (child instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) child).getChildCount(); j++) {
                    View rowChild = ((LinearLayout) child).getChildAt(j);
                    if (rowChild instanceof TextView && !(rowChild instanceof Button)) {
                        answer[0] = ((TextView) rowChild).getText().toString();
                    }
                }
            } else {
                Log.d("hi_op","nopebadindxx"+index);
            }
        }

        final LinearLayout.LayoutParams[] layoutParams = {new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)};
        layoutParams[0].setMargins(10, 5, 10, 5);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView answerText = new TextView(this);
        answerText.setText("Enter answer");
        answerText.setLayoutParams(layoutParams[0]);

        EditText answerInput = new EditText(this);
        answerInput.setLayoutParams(layoutParams[0]);
        answerInput.setHint("What is 1+1");
        answerInput.setInputType(InputType.TYPE_CLASS_TEXT);
        answerInput.setText(answer[0]);
        answerInput.setMaxLines(1);

        ImageButton answerPic = new ImageButton(this);
        answerPic.setImageResource(android.R.drawable.ic_menu_camera);
        answerPic.setVisibility(View.GONE);
        answerPic.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(this, TestPage.class);
            startActivity(takePictureIntent);
        });

        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(false);
        checkBox.setText("Picture ");
        checkBox.setOnClickListener(v -> {
            isPic[0] = checkBox.isChecked();
            answerInput.setVisibility(checkBox.isChecked() ? View.GONE : View.VISIBLE);
            answerPic.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
        });

        layout.addView(answerText);
        layout.addView(checkBox);
        layout.addView(answerInput);
        layout.addView(answerPic);

        //answersLayout.addView(layout);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this).setTitle("Edit Answer")
                .setView(layout)
                .setPositiveButton(R.string.UI_submit, ((dialogInterface, i) -> {}));

        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();


        String finalPic = pic;
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            answer[0] = answerInput.getText().toString();
            Log.d("hi_op","");
            View row = answersLayout.getChildAt(index);
            if (row instanceof LinearLayout) {
                Log.d("hi_op", ""+((LinearLayout) row).getChildCount());
                for (int i = 0; i < ((LinearLayout) row).getChildCount(); i++) {
                    View rowChild = ((LinearLayout) row).getChildAt(i);
//                    Log.d("hi_op",""+rowChild+" "+(rowChild instanceof TextView) + " "+ (rowChild instanceof ImageView)+ " "+(rowChild instanceof Button));
//                    Log.d("hi_op",""+rowChild+"text "+(rowChild instanceof TextView && !(rowChild instanceof Button)));
                    if (isPic[0]) {
                        if (rowChild instanceof ImageView && !(rowChild instanceof ImageButton)) {
                            try {
                                if (!finalPic.equals("")) {
                                    ((ImageView) rowChild).setImageBitmap(getBitmapFromUrl(finalPic));
                                } else {
                                    ((ImageView) rowChild).setImageResource(android.R.drawable.ic_menu_gallery);
                                }
                            } catch (Exception e) {
                                ((ImageView) rowChild).setImageResource(android.R.drawable.ic_menu_gallery);
                            }
                            rowChild.setVisibility(View.VISIBLE);
                        } else
                        if (rowChild instanceof TextView && !(rowChild instanceof Button)) {
                            rowChild.setVisibility(View.GONE);
                        }
                    } else {
                        if (rowChild instanceof TextView && !(rowChild instanceof Button)) {
                            Log.d("hi_op", "old "+((TextView) rowChild).getText());
                            ((TextView) rowChild).setText(answerInput.getText());
                            rowChild.setVisibility(View.VISIBLE);
                            Log.d("hi_op", "new "+((TextView) rowChild).getText());
                        } else if (rowChild instanceof ImageView && !(rowChild instanceof ImageButton)) {
                            rowChild.setVisibility(View.GONE);
                        }
                    }
                }
            }
            Log.d("hi_op","dismoo");
            alertDialog.dismiss();
            Log.d("hi_op","dismoo");
        });
    }


    private void addQuestionToList() {
        JsonObject jsonObject = new JsonObject();
        try {
            currChoices = Arrays.asList(new ChoicePair(false, "$$2$$"), new ChoicePair(false, "$$3$$"));
            currCorrectAnsNum = 1;
            questionPackage.addMCQuestion(currCategory, currQuestion, currHasPic, currPicSrc, currChoices, currCorrectAnsNum);
        } catch (Exception e) {
            Log.d("add_question_failed", "failed");
            Log.d(e.getMessage(), "except");
        }
    }
/*
    private void createQuiz() {
        QuestionPackage Quiz = new QuestionPackage();
        addMCQuestion(CourseCategory category, String question,
        boolean hasPic, String picSrc,
                List<ChoicePair> choices, int correctAnsNum)
    }*/
}