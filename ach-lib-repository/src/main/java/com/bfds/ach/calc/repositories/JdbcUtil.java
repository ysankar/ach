package com.bfds.ach.calc.repositories;


import java.sql.Date;

public abstract class JdbcUtil {

   public static Date toSqlDate(java.util.Date date) {
        return date == null ? null : new Date(date.getTime());
   }
}
