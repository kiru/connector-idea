package com.atlassian.connector.intellij.tasks;

///import com.intellij.tasks.LocalTask;

/**
 * @author pmaruszak
 * @date Feb 2, 2010
 */
public class TaskListenerImpl {

};/*implements com.intellij.tasks.TaskListener {
    private final Project project;
    private final PluginTaskManager pluginTaskManager;
    private final PluginConfiguration pluginConfiguration;

    public TaskListenerImpl(final Project project, PluginTaskManager pluginTaskManager,
                            PluginConfiguration pluginConfiguration) {
        this.project = project;
        this.pluginTaskManager = pluginTaskManager;
        this.pluginConfiguration = pluginConfiguration;
    }


    public void taskActivated(final LocalTask localTask) {
        if (pluginConfiguration != null
                && pluginConfiguration.getJIRAConfigurationData().isSynchronizeWithIntelliJTasks()) {
            if (!PluginTaskManager.isDefaultTask(project, localTask)) {
                final ActiveJiraIssue jiraIssue = ActiveIssueUtils.getActiveJiraIssue(project);
                if (jiraIssue == null || !localTask.getId().equals(jiraIssue.getIssueKey())) {
                    final JiraServerData sd = pluginTaskManager.findJiraPluginJiraServer(localTask.getIssueUrl());

                    if (sd != null) {
                        final ActiveJiraIssueBean ai = new ActiveJiraIssueBean(sd.getServerId(), localTask.getIssueUrl(), localTask.getId(),
                                new DateTime());
                        ai.setSource(ActiveJiraIssueBean.ActivationSource.INTELLIJ);
                                ActiveIssueUtils.activateIssue(project, null, ai, sd, null);

                    } else {
                        //do nothing
                    }
                } else {
                    //the same or none JIRA issue found inside plugin do nothing
                }

            } else {
                SwingUtilities.invokeLater(new DeactivateIssueRunnable(project));

            }
            
        }
    }
    }*/

