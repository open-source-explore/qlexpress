package com.ql.util.express.instruction;

import com.ql.util.express.InstructionSet;

import java.io.Serializable;


public class FunctionInstructionSet implements Serializable {
    private static final long serialVersionUID = 8735208809492617401L;
    public String name;
    public String type;
    public InstructionSet instructionSet;

    public FunctionInstructionSet(String aName, String aType, InstructionSet aInstructionSet) {
        this.name = aName;
        this.type = aType;
        this.instructionSet = aInstructionSet;
    }

}