package flowz.cloudflowz.services;

import flowz.cloudflowz.domain.Users;

public interface UsersService {
	
	Iterable<Users> listAllUsers();
	
	Users findByUsername(String username);
    
}
