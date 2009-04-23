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

package com.atlassian.theplugin.idea.action;

import com.atlassian.theplugin.commons.cfg.ServerCfg;
import com.atlassian.theplugin.commons.remoteapi.ServerData;
import com.atlassian.theplugin.idea.Constants;
import com.atlassian.theplugin.idea.IdeaHelper;
import com.atlassian.theplugin.idea.ProjectConfigurationComponent;
import com.atlassian.theplugin.cfg.CfgUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

/**
 * Simple action to show the settings for the plugin.
 */
public class ShowProjectSettingsAction extends AnAction {
	@Override
	public void update(final AnActionEvent event) {
		event.getPresentation().setEnabled(IdeaHelper.getCurrentProject(event) != null);
	}

	@Override
	public void actionPerformed(AnActionEvent event) {
		Project project = IdeaHelper.getCurrentProject(event);
		if (project != null) {
			ProjectConfigurationComponent component = project.getComponent(ProjectConfigurationComponent.class);
			ServerData server = event.getData(Constants.SERVER_KEY);
			component.setSelectedServer(IdeaHelper.getCfgManager(event).getServer(CfgUtil.getProjectId(project), server));
			final ShowSettingsUtil settingsUtil = ShowSettingsUtil.getInstance();
			if (settingsUtil != null) {
				settingsUtil.editConfigurable(project, component);
			}
		}
	}

}