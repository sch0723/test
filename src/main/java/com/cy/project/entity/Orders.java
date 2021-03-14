package com.cy.project.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import javax.persistence.*;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table
@Data
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

	private Integer ordersState;

	@ManyToOne(targetEntity = Users.class)
	@JoinColumn(name="usersId",referencedColumnName = "usersId")
	private Users users;

	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
	@ToString.Exclude
	private Set<OrdersDetail> ordersOrdersDetail = new HashSet<>();



}
