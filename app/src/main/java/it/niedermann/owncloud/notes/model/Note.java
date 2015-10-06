package it.niedermann.owncloud.notes.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.niedermann.owncloud.notes.persistence.NoteSQLiteOpenHelper;
import it.niedermann.owncloud.notes.util.NoteUtil;

@SuppressWarnings("serial")
public class Note implements Serializable {
	private long id = 0;
	private String title = "";
	private Calendar modified = null;
	private String content = "";
    private String excerpt = "";
    private String htmlContent = null;

	public Note(long id, Calendar modified, String title, String content) {
		this.id = id;
        if(title != null)
            setTitle(title);
        setTitle(title);
        setContent(content);
        this.modified = modified;
    }

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

    public void setTitle(String title) {
        this.title = NoteUtil.removeMarkDown(title);
    }

	@SuppressWarnings("WeakerAccess")
    public Calendar getModified() {
		return modified;
	}

	public String getModified(String format) {
		return new SimpleDateFormat(format, Locale.GERMANY)
				.format(this.getModified().getTime());
	}

	public String getContent() {
		return content;
	}

    public void setContent(String content) {
        setExcerpt(content);
        this.content = content;
        this.htmlContent = null;
    }

    public String getExcerpt() {
        return excerpt;
    }

    private void setExcerpt(String content) {
        if (content.contains("\n")) {
            String[] lines = content.split("\n");
            int currentLine = 1;
            while (NoteUtil.isEmptyLine(lines[currentLine]) && currentLine < lines.length) {
                currentLine++;
            }
            excerpt = NoteUtil.removeMarkDown(lines[currentLine]);
        } else {
            excerpt = content;
        }
    }

    public String getHtmlContent() {
        if(htmlContent == null && getContent() != null) {
            htmlContent = NoteUtil.parseMarkDown(getContent());
        }
        return htmlContent;
    }

	@Override
	public String toString() {
		return "#" + getId() + " " + getTitle() + " (" + getModified(NoteSQLiteOpenHelper.DATE_FORMAT) + ")";
	}
}