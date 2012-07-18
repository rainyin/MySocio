/**
 * 
 */
package net.mysocio.data.accounts.facebook;

import net.mysocio.data.NamedObject;

/**
 * @author Aladdin
 *
 */
public class NamedFacebookObject extends NamedObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3395293894155152292L;
	private String facebookId;

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
}
