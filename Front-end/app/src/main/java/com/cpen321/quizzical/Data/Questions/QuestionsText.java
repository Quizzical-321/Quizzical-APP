package com.cpen321.quizzical.Data.Questions;

import com.cpen321.quizzical.Data.CourseCategory;

import org.json.JSONArray;
import org.json.JSONObject;

public class QuestionsText implements IQuestion {
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
        return null;
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