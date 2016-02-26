package com.ql.util.express;

import com.ql.util.express.instruction.FunctionInstructionSet;
import com.ql.util.express.instruction.OperateDataCacheManager;
import com.ql.util.express.instruction.detail.Instruction;
import com.ql.util.express.instruction.detail.InstructionConstData;
import com.ql.util.express.instruction.detail.InstructionLoadAttr;
import com.ql.util.express.instruction.detail.InstructionOperator;
import com.ql.util.express.instruction.opdata.OperateDataLocalVar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * ����ʽִ�б�����γɵ�ָ���
 *
 * @author qhlhl2010@gmail.com
 */

public class InstructionSet implements Serializable {

    private static final long serialVersionUID = 1841743860792681669L;

    private static final transient Log log = LogFactory.getLog(InstructionSet.class);
    public static AtomicInteger uniqIndex = new AtomicInteger(1);
    public static String TYPE_MAIN = "main";
    public static String TYPE_CLASS = "VClass";
    public static String TYPE_FUNCTION = "function";
    public static String TYPE_MARCO = "marco";


    private String type = "main";
    private String name;
    private String globeName;

    /**
     * ָ��
     */
    private Instruction[] instructionList = new Instruction[0];
    /**
     * �����ͺ궨��
     */
    private Map<String, FunctionInstructionSet> functionDefine = new HashMap<String, FunctionInstructionSet>();
    private List<ExportItem> exportVar = new ArrayList<ExportItem>();
    /**
     * ������������
     */
    private List<OperateDataLocalVar> parameterList = new ArrayList<OperateDataLocalVar>();

    public static int getUniqClassIndex() {
        return uniqIndex.getAndIncrement();
    }

    public InstructionSet(String aType) {
        this.type = aType;
    }

    public String[] getOutAttrNames() throws Exception {
        Map<String, String> result = new TreeMap<String, String>();
        for (Instruction instruction : instructionList) {
            if (instruction instanceof InstructionLoadAttr) {
                result.put(((InstructionLoadAttr) instruction).getAttrName(), null);
            }
        }

        //�޳����ر�������ͱ�������
        for (int i = 0; i < instructionList.length; i++) {
            Instruction instruction = instructionList[i];
            if (instruction instanceof InstructionOperator) {
                String opName = ((InstructionOperator) instruction)
                        .getOperator().getName();
                if (opName.equalsIgnoreCase("def")
                        || opName.equalsIgnoreCase("exportDef")) {
                    String varLocalName = (String) ((InstructionConstData) instructionList[i - 1])
                            .getOperateData().getObject(null);
                    result.remove(varLocalName);
                } else if (opName.equalsIgnoreCase("alias")
                        || opName.equalsIgnoreCase("exportAlias")) {
                    String varLocalName = (String) ((InstructionConstData) instructionList[i - 2])
                            .getOperateData().getObject(null);
                    result.remove(varLocalName);
                }
            }
        }
        return result.keySet().toArray(new String[0]);
    }


    /**
     * ����ָ�Ϊ����������ڵ�Ч�ʣ�ָ�������洢
     *
     * @param item
     * @return
     */
    private void addArrayItem(Instruction item) {
        Instruction[] newArray = new Instruction[this.instructionList.length + 1];
        System.arraycopy(this.instructionList, 0, newArray, 0, this.instructionList.length);
        newArray[this.instructionList.length] = item;
        this.instructionList = newArray;
    }

    /**
     * ��������
     *
     * @param aPoint
     * @param item
     */
    private void insertArrayItem(int aPoint, Instruction item) {
        Instruction[] newArray = new Instruction[this.instructionList.length + 1];
        System.arraycopy(this.instructionList, 0, newArray, 0, aPoint);
        System.arraycopy(this.instructionList, aPoint, newArray, aPoint + 1, this.instructionList.length - aPoint);
        newArray[aPoint] = item;
        this.instructionList = newArray;
    }

