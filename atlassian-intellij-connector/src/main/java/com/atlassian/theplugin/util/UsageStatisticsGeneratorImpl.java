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
package com.atlassian.theplugin.util;

import com.atlassian.theplugin.commons.ServerType;
import com.atlassian.theplugin.commons.configuration.GeneralConfigurationBean;
import com.atlassian.theplugin.commons.remoteapi.ServerData;
import com.atlassian.theplugin.commons.util.UrlUtil;

import java.util.Collection;
import java.util.TreeSet;

public class UsageStatisticsGeneratorImpl implements UsageStatisticsGenerator {
	private final boolean reportStatistics;
	private final long uid;
	private GeneralConfigurationBean generalConfig;
	private final Collection<ServerData> servers;

	public UsageStatisticsGeneratorImpl(boolean reportStatistics, final long uid,
			GeneralConfigurationBean generalConfig, final Collection<ServerData> servers) {
		this.reportStatistics = reportStatistics;
		this.uid = uid;
		this.generalConfig = generalConfig;
		this.servers = servers;
	}

	public String getStatisticsUrlSuffix() {
		StringBuilder sb = new StringBuilder();

		if (reportStatistics) {
            sb.append("uid=").append(uid);

			int[] counts = new int[ServerType.values().length];

			for (ServerData serverCfg : servers) {
				counts[serverCfg.getServerType().ordinal()]++;
			}

			sb.append("&version=").append(UrlUtil.encodeUrl(PluginUtil.getInstance().getVersion()));
			sb.append("&bambooServers=").append(counts[ServerType.BAMBOO_SERVER.ordinal()]);
			sb.append("&crucibleServers=").append(counts[ServerType.CRUCIBLE_SERVER.ordinal()]);
			sb.append("&jiraServers=").append(counts[ServerType.JIRA_SERVER.ordinal()]);

			if (generalConfig != null) {
				TreeSet<String> counters = new TreeSet<String>(generalConfig.getStatsCountersMap().keySet());
				for (String counter : counters) {
					sb.append("&").append(counter).append("=").append(generalConfig.getStatsCountersMap().get(counter));
				}
			}
		} else {
            return "uid=0";
        }
		return sb.toString();
	}
}
