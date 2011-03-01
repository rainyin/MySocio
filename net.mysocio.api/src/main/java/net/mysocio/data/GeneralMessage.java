/**
 * 
 */
package net.mysocio.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;



/**
 * @author Aladdin
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public abstract class GeneralMessage extends SocioObject implements IMessage{
	@Persistent(nullValue=NullValue.DEFAULT)
	private String link;
	@Persistent(nullValue=NullValue.DEFAULT)
	private String title;
	@Persistent(nullValue=NullValue.DEFAULT)
	private String text;
	@Persistent(nullValue=NullValue.DEFAULT)
	private Long sourceId;

	public GeneralMessage() {
		super();
	}

	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	@Override
	public void setText(String text) {
		String cutText = cutMessageText(text);
		this.text = cutText;
	}
	
	/**
	 * @return the link
	 */
	@Override
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	@Override
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the source
	 */
	public Long getSourceId() {
		return sourceId;
	}

	/**
	 * @param source the source to set
	 */
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	
	/**
	 * Cleans text from HTML tags and cuts it to 250 symbols
	 * @param text
	 * @return
	 */
	private static String cutMessageText(String text) {
		Pattern p = Pattern.compile("<(.|\n)+?>");
		Matcher matcher = p.matcher(text);
		String cutText = matcher.replaceAll("");
		int textLength = Math.min(cutText.length(), 250);
		cutText = cutText.substring(0,textLength) + " ...";
		return cutText;
	}
}
