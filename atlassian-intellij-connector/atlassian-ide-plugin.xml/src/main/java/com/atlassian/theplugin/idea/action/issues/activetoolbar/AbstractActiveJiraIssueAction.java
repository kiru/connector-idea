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
package com.atlassian.theplugin.idea.action.issues.activetoolbar;

import com.atlassian.theplugin.jira.model.ActiveJiraIssue;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * User: pmaruszak
 */
public abstract class AbstractActiveJiraIssueAction extends AnAction {
	public abstract void onUpdate(AnActionEvent event);

	public void onUpdate(AnActionEvent event, boolean enabled) {
	}

	public final void update(final AnActionEvent event) {
		final ActiveJiraIssue activeJiraIssue = ActiveIssueUtils.getActiveJiraIssue(event);
		boolean enabled = activeJiraIssue != null;
		if (enabled) {
			onUpdate(event);
		}
		onUpdate(event, enabled);
	}

//	protected void refreshLabel(ActiveJiraIssue issue) {
//		ActionManager aManager = ActionManager.getInstance();
//		AnAction action = aManager.getAction(Constants.ACTIVE_JIRA_ISSUE_ACTION);
//		String label = getLabelText(issue);
//		if (issue != null) {
//			action.getTemplatePresentation().setText(label, true);
//			action.getTemplatePresentation().setEnabled(true);
//
//		} else {
//			action.getTemplatePresentation().setText(label, true);
//			action.getTemplatePresentation().setEnabled(false);
//		}
//
//		//createTooltipText("Open Issue", this);
//
//
//	}	
}

