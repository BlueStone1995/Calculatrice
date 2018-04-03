package model;


import model.Exception.DivisionException;
import model.Exception.OpException;

import java.util.HashMap;
import java.util.Map;

public class Calculator implements InterfaceModel {

    private Map<String, Operation> operations;

    public Calculator() {
        this.operations = new HashMap<String, Operation>();
    }

    public void addOperation(String sym, Operation op) throws OpException {
            this.operations.put(sym, op); //On rajoute opération dans map
    }


    public float calculer(float a, float b, String op) throws OpException, DivisionException {
        Operation o = this.operations.get(op); //Récupère opération dans mon map via sa version string

        if (o == null) {
            throw new OpException();
        }

        return o.execute(a, b); // Retourne résultat
    }
}
