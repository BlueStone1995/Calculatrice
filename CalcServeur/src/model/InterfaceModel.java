package model;


import model.Exception.DivisionException;
import model.Exception.OpException;

public interface InterfaceModel {
    float calculer(float a, float b, String op) throws OpException, DivisionException;
}
