package com.cpen321.quizzical.data.Questions;

import com.cpen321.quizzical.data.CourseCategory;

public class QuestionsBuzzer implements IQuestion {

    private QuestionType questionType = QuestionType.Buzzer;

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public CourseCategory getCategory() {
        return null;
    }

    @Override
    public QuestionType getQuestionType() {
        return this.questionType;
    }

    @Override
    public String getQuestion() {
        return null;
    }

    @Override
    public boolean hasPic() {
        return false;
    }

    @Override
    public String getPicSrc() {
        return null;
    }

    @Override
    public String toJsonString() {
        return null;
    }


}