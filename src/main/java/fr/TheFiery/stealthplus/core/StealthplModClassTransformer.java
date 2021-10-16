package fr.TheFiery.stealthplus.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import fr.TheFiery.stealthplus.StealthplMod;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.World;

/**
 * @author SCAREX
 *
 */
public class StealthplModClassTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.equals("agw") || name.equals("net.minecraft.world.Explosion")) {
			StealthplMod.LOGGER.info("About to patch : " + name);
			return patchExplosion(name, basicClass, name.equals("agw"));
		}
		if (name.equals("akx") || name.equals("net.minecraft.block.BlockFalling")) {
			StealthplMod.LOGGER.info("About to patch : " + name);
			return patchBlockFalling(name, basicClass, name.equals("akx"));
		}
		return basicClass;
	}

	/**
	 * @param name
	 * @param basicClass
	 * @param equals
	 * @return
	 */
	private byte[] patchBlockFalling(String name, byte[] basicClass, boolean obf) {
		ClassNode cnode = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cnode, ClassReader.EXPAND_FRAMES);

		String func_149830_mMethodName = obf ? "m" : "func_149830_m";
		String worldClassName = obf ? "ahb" : "net/minecraft/world/World";

		MethodNode mnfunc_149830_m = ASMHelper.findMethod(cnode, func_149830_mMethodName, "(L" + worldClassName + ";III)V");
		AbstractInsnNode nodeif = ASMHelper.getFirstInstrWithOpcode(mnfunc_149830_m, Opcodes.IFLT);
		InsnList insnList = new InsnList();
		insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
		insnList.add(new VarInsnNode(Opcodes.ILOAD, 2));
		insnList.add(new VarInsnNode(Opcodes.ILOAD, 3));
		insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));
		insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
		insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
		insnList.add(new VarInsnNode(Opcodes.ILOAD, 2));
		insnList.add(new VarInsnNode(Opcodes.ILOAD, 3));
		insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));
		insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, worldClassName, obf ? "e" : "getBlockMetadata", "(III)I", false));
		insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/TheFiery/stealthpl/core/event/TutorialModEventFactory", "onBlockFallingEvent", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)Z", false));
		LabelNode l1 = new LabelNode();
		insnList.add(new JumpInsnNode(Opcodes.IFEQ, l1));
		insnList.add(new InsnNode(Opcodes.RETURN));
		insnList.add(l1);

		mnfunc_149830_m.instructions.insert(nodeif, insnList);

		cnode.interfaces.add("fr/TheFiery/stealthpl/core/ICanFall");

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cnode.accept(cw);

		try {
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "canBlockFall", Type.getMethodDescriptor(ICanFall.class.getMethod("canBlockFall", World.class, int.class, int.class, int.class)), null, new String[0]);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitVarInsn(Opcodes.ILOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, obf ? "akx" : "net/minecraft/block/BlockFalling", obf ? "e" : "func_149831_e", "(Lnet/minecraft/world/World;III)Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cw.toByteArray();
	}

	/**
	 * @param name
	 * @param basicClass
	 * @param obf
	 * @return
	 */
	private byte[] patchExplosion(String name, byte[] basicClass, boolean obf) {
		String targetMethodName = obf ? "a" : "doExplosionB";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
		MethodNode mnode = ASMHelper.findMethod(classNode, targetMethodName, "(Z)V");

		InsnList instr = new InsnList();
		instr.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		instr.add(new LdcInsnNode("Explosion !"));
		instr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
		mnode.instructions.insertBefore(mnode.instructions.getFirst(), instr);

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(cw);
		return cw.toByteArray();
	}
}
