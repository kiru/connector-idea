package com.atlassian.theplugin.crucible.model;

import com.atlassian.theplugin.cfg.CfgUtil;
import com.atlassian.theplugin.commons.cfg.CrucibleServerCfg;
import com.atlassian.theplugin.commons.crucible.CrucibleServerFacade;
import com.atlassian.theplugin.commons.crucible.api.model.*;
import com.atlassian.theplugin.commons.exception.ServerPasswordNotProvidedException;
import com.atlassian.theplugin.commons.remoteapi.RemoteApiException;
import com.atlassian.theplugin.commons.remoteapi.RemoteApiLoginFailedException;
import com.atlassian.theplugin.idea.config.ProjectCfgManager;
import com.atlassian.theplugin.idea.crucible.ReviewNotificationBean;
import com.atlassian.theplugin.remoteapi.MissingPasswordHandler;
import com.atlassian.theplugin.util.PluginUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;

import java.util.*;

public class CrucibleQueryExecutor {
	private final CrucibleServerFacade crucibleServerFacade;
	private final ProjectCfgManager projectCfgManager;
	private final Project project;
	private final MissingPasswordHandler missingPasswordHandler;
	private final CrucibleReviewListModel crucibleReviewListModel;

	public CrucibleQueryExecutor(final CrucibleServerFacade crucibleServerFacade,
			final ProjectCfgManager projectCfgManager,
			final Project project,
			final MissingPasswordHandler missingPasswordHandler,
			final CrucibleReviewListModel crucibleReviewListModel) {
		this.crucibleServerFacade = crucibleServerFacade;
		this.projectCfgManager = projectCfgManager;
		this.project = project;
		this.missingPasswordHandler = missingPasswordHandler;
		this.crucibleReviewListModel = crucibleReviewListModel;
	}

	public Map<CrucibleFilter, ReviewNotificationBean> runQuery(
			final Boolean[] predefinedFilters,
			final CustomFilter manualFilter,
			final RecentlyOpenReviewsFilter recentlyOpenFilter,
			final long epoch) throws InterruptedException {
		// collect review info from each server and each required filter
		final Map<CrucibleFilter, ReviewNotificationBean> reviews
				= new HashMap<CrucibleFilter, ReviewNotificationBean>();

		for (final CrucibleServerCfg server : projectCfgManager.getCfgManager().
				getAllCrucibleServers(CfgUtil.getProjectId(project))) {
			if (server.isEnabled()) {

				// retrieve reviews for predefined filters
				for (int i = 0;
					 i < predefinedFilters.length
							 && i < PredefinedFilter.values().length; i++) {

					// if predefined filter is enabled
					if (predefinedFilters[i]) {
						PredefinedFilter filter = PredefinedFilter.values()[i];
						if (filter.isRemote()) {

							// create notification bean for the filter if not exist
							if (!reviews.containsKey(filter)) {
								ReviewNotificationBean predefinedFiterNofificationbean = new ReviewNotificationBean();
								List<ReviewAdapter> list = new ArrayList<ReviewAdapter>();
								predefinedFiterNofificationbean.setReviews(list);
								reviews.put(filter, predefinedFiterNofificationbean);
							}

							ReviewNotificationBean predefinedFiterNotificationBean = reviews.get(filter);

							// get reviews for filter from the server
							try {
								PluginUtil.getLogger().debug("Crucible: updating status for server: "
										+ server.getUrl() + ", filter type: " + filter);

								if (crucibleReviewListModel.isRequestObsolete(epoch)) {
									throw new InterruptedException();
								}

								List<Review> review = crucibleServerFacade
										.getReviewsForFilter(projectCfgManager.getServerData(server), filter);
								List<ReviewAdapter> reviewData = new ArrayList<ReviewAdapter>(review.size());
								for (Review r : review) {
									final ReviewAdapter reviewAdapter = new ReviewAdapter(r, projectCfgManager.getServerData(server));
									reviewData.add(reviewAdapter);
								}

								predefinedFiterNotificationBean.getReviews().addAll(reviewData);

							} catch (ServerPasswordNotProvidedException exception) {
								ApplicationManager.getApplication().invokeLater(missingPasswordHandler,
										ModalityState.defaultModalityState());
								predefinedFiterNotificationBean.setException(exception);
								predefinedFiterNotificationBean.setServer(projectCfgManager.getServerData(server));
								break;
							} catch (RemoteApiLoginFailedException exception) {
								ApplicationManager.getApplication().invokeLater(missingPasswordHandler,
										ModalityState.defaultModalityState());
								predefinedFiterNotificationBean.setException(exception);
								predefinedFiterNotificationBean.setServer(projectCfgManager.getServerData(server));
								break;
							} catch (RemoteApiException e) {
								PluginUtil.getLogger().info("Error getting Crucible reviews for " + server.getName()
										+ " server", e);
								predefinedFiterNotificationBean.setException(e);
								predefinedFiterNotificationBean.setServer(projectCfgManager.getServerData(server));
								break;
							}
						}
					}
				}

				// retrieve reviews for custom filter
				retriveManualFilterReviews(manualFilter, reviews, server);

				// retrieve reviews for recently open filter
				retriveRecenltyOpenFilterReviews(recentlyOpenFilter, reviews, server);

			}
		}
		return reviews;
	}

