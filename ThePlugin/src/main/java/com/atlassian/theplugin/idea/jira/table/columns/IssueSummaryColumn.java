package com.atlassian.theplugin.idea.jira.table.columns;

import com.atlassian.theplugin.idea.TableColumnInfo;
import com.atlassian.theplugin.idea.jira.JiraIssueAdapter;

import java.util.Comparator;

public class IssueSummaryColumn extends TableColumnInfo {
	private static final int COL_WIDTH = 500;

	public Object valueOf(Object o) {
		return ((JiraIssueAdapter) o).getSummary();
	}

	public Class getColumnClass() {
		return String.class;
	}

	public Comparator getComparator() {
		return new Comparator() {
			public int compare(Object o, Object o1) {
				return ((JiraIssueAdapter) o).getSummary().compareTo(((JiraIssueAdapter) o1).getSummary());
			}
		};
	}

	public String getColumnName() {
		return "Summary";
	}

	public int getPrefferedWidth() {
		return COL_WIDTH;
	}
}