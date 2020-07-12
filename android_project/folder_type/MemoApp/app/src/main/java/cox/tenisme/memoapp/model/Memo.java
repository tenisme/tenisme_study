package cox.tenisme.memoapp.model;

public class Memo {
    private int id;
    private String title;
    private String memo;

    public Memo(){

    }

    public Memo(int id,String title,String memo){
        this.id = id;
        this.title = title;
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
