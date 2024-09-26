package com.datadog.test;
import io.opentracing.Span;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.opentracing.util.GlobalTracer;

@RestController
public class Controller {

    @RequestMapping(value = "/**")
    public String homePage(@RequestBody(required = false) String req) {
        final Span span = GlobalTracer.get().activeSpan();
        if (span != null) {
            span.setTag("appsec.event", true);
        }

        // NOTE: see README.md for setting up the docker image to quickly test this

        String url = "jdbc:postgresql://localhost/sportsdb?user=postgres&password=postgres";
        try (Connection pgConn = DriverManager.getConnection(url)) {

            Statement st = pgConn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM display_names LIMIT 10");
            while (rs.next())
            {
                System.out.print("Column 2 returned ");
                System.out.println(rs.getString(2));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return "pgsql exception :(";
        }

        // NOTE: SQLite spans do not seem to be sent in the trace for some reason

        // try (Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:")) {
        //     try (Statement st = conn.createStatement()) {
        //         st.execute("restore from /tmp/chinook.db");

        //         // let's see if we can read one of the tables
        //         ResultSet rs = st.executeQuery("SELECT * FROM artists");
        //         while (rs.next()) {
        //             System.out.println(rs.getString(1));
        //         }
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace(System.err);
        //     return "sql exception :(";
        // }


        return "hello in-memory sqlite " + req;
    }
}
