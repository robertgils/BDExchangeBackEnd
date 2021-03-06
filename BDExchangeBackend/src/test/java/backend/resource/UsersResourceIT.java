package backend.resource;

import backend.App;
import backend.dao.UserDao;
import backend.domain.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class UsersResourceIT {

    @ArquillianResource
    private URL deploymentURL;

    private String usersResource;

    @Before
    public void setup() {
        usersResource = deploymentURL + "api/users";
    }

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(App.class)
                .addClass(UsersResource.class)
                .addClass(UserDao.class)
                .addPackage(User.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("test-beans.xml", "META-INF/beans.xml")
                .addAsLibraries(hibernate());
        System.out.println(archive.toString(true));
        return archive;
    }

    private static File[] hibernate() {
        return Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.hibernate:hibernate-entitymanager")
                .withTransitivity()
                .asFile();
    }

    @Test
    public void testUsersEndpointGetAll() {
        System.out.println("UsersResource = " + usersResource);
        String message = ClientBuilder.newClient()
                .target(usersResource)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        System.out.println(message);
        assertThat(message, containsString("[{\"id\":"));
    }

    @Test
    public void testUsersEndpointGetID() {
        System.out.println("UsersResource = " + usersResource + "/102");
        String message = ClientBuilder.newClient()
                .target(usersResource + "/102")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        System.out.println(message);
        assertThat(message, containsString("{\"id\":"));
    }

    @Test
    public void testUsersEndpointQueryParam() {
        System.out.println("UsersResource = " + usersResource + "/q?email=123@123.nl&password=123");
        String message = ClientBuilder.newClient()
                .target(usersResource + "/q?email=123@123.nl&password=123")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        System.out.println(message);
        assertThat(message, containsString("{\"id\":"));
    }

    @Test
    public void testUsersEndpointPost() {
        System.out.println("UsersResource = " + usersResource);
        User testUser = new User("TestUser@Test.nl", "Test1234");
        User response = ClientBuilder.newClient()
                .target(usersResource)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(testUser), User.class);

        assertThat(response.getId(), is(not(nullValue())));
    }
}
