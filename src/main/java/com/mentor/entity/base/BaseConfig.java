package com.mentor.entity.base;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * This is an object that contains data related to the jo_config table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jo_config"
 */
@MappedSuperclass
public abstract class BaseConfig  implements Serializable {
	private static final long serialVersionUID = 5131974517661845402L;
	
	public static String REF = "Config";
	public static String PROP_VALUE = "value";
	public static String PROP_ID = "id";


	// constructors
	public BaseConfig () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseConfig (java.lang.String id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}


	// primary key
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "configId", unique = true, nullable = false, insertable = true, updatable = false)
	private java.lang.String id;

	// fields
	@Column(length=20000)
	private java.lang.String value;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
     *  column="cfg_key"
     */
	public java.lang.String getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.String id) {
		this.id = id;
	}




	/**
	 * Return the value associated with the column: cfg_value
	 */
	public java.lang.String getValue () {
		return value;
	}

	/**
	 * Set the value related to the column: cfg_value
	 * @param value the cfg_value value
	 */
	public void setValue (java.lang.String value) {
		this.value = value;
	}



	public String toString () {
		return super.toString();
	}

}