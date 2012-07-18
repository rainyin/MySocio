/**
 * 
 */
package net.mysocio.data.management;

import java.util.ArrayList;
import java.util.List;

import net.mysocio.connection.writers.Destination;
import net.mysocio.data.IDataManager;
import net.mysocio.data.IMessagesManager;
import net.mysocio.data.SocioUser;
import net.mysocio.data.messages.GeneralMessage;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aladdin
 *
 */
public class MessagesManager implements IMessagesManager {
	private static final Logger logger = LoggerFactory.getLogger(MessagesManager.class);
	private static MessagesManager instance = new MessagesManager();
	
	private MessagesManager(){}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IMessagesManager#postMessage(net.mysocio.data.messages.IMessage, net.mysocio.connection.writers.IDestination)
	 */
	public void postMessage(GeneralMessage message, Destination destination) {
		destination.postMessage(message);
	}

	public static IMessagesManager getInstance() {
		return instance;
	}

	public List<String> storeMessages(List<GeneralMessage> messages) {
		List<String> stored = new ArrayList<String>();
		for (GeneralMessage message : messages) {
			GeneralMessage savedMessage = storeMessage(message);
			stored.add(savedMessage.getId().toString());
		}
		return stored;
	}

	public GeneralMessage storeMessage(GeneralMessage message) {
		IDataManager dataManager = DataManagerFactory.getDataManager();
		GeneralMessage savedMessage = dataManager.createMessage(message);
		return savedMessage;
	}

	public List<GeneralMessage> getMessagesForSelectedSource(String userId, String tagId) {
		IDataManager dataManager = DataManagerFactory.getDataManager();
		List<GeneralMessage> unreadMessages = dataManager.getUnreadMessages(userId, tagId);
//		for (String id : unreadMessages) {
//			IMessage message = getCacheMessage(id);
//			if (message != null){
//				messages.add(message);
//			}else{
//				message = (GeneralMessage)DataManagerFactory.getDataManager(user).getObject(id);
//				if (message == null){
//					//It's data loss, should be investigated
//					logger.error("Message with id " + id + "is missing");
//					setMessagesReadden(user, id);
//				}
//				messages.add(message);
//			}
//		}
		return unreadMessages;
	}
	
	public void setMessagesReadden(SocioUser user, String messagesId){
		if (messagesId.isEmpty()){
			return;
		}
		String[] ids = messagesId.split(",");
		ProducerTemplate producerTemplate = CamelContextManager.getProducerTemplate();
		for (String id : ids) {
			producerTemplate.sendBody("activemq:" + user.getId() + ".messageReaden",id);
		}
	}
	private void cacheMessage(GeneralMessage message){
		CacheManager cm = CacheManager.create();
		Cache cache = cm.getCache("Messages");
		Element element = new Element(message.getId(), message);
		cache.put(element);
	}
	
	private GeneralMessage getCacheMessage(String id){
		CacheManager cm = CacheManager.create();
		Cache cache = cm.getCache("Messages");
		Element element = cache.get(id);
		if (element == null){
			return null;
		}
		return (GeneralMessage)element.getValue();
	}
}
