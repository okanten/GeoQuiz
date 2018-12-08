package solutions.kanten.geoquiz.models;

public class Capital {
    private String capitalName;
    private Boolean isCorrectAnswer = false;
    private Country country;

    public Capital(String capitalName, Boolean isCorrectAnswer, Country country) {
        this.capitalName = capitalName;
        this.isCorrectAnswer = isCorrectAnswer;
        this.country = country;
    }

    public String getCapitalName() {
        return capitalName;
    }

    public void setCapitalName(String capitalName) {
        this.capitalName = capitalName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Boolean getCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }
}
