package com.yuanandrui.blog.service;

import com.yuanandrui.blog.po.User;

public interface UserService {

    User checkUser(String username, String password);
}
