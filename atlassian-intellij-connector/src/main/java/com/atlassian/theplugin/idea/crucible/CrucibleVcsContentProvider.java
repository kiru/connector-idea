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

package com.atlassian.theplugin.idea.crucible;

import com.atlassian.theplugin.commons.VersionedVirtualFile;
import com.atlassian.theplugin.commons.crucible.api.content.ReviewFileContentException;
import com.atlassian.theplugin.commons.crucible.api.model.CrucibleFileInfo;
import com.atlassian.theplugin.commons.crucible.api.model.ReviewAdapter;
import com.atlassian.theplugin.idea.VcsIdeaHelper;
import com.atlassian.theplugin.util.PluginUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.BinaryContentRevision;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.diff.DiffProvider;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vcs.vfs.VcsVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;

import java.io.IOException;
import java.util.Arrays;

public class CrucibleVcsContentProvider implements IdeaReviewFileContentProvider {
	private final Project project;
	private final CrucibleFileInfo fileInfo;
	private final VirtualFile virtualFile;

	public CrucibleVcsContentProvider(Project project, CrucibleFileInfo fileInfo, VirtualFile virtualFile) {
		this.project = project;
		this.fileInfo = fileInfo;
		this.virtualFile = virtualFile;
	}

	public CrucibleFileInfo getFileInfo() {
		return fileInfo;
	}

    public boolean isLocalFileDirty() {
        return VcsIdeaHelper.isFileDirty(project, virtualFile);
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public IdeaReviewFileContent getContent(final ReviewAdapter review,
			final VersionedVirtualFile versionedVirtualFile) throws ReviewFileContentException {
		AbstractVcs vcs = VcsUtil.getVcsFor(project, virtualFile);
		if (vcs == null) {
			return null;
		}
		VcsRevisionNumber vcsRevisionNumber = vcs.parseRevisionNumber(versionedVirtualFile.getRevision());
		if (vcsRevisionNumber == null) {
			throw new ReviewFileContentException(
					"Cannot parse revision number [" + versionedVirtualFile.getRevision() + "] for file ["
							+ virtualFile.getPath() + "]");
		}

		DiffProvider diffProvider = vcs.getDiffProvider();
		if (diffProvider == null) {
			return null;
		}
		ContentRevision contentRevision = diffProvider.createFileContent(vcsRevisionNumber, virtualFile);
		if (contentRevision == null) {
			return null;
		}
		final byte[] content;
		try {
			if (contentRevision instanceof BinaryContentRevision) {
				content = ((BinaryContentRevision) contentRevision).getBinaryContent();
			} else {
				// this operation is typically quite costly
				final String strContent = contentRevision.getContent();
				content = (strContent != null) ? strContent.getBytes() : null;
			}
            
            try {
                 if (Arrays.equals(virtualFile.contentsToByteArray(), content)) {
                     //virtualFile.putUserData(CommentHighlighter.REVIEWITEM_CURRENT_CONTENT_KEY, Boolean.TRUE);

                     IdeaReviewFileContent localContent = new IdeaReviewFileContent(virtualFile, null, true);
                     return localContent;
                 }
             } catch (IOException e) {
                 PluginUtil.getLogger().warn("Cannot retrieve content for " + virtualFile.getPath() + virtualFile.getName());
             }

			VirtualFile file = new VcsVirtualFile(contentRevision.getFile().getPath(), content,
					contentRevision.getRevisionNumber().asString(),
					virtualFile.getFileSystem());
            IdeaReviewFileContent remoteContent = new IdeaReviewFileContent(file, null, false);
            return remoteContent;

		} catch (VcsException e) {
			throw new ReviewFileContentException(e);
		}
	}
}