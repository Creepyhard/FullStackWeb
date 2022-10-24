package br.com.back.end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.back.end.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(nativeQuery = true, value = "select u.* from user u where u.email = :email and u.password = :password")
	public User findEmailPasswordsUser(@Param("email") String emailT, @Param("password") String passwT);

}
