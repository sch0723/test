package com.cy.project.service;

import com.cy.project.entity.Users;
import com.cy.project.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public Users findByUsersAccount(String account){
        return usersRepository.findByUsersAccount(account);
    }
}
