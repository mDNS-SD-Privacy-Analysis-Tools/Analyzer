package analyzer.rest;

/**
 * Created by codewing on 25.01.2017.
 */
public class TxtData {
    private int id;
    private String name, content;

    public TxtData() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return name + "=" + content;
    }
}
