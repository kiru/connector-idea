/**
 * Copyright (C) 2008 Atlassian
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atlassian.theplugin.idea.jira;

import com.atlassian.theplugin.jira.api.JIRAConstant;
import com.atlassian.theplugin.jira.api.JIRAIssue;

public class JiraIssueAdapter {
	private JIRAIssue issue;
	private boolean useIconDescription;

	public JiraIssueAdapter(JIRAIssue issue, boolean useIconDescription) {
		this.issue = issue;
		this.useIconDescription = useIconDescription;
	}

	public JIRAIssue getIssue() {
		return issue;
	}

	public boolean isUseIconDescription() {
		return useIconDescription;
	}

	public String getServerUrl() {
		return issue.getServerUrl();
	}

	public String getProjectUrl() {
		return issue.getProjectUrl();
	}

	public String getIssueUrl() {
		return issue.getIssueUrl();
	}

	public String getKey() {
		return issue.getKey();
	}

	public String getProjectKey() {
		return issue.getProjectKey();
	}

	public String getStatus() {
		return issue.getStatus();
	}

	public JiraIcon getStatusInfo() {
		return new JiraIcon(issue.getStatus(), issue.getStatusTypeUrl());		
	}

	public long getStatusId() {
		return issue.getStatusId();
	}

	public String getPriority() {
		return issue.getPriority() != null ? issue.getPriority() : "";
	}
	
	public JiraIcon getPriorityInfo() {
		return new JiraIcon(issue.getPriority(), issue.getPriorityIconUrl());
	}

	public long getPriorityId() {
		return issue.getPriorityId();
	}

	public String getSummary() {
		return issue.getSummary();
	}

	public String getType() {
		return issue.getType();
	}

	public JiraIcon getTypeInfo() {
		return new JiraIcon(issue.getType(), issue.getTypeIconUrl());
	}

	public long getTypeId() {
		return issue.getTypeId();
	}

	public String getDescription() {
		return issue.getDescription();
	}

	public JIRAConstant getTypeConstant() {
		return issue.getTypeConstant();
	}

	public JIRAConstant getStatusConstant() {
		return issue.getStatusConstant();
	}

	public String getAssignee() {
		return issue.getAssignee();
	}

	public String getReporter() {
		return issue.getReporter();
	}

	public String getResolution() {
		return issue.getResolution();
	}

	public String getCreated() {
		return issue.getCreated();
	}

	public String getUpdated() {
		return issue.getUpdated();
	}
}
