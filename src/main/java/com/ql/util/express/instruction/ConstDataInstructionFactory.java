package com.ql.util.express.instruction;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.detail.InstructionConstData;
import com.ql.util.express.instruction.opdata.OperateClass;
import com.ql.util.express.parse.ExpressNode;

import java.util.Stack;

public class ConstDataInstructionFactory extends InstructionFactory {
	public OperateData genOperateData(ExpressNode node) {
		if (node.isTypeEqualsOrChild("CONST_CLASS")) {
			return new OperateClass(node.getValue(), (Class<?>) node.getObjectValue());
		} else {
			return new OperateData(node.getObjectValue(), node.getObjectValue().getClass());
		}
	}

	public boolean createInstruction(ExpressRunner aCompile,
			InstructionSet result, Stack<ForRelBreakContinue> forStack,
			ExpressNode node, boolean isRoot) throws Exception {
		result.addInstruction(new InstructionConstData(genOperateData(node)));
		return false;
	}
}
