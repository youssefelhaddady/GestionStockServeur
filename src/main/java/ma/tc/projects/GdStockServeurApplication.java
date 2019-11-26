package ma.tc.projects;

import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ma.tc.projects.entity.AppRole;
import ma.tc.projects.entity.AppUser;
import ma.tc.projects.entity.Categorie;
import ma.tc.projects.entity.Client;
import ma.tc.projects.entity.Fournisseur;
import ma.tc.projects.entity.Magasin;
import ma.tc.projects.enums.TypeClientEnum;
import ma.tc.projects.repository.AppRoleRepository;
import ma.tc.projects.repository.AppUserRepository;
import ma.tc.projects.repository.CategorieRepository;
import ma.tc.projects.repository.ClientRepository;
import ma.tc.projects.repository.FournisseurRepository;
import ma.tc.projects.repository.MagasinRepository;
import ma.tc.projects.service.AccountService;


@SpringBootApplication
@EnableScheduling
public class GdStockServeurApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(GdStockServeurApplication.class, args);

		AccountService accountService = ctx.getBean(AccountService.class);
		MagasinRepository magasinRepository = ctx.getBean(MagasinRepository.class);
		AppUserRepository userRepository = ctx.getBean(AppUserRepository.class);
		AppRoleRepository appRoleRepository = ctx.getBean(AppRoleRepository.class);


        
      	Stream.of("admin","user").forEach(e->{
      		// adding the default roles
      		if(!appRoleRepository.existsByRoleName(e.toUpperCase()))
      			accountService.saveAppRole(new AppRole(null, e.toUpperCase()));
      		
      		// adding the default users and assigning there roles
      		if(!userRepository.existsByUsername(e)) {
      			accountService.saveUser(e, e+e, e+e);
	      		accountService.addRoleToUser("admin", e.toUpperCase());
			}

      		// adding the default magasins to the users
			if ( !magasinRepository.existsByUser(userRepository.findByUsername(e).get())) {
					Magasin magasin = new Magasin(e + " mag", null, 0, 0);
	
					AppUser user=new AppUser();
					user = userRepository.findByUsername(e).get();
					System.err.println(user.toString());
					magasin.setUser(user);
					magasinRepository.save(magasin);
			}
     	});

     	setFirstLaunchData(ctx);
	}
	/*@Bean
	CommandLineRunner start(AccountService accountService, MagasinRepository magasinRepository, AppUserRepository userRepository,AppRoleRepository appRoleRepository){
		return  args -> {

		//	      accountService.save(new AppRole(null,"USER"));
          //  accountService.save(new AppRole(null,"ADMIN"));
         /*  Stream.of("user11110","user22220","user333330").forEach(e->{
                accountService.saveUser(e,"12345678","12345678");
            });
           */
         //accountService.addRoleToUser("user11110","ADMIN");

			/*if (!magasinRepository.existsByUser(userRepository.findByUsername("user11110").get())) {
					Magasin magasin = new Magasin("admin mag", null, 0, 0);

					AppUser user=new AppUser();
					user=userRepository.findByUsername("user11110").get();
					System.err.println(user.toString());
					magasin.setUser(user);
					magasinRepository.save(magasin);

				}
		};
	}*/

	private static void setFirstLaunchData(ConfigurableApplicationContext ctx) {


		CategorieRepository categoryRepository = ctx.getBean(CategorieRepository.class);
		//RoleRepository roleRepo = ctx.getBean(RoleRepository.class);
		//UserRepository userRepo = ctx.getBean(UserRepository.class);
//		MagasinRepository magasinRepo = ctx.getBean(MagasinRepository.class);
		ClientRepository clientRepo = ctx.getBean(ClientRepository.class);
		FournisseurRepository fournisseurRepo = ctx.getBean(FournisseurRepository.class);

		// Insert unknown category for the first running
		if (!categoryRepository.existsByLabel("غير مصنف"))
			categoryRepository.save(new Categorie("غير مصنف", null));

		/*// Insert all roles
		if (!roleRepo.existsByName(RoleEnum.ROLE_ADMIN))
			roleRepo.save(new Role(RoleEnum.ROLE_ADMIN));

		if (!roleRepo.existsByName(RoleEnum.ROLE_USER))
			roleRepo.save(new Role(RoleEnum.ROLE_USER));

		if (!roleRepo.existsByName(RoleEnum.ROLE_PM))
			roleRepo.save(new Role(RoleEnum.ROLE_PM));

		// Insert default user and admin
		if (!userRepo.existsByUsername("admin")) {
			User admin = new User("admin", "$2a$10$.bwa.i9616Lg3XsRr.T/2ecPq.6LZVctpcIiQi5jascZqneOJAz7m");
			userRepo.save(admin);

			Role adminRole = roleRepo.findByName(RoleEnum.ROLE_ADMIN).orElseThrow(null);
			roleRepo.insertUsersRole(admin.getIdUser(), adminRole.getIdRole());

			if (!magasinRepo.existsByUser(admin)) {
				Magasin magasin = new Magasin("admin mag", null, 0, 0);
				magasin.setUser(admin);
				magasinRepo.save(magasin);
			}
		}

		if (!userRepo.existsByUsername("user")) {
			User user = new User("user", "$2a$10$XN.u.va7dqGHvG.oWy..AupjI4ze62g72ZLEWTTS1WwSokdl9SQTa");
			userRepo.save(user);

			Role userRole = roleRepo.findByName(RoleEnum.ROLE_USER).orElseThrow(null);
			roleRepo.insertUsersRole(user.getIdUser(), userRole.getIdRole());

			if (!magasinRepo.existsByUser(user)) {
				Magasin magasin = new Magasin("user mag", null, 0, 0);
				magasin.setUser(user);
				magasinRepo.save(magasin);
			}
		}*/

		// Insert unknown client
		if (!clientRepo.existsByName("غير معروف"))
			clientRepo.save(new Client(null, "غير معروف", 0, null, null, null, null, null, TypeClientEnum.NORMAL));

		// Insert unknown provider (fournisseur)
		if (!fournisseurRepo.existsByName("غير معروف"))
			fournisseurRepo.save(new Fournisseur(null, "غير معروف", 0, null, null, null, null, null));
		
	}

	@Bean
	BCryptPasswordEncoder getBcBCryptPasswordEncoder(){
		return  new BCryptPasswordEncoder();
	}
}
