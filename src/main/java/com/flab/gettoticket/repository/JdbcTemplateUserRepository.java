package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SignupReqeust;
import com.flab.gettoticket.entity.Users;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateUserRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int saveUser(SignupReqeust signupReqeust) {
        String sql = "INSERT INTO users (email,name,password) VALUES (?,?,?)";

        String email = signupReqeust.getEmail();
        String name = signupReqeust.getName();
        String password = signupReqeust.getPassword();

        return jdbcTemplate.update(sql, email, name, password);
    }

    @Override
    public Optional<Users> findUserEmail(String email) {
        String sql = "SELECT email, name, password, created_at FROM users WHERE email=?";

        List<Users> user = jdbcTemplate.query(sql, usersRowMapper(), email);

        return Optional.ofNullable(user.isEmpty() ? null : user.get(0));
    }

    private RowMapper<Users> usersRowMapper() {
        return ((rs, rowNum) -> {
            Users user = new Users();
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setCreatedAt(rs.getString("created_at"));
            return user;
        });
    }
}
