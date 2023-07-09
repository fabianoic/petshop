package br.com.ficampos.petshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class EntidadeBase<T, O> implements ConversorDTO<T, O> {

    @Version
    @JsonIgnore
    private Integer optmisticklock;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date dtCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date dtUpdated;


    @PrePersist
    protected void prePersistBaseEntity() {
        this.dtCreated = new Date();
        this.dtUpdated = new Date();
        this.optmisticklock = 1;
    }

    @PreUpdate
    protected void preUpdateBaseEntity() {
        this.dtUpdated = new Date();
    }
}
