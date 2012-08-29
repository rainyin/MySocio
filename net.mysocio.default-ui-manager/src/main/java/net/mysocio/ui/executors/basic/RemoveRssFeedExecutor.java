/**
 * 
 */
package net.mysocio.ui.executors.basic;

import net.mysocio.data.IConnectionData;
import net.mysocio.data.IDataManager;
import net.mysocio.data.management.DataManagerFactory;
import net.mysocio.ui.management.CommandExecutionException;
import net.mysocio.ui.management.ICommandExecutor;

/**
 * @author Aladdin
 *
 */
public class RemoveRssFeedExecutor implements ICommandExecutor {
//	private static final Logger logger = LoggerFactory.getLogger(RemoveRssFeedExecutor.class);
	/** 
	 * For now, I'm just removing rss feed from User, in future maybe reference counted management should be implemented   
	 */
	@Override
	public String execute(IConnectionData connectionData)
			throws CommandExecutionException {
		String userId = connectionData.getUserId();
		IDataManager dataManager = DataManagerFactory.getDataManager();
		dataManager.removeSource(userId, connectionData.getRequestParameter("id"));
		return new GetRssFeedsExecutor().execute(connectionData);
	}

}
