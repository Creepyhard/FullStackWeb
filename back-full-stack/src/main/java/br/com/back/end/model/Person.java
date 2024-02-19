package br.com.back.end.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "tblperson")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

//    @Column
//    @OneToOne
//    @JoinColumn(name = "user", referencedColumnName = "id")
//    private User user;
    @Column
    private Timestamp inclusion;

    @Column
    private Timestamp alteration;

    @Column(length = 100)
    private String fullName;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telephone;

    public Timestamp getInclusion() {
        Long datetime = System.currentTimeMillis();
        return new Timestamp(datetime);
    }
}
