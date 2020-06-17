package backend.resource;

import backend.App;
import backend.domain.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.net.URL;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

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
                .addPackage(User.class.getPackage());
        System.out.println(archive.toString(true));
        return archive;
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
}
