package com.cpen321.quizzical.data.questions;

import com.cpen321.quizzical.data.CourseCategory;

public class QuestionsText implements IQuestion {

    @Override
    public void setID(int id) {
        /*not implemented*/
    }

    @Override
    public void setCategory(CourseCategory category) {
        /*not implemented*/
    }

    @Override
    public void setQuestion(String question) {
        /*not implemented*/
    }

    @Override
    public void setHasPic(boolean hasPic) {
        /*not implemented*/
    }

    @Override
    public void setPicSrc(String picSrc) {
        /*not implemented*/
    }

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
