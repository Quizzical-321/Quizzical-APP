package com.cpen321.quizzical.utils;

import com.cpen321.quizzical.data.CourseCategory;
import com.cpen321.quizzical.data.Questions.IQuestion;
import com.cpen321.quizzical.data.Questions.QuestionsMC;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionPackage {

    private static Random rng = new Random();
    /**
     * This class provides an interface for questions and quizzes
     * When an instructor creates a quiz, addQuestions will be called once a question is created
     * When a student/instructor do a quiz, GetQuestions will be called
     */

    private int id;
    private List<IQuestion> questionList;

    public QuestionPackage() {
        questionList = new ArrayList<>();
    }

    public QuestionPackage(int id) {
        this.id = id;
        questionList = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public List<IQuestion> getQuestionList() {
        return this.questionList;
    }

    public List<IQuestion> getQuestionsByCategory(CourseCategory category, int questionNumber) {
        Collections.shuffle(this.questionList, rng);
        List<IQuestion> questions = new ArrayList<>();
        for (IQuestion q : questionList) {
            if (q.getCategory().equals(category))
                questions.add(q);
            if (questions.size() >= questionNumber)
                break;
        }

        return questions;
    }

    public void addMCQuestion(CourseCategory category, String question,
                              boolean hasPic, String picSrc,
                              List<ChoicePair> choices, int correctAnsNum) throws Exception {
        if (correctAnsNum < 0 || correctAnsNum >= choices.size())
            throw new IllegalArgumentException("correct answer does not match any of the answers");
        QuestionsMC q = new QuestionsMC(category, question, hasPic, picSrc, choices, correctAnsNum);
        questionList.add(q);
    }

    public void addMCQuestion(int id, CourseCategory category, String question,
                              boolean hasPic, String picSrc,
                              List<ChoicePair> choices, int correctAnsNum) throws Exception {
        if (correctAnsNum < 0 || correctAnsNum >= choices.size())
            throw new IllegalArgumentException("correct answer does not match any of the answers");
        QuestionsMC q = new QuestionsMC(id, category, question, hasPic, picSrc, choices, correctAnsNum);
        questionList.add(q);
    }

    public void addMCQuestion(String jsonString) {
        Gson gson = new Gson();
        QuestionsMC q = gson.fromJson(jsonString, QuestionsMC.class);
        questionList.add(q);
    }


}