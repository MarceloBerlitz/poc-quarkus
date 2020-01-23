package dev.berlitz;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.internal.ObjectUtil;
import io.quarkus.resteasy.runtime.NotFoundExceptionMapper;
import org.jboss.resteasy.annotations.Body;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource {

    private List<Customer> customers = new ArrayList<>();
    private Integer count = 1;

    @POST
    public Customer create(Customer customer) {
        customer.setId(count++);
        customers.add(customer);
        return customer;
    }

    @GET
    @Path("/{id}")
    public Response read(@PathParam Integer id) {
        Customer customer = customers.stream()
                .filter(cust -> cust.getId().equals(id))
                .findAny().orElse(null);

        return !Objects.isNull(customer) ? Response.ok(customer).build() :
                Response.status(404, "Customer not found.").build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam Integer id, Customer customer) {
        Customer old = customers.stream()
                .filter(cust -> cust.getId().equals(id))
                .findAny().get();

        old.setName(customer.getName());
        old.setPhone(customer.getPhone());

        return Response.ok(old).build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam Integer id) {
        customers = customers.stream()
                .filter(cust -> !cust.getId().equals(id))
                .collect(Collectors.toList());
    }
}