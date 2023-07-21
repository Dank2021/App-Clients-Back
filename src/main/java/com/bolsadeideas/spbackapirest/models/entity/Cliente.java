package com.bolsadeideas.spbackapirest.models.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
//Representa que la clase es una entidad persistente; objeto que representa una tabla en una base de datos relacional
@Table(name = "clientes")//Cuando la clase se llame igual a la tabla. Se puede omitir esta anotacion.
public class Cliente implements Serializable {

    @Id        //Llave primaria primaria en la base de datos.
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //Estrategia de generacion.
    private Long id;
    @NotEmpty(message = "No puede estar vacio") //Se aplica específicamente a tipo String, colecciones o arrays, ya que tiene en cuenta el tamaño del contenido.
    @Size(min=3, max=12, message = "El tamaño de estar entre 3 y 12 caracteres")
    @Column(nullable = false)
    private String nombre;
    @NotEmpty(message = "No puede estar vacio")
    @Size(min=3, max=12, message = "El tamaño de estar entre 3 y 12 caracteres")
    private String apellido;
    @NotEmpty(message = "No puede estar vacio")
    @Email(message = "No es una direccion de correo valida")
    @Column(nullable = false, unique = false)
    private String email;
    //@NotNull(message = "No puede estar vacio")//Se puede aplicar a cualquier tipo de dato, ya sean campos de clase o parámetros de métodos.
    @Column(name = "create_at")//Cuando el atributo se llame igual a la columna. Se puede omitir esta anotacion.
    @Temporal(TemporalType.DATE)
    //Indicar cuál va a ser la transformacion o el tipo de dato equivalente con el que se va a trabajar en al BD (DATE,TIME...)
    private Date createAt;
    private String foto;

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }   //Creamos la fecha automatica e inmediatamente. Haciendo que los objetos lo primero que tenga sea una fecha.

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

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    private static final long serialVersionUID = 1L;

}

/*
* @PrePersist:
* Esta anotación se aplica a métodos que deben ejecutarse antes de que se persista (almacene en la base de datos)
* una entidad en el EntityManager (gestor de entidades). Es decir, antes de que se cree una nueva fila en la tabla
* correspondiente a la entidad en la base de datos.*/