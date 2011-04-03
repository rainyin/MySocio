/**
 * 
 */
package net.mysocio.data.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import net.mysocio.connection.readers.ISource;
import net.mysocio.connection.readers.Source;
import net.mysocio.connection.readers.SourcesGroup;
import net.mysocio.data.Contact;
import net.mysocio.data.GeneralMessage;
import net.mysocio.data.IMessage;
import net.mysocio.data.ISocioObject;
import net.mysocio.data.IUiObject;
import net.mysocio.data.SocioUser;
import net.mysocio.data.UiObject;
import net.mysocio.data.UserUiObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aladdin
 *
 */
public class JdoDataManager extends AbstractDataManager {
	private static final Logger logger = LoggerFactory.getLogger(JdoDataManager.class);
	private static PersistenceManager pm;
	private static JdoDataManager instance = new JdoDataManager();
	
	public static JdoDataManager getInstance() {
		if (pm == null){
			// Create a PersistenceManagerFactory for this datastore
	        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	        pm = pmf.getPersistenceManager();
		}
		return instance;
	}
	
	private JdoDataManager(){}
	
	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#createUser(java.lang.String, java.lang.String)
	 */
	@Override
	public SocioUser createUser(String identifier, String identifierValue, Locale locale) {
		SocioUser user = new SocioUser();
		setUserIdentifier(user, identifier, identifierValue);
		user.setName(getNameFromIdentifier(identifier, identifierValue));
		user.setLocale(locale.getLanguage());
		saveUser(user);
		return user;//getUser(identifier, identifierValue);
	}

	private String getNameFromIdentifier(String identifier,
			String identifierValue) {
		UserIdentifier userIdentifier = UserIdentifier.valueOf(identifier);
		String name = "";
		switch (userIdentifier) {
		case email:
			name = identifierValue.split("@")[0];
			break;
		default:
			break;
		}
		return name;
	}
	
	public IUiObject getUiObject(String category, String name){
		Transaction tx = pm.currentTransaction();
		UiObject uiObject;
        try
        {
            tx.begin();
            Extent<UiObject> e=pm.getExtent(UiObject.class,true);
            Query q=pm.newQuery(e, "category == " + category + " and name == " + name);
            q.setUnique(true);
            uiObject = (UiObject)q.execute();
            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
        return uiObject;
	}
	
	public void saveUiObject(UiObject uiObject) throws DuplicateMySocioObjectException{
		String category = uiObject.getCategory();
		String name = uiObject.getName();
		if (getUiObject(category, name) == null){
			throw new DuplicateMySocioObjectException("UI object with name " + name + " already exists in category " + category);
		}
		saveObject(uiObject);
	}
	
	
	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#saveObjects(java.util.List)
	 */
	@Override
	public void saveObjects(List<? extends ISocioObject> objects) {
		for (ISocioObject iSocioObject : objects) {
			saveObject(iSocioObject);
		}
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#saveObject(net.mysocio.data.ISocioObject)
	 */
	@Override
	public void saveObject(ISocioObject object) {
		Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            pm.makePersistent(object);
            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#saveSource(net.mysocio.connection.readers.ISource)
	 */
	@Override
	public void saveSource(ISource source) {
		saveObject(source);
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#saveSourcesGroup(net.mysocio.connection.readers.SourcesGroup)
	 */
	@Override
	public void saveSourcesGroup(SourcesGroup sourcesGroup) {
		saveObject(sourcesGroup);
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#saveContact(net.mysocio.data.Contact)
	 */
	@Override
	public void saveContact(Contact contact) {
		saveObject(contact);
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#saveUser(net.mysocio.data.SocioUser)
	 */
	@Override
	public void saveUser(SocioUser user) {
		saveObject(user);
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#getSources(java.lang.Class)
	 */
	@Override
	public List<ISource> getSources(Class clazz) {
		Transaction tx = pm.currentTransaction();
		List<ISource> sources = new ArrayList<ISource>();
        try
        {
            tx.begin();
            Extent<ISource> e = pm.getExtent(clazz, true);
            Iterator<ISource> iter = e.iterator();
            while (iter.hasNext())
            {
            	sources.add(iter.next());
            }
            tx.commit();
        }
        catch (Exception e)
        {
        	logger.error("Exception thrown during retrieval of Extent : ", e);
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
		return sources;
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#getSource(java.lang.Long)
	 */
	@Override
	public ISource getSource(Long id) {
		Transaction tx = pm.currentTransaction();
		Source source;
        try
        {
            tx.begin();
            Extent<Source> e=pm.getExtent(Source.class,true);
            Query q=pm.newQuery(e, "id == " + id);
            q.setUnique(true);
            source = (Source)q.execute();
            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
		return source;
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#getMessages(net.mysocio.connection.readers.ISource, java.lang.Long)
	 */
	@Override
	public List<IMessage> getMessages(ISource source, Long firstId) {
		Transaction tx = pm.currentTransaction();
		List<IMessage> messages = new ArrayList<IMessage>();
        try
        {
            tx.begin();
            Extent<GeneralMessage> e=pm.getExtent(GeneralMessage.class,true);
            Query q=pm.newQuery(e, "sourceId == " + source.getId() + " and id > " + firstId);
            q.setOrdering("id ascending");
            messages = (List<IMessage>)q.execute();
            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
		return messages;
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.management.IDataManager#getUser(java.lang.String, java.lang.String)
	 */
	@Override
	public SocioUser getUser(String identifier, String identifierValue) {
		Transaction tx = pm.currentTransaction();
		SocioUser user;
        try
        {
            tx.begin();
            Extent<SocioUser> e=pm.getExtent(SocioUser.class,true);
            Query q=pm.newQuery(e, identifier + " == \"" + identifierValue + "\"");
            q.setUnique(true);
            user = (SocioUser)q.execute();
            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
		return user;
	}

	@Override
	public Map<String, IUiObject> getUserUiObjects(SocioUser user) {
		UserUiObjects objects;
		Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Extent<UserUiObjects> e=pm.getExtent(UserUiObjects.class,true);
            Query q=pm.newQuery(e, "userId == " + user.getId());
            q.setUnique(true);
            objects = (UserUiObjects)q.execute();
            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
        if (objects == null){
        	return new HashMap<String, IUiObject>();
        }
		return objects.getUserUiObjects();
	}
}
