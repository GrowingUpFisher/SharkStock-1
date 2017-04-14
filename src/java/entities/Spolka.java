/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Kingu
 */
@Entity
@Table(name = "spolka")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Spolka.findAll", query = "SELECT s FROM Spolka s")
    , @NamedQuery(name = "Spolka.findByIdSpolka", query = "SELECT s FROM Spolka s WHERE s.idSpolka = :idSpolka")
    , @NamedQuery(name = "Spolka.findByNazwa", query = "SELECT s FROM Spolka s WHERE s.nazwa = :nazwa")})
public class Spolka implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_spolka")
    private Integer idSpolka;
    @Column(name = "nazwa")
    private String nazwa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSpolkiFk")
    private List<Kurs> kursList;

    public Spolka() {
    }

    public Spolka(Integer idSpolka) {
        this.idSpolka = idSpolka;
    }

    public Integer getIdSpolka() {
        return idSpolka;
    }

    public void setIdSpolka(Integer idSpolka) {
        this.idSpolka = idSpolka;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @XmlTransient
    public List<Kurs> getKursList() {
        return kursList;
    }

    public void setKursList(List<Kurs> kursList) {
        this.kursList = kursList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSpolka != null ? idSpolka.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Spolka)) {
            return false;
        }
        Spolka other = (Spolka) object;
        if ((this.idSpolka == null && other.idSpolka != null) || (this.idSpolka != null && !this.idSpolka.equals(other.idSpolka))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Spolka[ idSpolka=" + idSpolka + " ]";
    }
    
}
