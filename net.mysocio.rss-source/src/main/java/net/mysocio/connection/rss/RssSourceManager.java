/**
 * 
 */
package net.mysocio.connection.rss;

import java.util.List;

import net.mysocio.connection.readers.ISource;
import net.mysocio.connection.readers.ISourceManager;
import net.mysocio.data.messages.IMessage;

/**
 * @author Aladdin
 *
 */
public class RssSourceManager implements ISourceManager {

	/* (non-Javadoc)
	 * @see net.mysocio.connection.readers.ISourceManager#getLastMessages(net.mysocio.connection.readers.ISource, java.lang.Long, java.lang.Long)
	 */
	public List<IMessage> getLastMessages(ISource source, Long from, Long to)
			throws Exception {
		return null;
	}

}
