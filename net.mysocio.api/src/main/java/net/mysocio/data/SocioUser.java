/**
 * 
 */
package net.mysocio.data;

import net.mysocio.data.accounts.Account;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/**
 * @author Aladdin
 * Although User is subclass of MySocio object, tags list is private list of tags usable by this user and not object tags.
 */
@Entity("my_socio_users")
public class SocioUser extends NamedObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2886854604233072581L;
	@Reference
	private Account mainAccount;
	private String locale;
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Account getMainAccount() {
		return mainAccount;
	}

	public void setMainAccount(Account mainAccount) {
		this.mainAccount = mainAccount;
	}
}