	private void retriveRecenltyOpenFilterReviews(final RecentlyOpenReviewsFilter recentlyOpenFilter,
			final Map<CrucibleFilter, ReviewNotificationBean> reviews, final CrucibleServerCfg server) {

		if (recentlyOpenFilter != null && recentlyOpenFilter.isEnabled()) {

			// create notification bean for the filter if not exist
			if (!reviews.containsKey(recentlyOpenFilter)) {
				List<ReviewAdapter> list = new ArrayList<ReviewAdapter>();
				ReviewNotificationBean bean = new ReviewNotificationBean();
				bean.setReviews(list);
				reviews.put(recentlyOpenFilter, bean);
			}

			ReviewNotificationBean recenltyOpenFilterNotificationBean = reviews.get(recentlyOpenFilter);

			for (ReviewRecentlyOpenBean recentReview : recentlyOpenFilter.getRecentlyOpenReviews()) {

				if (server.getServerId().toString().equals(recentReview.getServerId())) {

					// get review from the server
					try {
						PluginUtil.getLogger().debug(
								"Crucible: updating status for server: " + server.getUrl()
										+ ", recenlty viewed reviews filter");

						Review r = crucibleServerFacade.getReview(projectCfgManager.getServerData(server),
								new PermIdBean(recentReview.getReviewId()));
						recenltyOpenFilterNotificationBean.getReviews()
								.add(new ReviewAdapter(r, projectCfgManager.getServerData(server)));

					} catch (ServerPasswordNotProvidedException exception) {
						ApplicationManager.getApplication().invokeLater(
								new MissingPasswordHandler(crucibleServerFacade, projectCfgManager, project),
								ModalityState.defaultModalityState());
						recenltyOpenFilterNotificationBean.setException(exception);
						recenltyOpenFilterNotificationBean.setServer(projectCfgManager.getServerData(server));
					} catch (RemoteApiException e) {
						PluginUtil.getLogger().info("Error getting Crucible review for " + server.getName()
								+ " server", e);
						recenltyOpenFilterNotificationBean.setException(e);
						recenltyOpenFilterNotificationBean.setServer(projectCfgManager.getServerData(server));
					}
				}
			}
		}

	}

	private void retriveManualFilterReviews(final CustomFilter manualFilter,
			final Map<CrucibleFilter, ReviewNotificationBean> reviews, final CrucibleServerCfg server) {
		if (manualFilter != null && manualFilter.isEnabled()) {

			// create notification bean for the filter if not exist
			if (!reviews.containsKey(manualFilter)) {
				List<ReviewAdapter> list = new ArrayList<ReviewAdapter>();
				ReviewNotificationBean bean = new ReviewNotificationBean();
				bean.setReviews(list);
				reviews.put(manualFilter, bean);
			}

			ReviewNotificationBean customFilterNotificationBean = reviews.get(manualFilter);

			if (server.getServerId().toString().equals(manualFilter.getServerUid())) {

				// get reviews for filter from the server
				try {
					PluginUtil.getLogger().debug("Crucible: updating status for server: "
							+ server.getUrl() + ", custom filter");
					List<Review> customFilter
							= crucibleServerFacade.getReviewsForCustomFilter(projectCfgManager.getServerData(server),
							manualFilter);

					List<ReviewAdapter> reviewData = new ArrayList<ReviewAdapter>(customFilter.size());
					for (Review r : customFilter) {
						final ReviewAdapter reviewAdapter = new ReviewAdapter(r, projectCfgManager.getServerData(server));
						reviewData.add(reviewAdapter);
					}

					customFilterNotificationBean.getReviews().addAll(reviewData);

				} catch (ServerPasswordNotProvidedException exception) {
					ApplicationManager.getApplication().invokeLater(
							new MissingPasswordHandler(crucibleServerFacade, projectCfgManager, project),
							ModalityState.defaultModalityState());
					customFilterNotificationBean.setException(exception);
					customFilterNotificationBean.setServer(projectCfgManager.getServerData(server));
				} catch (RemoteApiException e) {
					PluginUtil.getLogger().info("Error getting Crucible reviews for " + server.getName()
							+ " server", e);
					customFilterNotificationBean.setException(e);
					customFilterNotificationBean.setServer(projectCfgManager.getServerData(server));
				}
			}
		}
	}

	public ReviewNotificationBean runDetailedReviewsQuery(
			final Collection<ReviewAdapter> reviews, final long epoch) throws InterruptedException {
		ReviewNotificationBean reviewNotificationBean = new ReviewNotificationBean();
		List<ReviewAdapter> outReviews = new ArrayList<ReviewAdapter>();
		reviewNotificationBean.setReviews(outReviews);

		for (ReviewAdapter review : reviews) {
			if (review != null && review.getServerData() != null
					&& projectCfgManager.getServer(review.getServerData()).isEnabled()) {

				try {
					PluginUtil.getLogger().debug("Crucible: updating status for server: "
							+ review.getServerData().getUrl() + ", review: " + review.getPermId().getId());

					if (crucibleReviewListModel.isRequestObsolete(epoch)) {
						throw new InterruptedException();
					}

					Review r = crucibleServerFacade.getReview(review.getServerData(),
							review.getPermId());
					reviewNotificationBean.getReviews().add(new ReviewAdapter(r, review.getServerData()));
				} catch (ServerPasswordNotProvidedException exception) {
					ApplicationManager.getApplication().invokeLater(missingPasswordHandler,
							ModalityState.defaultModalityState());
					reviewNotificationBean.setException(exception);
					reviewNotificationBean.setServer(review.getServerData());
				} catch (RemoteApiLoginFailedException exception) {
					ApplicationManager.getApplication().invokeLater(missingPasswordHandler,
							ModalityState.defaultModalityState());
					reviewNotificationBean.setException(exception);
					reviewNotificationBean.setServer(review.getServerData());
				} catch (RemoteApiException e) {
					PluginUtil.getLogger().info("Error getting Crucible reviews for " + review.getServerData().getName()
							+ " server", e);
					reviewNotificationBean.setException(e);
					reviewNotificationBean.setServer(review.getServerData());
				}
			}
		}

		return reviewNotificationBean;
	}
}