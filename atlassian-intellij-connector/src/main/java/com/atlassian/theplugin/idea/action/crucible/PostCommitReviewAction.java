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

package com.atlassian.theplugin.idea.action.crucible;

import com.atlassian.theplugin.commons.crucible.CrucibleServerFacadeImpl;
import com.atlassian.theplugin.idea.IdeaHelper;
import com.atlassian.theplugin.idea.crucible.CrucibleCreatePostCommitReviewFromChangeListForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeList;

public class PostCommitReviewAction extends Crucible16MultiRevisionRepositoryAction {
	@Override
	public void actionPerformed(AnActionEvent event) {
		final Project project = event.getData(DataKeys.PROJECT);
		@SuppressWarnings({"deprecation"}) final ChangeList[] changes = event.getData(DataKeys.CHANGE_LISTS);
		if (project == null || changes == null) {
			return;
		}

		new CrucibleCreatePostCommitReviewFromChangeListForm(project, CrucibleServerFacadeImpl.getInstance(),
				changes, IdeaHelper.getProjectCfgManager(event)).show();
	}
}