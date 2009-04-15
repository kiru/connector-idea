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
package com.atlassian.theplugin.idea.crucible.tree;

import com.atlassian.theplugin.crucible.model.CrucibleReviewListModel;
import com.atlassian.theplugin.idea.ui.tree.paneltree.AbstractTreeNode;
import com.atlassian.theplugin.idea.ui.tree.paneltree.SelectableLabel;

import javax.swing.*;

/**
 * @author Jacek Jaroczynski
 */
public class CrucibleMyReviewsTreeNode extends AbstractTreeNode {
	private static final String NAME = "All My Reviews";
	private final CrucibleReviewListModel reviewListModel;

	public CrucibleMyReviewsTreeNode(CrucibleReviewListModel reviewListModel) {
		super(NAME, null, null);
		this.reviewListModel = reviewListModel;
	}

	public String toString() {
		int cnt = reviewListModel.getPredefinedFiltersReviewCount();
		String txt = NAME;
		if (cnt > -1) {
			txt += " (" + cnt + ")";
		}
		return txt;
	}

	public JComponent getRenderer(JComponent c, boolean selected, boolean expanded, boolean hasFocus) {
		String txt = selected ? "<html>" + toString() : NAME;
		return new SelectableLabel(selected, c.isEnabled(), c.getFont(), txt, ICON_HEIGHT);
	}

	public void onSelect() {
	}
}