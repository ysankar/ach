import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring/ach-repository-jpa-test.xml")
public class EntityManagerBootstrapIntegrationTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void test() {
        assertThat(entityManager).isNotNull();
    }
}
