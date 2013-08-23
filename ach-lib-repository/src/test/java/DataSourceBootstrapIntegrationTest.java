import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring/ach-repository-jpa-test.xml")
public class DataSourceBootstrapIntegrationTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void test() {
        assertThat(dataSource).isNotNull();
    }
}
