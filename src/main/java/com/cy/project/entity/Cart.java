package com.cy.project.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.ToString;

@Entity
@Table
@Data
public class Cart implements Serializable {

	private static final long serialVersionUID = -308733570196544396L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartId;

	//@Column
	//private String cartName;

	private Integer cartNums;

	//@Column
	//private Integer cartPrice;

	@ManyToOne(targetEntity = Users.class)
	@JoinColumn(name="usersId",referencedColumnName = "usersId")
	@ToString.Exclude
	private Users users;

	@ManyToOne(targetEntity = Product.class ,cascade = CascadeType.DETACH)
	@JoinColumn(name="productId",referencedColumnName = "productId")
	private Product product;
}
