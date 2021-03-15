package com.cy.project.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Users implements Serializable {

	private static final long serialVersionUID = 7971373323053352652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer usersId;

	@Column(unique = true,nullable = false)
	private String usersAccount;

	@Column(nullable = false)
	private String usersPwd;

	private String usersName;

	private String usersEmail;

	@OneToMany(mappedBy = "users",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private List<Orders> usersOrders = new ArrayList<>();
}
