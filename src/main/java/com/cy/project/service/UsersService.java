package com.cy.project.service;

import com.cy.project.entity.Users;
import com.cy.project.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository ur;

    public UsersService(UsersRepository usersRepository) {
        this.ur = usersRepository;
    }

    /**
     * 以帳號搜索users
     * @param account 帳號
     * @return
     */
    public Users findByUsersAccount(String account) {
        return ur.findByUsersAccount(account);
    }
}
