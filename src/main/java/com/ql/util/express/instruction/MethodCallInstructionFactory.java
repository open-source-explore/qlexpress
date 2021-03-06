package com.ql.util.express.instruction;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.instruction.detail.InstructionOperator;
import com.ql.util.express.instruction.op.OperatorBase;
import com.ql.util.express.parse.ExpressNode;

import java.util.Stack;


public class MethodCallInstructionFactory extends InstructionFactory {
    public boolean createInstruction(ExpressRunner aCompile,
                                     InstructionSet result, Stack<ForRelBreakContinue> forStack,
                                     ExpressNode node, boolean isRoot) throws Exception {
        boolean returnVal = false;
        ExpressNode[] children = node.getChildren();
        for (int i = 0; i < children.length; i++) {
            boolean tmpHas = aCompile.createInstructionSetPrivate(result, forStack, children[i], false);
            returnVal = returnVal || tmpHas;
        }
        OperatorBase op = aCompile.getOperatorFactory().newInstance(node);
        result.addInstruction(new InstructionOperator(op, children.length));
        return returnVal;
    }

}
