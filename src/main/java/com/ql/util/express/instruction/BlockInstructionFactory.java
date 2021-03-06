package com.ql.util.express.instruction;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.instruction.detail.InstructionClearDataStack;
import com.ql.util.express.instruction.detail.InstructionCloseNewArea;
import com.ql.util.express.instruction.detail.InstructionOpenNewArea;
import com.ql.util.express.parse.ExpressNode;

import java.util.Stack;

public class BlockInstructionFactory extends InstructionFactory {
    public boolean createInstruction(ExpressRunner aCompile, InstructionSet result, Stack<ForRelBreakContinue> forStack, ExpressNode node, boolean isRoot)
            throws Exception {
        if (node.isTypeEqualsOrChild("STAT_SEMICOLON")
                && result.getCurrentPoint() >= 0 && result.getInstruction(result.getCurrentPoint()) instanceof InstructionClearDataStack == false) {
            result.addInstruction(new InstructionClearDataStack());
        }

        int tmpPoint = result.getCurrentPoint() + 1;
        boolean returnVal = false;
        boolean hasDef = false;
        for (ExpressNode tmpNode : node.getChildren()) {
            boolean tmpHas = aCompile.createInstructionSetPrivate(result, forStack, tmpNode, false);
            hasDef = hasDef || tmpHas;
        }
        if (hasDef == true && isRoot == false
                && node.getTreeType().isEqualsOrChild("STAT_BLOCK")) {
            result.insertInstruction(tmpPoint, new InstructionOpenNewArea());
            result.insertInstruction(result.getCurrentPoint() + 1, new InstructionCloseNewArea());
            returnVal = false;
        } else {
            returnVal = hasDef;
        }
        return returnVal;
    }
}
