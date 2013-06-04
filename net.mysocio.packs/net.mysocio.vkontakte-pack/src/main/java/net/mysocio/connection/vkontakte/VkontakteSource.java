/**
 * 
 */
package net.mysocio.connection.vkontakte;

import net.mysocio.connection.readers.AccountSource;
import net.mysocio.data.accounts.vkontakte.VkontakteAccount;
import net.mysocio.data.management.DataManagerFactory;
import net.mysocio.data.messages.vkontakte.VkontakteMessage;

import com.github.jmkgreen.morphia.annotations.Entity;

/**
 * @author Aladdin
 *
 */
@Entity("sources")
public class VkontakteSource extends AccountSource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4679653964230964105L;

	public Class<?> getMessageClass() {
		return VkontakteMessage.class;
	}

	public void createRoute(String to) throws Exception {
		VkontakteInputProcessor processor = new VkontakteInputProcessor();
		VkontakteAccount account = (VkontakteAccount)getAccount();
		processor.setTo(to);
		processor.setToken(account.getToken());
		processor.setAccountId(account.getId().toString());
		DataManagerFactory.getDataManager().createRoute("timer://" + getId() + "?fixedRate=true&period=60s", processor, 0l);
	}

	@Override
	public void removeRoute(String userId) throws Exception {
		DataManagerFactory.getDataManager().removeRoute("timer://" + getId() + "?fixedRate=true&period=60s", userId);
	}
}
