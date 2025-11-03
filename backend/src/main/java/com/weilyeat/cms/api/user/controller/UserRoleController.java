package com.weilyeat.cms.api.user.controller;

import com.weilyeat.cms.api.user.service.UserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/user/role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

}