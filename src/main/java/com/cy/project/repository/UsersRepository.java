package com.cy.project.repository;

import com.cy.project.entity.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends org.springframework.data.repository.Repository<Users,Long> {

    Users findByUsersAccount(String account);

    Users findByUsersAccountAndUsersPwd(String account,String usersPwd);
}
