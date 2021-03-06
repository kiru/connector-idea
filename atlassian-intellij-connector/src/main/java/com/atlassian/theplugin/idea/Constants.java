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

package com.atlassian.theplugin.idea;

import com.atlassian.theplugin.commons.bamboo.BambooBuild;
import com.atlassian.theplugin.commons.cfg.ServerCfg;
import com.atlassian.theplugin.commons.jira.api.JiraIssueAdapter;
import com.atlassian.theplugin.commons.remoteapi.ServerData;
import com.atlassian.theplugin.idea.config.serverconfig.ServerConfigPanel;
import com.atlassian.theplugin.idea.jira.StatusBarPane;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public final class Constants {
    // TODO - very uncool to hardcode the particular action ID.
	// What if Anton's people decide to change the ID of this action?
	public enum JiraActionId {
		START_PROGRESS(4),
		STOP_PROGRESS(301);

		JiraActionId(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}

		private long id;
	}

    public enum JiraStatusId {
        IN_PROGRESS(3);

        private int id;

        JiraStatusId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

	public static final Color FAIL_COLOR = new Color(255, 100, 100);

	public static final String SERVER_CONFIG_PANEL = "theplugin.server_config_panel";
	public static final String SERVER = "theplugin.server";
	public static final String SERVER_TYPE = "theplugin.servertype";
	public static final String SERVERS = "theplugin.servers";

	public static final DataKey<Collection<ServerCfg>> SERVERS_KEY = DataKey.create(SERVERS);
	public static final DataKey<ServerConfigPanel> SERVER_CONFIG_PANEL_KEY = DataKey.create(SERVER_CONFIG_PANEL);
	public static final DataKey<ServerData> SERVER_KEY = DataKey.create(SERVER);
	public static final DataKey<ServerCfg> SERVER_CFG_KEY = DataKey.create("com.atlassian.connector.cfg.server");


	public static final DataKey<BambooBuild> BAMBOO_BUILD_KEY = DataKey.create("atlassian.connector.bamoo.build");

    public static final String STATUS_BAR_PANE = "com.atlassian.connector.statusbarpane";
    public static final DataKey<StatusBarPane> STATUS_BAR_PANE_KEY = DataKey.create(STATUS_BAR_PANE);    
	public static final String ISSUE = "com.atlassian.connector.issue";
	public static final DataKey<JiraIssueAdapter> ISSUE_KEY = DataKey.create(ISSUE);

	private Constants() {
	}

	public static final int DIALOG_MARGIN = 12;

	public static final int BG_COLOR_R = 238;
	public static final int BG_COLOR_G = 238;
	public static final int BG_COLOR_B = 238;

	public static final String HELP_URL_BASE = "theplugin.help.url.prefix";
	public static final String HELP_CONFIG_PANEL = "theplugin.config";

	public static final String HELP_JIRA = "theplugin.jira";
	public static final String HELP_BAMBOO = "theplugin.bamboo";
	public static final String HELP_CRUCIBLE = "theplugin.crucible";
	//	public static final String HELP_JIRA_WORKLOG = "theplugin.jira.worklog";
	public static final String HELP_TEST_CONNECTION = "theplugin.testconnection";

	public static final String HELP_ISSUE = "theplugin.issue";
	public static final String HELP_BUILD = "theplugin.build";

	public static final Icon HELP_ICON = IconLoader.getIcon("/actions/help.png");
	public static final Icon JIRA_ISSUE_TAB_ICON = IconLoader.getIcon("/icons/tab_jira-white.png");
	public static final Icon JIRA_ISSUE_PANEL_ICON = IconLoader.getIcon("/icons/tab_jira.png");
	public static final Icon BAMBOO_BUILD_TAB_ICON = IconLoader.getIcon("/icons/tab_bamboo-white.png");
	public static final Icon BAMBOO_BUILD_PANEL_ICON = IconLoader.getIcon("/icons/tab_bamboo.png");
	public static final Icon BAMBOO_TRACE_ICON = IconLoader.getIcon("/icons/bamboo-traces.png");
	public static final Icon BAMBOO_COMMITS_ICON = IconLoader.getIcon("/icons/bamboo-commits.png");

	public static final String ACTIVE_TOOLBAR_NAME = "ThePlugin.ActiveToolbar";
	public static final String ACTIVE_JIRA_ISSUE_ACTION = "ThePlugin.ActiveTaskBar.ShowActiveJiraIssue";

    public static final String BODY_WITH_STYLE =
			"<body style=\"font-size:12pt ; font-family: arial, helvetica, sans-serif\">";
}

