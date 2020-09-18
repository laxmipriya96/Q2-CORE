package com.vm.qsmart2api;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Import({
//    DispatcherServletAutoConfiguration.class,
//    EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
//    ErrorMvcAutoConfiguration.class,
//    HttpEncodingAutoConfiguration.class,
//    HttpMessageConvertersAutoConfiguration.class,
//    JacksonAutoConfiguration.class,
//    PropertyPlaceholderAutoConfiguration.class,
//    ServletWebServerFactoryAutoConfiguration.class,
//    MultipartAutoConfiguration.class,
//    SwaggerConfiguration.class,
//    Swagger2DocumentationConfiguration.class,
//    WebServerConfig.class,
//    WebMvcAutoConfiguration.class,
//    MssqlDataSource.class,
//    TestController.class,
//    TestService.class,
//    JwtTokenUtil.class,
//    LocalResolverConfig.class,
//    AuthController.class,
//    UserController.class,
//    AuditService.class,
//    RoleController.class,
//    RoleService.class,
//    UserService.class,
//    MenuController.class,
//    MenuNewController.class,
//    MenuService.class,
//    EnterpriseController.class,
//    EnterpriseService.class,
//    LocationController.class,
//    LocationService.class,
//    BranchController.class,
//    BranchService.class,
//    BranchTypeService.class,
//    ApptTypeController.class,
//    ApptTypeService.class,
//    ServiceController.class,
//    ServiceService.class,
//    RoomController.class,
//    RoomService.class,
//    SecurityConfig.class,
//    DateUtils.class,
//    CustomizedResponseEntityExceptionHandler.class})
@SpringBootApplication
@EnableScheduling
public class Qsmart2ApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Qsmart2ApiApplication.class, args);
        String[] beans = ctx.getBeanDefinitionNames();
        for (String s : beans) {
            // System.out.println("Bean Name : " + s);
        }
    }

//    @Autowired
//    @PersistenceContext(name = DBConstants.JPA_UNIT_NAME_QSMART)
//    EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        //   System.out.println("----------->"+entityManager.toString());
      //  SampleProjectApplication.main(args);

    }

    private String roomEngName;
    private String roomArbName;

    private String serviceEngName;

    private String serviceArbName;

}
