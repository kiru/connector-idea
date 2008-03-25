package com.atlassian.theplugin.idea.config.serverconfig;

import com.atlassian.theplugin.LoginDataProvided;
import com.atlassian.theplugin.configuration.Server;
import com.atlassian.theplugin.configuration.ServerBean;
import com.atlassian.theplugin.idea.TestConnectionListener;
import com.atlassian.theplugin.util.Connector;
import com.atlassian.theplugin.util.UrlUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Plugin configuration form.
 */
public class GenericServerConfigForm extends JComponent implements ServerPanel, LoginDataProvided {
	private JPanel rootComponent;
	private JTextField serverName;
	private JTextField serverUrl;
	private JTextField username;
	private JPasswordField password;
	private JButton testConnection;
	private JCheckBox chkPasswordRemember;
	private JCheckBox cbEnabled;

	private transient Server originalServer;

	public GenericServerConfigForm(final Connector tester) {

		$$$setupUI$$$();
		testConnection.addActionListener(new TestConnectionListener(tester, this));
		serverUrl.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				adjustUrl();
			}
		});
	}

	private void adjustUrl() {
		String url = serverUrl.getText();
		url = UrlUtil.addHttpPrefix(url);
		url = UrlUtil.removeUrlTrailingSlashes(url);
		serverUrl.setText(url);
	}

	public void setData(Server server) {
		this.originalServer = new ServerBean(server);

		serverName.setText(server.getName());
		serverUrl.setText(server.getUrlString());
		username.setText(server.getUserName());
		chkPasswordRemember.setSelected(server.getShouldPasswordBeStored());
		password.setText(server.getPasswordString());
		cbEnabled.setSelected(server.getEnabled());
	}

	public Server getData() {
		adjustUrl();

		Server server = new ServerBean(originalServer);
		server.setName(serverName.getText());
		server.setUrlString(serverUrl.getText());
		server.setUserName(username.getText());
		server.setPasswordString(String.valueOf(password.getPassword()), chkPasswordRemember.isSelected());
		server.setEnabled(cbEnabled.isSelected());
		return server;
	}

	public boolean isModified() {
		boolean isModified = false;

		if (originalServer != null) {
			if (chkPasswordRemember.isSelected() != originalServer.getShouldPasswordBeStored()) {
				return true;
			}
			if (serverName.getText() != null
					? !serverName.getText().equals(originalServer.getName()) : originalServer.getName() != null) {
				return true;
			}
			if (cbEnabled.isSelected() != originalServer.getEnabled()) {
				return true;
			}
			if (serverUrl.getText() != null
					? !serverUrl.getText().equals(originalServer.getUrlString()) : originalServer.getUrlString() != null) {
				return true;
			}
			if (username.getText() != null
					? !username.getText().equals(originalServer.getUserName()) : originalServer.getUserName() != null) {
				return true;
			}
			String pass = String.valueOf(password.getPassword());
			if (!pass.equals(originalServer.getPasswordString())) {
				return true;
			}

		}
		return isModified;
	}


	public JComponent getRootComponent() {
		return rootComponent;
	}

	public void setVisible(boolean visible) {
		rootComponent.setVisible(visible);
	}

	private void createUIComponents() {
	}

	public String getServerUrl() {
		return serverUrl.getText();
	}

	public String getUserName() {
		return username.getText();
	}

	public String getPassword() {
		return String.valueOf(password.getPassword());
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
		rootComponent.setLayout(new GridBagLayout());
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		rootComponent.add(panel1, gbc);
		serverName = new JTextField();
		serverName.setText("");
		panel1.add(serverName, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText("Server Name");
		label1.setDisplayedMnemonic('S');
		label1.setDisplayedMnemonicIndex(0);
		panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		serverUrl = new JTextField();
		panel1.add(serverUrl, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		username = new JTextField();
		panel1.add(username, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		password = new JPasswordField();
		panel1.add(password, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Server URL");
		label2.setDisplayedMnemonic('U');
		label2.setDisplayedMnemonicIndex(7);
		panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label3 = new JLabel();
		label3.setText("Username");
		label3.setDisplayedMnemonic('N');
		label3.setDisplayedMnemonicIndex(4);
		panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label4 = new JLabel();
		label4.setText("Password");
		label4.setDisplayedMnemonic('P');
		label4.setDisplayedMnemonicIndex(0);
		panel1.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		testConnection = new JButton();
		testConnection.setText("Test Connection");
		testConnection.setMnemonic('T');
		testConnection.setDisplayedMnemonicIndex(0);
		panel1.add(testConnection, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		chkPasswordRemember = new JCheckBox();
		chkPasswordRemember.setSelected(true);
		chkPasswordRemember.setText("Remember Password");
		chkPasswordRemember.setMnemonic('R');
		chkPasswordRemember.setDisplayedMnemonicIndex(0);
		panel1.add(chkPasswordRemember, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		cbEnabled = new JCheckBox();
		cbEnabled.setHorizontalTextPosition(11);
		cbEnabled.setText("Server Enabled");
		cbEnabled.setMnemonic('E');
		cbEnabled.setDisplayedMnemonicIndex(7);
		panel1.add(cbEnabled, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel spacer1 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		rootComponent.add(spacer1, gbc);
		label1.setLabelFor(serverName);
		label2.setLabelFor(serverUrl);
		label3.setLabelFor(username);
		label4.setLabelFor(password);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return rootComponent;
	}
}