/**
 * 
 */
package net.mysocio.authentication.test;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import net.mysocio.data.messages.GeneralMessage;

/**
 * @author Aladdin
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class TestMessage extends GeneralMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1343088110385165921L;

}