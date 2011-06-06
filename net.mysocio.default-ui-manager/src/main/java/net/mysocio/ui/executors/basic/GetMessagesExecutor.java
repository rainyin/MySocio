/**
 * 
 */
package net.mysocio.ui.executors.basic;

import java.util.Collections;
import java.util.List;

import net.mysocio.data.IConnectionData;
import net.mysocio.data.IMessage;
import net.mysocio.data.SocioUser;
import net.mysocio.ui.management.CommandExecutionException;
import net.mysocio.ui.management.ICommandExecutor;

/**
 * @author Aladdin
 *
 */
public class GetMessagesExecutor implements ICommandExecutor {
	public static final String ALL_MESSAGES_PLACEHOLDER = "ALL";

	/* (non-Javadoc)
	 * @see net.mysocio.ui.management.ICommandExecutor#execute(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String execute(IConnectionData connectionData) throws CommandExecutionException{
		StringBuffer output = new StringBuffer();
		List<? extends IMessage> messages = getMessages(connectionData);
		for (IMessage message : messages) {
			output.append(wrapMessage(message.getId(), message.getTitle(), message.getLink(), message.getText()));
		}
		return output.toString();
	}
	
	private static List<IMessage> getMessages(IConnectionData connectionData) {
		String id = connectionData.getRequestParameter("sourceId");
		if (id == null){
			return Collections.emptyList();
		}
		List<IMessage> unreadMessages;
		SocioUser user = connectionData.getUser();
		if (id.equalsIgnoreCase(ALL_MESSAGES_PLACEHOLDER)){
			unreadMessages = user.getAllUnreadMessages();
		}else{
			unreadMessages = user.getUnreadMessages(user.getSortedSources().get(id));
		}
		return unreadMessages;
	}
	
	/**
	 * @param output
	 * @param text
	 */
	private static String wrapMessage(String id, String title, String link, String text) {
		StringBuffer output = new StringBuffer();
		output.append("<div class='Message' id=\"").append(id).append("\">");
		String actualTitle = "No Title";
		if (title != null && title.length() >= 0){
			actualTitle = title;
		}
		output.append("<div class='MessageTitle'>");
		output.append("<a href=\""+ link + "\" target=\"_blank\">").append(actualTitle).append("</a>");
		output.append("</div>");
		output.append(text);
		output.append("</div>");
		return output.toString();
	}
}