package br.com.back.end.repository;

import br.com.back.end.model.transactions.TaxTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.back.end.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u.email, u.password from User u where u.email = :email and u.password = :password")
	public User findEmailPasswordsUser(@Param("email") String emailT, @Param("password") String passwT);

	@Query("SELECT u from User u where u.email = :email")
	public User findEmailPasswordsTest(@Param("email") String emailT);

	@Query("SELECT tt FROM TaxTransfer tt WHERE tt.homeDay <= :qtdDay and tt.finalDay >= :qtdDay")
	public TaxTransfer returnsRateReferentToDay(@Param("qtdDay") int qtdDay);

	@Query("SELECT u FROM User u WHERE u.numberCC = :cd")
	public User returnAccount(@Param("cd") String cd);

}
