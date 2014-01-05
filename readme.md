## JAX-RS 2 / JSR-339 (using Jersey 2.4) integration with Tapestry 5 configured application

Jersey documentation is located at [jersey.java.net](https://jersey.java.net/documentation/latest/index.html). The [Resources](https://jersey.java.net/documentation/latest/jaxrs-resources.html) section is particular useful.

Some simple, runnable, JAX-RS examples are in the [test tree](src/test/java/org/apache/tapestry5/services/jersey/rest/services). Run the pre-integrated [JettyRunner](/src/test/java/org/apache/tapestry5/services/jersey/rest/JettyRunner.java) class and hit http://localhost:8080/

To configure your JAX-RS 2.0 app, import [JerseyModule](src/main/java/org/apache/tapestry5/services/jersey/JerseyModule.java) as a SubModule. Additionally, bind your JAX-RS Application class extending [TapestryBackedJerseyApplication](src/main/java/org/apache/tapestry5/services/jersey/TapestryBackedJerseyApplication.java) and any additional resource services. Contribute the JAX-RS application to the TapestryInitializedJerseyApplications service.


#### MyRestModule.java
```
@SubModule([JerseyModule.class])
public class MyRestModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(HelloApp.class);
		binder.bind(HelloResource.class, HelloResourceImpl.class);
		binder.bind(GoodbyeResourceImpl.class); // OK, but won't be re-loadable by T5 IOC
	}

    public static void contributeTapestryInitializedJerseyApplications(Configuration<TapestryBackedJerseyApplication> configuration, HelloApp helloApp)
    {
        configuration.add(colorsApp);
    }
}
```


Your JAX-RS application class is required to extend TapestryBackedJerseyApplication and must have an javax.ws.rs.ApplicationPath annotation applied.
You must include any JAX-RS Resource classes as Singletons. Typically, these resource classes will contain other Tapestry IOC configured services.

Additionally, several useful add-on modules are available.
 - `org.apache.tapestry5.services.jersey.providers.JerseyCheckForUpdatesProviderFilter` - Enables hot class re-loading of resource classes.
 - `org.apache.tapestry5.services.jersey.providers.gson.GsonMessageBodyHandler` - Enables Gson JSON message handler.
 - `org.apache.tapestry5.services.jersey.providers.ValueEncoderSourceParamConverterProvider` - Enables T5 Value Encoder conversions in resource classes.

#### HelloApp.java
```
@ApplicationPath("/api")
public class GreetingApp extends TapestryBackedJerseyApplication
{
    private final boolean productionMode; // used to only add updatesProvider if production mode is FALSE

    private final JerseyCheckForUpdatesProviderFilter updatesProvider; // optional; enables service class re-loading of JAX-RS resource classes

    private final GsonMessageBodyHandler gsonMessageBodyHandler; // optional; enables GSON JSON conversion

	private final Set<Object> singletonResources = new HashSet<Object>();

	public GreetingApp(@Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
					   JerseyTapestryRequestContext requestContext,
                       ContainerRequestContextProvider containerRequestContextProvider,
                       JerseyCheckForUpdatesProviderFilter updatesProvider,
                       GsonMessageBodyHandler gsonMessageBodyHandler,
                       HelloResource helloResource,
                       GoodbyeResource goodbyeResource) {
		super(requestContext, containerRequestContextProvider);
		this.productionMode = productionMode;
		this.updatesProvider = updatesProvider;
		this.gsonMessageBodyHandler = gsonMessageBodyHandler;

		singletonResources.add(helloResource);
		singletonResources.add(goodbyeResource);
	}

	@Override
	public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();

        singletonResources.add(gsonMessageBodyHandler);

        if (!productionMode)
        {
            log.info("Adding T5 service re-loader provider");
            singletonResources.add(updatesProvider);
        }

        return singletonResources;
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(org.glassfish.jersey.filter.LoggingFilter.class);
		return classes;
	}
}
```

Example of a typical Resource class. All JAX-RS annotations should be declared on the interface -- this allows the resource class to be re-loadable.

#### HelloResource.java
```
@Path("/hello/{name}")
public interface HelloResource
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting getHelloResponse(@PathParam("firstname") String firstname,
                                     @QueryParam("lastname") @DefaultValue("unknown") String lastname,
                                     @QueryParam("catch-phrase") @DefaultValue("An apple a day keeps...") String catchPhrase,
                                     @Context Request request,
                                     @Context UriInfo uriInfo));
}
```

#### HelloResourceImpl.java
```
public class HelloResourceImpl implements HelloResource
{
    private final Logger log = LoggerFactory.getLogger(HelloResourceImpl.class);

    @org.apache.tapestry5.ioc.annotations.Inject // T5 Injection; can use constructor injection too
    private GreetingService greetingService;

    @Override
    public Greeting getHelloResponse(String name, String last, String phrase, Request request, UriInfo uriInfo)
    {
        log.info("Request: {} {}", request.getMethod(), uriInfo.getRequestUri());
        return greetingService.getHelloGreeting(name, last, phrase);
    }
}
```
