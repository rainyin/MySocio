/**
 * 
 */
package net.mysocio.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Key;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Value;

/**
 * @author Aladdin
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class UiObject extends NamedObject implements IUiObject {
	public static final String TAG_START = "<<";
	public static final String TAG_END = ">>";
	private String category;
	@Key(types=String.class)
    @Value(types=UiObject.class)
	private Map<String, IUiObject> innerObjects = new HashMap<String, IUiObject>();
	private List<String> textLabels = new ArrayList<String>();


	public String getObjectTag(int index){
		return getTag(this.category + "." + index);
	}
	public String getTag(String tagName){
		return UiObject.TAG_START + tagName + TAG_END;
	}
	public void setInnerObjects(Map<String, IUiObject> innerObjects) {
		this.innerObjects = innerObjects;
	}

	public void setTextLabels(List<String> textLabels) {
		this.textLabels = textLabels;
	}
	
	public void addInnerObject(UiObject innerObject, int id) {
		this.innerObjects.put(innerObject.getObjectTag(id), innerObject);
	}

	public void addTextLabel(String textLabel) {
		this.textLabels.add(textLabel);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.IUiObject#getCategory()
	 */
	@Override
	public String getCategory() {
		return category;
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.IUiObject#getInnerUiOjjects()
	 */
	@Override
	public Map<String, IUiObject> getInnerUiObjects() {
		return innerObjects;
	}

	/* (non-Javadoc)
	 * @see net.mysocio.data.IUiObject#getInnerTextLabels()
	 */
	@Override
	public List<String> getInnerTextLabels() {
		return textLabels;
	}

	@Override
	public String getHtmlTemplate() {return "";}
}
