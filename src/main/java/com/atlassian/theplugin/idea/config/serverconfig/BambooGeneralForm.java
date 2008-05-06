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

package com.atlassian.theplugin.idea.config.serverconfig;

import com.atlassian.theplugin.commons.ServerType;
import com.atlassian.theplugin.commons.configuration.PluginConfiguration;
import com.atlassian.theplugin.commons.configuration.BambooTooltipOption;
import com.atlassian.theplugin.commons.configuration.BambooConfigurationBean;
import com.atlassian.theplugin.configuration.*;
import com.atlassian.theplugin.idea.config.ContentPanel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class BambooGeneralForm extends JComponent implements ContentPanel {
	private JRadioButton allFailuresFirstSuccess;
	private JRadioButton firstFailureFirstSuccess;
	private JRadioButton never;
	private JPanel rootComponent;
	private JSpinner pollTimeSpinner;
	private SpinnerModel model;

	private transient BambooConfigurationBean bambooConfiguration;
	private transient PluginConfiguration localPluginConfigurationCopy;

	private final transient PluginConfigurationBean globalPluginConfiguration;

	private static BambooGeneralForm instance;

	private BambooGeneralForm(PluginConfigurationBean globalPluginConfiguration) {
		this.globalPluginConfiguration = globalPluginConfiguration;
		$$$setupUI$$$();
		setLayout(new CardLayout());
		model = new SpinnerNumberModel(1, 1, 1000, 1);
		pollTimeSpinner.setModel(model);
		add(rootComponent, "BambooGeneralForm");
	}


	public static BambooGeneralForm getInstance(PluginConfigurationBean globalPluginConfiguration) {
		if (instance == null){
			instance = new BambooGeneralForm(globalPluginConfiguration);
		}
		return instance;
	}


	public void setData(PluginConfiguration config) {
		localPluginConfigurationCopy = config;
		bambooConfiguration =
				(BambooConfigurationBean) localPluginConfigurationCopy.getProductServers(ServerType.BAMBOO_SERVER);
		BambooTooltipOption configOption = this.bambooConfiguration.getBambooTooltipOption();

		if (configOption != null) {
			switch (configOption) {
				case ALL_FAULIRES_AND_FIRST_SUCCESS:
					allFailuresFirstSuccess.setSelected(true);
					break;
				case FIRST_FAILURE_AND_FIRST_SUCCESS:
					firstFailureFirstSuccess.setSelected(true);
					break;
				case NEVER:
					never.setSelected(true);
					break;
				default:
					never.setSelected(true);
					break;
			}
		} else {
			setDefaultTooltipOption();
		}
		model.setValue(bambooConfiguration.getPollTime());
	}

	public void getData() {
		((BambooConfigurationBean) getPluginConfiguration()
				.getProductServers(ServerType.BAMBOO_SERVER))
				.setBambooTooltipOption(getBambooTooltipOption());
		((BambooConfigurationBean) globalPluginConfiguration.getProductServers(ServerType.BAMBOO_SERVER))
				.setBambooTooltipOption(getBambooTooltipOption());

		((BambooConfigurationBean) getPluginConfiguration()
				.getProductServers(ServerType.BAMBOO_SERVER))
				.setPollTime((Integer) model.getValue());
		((BambooConfigurationBean) globalPluginConfiguration
				.getProductServers(ServerType.BAMBOO_SERVER))
				.setPollTime((Integer) model.getValue());
	}

	private BambooTooltipOption getBambooTooltipOption() {
		if (allFailuresFirstSuccess.isSelected()) {
			return BambooTooltipOption.ALL_FAULIRES_AND_FIRST_SUCCESS;
		} else if (firstFailureFirstSuccess.isSelected()) {
			return BambooTooltipOption.FIRST_FAILURE_AND_FIRST_SUCCESS;
		} else if (never.isSelected()) {
			return BambooTooltipOption.NEVER;
		} else {
			return getDefaultTooltipOption();
		}
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isModified() {
		if (bambooConfiguration.getBambooTooltipOption() != null) {
			if (bambooConfiguration.getBambooTooltipOption() != getBambooTooltipOption()) {
				return true;
			}
		}
		return (Integer) model.getValue() != bambooConfiguration.getPollTime();

	}

	public String getTitle() {
		return "Bamboo";
	}

	private void setDefaultTooltipOption() {
		allFailuresFirstSuccess.setSelected(true);
	}

	private BambooTooltipOption getDefaultTooltipOption() {
		return BambooTooltipOption.ALL_FAULIRES_AND_FIRST_SUCCESS;
	}

	public PluginConfiguration getPluginConfiguration() {
		return localPluginConfigurationCopy;
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		rootComponent = new JPanel();
		rootComponent.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
		rootComponent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12), null));
		allFailuresFirstSuccess = new JRadioButton();
		allFailuresFirstSuccess.setSelected(true);
		allFailuresFirstSuccess.setText("All build failures and first build success");
		CellConstraints cc = new CellConstraints();
		rootComponent.add(allFailuresFirstSuccess, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		firstFailureFirstSuccess = new JRadioButton();
		firstFailureFirstSuccess.setText("First build failure and first build success");
		rootComponent.add(firstFailureFirstSuccess, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
		never = new JRadioButton();
		never.setText("Never");
		rootComponent.add(never, cc.xy(3, 5, CellConstraints.LEFT, CellConstraints.DEFAULT));
		final Spacer spacer1 = new Spacer();
		rootComponent.add(spacer1, cc.xy(3, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
		final Spacer spacer2 = new Spacer();
		rootComponent.add(spacer2, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
		final JLabel label1 = new JLabel();
		label1.setText("Show Popup:");
		rootComponent.add(label1, cc.xy(1, 1));
		final JLabel label2 = new JLabel();
		label2.setText("Polling Time [minutes]");
		rootComponent.add(label2, cc.xy(1, 7));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		rootComponent.add(panel1, cc.xy(3, 7));
		pollTimeSpinner = new JSpinner();
		panel1.add(pollTimeSpinner, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
		final Spacer spacer3 = new Spacer();
		panel1.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		ButtonGroup buttonGroup;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(allFailuresFirstSuccess);
		buttonGroup.add(firstFailureFirstSuccess);
		buttonGroup.add(never);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return rootComponent;
	}
}
