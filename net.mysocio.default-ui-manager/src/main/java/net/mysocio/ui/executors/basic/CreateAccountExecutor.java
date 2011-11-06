/**
 * 
 */
package net.mysocio.ui.executors.basic;

import net.mysocio.data.IConnectionData;
import net.mysocio.data.SocioUser;
import net.mysocio.data.accounts.Account;
import net.mysocio.data.management.AccountsManager;
import net.mysocio.data.management.DataManagerFactory;
import net.mysocio.data.management.DefaultResourcesManager;
import net.mysocio.data.management.MessagesManager;
import net.mysocio.ui.management.CommandExecutionException;
import net.mysocio.ui.management.ICommandExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aladdin
 * 
 */
public class CreateAccountExecutor implements ICommandExecutor {
	private static final Logger logger = LoggerFactory
			.getLogger(CreateAccountExecutor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mysocio.ui.management.ICommandExecutor#execute(net.mysocio.data.
	 * IConnectionData)
	 */
	@Override
	public String execute(IConnectionData connectionData)
			throws CommandExecutionException {
		String flow = connectionData.getSessionAttribute("flow");
		String responseString = null;
		try {
			SocioUser user = connectionData.getUser();
			Account account = AccountsManager.getInstance().getAccount(
					connectionData);
			if ("login".equals(flow)) {
				user = DataManagerFactory.getDataManager().getUser(account,connectionData.getLocale());
				connectionData.setUser(user);
				MessagesManager.getInstance().updateUnreaddenMessages(user);
				responseString = DefaultResourcesManager.getPage("closingLoginWindow.html");
			} else if ("addAccount".equals(flow)) {
				if (user == null) {
					logger.error("Attempt to create account wile in logged out state. Probable attempt of hacking.");
					throw new CommandExecutionException(
							"Attempt to create account wile in logged out state. Probable attempt of hacking.");
				}
				DataManagerFactory.getDataManager().addAccountToUser(account, user);
				responseString = DefaultResourcesManager.getPage("closingAddAccountWindow.html");
			} else {
				logger.error("Unknown flow:" + flow);
				throw new CommandExecutionException("Unknown flow:" + flow);
			}
		} catch (Exception e) {
			logger.error("Failed to create account.", e);
			throw new CommandExecutionException(e);
		}
		connectionData.removeSessionAttribute("identifier");
		connectionData.removeSessionAttribute("flow");
		return responseString;
	}

}