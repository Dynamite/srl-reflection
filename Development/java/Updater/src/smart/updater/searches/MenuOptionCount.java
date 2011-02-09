package smart.updater.searches;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.InstructionFinder;

import smart.updater.RSClass;
import smart.updater.RSClient;
import smart.updater.Search;
import smart.updater.Search.SearchResult;

public class MenuOptionCount extends Search{
	
	public SearchResult run(RSClient data, HashMap<String, ClassGen> classes) {
        for (Entry<String, ClassGen> c : classes.entrySet()) {
        	if(c.getValue().getClassName().equals("client"))
        	{
	            for (Method m : c.getValue().getMethods()) {
	                if (m.isStatic() && m.isFinal() && m.getReturnType().equals(Type.VOID) && m.getArgumentTypes().length == 10) {
	                	ConstantPoolGen cpg = c.getValue().getConstantPool();
	                    MethodGen gen = new MethodGen(m, c.getValue().getClassName(), cpg);
	                    InstructionList il = gen.getInstructionList();
	                    if (il == null) {
	                        continue;
	                    }
	                    InstructionFinder f = new InstructionFinder(il);
	                    Iterator e = f.search("INVOKESTATIC IFEQ GETSTATIC ICONST_2 IF_ICMPLE");
	                    if (e.hasNext()) {
	                        InstructionHandle[] handles = (InstructionHandle[]) e.next();
	                        String opCount = ((GETSTATIC) handles[2].getInstruction()).getClassName(cpg)+'.'+((GETSTATIC) handles[2].getInstruction()).getFieldName(cpg);
	                        data.addField("MenuOptionCount", opCount);
	                        return SearchResult.Success;
	                    }
	                }
	            }
        	}
        }
        return SearchResult.Failure;
    }

}
