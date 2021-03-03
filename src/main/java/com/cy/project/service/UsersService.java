package com.cy.project.service;

import com.cy.project.entity.Users;
import com.cy.project.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {

    private final UsersRepository ur;

    public UsersService(UsersRepository usersRepository) {
        this.ur = usersRepository;
    }

    public Users findByUsersAccount(String account) {
        return ur.findByUsersAccount(account);
    }
}
