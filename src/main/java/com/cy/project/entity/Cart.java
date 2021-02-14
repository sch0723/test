package com.cy.project.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;

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

	@ManyToOne
	@JoinColumn(name="users_id")
	private Users users;

	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
}
