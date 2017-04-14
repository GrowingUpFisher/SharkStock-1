/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kingu
 */
@Entity
@Table(name = "kurs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kurs.findAll", query = "SELECT k FROM Kurs k")
    , @NamedQuery(name = "Kurs.findByIdKurs", query = "SELECT k FROM Kurs k WHERE k.idKurs = :idKurs")
    , @NamedQuery(name = "Kurs.findByWartosc", query = "SELECT k FROM Kurs k WHERE k.wartosc = :wartosc")
    , @NamedQuery(name = "Kurs.findByZnaczekCzasowy", query = "SELECT k FROM Kurs k WHERE k.znaczekCzasowy = :znaczekCzasowy")})
public class Kurs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_kurs")
    private Integer idKurs;
    @Basic(optional = false)
    @Column(name = "wartosc")
    private double wartosc;
    @Basic(optional = false)
    @Column(name = "znaczek_czasowy")
    @Temporal(TemporalType.TIME)
    private Date znaczekCzasowy;
    @JoinColumn(name = "id_spolki_fk", referencedColumnName = "id_spolka")
    @ManyToOne(optional = false)
    private Spolka idSpolkiFk;

    public Kurs() {
    }

    public Kurs(Integer idKurs) {
        this.idKurs = idKurs;
    }

    public Kurs(Integer idKurs, double wartosc, Date znaczekCzasowy) {
        this.idKurs = idKurs;
        this.wartosc = wartosc;
        this.znaczekCzasowy = znaczekCzasowy;
    }

    public Integer getIdKurs() {
        return idKurs;
    }

    public void setIdKurs(Integer idKurs) {
        this.idKurs = idKurs;
    }

    public double getWartosc() {
        return wartosc;
    }

    public void setWartosc(double wartosc) {
        this.wartosc = wartosc;
    }

    public Date getZnaczekCzasowy() {
        return znaczekCzasowy;
    }

    public void setZnaczekCzasowy(Date znaczekCzasowy) {
        this.znaczekCzasowy = znaczekCzasowy;
    }

    public Spolka getIdSpolkiFk() {
        return idSpolkiFk;
    }

    public void setIdSpolkiFk(Spolka idSpolkiFk) {
        this.idSpolkiFk = idSpolkiFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKurs != null ? idKurs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kurs)) {
            return false;
        }
        Kurs other = (Kurs) object;
        if ((this.idKurs == null && other.idKurs != null) || (this.idKurs != null && !this.idKurs.equals(other.idKurs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Kurs[ idKurs=" + idKurs + " ]";
    }
    
}
