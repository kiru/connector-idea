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

package com.atlassian.theplugin.idea.autoupdate;

import com.atlassian.theplugin.commons.SchedulableChecker;
import com.atlassian.theplugin.commons.cfg.CfgManager;
import com.atlassian.theplugin.commons.configuration.GeneralConfigurationBean;
import com.atlassian.theplugin.commons.configuration.PluginConfiguration;
import com.atlassian.theplugin.commons.exception.IncorrectVersionException;
import com.atlassian.theplugin.commons.exception.ThePluginException;
import com.atlassian.theplugin.commons.util.Version;
import com.atlassian.theplugin.exception.VersionServiceException;
import com.atlassian.theplugin.util.InfoServer;
import com.atlassian.theplugin.util.PluginUtil;
import com.atlassian.theplugin.util.UsageStatisticsGeneratorImpl;
import com.intellij.openapi.application.ApplicationManager;

import java.util.TimerTask;

/**
 * Provides functionality to check for new version and update plugin
 */
public final class NewVersionChecker implements SchedulableChecker {
	private static final long PLUGIN_UPDATE_ATTEMPT_DELAY = 60 * 60 * 1000; // every hour

	private transient PluginConfiguration pluginConfiguration;
	private final CfgManager cfgManager;

	private static final String NAME = "New Version checker";

	public NewVersionChecker(final PluginConfiguration pluginConfiguration, final CfgManager cfgManager) {
		this.pluginConfiguration = pluginConfiguration;
		this.cfgManager = cfgManager;
	}

	public static NewVersionChecker getInstance() {
		return (NewVersionChecker) ApplicationManager.getApplication()
				.getPicoContainer().getComponentInstanceOfType(NewVersionChecker.class);
	}

	/**
	 * Connects to the server, checks for new version and updates if necessary
	 * @return new TimerTask to be scheduled
	 */
	public TimerTask newTimerTask() {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					doRun(new ForwardToIconHandler(pluginConfiguration.getGeneralConfigurationData()), true);
				} catch (ThePluginException e) {
					PluginUtil.getLogger().info("Error checking new version: " + e.getMessage());
				}
			}
		};
	}

	public boolean canSchedule() {
		return true; // NewVersionChecker is always enabled
	}

	public long getInterval() {
		return PLUGIN_UPDATE_ATTEMPT_DELAY;
	}

	public void resetListenersState() {
		// do nothing
	}

	public String getName() {
		return NAME;
	}

	protected void doRun(UpdateActionHandler action, boolean showConfigPath) throws ThePluginException {
		doRun(action, showConfigPath, pluginConfiguration.getGeneralConfigurationData());
	}

	protected void doRun(UpdateActionHandler action, boolean showConfigPath, GeneralConfigurationBean configuration)
			throws ThePluginException {
		if (!configuration.isAutoUpdateEnabled()) {
			return;
		}
		if (action == null) {
			throw new IllegalArgumentException("UpdateAction handler not provided.");
		}

		// get latest version
		InfoServer.VersionInfo versionInfo = getLatestVersion(configuration);
		Version newVersion = versionInfo.getVersion();
		// get current version
		Version thisVersion = new Version(PluginUtil.getInstance().getVersion());
		if (newVersion.greater(thisVersion)) {
			action.doAction(versionInfo, showConfigPath);
		}
	}

	private InfoServer.VersionInfo getLatestVersion(GeneralConfigurationBean configuration)
			throws VersionServiceException, IncorrectVersionException {
		final Boolean anonymousFeedbackEnabled = configuration.getAnonymousEnhancedFeedbackEnabled();
		return InfoServer.getLatestPluginVersion(
				new UsageStatisticsGeneratorImpl(anonymousFeedbackEnabled != null ? anonymousFeedbackEnabled : false,
				            configuration.getUid(), pluginConfiguration.getGeneralConfigurationData(), cfgManager),
                configuration.isCheckUnstableVersionsEnabled());
	}
}