    /**
     * @param environmen
     * @param context
     * @param errorList
     * @param isLast
     * @param isReturnLastData �Ƿ����Ľ������Ҫ����ִ�к궨���ʱ����Ҫ
     * @param aLog
     * @return
     * @throws Exception
     */
    public CallResult excute(RunEnvironment environmen, InstructionSetContext context,
                             List<String> errorList, boolean isLast, boolean isReturnLastData, Log aLog)
            throws Exception {
        //������export����������
        for (FunctionInstructionSet item : this.functionDefine.values()) {
            context.addSymbol(item.name, item.instructionSet);
        }
        this.executeInnerOrigiInstruction(environmen, errorList, aLog);
        if (environmen.isExit() == false && isLast == true) {// ����ִ�������е�ָ�������Ĵ���
            if (environmen.getDataStackSize() > 0) {
                OperateData tmpObject = environmen.pop();
                if (tmpObject == null) {
                    environmen.quitExpress(null);
                } else {
                    if (isReturnLastData == true) {
                        if (tmpObject.getType(context) != null && tmpObject.getType(context).equals(void.class)) {
                            environmen.quitExpress(null);
                        } else {
                            environmen.quitExpress(tmpObject.getObject(context));
                        }
                    } else {
                        environmen.quitExpress(tmpObject);
                    }
                }
            }
        }
        if (environmen.getDataStackSize() > 1) {
            throw new Exception("�ڱ���ʽִ����Ϻ󣬶�ջ�л����ڶ������");
        }
        CallResult result = OperateDataCacheManager.fetchCallResult(environmen.getReturnValue(), environmen.isExit());
        return result;
    }

    public void executeInnerOrigiInstruction(RunEnvironment environmen, List<String> errorList, Log aLog) throws Exception {
        Instruction instruction;
        while (environmen.getProgramPoint() < this.instructionList.length) {
            if (environmen.isExit() == true) {
                return;
            }
            instruction = this.instructionList[environmen.getProgramPoint()];
            instruction.setLog(aLog);//����log
            try {
                instruction.execute(environmen, errorList);
            } catch (Exception e) {
                log.error("��ǰProgramPoint = " + environmen.getProgramPoint());
                log.error("��ǰָ��" + instruction);
                log.error(e);
                throw e;
            }
        }
    }

    public void addMacroDefine(String macroName, FunctionInstructionSet iset) {
        this.functionDefine.put(macroName, iset);
    }

    public FunctionInstructionSet getMacroDefine(String macroName) {
        return this.functionDefine.get(macroName);
    }

    public FunctionInstructionSet[] getFunctionInstructionSets() {
        return this.functionDefine.values().toArray(new FunctionInstructionSet[0]);
    }

    public void addExportDef(ExportItem e) {
        this.exportVar.add(e);
    }

    public List<ExportItem> getExportDef() {
        List<ExportItem> result = new ArrayList<ExportItem>();
        result.addAll(this.exportVar);
        return result;
    }


    public OperateDataLocalVar[] getParameters() {
        return this.parameterList.toArray(new OperateDataLocalVar[0]);
    }

    public void addParameter(OperateDataLocalVar localVar) {
        this.parameterList.add(localVar);
    }

    public void addInstruction(Instruction instruction) {
        this.addArrayItem(instruction);
    }

    public void insertInstruction(int point, Instruction instruction) {
        this.insertArrayItem(point, instruction);
    }

    public Instruction getInstruction(int point) {
        return this.instructionList[point];
    }

    public int getCurrentPoint() {
        return this.instructionList.length - 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGlobeName() {
        return globeName;
    }

    public void setGlobeName(String globeName) {
        this.globeName = globeName;
    }

    public boolean hasMain() {
        return this.instructionList.length > 0;
    }

    public String getType() {
        return type;
    }

    public void appendSpace(StringBuffer buffer, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append("    ");
        }
    }

    public String toString() {
        return "\n" + toString(0);
    }

    public String toString(int level) {
        try {
            StringBuffer buffer = new StringBuffer();
            // ����궨��
            for (FunctionInstructionSet set : this.functionDefine.values()) {
                appendSpace(buffer, level);
                buffer.append(set.type + ":" + set.name).append("(");
                for (int i = 0; i < set.instructionSet.parameterList.size(); i++) {
                    OperateDataLocalVar var = set.instructionSet.parameterList.get(i);
                    if (i > 0) {
                        buffer.append(",");
                    }
                    buffer.append(var.getType(null).getName()).append(" ").append(var.getName());
                }
                buffer.append("){\n");
                buffer.append(set.instructionSet.toString(level + 1));
                appendSpace(buffer, level);
                buffer.append("}\n");
            }
            for (int i = 0; i < this.instructionList.length; i++) {
                appendSpace(buffer, level);
                buffer.append(i + 1).append(":").append(this.instructionList[i])
                        .append("\n");
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

	