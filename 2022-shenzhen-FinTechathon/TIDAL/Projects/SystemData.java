package org.fisco.bcos.upload.contract;

import java.util.ArrayList;
import java.util.List;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

class SystemData {
    // Size of some data within array format.
    public int number_of_policy_read;
    public int number_of_policy_edit;
    public int number_of_attributes;

    // System parameters.
    public Element g, p;
    public Field G;
    public Field GT;
    public Field ZP;
    public Pairing e;

    // Attributes.
    public List<String> attributesList;

    // The secret key and the corresponding part in public key parameters.
    public Element[] r_i;
    public Element[] R_i;

    // Other parameters.
    public Element gamma;

    public void initAttributes() {
        attributesList = new ArrayList<>();
        attributesList.add("China");
        attributesList.add("America");
        attributesList.add("BIT");
        attributesList.add("MIT");
        attributesList.add("CST");
        attributesList.add("CS");
        attributesList.add("Student");
        attributesList.add("Professor");
        attributesList.add("Man");
        attributesList.add("Woman");
    }

    public SystemData() {
        this.number_of_policy_read = 10;
        this.number_of_policy_edit = 10;
        this.number_of_attributes = 20;

        this.r_i = new Element[number_of_attributes];
        this.R_i = new Element[number_of_attributes];
    }

    public SystemData(int number_of_policy_read, int number_of_policy_edit, int number_of_attributes) {
        this.number_of_policy_read = number_of_policy_read;
        this.number_of_policy_edit = number_of_policy_edit;
        this.number_of_attributes = number_of_attributes;

        this.r_i = new Element[number_of_attributes];
        this.R_i = new Element[number_of_attributes];
    }
}