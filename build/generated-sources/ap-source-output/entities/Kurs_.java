package entities;

import entities.Spolka;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-13T12:26:15")
@StaticMetamodel(Kurs.class)
public class Kurs_ { 

    public static volatile SingularAttribute<Kurs, Date> znaczekCzasowy;
    public static volatile SingularAttribute<Kurs, Integer> idKurs;
    public static volatile SingularAttribute<Kurs, Double> wartosc;
    public static volatile SingularAttribute<Kurs, Spolka> idSpolkiFk;

}