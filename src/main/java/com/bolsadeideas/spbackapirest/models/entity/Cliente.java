package com.bolsadeideas.spbackapirest.models.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne(fetch = FetchType.LAZY) // La relación es de tipo "Muchos a Uno" y se carga de manera diferida (lazy).
    @JoinColumn(name = "region_id") // Columna en la tabla Cliente que se utiliza para la relación con la tabla Región.
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Propiedades a ignorar durante la serialización JSON. >1
    @NotNull(message = "La region no puede estar vacia")
    private Region region; // La región a la que pertenece este cliente.



    @PrePersist //>2: Establece automáticamente la fecha de creación antes de que la entidad sea persistida en BD.
    public void prePersist() {  // Se ejecuta antes de que la entidad sea persistida (guardada) en la base de datos.
        createAt = new Date(); // Asignamos la fecha actual como fecha de creación.
    }

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

    public void setCreateAt(Date creatAt) {
        this.createAt = creatAt;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Serial
    private static final long serialVersionUID = 1L;

}

/*
* >1: @JsonIgnoreProperties
*    La anotación @JsonIgnoreProperties se utiliza para indicar a Jackson que ciertas propiedades de un objeto Java deben
*    ser ignoradas durante el proceso de serialización (convertir un objeto Java en JSON) o deserialización (convertir JSON en un objeto Java).
*    Esto puede ser útil cuando deseas controlar qué propiedades deben o no deben incluirse en la representación JSON de un objeto.
*
*    Se utiliza @JsonIgnoreProperties en conjunto con JPA y Hibernate en una relación @ManyToOne para evitar que las propiedades
*    "hibernateLazyInitializer" y "handler" se incluyan en la representación JSON del objeto. Estas propiedades están relacionadas
*    con la inicialización diferida y la gestión interna de Hibernate y generalmente no son relevantes para la serialización JSON.
*
* >2: @PrePersist:
*    Esta anotación se aplica a métodos que deben ejecutarse antes de que se persista (almacene en la base de datos)
*    una entidad en el EntityManager (gestor de entidades). Es decir, antes de que se cree una nueva fila en la tabla
*    correspondiente a la entidad en la base de datos.*/