package de.hka.iwii.db1.jdbc.formatting.options;

public class FmtOptions {
    private final Where highlightWhere;
    private String title;

    public FmtOptions(Where highlightWhere) {
        this.highlightWhere = highlightWhere;
        this.title = "";
    }

    public FmtOptions() {
        this.highlightWhere = null;
        this.title = "";
    }

    public FmtOptions withTitle(String title) {
        this.title = title;
        return this;
    }

    public static FmtOptions highlight(Where where) {
        return new FmtOptions(where);
    }

    public Where getHighlightWhere() {
        return highlightWhere;
    }

    public String getTitle() {
        return title;
    }

    public static FmtOptions ifDefined(FmtOptions options) {
        return options == null ? new FmtOptions(null) : options;
    }
}
