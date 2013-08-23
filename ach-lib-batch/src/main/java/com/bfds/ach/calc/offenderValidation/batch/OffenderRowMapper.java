package com.bfds.ach.calc.offenderValidation.batch;

import com.bfds.ach.calc.validation.domain.Offender;
import com.bfds.ach.calc.validation.domain.OffenderAddress;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OffenderRowMapper implements RowMapper<Offender>{


    @Override
    public Offender mapRow(ResultSet rs, int i) throws SQLException {
        Offender offender = new Offender();
        offender.setFirstName(rs.getString("first_name"));
        offender.setLastName(rs.getString("last_name"));
        offender.setSsn(rs.getString("ssn"));
        offender.setTin(rs.getString("tin"));
        offender.setAddress(new OffenderAddress());
        offender.getAddress().setAddress1(rs.getString("address1"));
        offender.getAddress().setCity(rs.getString("city"));
        offender.getAddress().setStateCode(rs.getString("state_code"));
        

        return offender;
    }
}
