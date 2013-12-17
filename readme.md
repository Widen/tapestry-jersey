## JAX-RS 2 (Jersey 2.4) integration with Tapestry 5 application

Jersey documentation is located at [jersey.java.net](https://jersey.java.net/documentation/latest/index.html). The [Resources](https://jersey.java.net/documentation/latest/jaxrs-resources.html) section is particular useful.

Some simple, runnable, JAX-RS examples are in the [test tree](src/test/java/org/apache/tapestry5/services/jersey/rest).

    MyRestModule.java

```
@SubModule([JerseyModule])

public static void bind(ServiceBinder binder) {
	binder.bind(HelloApp.class);
	binder.bind(HelloResource.class, HelloResourceImpl.class);
	binder.bind(GoodbyeResource.class, GoodbyeResourceImpl.class);
}

public static void contributeJerseyApplications(Configuration<Application> configuration, HelloApp helloApp)
{
	configuration.add(helloApp);
}
```

	HelloApp.java

```
@ApplicationPath("/api")
public class GreetingApp extends Application
{
	private final HelloResource helloResource;
	private final GoodbyeResource goodbyeResource;

	public GreetingApp(HelloResource helloResource, GoodbyeResource goodbyeResource)
	{
		this.helloResource = helloResource;
		this.goodbyeResource = goodbyeResource;
	}

	@Override
	public Set<Object> getSingletons()
	{
		return Sets.newHashSet(helloResource, goodbyeResource, new LoggingFilter());
	}
}
```

	HelloResource.java

```
@Path("/hello/{name}")
public interface HelloResource
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting getHelloResponse(@PathParam("firstname") String firstname,
                                     @QueryParam("lastname") @DefaultValue("unknown") String lastname,
                                     @QueryParam("catch-phrase") @DefaultValue("An apple a day keeps...") String catchPhrase);
}
```
