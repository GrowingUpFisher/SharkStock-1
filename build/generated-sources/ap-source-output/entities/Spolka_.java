package entities;

import entities.Kurs;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-13T12:26:15")
@StaticMetamodel(Spolka.class)
public class Spolka_ { 

    public static volatile SingularAttribute<Spolka, Integer> idSpolka;
    public static volatile ListAttribute<Spolka, Kurs> kursList;
    public static volatile SingularAttribute<Spolka, String> nazwa;

}