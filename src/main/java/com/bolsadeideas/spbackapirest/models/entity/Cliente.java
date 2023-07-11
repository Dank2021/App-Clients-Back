package com.bolsadeideas.spbackapirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
//Representa que la clase es una entidad persistente; objeto que representa una tabla en una base de datos relacional
@Table(name = "clientes")//Cuando la clase se llame igual a la tabla. Se puede omitir esta anotacion.
public class Cliente implements Serializable {

    @Id        //Llave primaria primaria en la base de datos.
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //Estrategia de generacion.
    private Long id;
    @NotEmpty(message = "No puede estar vacio")
    @Size(min=3, max=12, message = "El tamaño de estar entre 3 y 12 caracteres")
    @Column(nullable = false)
    private String nombre;
    @NotEmpty(message = "No puede estar vacio")
    @Size(min=3, max=12, message = "El tamaño de estar entre 3 y 12 caracteres")
    private String apellido;
    @NotEmpty@NotEmpty(message = "No puede estar vacio")
    @Email(message = "No es una direccion de correo valida")
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "create_at")//Cuando el atributo se llame igual a la columna. Se puede omitir esta anotacion.
    @Temporal(TemporalType.DATE)
    //Indicar cual va a ser la transformacion o el tipo de dato equivalente con el que se va a trabajar en al BD (DATE,TIME...)
    private Date createAt;

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }   //Creamos la fecha autamaticamente

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreteAt(Date creatAt) {
        this.createAt = creatAt;
    }

    private static final long serialVersionUID = 1L;

}
