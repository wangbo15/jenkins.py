package jenkins.python.expoint;


import hudson.Extension;
import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.AbstractProject;
import jenkins.model.Jenkins;
import java.util.Collection;
import java.util.Collections;
import hudson.model.queue.*;
import hudson.model.queue.SubTaskContributor.*;
import jenkins.python.DataConvertor;
import jenkins.python.PythonExecutor;

/**
 * This class was automatically generated by the PWM tool on 2014/03/21.
 * @see hudson.model.queue.SubTaskContributor
 */
public abstract class SubTaskContributorPW extends SubTaskContributor {
	private transient PythonExecutor pexec;

	private void initPython() {
		if (pexec == null) {
			pexec = new PythonExecutor(this);
			String[] jMethods = new String[0];
			String[] pFuncs = new String[0];
			Class[][] argTypes = new Class[0][];
			pexec.checkAbstrMethods(jMethods, pFuncs, argTypes);
			String[] functions = new String[1];
			functions[0] = "for_project";
			int[] argsCount = new int[1];
			argsCount[0] = 1;
			pexec.registerFunctions(functions, argsCount);
		}
	}

	@Override
	public Collection<? extends SubTask> forProject(AbstractProject<?, ?> p) {
		initPython();
		if (pexec.isImplemented(0)) {
			return (Collection) pexec.execPython("for_project", p);
		} else {
			return super.forProject(p);
		}
	}

	public Collection<? extends SubTask> superForProject(AbstractProject<?, ?> p) {
		return super.forProject(p);
	}

	public Object execPython(String function, Object... params) {
		initPython();
		return pexec.execPython(function, params);
	}

	public byte execPythonByte(String function, Object... params) {
		initPython();
		return pexec.execPythonByte(function, params);
	}

	public short execPythonShort(String function, Object... params) {
		initPython();
		return pexec.execPythonShort(function, params);
	}

	public char execPythonChar(String function, Object... params) {
		initPython();
		return pexec.execPythonChar(function, params);
	}

	public int execPythonInt(String function, Object... params) {
		initPython();
		return pexec.execPythonInt(function, params);
	}

	public long execPythonLong(String function, Object... params) {
		initPython();
		return pexec.execPythonLong(function, params);
	}

	public float execPythonFloat(String function, Object... params) {
		initPython();
		return pexec.execPythonFloat(function, params);
	}

	public double execPythonDouble(String function, Object... params) {
		initPython();
		return pexec.execPythonDouble(function, params);
	}

	public boolean execPythonBool(String function, Object... params) {
		initPython();
		return pexec.execPythonBool(function, params);
	}

	public void execPythonVoid(String function, Object... params) {
		initPython();
		pexec.execPythonVoid(function, params);
	}
}