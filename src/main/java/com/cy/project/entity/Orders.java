package com.cy.project.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table
@Getter
@Setter
//@EqualsAndHashCode
public class Orders implements Serializable {

	private static final long serialVersionUID = 1862980183182340905L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ordersId;

	@DateTimeFormat(pattern="yyyy-MM-dd HH-mm-ss")
	private Date ordersBuyDate;

	private Integer ordersSumPrice;

	private String ordersReceiveAddress;

	private String ordersContactName;

	private String ordersPhone;

	private String ordersEmail;

	private Integer ordersState;

	@ManyToOne(targetEntity = Users.class,fetch = FetchType.LAZY)
	@JoinColumn(name="usersId",referencedColumnName = "usersId")
	private Users users;

	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<OrdersDetail> ordersOrdersDetail = new HashSet<>();



}
