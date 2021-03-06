package com.ql.util.express.instruction.op;

import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.OperateDataCacheManager;

import java.util.ArrayList;
import java.util.List;

public class OperatorAnonymousNewList extends OperatorBase {
    public OperatorAnonymousNewList(String aName) {
        this.name = aName;
    }

    public OperatorAnonymousNewList(String aAliasName, String aName, String aErrorInfo) {
        this.name = aName;
        this.aliasName = aAliasName;
        this.errorInfo = aErrorInfo;
    }

    public OperateData executeInner(InstructionSetContext context, OperateData[] list) throws Exception {
        List<Object> result = new ArrayList<Object>();
        for (int i = 0; i < list.length; i++) {
            result.add(list[i].getObject(context));
        }
        return OperateDataCacheManager.fetchOperateData(result, List.class);
    }
}
