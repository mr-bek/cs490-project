package mum.pmp.mstore.model;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class Address {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;
	
	@Column(name = "CITY")
	private String city;
	
	//add street
	@Column(name = "STREET")
	private String street;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "ZIPCODE")
	private String zipcode;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getId() {
		return id;
	}

}
