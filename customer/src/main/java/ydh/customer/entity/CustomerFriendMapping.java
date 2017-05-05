package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

@Entity(name = "CUSTOMER_FRIEND_MAPPING")
public class CustomerFriendMapping {
	@Id
	@Column(name = "CUS_FRIEND_MAPPING_ID")
	private String cusFriendMappingId;

	@Column(name = "CUS_ID")
	private Integer cusId;

	@Column(name = "FRIEND_ID")
	private Integer friendId;

	public CustomerFriendMapping() {
	}

	public String getCusFriendMappingId() {
		return cusFriendMappingId;
	}

	public void setCusFriendMappingId(String cusFriendMappingId) {
		this.cusFriendMappingId = cusFriendMappingId;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public Integer getFriendId() {
		return friendId;
	}

	public void setFriendId(Integer friendId) {
		this.friendId = friendId;
	}
}
