package fr.TheFiery.stealthplus.core;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import fr.TheFiery.stealthplus.StealthplMod;

/**
 * @author SCAREX
 *
 */
public final class ASMHelper
{
	public static MethodNode findMethod(ClassNode cnode, String name, String desc) {
		for (MethodNode m : cnode.methods) {
			if (name.equals(m.name) && desc.equals(m.desc)) return m;
		}
		return null;
	}

	public static AbstractInsnNode getFirstInstrWithOpcode(MethodNode mn, int opcode) {
		Iterator<AbstractInsnNode> ite = mn.instructions.iterator();
		while (ite.hasNext()) {
			AbstractInsnNode n = ite.next();
			if (n.getOpcode() == opcode) return n;
		}
		return null;
	}

	public static LabelNode findLineLabel(MethodNode mn, int line) {
		Iterator<AbstractInsnNode> ite = mn.instructions.iterator();
		while (ite.hasNext()) {
			AbstractInsnNode n = ite.next();
			if (n instanceof LineNumberNode && ((LineNumberNode) n).line == line) return ((LineNumberNode) n).start;
		}
		return null;
	}
}
