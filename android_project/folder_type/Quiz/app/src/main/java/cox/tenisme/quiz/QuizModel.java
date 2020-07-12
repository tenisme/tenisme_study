package cox.tenisme.quiz;

// 퀴즈와 답을 다루는(저장하고 사용하는) 클래스
public class QuizModel {

    // 문제와 답을 저장하는 private 변수 만들기
    // 문제의 데이터 타입은 int, 답의 데이터 타입은 boolean
        // 문제는 values > strings.xml 에 저장한 문제를 불러오는 것이기 때문에 데이터 타입은 int 여야한다.
    private int quiz;
    private boolean answer;

    // quiz 와 answer 를 저장하는 생성자 만들기
    public QuizModel(int quiz, boolean answer) {
        this.quiz = quiz;
        this.answer = answer;
    }

    // 각 변수의 getter, setter 만들기 (private)
    public int getQuiz() {
        return quiz;
    }

    public void setQuiz(int quiz) {
        this.quiz = quiz;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
