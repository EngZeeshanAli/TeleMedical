package com.example.telemedical.Formaters;

import android.widget.EditText;

public class QuestionFormater {
    String problemDuration, cause, symtoms, weakness, numbness;

    public QuestionFormater() {
    }

    public QuestionFormater(String problemDuration, String cause, String symtoms, String weakness, String numbness) {
        this.problemDuration = problemDuration;
        this.cause = cause;
        this.symtoms = symtoms;
        this.weakness = weakness;
        this.numbness = numbness;
    }

    public String getProblemDuration() {
        return problemDuration;
    }

    public String getCause() {
        return cause;
    }

    public String getSymtoms() {
        return symtoms;
    }

    public String getWeakness() {
        return weakness;
    }

    public String getNumbness() {
        return numbness;
    }
}
