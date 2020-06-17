package backend.resource;

import backend.dao.UserDao;
import backend.domain.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Inject
    private UserDao dao;

    @GET
    public List<User> getAll() {
        return dao.getAll();
    }

    @GET @Path("{id}")
    public User getStudentById(@PathParam("id") int id) {
        return dao.get(id);
    }

    @GET @Path("q") // read
    public User get(
            @QueryParam("email") String emailaddress,
            @QueryParam("password") String password) {
        return dao.getUserByEmailAndPassword(emailaddress, password);
    }

    @POST
    public User addUser(User user) {
        return dao.insert(user);
    }
}
