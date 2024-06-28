package com.bank.repository;


import com.bank.util.Money;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;


import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyCustomType implements UserType<Money> {
    @Override
    public int getSqlType() {
        return 0;
    }

    @Override
    public Class<Money> returnedClass() {
        return null;
    }

    @Override
    public boolean equals(Money x, Money y) {
        return false;
    }

    @Override
    public int hashCode(Money x) {
        return 0;
    }

    @Override
    public Money nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Money value, int index, SharedSessionContractImplementor session) throws SQLException {

    }

    @Override
    public Money deepCopy(Money value) {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Money value) {
        return null;
    }

    @Override
    public Money assemble(Serializable cached, Object owner) {
        return null;
    }
}
