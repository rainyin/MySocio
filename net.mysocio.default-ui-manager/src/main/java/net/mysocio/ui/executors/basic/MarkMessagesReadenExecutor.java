/**
 * 
 */
package net.mysocio.ui.executors.basic;

import net.mysocio.data.IConnectionData;
import net.mysocio.data.management.MessagesManager;
import net.mysocio.ui.management.CommandExecutionException;
import net.mysocio.ui.management.ICommandExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aladdin
 *
 */
public class MarkMessagesReadenExecutor implements ICommandExecutor{
	private static final Logger logger = LoggerFactory.getLogger(MarkMessagesReadenExecutor.class);
	@Override
	public String execute(IConnectionData connectionData)
			throws CommandExecutionException {
		String messagesIds = connectionData.getRequestParameter("messagesIds");
		logger.debug("Marking readen message with ids: " + messagesIds);
		String userId = connectionData.getUserId();
		MessagesManager.getInstance().setMessagesReadden(userId, messagesIds);
		return "";
	}
}
