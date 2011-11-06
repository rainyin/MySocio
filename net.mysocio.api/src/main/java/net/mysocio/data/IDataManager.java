/**
 * 
 */
package net.mysocio.data;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.mysocio.connection.readers.ISource;
import net.mysocio.data.accounts.Account;
import net.mysocio.data.messages.IMessage;
import net.mysocio.data.ui.IUiObject;

/**
 * @author Aladdin
 *
 */
public interface IDataManager {

	public void saveObjects(List<? extends Object> objects);

	public void saveObject(Object object);
	
	public void deleteObject(Object object);

	public SocioUser getUser(Account account, Locale locale);
	
	public Account getAccount(Class clazz, String accountUniqueId);

	public Map<String, IUiObject> getUserUiObjects(SocioUser user);

	public ISource createSource(ISource source);
	
	public IMessage createMessage(IMessage message);
	
	public List<IMessage> getMessages(List<String> ids);
	
	public ISocioObject getObject(Class<?> clazz, String id);
	
	public<T extends ISocioObject> List<T> getObjects(Class<?> T);
	
	public void addAccountToUser(Account account, SocioUser user);
	
	public void flush();
}
