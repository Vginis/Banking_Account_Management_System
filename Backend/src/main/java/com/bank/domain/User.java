package com.bank.domain;

import com.bank.util.Money;
import jakarta.persistence.*;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name="first_name",length = 20,nullable = false)
    private String firstName;
    @Column(name="last_name",length = 20,nullable = false)
    private String lastName;
    @Column(name="username",length = 20,nullable = false,unique = true)
    private String username;
    @Column(name="password",length = 100,nullable = false)
    private String password;
    @Column(name="email",length = 20,nullable = false)
    private String email;
    @Embedded
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "user")
    private List<Account> accountList;
    @Column(name="isAdmin",nullable = false)
    private boolean isAdmin;

    public User(Integer userId, String firstName, String lastName, String email, Address address, List<Account> accountList) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.accountList = accountList;
        this.isAdmin = false;
    }

    public User() {
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws BadRequestException {
        String regexPattern = "^[A-Za-z0-9._%+-]+@(?:aueb\\.gr|outlook\\.com|yahoo\\.com|gmail\\.com)$";
        if (email.matches(regexPattern)){
            this.email = email;}
        else{
            throw new BadRequestException("Invalid Email format. Email must end in aueb.gr or gmail.com or outlook.com or yahoo.com");
        }
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(isAdmin){
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void addAccount(Integer accountNumber, User user, Money balance) throws BadRequestException {
        if (accountNumber==null || user==null || balance==null){throw new BadRequestException("You gave null values");}
        if (!Objects.equals(user.userId, this.userId)){ throw new BadRequestException("Invalid user id");}
        Account newAccount = new Account(accountNumber, user, balance, new ArrayList<>(), new ArrayList<>());
        this.accountList.add(newAccount);
    }

    public void deleteAccount(Account account) throws BadRequestException {
        if (account == null){throw new BadRequestException("You gave null account");}
        if (!this.accountList.contains(account)){throw new BadRequestException("Account not for that user");}
        this.accountList.removeIf(acc -> Objects.equals(acc.getAccountNumber(), account.getAccountNumber()));
    }
}
