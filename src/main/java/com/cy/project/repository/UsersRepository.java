package com.cy.project.repository;

import com.cy.project.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

    Users findByUsersAccount(String account);

    Users findByUsersAccountAndUsersPwd(String account,String usersPwd);
}
